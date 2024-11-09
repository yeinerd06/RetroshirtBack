package co.com.api.wise_stock.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.dto.EmailDTO;
import co.com.api.wise_stock.dto.PostArticuloDTO;
import co.com.api.wise_stock.dto.PostPedidoDTO;
import co.com.api.wise_stock.entity.Articulo;
import co.com.api.wise_stock.entity.EstadoPedido;
import co.com.api.wise_stock.entity.Pedido;
import co.com.api.wise_stock.entity.PedidoArticulo;
import co.com.api.wise_stock.entity.RolUsuario;
import co.com.api.wise_stock.entity.Usuario;
import co.com.api.wise_stock.enums.EstadoEnum;
import co.com.api.wise_stock.enums.TipoEntregaEnum;
import co.com.api.wise_stock.exepciones.PedidoException;
import co.com.api.wise_stock.repository.ArticuloRepository;
import co.com.api.wise_stock.repository.EstadoPedidoRepository;
import co.com.api.wise_stock.repository.PedidoArticuloRepository;
import co.com.api.wise_stock.repository.PedidoRepository;
import co.com.api.wise_stock.repository.RolUsuarioRepository;
import co.com.api.wise_stock.repository.UsuarioReporitory;
import co.com.api.wise_stock.util.EmailService;
import co.com.api.wise_stock.util.Response;

@Service
public class PedidoService {
    
    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoArticuloRepository pedidoArticuloRepository;

    @Autowired
    ArticuloRepository articuloRepository;

    @Autowired
    EstadoPedidoRepository estadoPedidoRepository;

    @Autowired
    UsuarioReporitory usuarioReporitory;

	@Autowired
	RolUsuarioRepository rolUsuarioRepository;

    @Autowired
	EmailService emailService;

    public Response listarPedidos() {
        return Response.crear(true, "Listado de pedidos", estadoPedidoRepository.findLastEstadoForEachPedido());
    }

    @Transactional
    public Response crearPedido(PostPedidoDTO postPedidoDTO) {
        if(postPedidoDTO.getTipoEntrega() == TipoEntregaEnum.DOMICILIO && postPedidoDTO.getDireccionEntrega().isEmpty()) {
            throw new PedidoException("El domicilio es obligatorio");
        }
        Pedido pedido = new Pedido();
        pedido.setComprobantePago(postPedidoDTO.getComprobantePago());
        pedido.setDireccionEntrega(postPedidoDTO.getDireccionEntrega());
        pedido.setTipoEntrega(postPedidoDTO.getTipoEntrega());
        Usuario usuario = usuarioReporitory.findById(postPedidoDTO.getIdUsuario())
            .orElseThrow(() -> new PedidoException("No se encontro el cliente"));
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now().toString());
        pedido.setFechaEstimadaEntrega(postPedidoDTO.getFechaEstimadaEntrega());
        
        // Asignar operario

        String fechaInicio = LocalDateTime.now().toString();
        String fechaFin = LocalDateTime.now().plusDays(3).toString();

        // Buscar el operario con menos carga en los próximos tres días
        List<RolUsuario> operarios = rolUsuarioRepository.findByRolId(5); // Obtenemos todos los operarios con rol 5
        Optional<RolUsuario> operarioAsignado = operarios.stream()
            .min(Comparator.comparingInt(op -> 
                pedidoRepository.findPedidosByOperarioAndFechaEstimadaEntregaBetween(op.getUsuario().getId(), fechaInicio, fechaFin).size()
            ));

        if (operarioAsignado.isPresent()) {
            pedido.setOperarioAsignado(operarioAsignado.get().getUsuario()); // Asigna el operario al pedido
        } else {
            throw new PedidoException("No hay operarios disponibles");
        }

        Pedido pedidoDB = pedidoRepository.save(pedido);

        for(PostArticuloDTO articuloDTO: postPedidoDTO.getArticulos()) {
            Optional<Articulo> articulo = articuloRepository.findById(articuloDTO.getArticulo());
            if(!articulo.isPresent() || articuloDTO.getCantidad() <= 0 || articulo.get().getStock() < articuloDTO.getCantidad()) {
                throw new PedidoException("Articulo no valido");
            }

            Articulo articuloDB = articulo.get();
            articuloDB.setStock(articuloDB.getStock() - articuloDTO.getCantidad());
            articuloRepository.save(articuloDB);

            PedidoArticulo pedidoArticulo = new PedidoArticulo();
            pedidoArticulo.setPedido(pedidoDB);
            pedidoArticulo.setArticulo(articulo.get());
            pedidoArticulo.setCantidad(articuloDTO.getCantidad());
            pedidoArticuloRepository.save(pedidoArticulo);
        }

        EstadoPedido estadoPedido = new EstadoPedido();
        estadoPedido.setEstado(EstadoEnum.ASIGNADO);
        estadoPedido.setPedido(pedidoDB);
        estadoPedido.setFechaCambio(LocalDateTime.now().toString());
        estadoPedidoRepository.save(estadoPedido);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es"));
        LocalDateTime fechaPedido = LocalDateTime.parse(pedido.getFechaEstimadaEntrega());
        String fechaFormateada = fechaPedido.format(formatter);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAsunto("Nuevo pedido asignado");
        emailDTO.setTitulo("Se te ha asignado un nuevo pedido");
        emailDTO.setDetalle("Tienes un nuevo pedido para estampar con una fecha estimada de entrega para el " + fechaFormateada);
        emailService.sendEmailPedidoListo(emailDTO, pedido.getOperarioAsignado().getEmail());


		return Response.crear(true, "Pedido creado con exito", null);
	}

    public Response eliminarPedido(Integer idPedido) {

        // Verificar si el pedido existe
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(idPedido);
        if (!pedidoOptional.isPresent()) {
            throw new PedidoException("El pedido no existe");
        }
        Pedido pedido = pedidoOptional.get();

        Optional<EstadoPedido> estadoPedido = estadoPedidoRepository.findByPedidoIdAndEstado(idPedido, EstadoEnum.EN_PRODUCCION);
        if(estadoPedido.isPresent()) {
            throw new PedidoException("El pedido ya esta en produccion");
        }

        EstadoPedido pedidoEliminado = new EstadoPedido();
        pedidoEliminado.setPedido(pedido);
        pedidoEliminado.setFechaCambio(LocalDateTime.now().toString());
        pedidoEliminado.setEstado(EstadoEnum.CANCELADO);
        estadoPedidoRepository.save(pedidoEliminado);

		return Response.crear(true, "Pedido eliminado con exito", null);
	}

    public Response actualizarPedido(Integer idPedido) {

        // Verificar si el pedido existe
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(idPedido);
        if (!pedidoOptional.isPresent()) {
            throw new PedidoException("El pedido no existe");
        }
        Pedido pedido = pedidoOptional.get();

        Optional<EstadoPedido> estadoPedido = estadoPedidoRepository.findByPedidoIdAndEstado(idPedido, EstadoEnum.EN_PRODUCCION);
        if(estadoPedido.isPresent()) {
            throw new PedidoException("El pedido ya esta en produccion");
        }

        return Response.crear(true, "Pedido actualizado con exito", null);
    }

    public Response pedidoListo(Integer idPedido) {
        
        // Verificar si el pedido existe
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(idPedido);
        if (!pedidoOptional.isPresent()) {
            throw new PedidoException("El pedido no existe");
        }
        Pedido pedido = pedidoOptional.get();

        Optional<EstadoPedido> estadoPedido = estadoPedidoRepository.findByPedidoIdAndEstado(idPedido, EstadoEnum.EN_PRODUCCION);
        if(!estadoPedido.isPresent()) {
            throw new PedidoException("El pedido no esta en producción");
        }

        estadoPedido = estadoPedidoRepository.findByPedidoIdAndEstado(idPedido, EstadoEnum.TERMINADO);
        if(estadoPedido.isPresent()) {
            throw new PedidoException("El pedido ya esta terminado");
        }

        EstadoPedido pedidoListo = new EstadoPedido();
        pedidoListo.setPedido(pedido);
        pedidoListo.setFechaCambio(LocalDateTime.now().toString());
        pedidoListo.setEstado(EstadoEnum.TERMINADO);
        estadoPedidoRepository.save(pedidoListo);

        List<RolUsuario> admins = rolUsuarioRepository.findByRolId(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es"));
        LocalDateTime fechaPedido = LocalDateTime.parse(pedido.getFechaPedido());
        String fechaFormateada = fechaPedido.format(formatter);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAsunto("Pedido listo");
        emailDTO.setTitulo("El pedido de " + pedido.getUsuario().getEmail() + " ya esta listo para entregar");
        emailDTO.setDetalle("El pedido realizado por " + pedido.getUsuario().getEmail() + ", en el dia " + fechaFormateada + " ya esta terminado");
        emailService.sendEmailPedidoListo(emailDTO, admins.get(0).getUsuario().getEmail());
        return Response.crear(true, "Pedido terminado exitosamente", null);
    } 
}
