package co.com.api.wise_stock.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.dto.ConfirmarDevolucionDTO;
import co.com.api.wise_stock.dto.DevolucionDTO;
import co.com.api.wise_stock.entity.Devolucion;
import co.com.api.wise_stock.entity.Pedido;
import co.com.api.wise_stock.exepciones.PedidoException;
import co.com.api.wise_stock.repository.DevolucionRepository;
import co.com.api.wise_stock.repository.PedidoRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class DevolucionService {
    
    @Autowired
    DevolucionRepository devolucionRepository;

    @Autowired
    PedidoRepository pedidoRepository;


    public Response crearDevolucion(DevolucionDTO devolucionDTO) {
        Devolucion devolucion = new Devolucion();
        Pedido pedido = validarPedido(devolucionDTO.getPedidoId());
        devolucion.setPedido(pedido);
        devolucion.setCantidad(devolucionDTO.getCantidad());
        devolucion.setFechaDevolucion(LocalDateTime.now().toString());
        devolucion.setMotivo(devolucionDTO.getMotivo());
        devolucion.setDevolucion(false);
        devolucionRepository.save(devolucion);
        return Response.crear(true, "Devolucion registrada", null);
    }

    public Response listarDevoluciones() {
        return Response.crear(true, "Listado de devoluciones", devolucionRepository.findAll());
    }

    public Response confirmarDevolucion(Integer devolucionId, ConfirmarDevolucionDTO confirmarDevolucionDTO) {
        Optional<Devolucion> devolucionOptional = devolucionRepository.findById(devolucionId);
        if (!devolucionOptional.isPresent()) {
            throw new PedidoException("La devolución no existe");
        }
        Devolucion devolucion = devolucionOptional.get();
        devolucion.setCantidadDevolucion(confirmarDevolucionDTO.getCantidadDevolucion());
        devolucion.setMotivoDevolucion(confirmarDevolucionDTO.getMotivoDevolucion());
        devolucion.setFechaConfirmarDevolucion(LocalDateTime.now().toString());
        devolucion.setDevolucion(true);
        devolucionRepository.save(devolucion);
        return Response.crear(true, "Devolucion confirmada", null);
    }

    private Pedido validarPedido(Integer idPedido) {
        // Verificar si el pedido existe
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(idPedido);
        if (!pedidoOptional.isPresent()) {
            throw new PedidoException("El pedido no existe");
        }
        return pedidoOptional.get();
    }
}
