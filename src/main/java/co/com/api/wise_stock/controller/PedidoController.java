package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.dto.PatchPedidoDTO;
import co.com.api.wise_stock.dto.PostPedidoDTO;
import co.com.api.wise_stock.dto.RechazarPedidoDTO;
import co.com.api.wise_stock.service.PedidoService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/pedido")
@CrossOrigin
public class PedidoController {
    
    @Autowired
    PedidoService pedidoService;

    @GetMapping
    public Response listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @PostMapping
    public Response crearPedido(@RequestBody PostPedidoDTO postPedidoDTO) {
        return pedidoService.crearPedido(postPedidoDTO);
    }

    @DeleteMapping("/{idPedido}")
    public Response eliminarPedido(@PathVariable("idPedido") Integer idPedido) {
        return pedidoService.eliminarPedido(idPedido);
    }

    @PatchMapping("/listo/{idPedido}")
    public Response pedidoListo(@PathVariable("idPedido") Integer idPedido) {
        return pedidoService.pedidoListo(idPedido);
    }

    @PatchMapping("/rechazar/{idPedido}")
    public Response rechazarPedido(@PathVariable("idPedido") Integer idPedido, @RequestBody RechazarPedidoDTO rechazarPedido) {
        return pedidoService.rechazarPedido(idPedido, rechazarPedido);
    }

    @GetMapping("/operario/{idUsuario}")
    public Response listarPedidosOperario(@PathVariable("idUsuario") Integer idUsuario) {
        return pedidoService.listarPedidosOperario(idUsuario);
    }

    @GetMapping("/detalle/{idPedido}")
    public Response detallePedido(@PathVariable("idPedido") Integer idPedido) {
        return pedidoService.detallePedido(idPedido);
    }

    @PatchMapping("/fechaEstimada/{idPedido}")
    public Response actualizarFechaEstimadaEntrega(@PathVariable("idPedido") Integer idPedido, @RequestBody PatchPedidoDTO patchPedidoDTO) {
        return pedidoService.actualizarFechaEstimadaEntrge(idPedido, patchPedidoDTO);
    }
}
