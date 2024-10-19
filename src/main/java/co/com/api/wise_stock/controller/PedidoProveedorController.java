package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.entity.ProveedorPedido;
import co.com.api.wise_stock.service.PedidoProveedorService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/pedido/proveedor")
@CrossOrigin
public class PedidoProveedorController {
	
	@Autowired
	PedidoProveedorService pedidoProveedorService;
	
	@GetMapping
	public Response listaPedidosProveedor() {
		return pedidoProveedorService.listaPedidosProveedor();
	}
	
	@PostMapping("/save")
	public Response saveProveedorPedido(@RequestBody ProveedorPedido proveedorPedido) {
		return pedidoProveedorService.savePedidoProveedor(proveedorPedido);
	}
	
	@PutMapping("/confirmar")
	public Response confirmarPedidoProveedor(@RequestBody ProveedorPedido proveedorPedido) {
		return pedidoProveedorService.confirmarPedidoProveedor(proveedorPedido);
	}
	

}
