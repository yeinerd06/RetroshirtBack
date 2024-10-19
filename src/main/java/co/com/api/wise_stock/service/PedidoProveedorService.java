package co.com.api.wise_stock.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.entity.Articulo;
import co.com.api.wise_stock.entity.ArticuloColor;
import co.com.api.wise_stock.entity.ArticuloColorTalla;
import co.com.api.wise_stock.entity.Color;
import co.com.api.wise_stock.entity.ProveedorPedido;
import co.com.api.wise_stock.entity.ProveedorPedidoArticulo;
import co.com.api.wise_stock.entity.Talla;
import co.com.api.wise_stock.repository.ArticuloColorRepository;
import co.com.api.wise_stock.repository.ArticuloColorTallaRepository;
import co.com.api.wise_stock.repository.ArticuloRepository;
import co.com.api.wise_stock.repository.ProveedorPedidoArticuloRepository;
import co.com.api.wise_stock.repository.ProveedorPedidoRepository;
import co.com.api.wise_stock.repository.ProveedorRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class PedidoProveedorService {
	
	@Autowired
	ProveedorRepository proveedorRepository;
	@Autowired
	ProveedorPedidoRepository proveedorPedidoRepository;
	@Autowired
	ProveedorPedidoArticuloRepository proveedorPedidoArticuloRepository;
	@Autowired
	ArticuloRepository articuloRepository;
	@Autowired
	ArticuloColorRepository articuloColorRepository;
	@Autowired
	ArticuloColorTallaRepository articuloColorTallaRepository;
	
	public Response listaPedidosProveedor() {
		return Response.crear(true, "Listado de pedidos proveedor", proveedorPedidoRepository.findAll());
	}
	
	
	public Response savePedidoProveedor( ProveedorPedido proveedorPedido) {
		
		
		try {
			Date fecha=new Date();
			String uuid = UUID.randomUUID().toString();
			proveedorPedido.setFechaRegistro(fecha);
			proveedorPedido.setUuid(uuid);
			proveedorPedido.setEstadoPedido(false);
			ProveedorPedido proveedorPedidoCurrent=proveedorPedidoRepository.save(proveedorPedido);
			
			List<ProveedorPedidoArticulo>listaArticulos=proveedorPedido.getProductos();
			Integer idProveedorPedido=proveedorPedidoCurrent.getId();
			for (ProveedorPedidoArticulo proveedorPedidoArticulo : listaArticulos) {
				proveedorPedidoArticulo.setProveedorPedido(idProveedorPedido);
				
			}
			proveedorPedidoArticuloRepository.saveAll(listaArticulos);
			
			Optional<ProveedorPedido>ProveedorPedidoReturn=proveedorPedidoRepository.findById(idProveedorPedido);
			return Response.crear(true, "Pedido Proveedor Registrado", ProveedorPedidoReturn.get());
			
			
		} catch (Exception e) {
			// TODO: handle exception
			return Response.crear(false, "Error creando pedido proveedor", e);
		}
		
		
	}
	public Response confirmarPedidoProveedor(ProveedorPedido proveedorPedido) {
	    try {
	        Optional<ProveedorPedido> pedidoCurrent = proveedorPedidoRepository.findById(proveedorPedido.getId());
	        if (!pedidoCurrent.isPresent()) {
	            return Response.crear(false, "Pedido Proveedor no está registrado", null);
	        }
	        if (pedidoCurrent.get().getEstadoPedido()) {
	            return Response.crear(false, "Pedido proveedor ya fue confirmado", pedidoCurrent.get());
	        }

	        ProveedorPedido pedidoActualizado = pedidoCurrent.get();
	        pedidoActualizado.setEstadoPedido(true);
	        pedidoActualizado.setPedidoPendiente(false);
	        pedidoActualizado.setTotal(proveedorPedido.getTotal());
	        pedidoActualizado.setCodigoFactura(proveedorPedido.getCodigoFactura());
	        pedidoActualizado.setDevolucion(proveedorPedido.getDevolucion());

	        List<ProveedorPedidoArticulo> listaArticulos = proveedorPedido.getProductos();
	        for (ProveedorPedidoArticulo proveedorPedidoArticulo : listaArticulos) {
	            // Obtengo color, talla, devoluciones y cantidad original
	            Color colorCurrent = proveedorPedidoArticulo.getColor();
	            Talla tallaCurrent = proveedorPedidoArticulo.getTalla();
	            Boolean devolucionArticulo = proveedorPedidoArticulo.getDevolucion() != null ? proveedorPedidoArticulo.getDevolucion() : false;
	            Integer cantidadDevolucion = devolucionArticulo ? proveedorPedidoArticulo.getCantidadDevolucion() : 0;
	            Integer cantidadOriginal = proveedorPedidoArticulo.getCantidad();
	            
	            // Verificar si la devolución es mayor a la cantidad original (esto no debería ser permitido)
	            if (cantidadDevolucion > cantidadOriginal) {
	                return Response.crear(false, "Error: la cantidad de devolución es mayor que la cantidad original para el artículo", null);
	            }

	            // Cantidad neta es la que se va a agregar al stock
	            Integer cantidadNeta = cantidadOriginal - cantidadDevolucion;

	            // Busco el ArticuloColor
	            Articulo articuloCurrent = proveedorPedidoArticulo.getProveedorArticulo().getArticulo();
	            ArticuloColor articuloColorCurrent = buscarArticuloColor(articuloCurrent, colorCurrent);
	            if (articuloColorCurrent == null) {
	                return Response.crear(false, "Error: el color del artículo no existe", null);
	            }

	            // Busco o creo un ArticuloColorTalla
	            Optional<ArticuloColorTalla> articuloColorTallaCurrent = articuloColorTallaRepository.findByArticuloColorAndTalla(articuloColorCurrent, tallaCurrent);
	            ArticuloColorTalla articuloColorTalla;
	            if (articuloColorTallaCurrent.isPresent()) {
	                articuloColorTalla = articuloColorTallaCurrent.get();
	                // Actualizo el stock sumando la cantidad neta
	                articuloColorTalla.setStock(articuloColorTalla.getStock() + cantidadNeta);
	            } else {
	                // Creo un nuevo ArticuloColorTalla si no existe
	                articuloColorTalla = new ArticuloColorTalla();
	                articuloColorTalla.setArticuloColor(articuloColorCurrent);
	                articuloColorTalla.setTalla(tallaCurrent);
	                articuloColorTalla.setStock(cantidadNeta); // Seteo el stock inicial
	            }
	            System.out.print(articuloColorTalla.toString());
	            // Guardar ArticuloColorTalla actualizado o creado
	            articuloColorTallaRepository.save(articuloColorTalla);
	            
	            // Actualizo el ProveedorPedidoArticulo en la base de datos
	            proveedorPedidoArticuloRepository.save(proveedorPedidoArticulo);
	        }

	        // Guardo el pedido actualizado en la base de datos
	        proveedorPedidoRepository.save(pedidoActualizado);

	        return Response.crear(true, "Pedido proveedor confirmado", pedidoActualizado);
	    } catch (Exception e) {
	        // Manejar y loggear excepciones
	        e.printStackTrace();
	        return Response.crear(false, "Error confirmando el pedido proveedor", e);
	    }
	}

	public Response confirmarPedidoProveedor2(ProveedorPedido proveedorPedido) {
		try {
			
			Optional<ProveedorPedido>pedidoCurrent=proveedorPedidoRepository.findById(proveedorPedido.getId());
			if(!pedidoCurrent.isPresent()) {
				return Response.crear(false, "Pedido Proveedor no esta registrado", null);
			}
			if(pedidoCurrent.get().getEstadoPedido()) {
				return Response.crear(false, "Pedido proveedor ya fue confirmado", pedidoCurrent.get());
			}
			
			ProveedorPedido pedidoActualizado=pedidoCurrent.get();
			System.out.println(pedidoActualizado.toString());
			pedidoActualizado.setEstadoPedido(true);
			pedidoActualizado.setPedidoPendiente(false);
			pedidoActualizado.setTotal(proveedorPedido.getTotal());
			pedidoActualizado.setCodigoFactura(proveedorPedido.getCodigoFactura());
			pedidoActualizado.setDevolucion(proveedorPedido.getDevolucion());
			
			List<ProveedorPedidoArticulo>listaArticulos=proveedorPedido.getProductos();
			Integer idProveedorPedido=pedidoActualizado.getId();
			for (ProveedorPedidoArticulo proveedorPedidoArticulo : listaArticulos) {
				//Obtengo el color
				Color colorCurrent=proveedorPedidoArticulo.getColor();
				//Obtengo la talla;
				Talla tallaCurrent=proveedorPedidoArticulo.getTalla();
				//Verifico si el articulo tiene devolucion
				Boolean devolucionArticulo=proveedorPedidoArticulo.getDevolucion() !=null ?proveedorPedidoArticulo.getDevolucion():false;
				//Obtengo la cantidad de devolucion
				Integer cantidadDevolucion=devolucionArticulo ? proveedorPedidoArticulo.getCantidadDevolucion():0;
				//Obtengo la cantidad original
				Integer cantidadOriginal=proveedorPedidoArticulo.getCantidad();
				//CantidadNeta es la cantidad que se va aumenta en articuloColorTalla
				Integer cantidadNeta=cantidadOriginal-cantidadDevolucion;
				//Obtengo el articulo del pedido
				Articulo articuloCurrent=proveedorPedidoArticulo.getProveedorArticulo().getArticulo();
				// Busco el articuloColor del pedido
				ArticuloColor articuloColorCurrent=buscarArticuloColor(articuloCurrent, proveedorPedidoArticulo.getColor());
				if(articuloColorCurrent==null) {
					return Response.crear(false, "Error articulo color no existe", null);
				}
				//Creo un objeto ArticuloColorTalla
				ArticuloColorTalla articuloColorTalla= new ArticuloColorTalla();
				//Busco el ArticuloColorTalla del pedido 
				Optional<ArticuloColorTalla> articuloColorTallaCurrent=articuloColorTallaRepository.findByArticuloColorAndTalla(articuloColorCurrent,tallaCurrent );
				
				//Si existe ArticuloColorTalla le agrego la cantidad nueva 
				if(articuloColorTallaCurrent.isPresent()) {
					articuloColorTalla=articuloColorTallaCurrent.get();
					Integer stockAcutal=articuloColorTallaCurrent.get().getStock();
					articuloColorTalla.setStock(stockAcutal+cantidadNeta);
					
				}else {
					articuloColorTalla.setArticuloColor(articuloColorCurrent);
					articuloColorTalla.setTalla(tallaCurrent);
					articuloColorTalla.setStock(cantidadNeta);
				}
				
				articuloColorTallaRepository.save(articuloColorTalla);
				proveedorPedidoArticuloRepository.save(proveedorPedidoArticulo);
				
			
			}
			proveedorPedidoRepository.save(pedidoActualizado);
			
			
			return Response.crear(true, "Pedido proveedor confirmado", pedidoActualizado);
		} catch (Exception e) {
			// TODO: handle exception
			return Response.crear(false, "Error comfirmando pedido proveedor", e);
		}
	}
	
	public ArticuloColor buscarArticuloColor(Articulo articulo , Color color) {
		
		List<ArticuloColor>articuloColors=articulo.getColores();
		for (ArticuloColor articuloColor : articuloColors) {
			if(articuloColor.getColor().getId()==color.getId()) {
				return articuloColor;
			}
		}
		return null;
		
	}

}
