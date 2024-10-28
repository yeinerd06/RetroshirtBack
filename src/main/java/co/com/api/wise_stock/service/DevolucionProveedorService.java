package co.com.api.wise_stock.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.entity.Articulo;
import co.com.api.wise_stock.entity.ArticuloColor;
import co.com.api.wise_stock.entity.ArticuloColorTalla;
import co.com.api.wise_stock.entity.Color;
import co.com.api.wise_stock.entity.DevolucionProveedor;
import co.com.api.wise_stock.entity.ProveedorArticulo;
import co.com.api.wise_stock.entity.ProveedorPedido;
import co.com.api.wise_stock.entity.ProveedorPedidoArticulo;
import co.com.api.wise_stock.repository.ArticuloColorTallaRepository;
import co.com.api.wise_stock.repository.DevolucionProveedorRepository;
import co.com.api.wise_stock.repository.ProveedorPedidoArticuloRepository;
import co.com.api.wise_stock.repository.ProveedorPedidoRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class DevolucionProveedorService {
	
	@Autowired
	ProveedorPedidoRepository proveedorPedidoRepository;
	@Autowired
	ProveedorPedidoArticuloRepository proveedorPedidoArticuloRepository;
	@Autowired
	DevolucionProveedorRepository devolucionProveedorRepository;
	@Autowired
	ArticuloColorTallaRepository articuloColorTallaRepository;
	
	
	public Response confirmarDevolucion(ProveedorPedido proveedorPedido) {
	    try {
	        proveedorPedido.setDevolucionConfirmada(true);  // Marca la devolución como confirmada
	       // System.out.println(proveedorPedido.toString());
	        List<ProveedorPedidoArticulo> listaProductos = proveedorPedido.getProductos();
	        
	        for (ProveedorPedidoArticulo proveedorPedidoArticulo : listaProductos) {
	            // Si hay un objeto DevolucionProveedor, procede a actualizar la devolución
	            if (proveedorPedidoArticulo.getDevolucionProveedor() != null) {
	                DevolucionProveedor devolucionProveedor = proveedorPedidoArticulo.getDevolucionProveedor();
	                devolucionProveedor.setEstadoDevolucion(true);  // Actualiza el estado de devolución a confirmada
	                devolucionProveedor.setFechaConfirmarDevolucion(new Date());  // Registra la fecha de confirmación
	                devolucionProveedorRepository.save(devolucionProveedor);  // Guarda los cambios de la devolución

	                Integer cantidadDevolucionInicial=devolucionProveedor.getCantidad();
	                // Actualiza el stock después de confirmar la devolución
	                Integer cantidadConfirmadaDevolucion = devolucionProveedor.getCantidadDevolucion() != null ? devolucionProveedor.getCantidadDevolucion() :0;
	                
	                Integer cantidadNeta=cantidadDevolucionInicial-cantidadConfirmadaDevolucion;
	                
	                Articulo articuloCurrent = proveedorPedidoArticulo.getProveedorArticulo().getArticulo();
	                ArticuloColor articuloColorCurrent = buscarArticuloColor(articuloCurrent, proveedorPedidoArticulo.getColor());

	                if (articuloColorCurrent == null) {
	                    return Response.crear(false, "Error: el color del artículo no existe", null);
	                }

	                Optional<ArticuloColorTalla> articuloColorTallaCurrent = articuloColorTallaRepository.findByArticuloColorAndTalla(articuloColorCurrent, proveedorPedidoArticulo.getTalla());
	                if (articuloColorTallaCurrent.isPresent()) {
	                    ArticuloColorTalla articuloColorTalla = articuloColorTallaCurrent.get();
	                    articuloColorTalla.setStock(articuloColorTalla.getStock() + cantidadNeta);  // Resta la cantidad devuelta del stock
	                    articuloColorTallaRepository.save(articuloColorTalla);  // Guarda el nuevo stock
	                    //System.out.println(articuloColorTalla);
	                } else {
	                    return Response.crear(false, "Error: la talla del artículo no existe", null);
	                }
	              

	                proveedorPedidoArticuloRepository.save(proveedorPedidoArticulo);  // Guarda el artículo actualizado
	            }
	        }

	        proveedorPedidoRepository.save(proveedorPedido);  // Guarda el pedido actualizado

	        return Response.crear(true, "Devolución confirmada exitosamente", proveedorPedido);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.crear(false, "Error confirmando la devolución", e);
	    }
	}
	
	public ArticuloColor buscarArticuloColor(Articulo articulo, Color color) {
	    List<ArticuloColor> articuloColors = articulo.getColores();
	    for (ArticuloColor articuloColor : articuloColors) {
	        if (articuloColor.getColor().equals(color)) { // Utiliza .equals() en lugar de ==
	            return articuloColor;
	        }
	    }
	    return null;
	}


}
