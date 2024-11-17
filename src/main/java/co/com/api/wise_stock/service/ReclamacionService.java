package co.com.api.wise_stock.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.dto.ConfirmarReclamacionDTO;
import co.com.api.wise_stock.dto.ReclamacionDTO;
import co.com.api.wise_stock.entity.Reclamacion;
import co.com.api.wise_stock.entity.Pedido;
import co.com.api.wise_stock.exepciones.PedidoException;
import co.com.api.wise_stock.repository.PedidoRepository;
import co.com.api.wise_stock.repository.ReclamacionRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class ReclamacionService {
    
    @Autowired
    ReclamacionRepository reclamacionRepository;

    @Autowired
    PedidoRepository pedidoRepository;


    public Response crearReclamacion(ReclamacionDTO reclamacionDTO) {
        Reclamacion reclamacion = new Reclamacion();
        Pedido pedido = validarPedido(reclamacionDTO.getPedidoId());
        reclamacion.setPedido(pedido);
        reclamacion.setCantidad(reclamacionDTO.getCantidad());
        reclamacion.setFechaReclamacion(LocalDateTime.now().toString());
        reclamacion.setMotivo(reclamacionDTO.getMotivo());
        reclamacion.setReclamacion(false);
        reclamacionRepository.save(reclamacion);
        return Response.crear(true, "Reclamacion registrada", null);
    }

    public Response listarReclamaciones() {
        return Response.crear(true, "Listado de reclamaciones", reclamacionRepository.findAll());
    }

    public Response confirmarReclamacion(Integer reclamacionId, ConfirmarReclamacionDTO confirmarReclamacionDTO) {
        Optional<Reclamacion> reclamacionOptional = reclamacionRepository.findById(reclamacionId);
        if (!reclamacionOptional.isPresent()) {
            throw new PedidoException("La devolución no existe");
        }
        Reclamacion reclamacion = reclamacionOptional.get();
        reclamacion.setCantidadReclamacion(confirmarReclamacionDTO.getCantidadReclamacion());
        reclamacion.setMotivoReclamacion(confirmarReclamacionDTO.getMotivoReclamacion());
        reclamacion.setFechaConfirmarReclamacion(LocalDateTime.now().toString());
        reclamacion.setReclamacion(true);
        reclamacionRepository.save(reclamacion);
        return Response.crear(true, "Reclamacion confirmada", null);
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
