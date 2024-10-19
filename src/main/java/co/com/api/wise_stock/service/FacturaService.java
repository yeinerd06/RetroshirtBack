package co.com.api.wise_stock.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.entity.Articulo;
import co.com.api.wise_stock.entity.Cliente;
import co.com.api.wise_stock.entity.Devolucion;
import co.com.api.wise_stock.entity.Factura;
import co.com.api.wise_stock.entity.FacturaProducto;
import co.com.api.wise_stock.entity.TipoFactura;
import co.com.api.wise_stock.repository.ArticuloRepository;
import co.com.api.wise_stock.repository.ClienteRepository;
import co.com.api.wise_stock.repository.DevolucionRepository;
import co.com.api.wise_stock.repository.FacturaProductoRepository;
import co.com.api.wise_stock.repository.FacturaRepository;
import co.com.api.wise_stock.repository.TipoFacturaRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class FacturaService {

	@Autowired
	FacturaRepository facturaRepository;

	@Autowired
	FacturaProductoRepository facturaProductoRepository;
	@Autowired
	ClienteRepository clienteRepository;
	@Autowired
	ArticuloRepository articuloRepository;
	@Autowired
	TipoFacturaRepository tipoFacturaRepository;
	@Autowired
	DevolucionRepository devolucionRepository;

	public Response listadoFacturas() {
		return Response.crear(true, "Listado de facturas", facturaRepository.findAll());
	}
	
	public Response savePagoFactura(Integer id) {
		
		Optional<Factura>factura=facturaRepository.findById(id);
		if(factura.isEmpty()) {
			return Response.crear(false, "Factura no esta registrada", null);
		}
		Factura facturaReturn=factura.get();
		facturaReturn.setPagoEfectivo(true);
		facturaRepository.save(facturaReturn);
		return Response.crear(true, "save pago factura", facturaReturn);
	}

	public Response saveFacturaVenta(Factura factura) {

		try {
			Date fecha = new Date();
			// Tipo factura venta id 1

			factura.setFechaRegistro(fecha);
			Double total = factura.getPrecioTotal();
			factura.setPrecioBase(total * 0.81);
			factura.setIva(total * 0.19);
			factura.setDevolucionTotal(0.0);
			factura.setDevolucion(false);
			if (factura.getCliente() != null) {
				Optional<Cliente> clienteCurrent = clienteRepository
						.findByDocumento(factura.getCliente().getDocumento());
				if (clienteCurrent.isPresent()) {
					factura.setCliente(clienteCurrent.get());
				} else {
					factura.setCliente(clienteRepository.save(factura.getCliente()));
				}
			}

			Factura facturaCurren = facturaRepository.save(factura);
			List<FacturaProducto> listadoProducto = new ArrayList<>();
			for (FacturaProducto facturaProducto : factura.getProductos()) {

				Articulo articulo = articuloRepository.findById(facturaProducto.getArticulo().getId()).get();
				Integer stock = articulo.getStock();
				Integer cantidad = facturaProducto.getCantidad();
				Boolean facturaCompra = facturaCurren.getTipoFactura().getId() == 2 ? true : false;
				if (facturaCompra) {
					facturaProducto.setFacturaId(facturaCurren.getId());
					facturaProducto.setFechaRegistro(fecha);
					listadoProducto.add(facturaProducto);
					articulo.setStock(stock + cantidad);
					if(!articulo.getEstado()) {
						articulo.setEstado(true);
					}
					articuloRepository.save(articulo);

				} else {
					if (stock >= cantidad) {
						facturaProducto.setFacturaId(facturaCurren.getId());
						facturaProducto.setFechaRegistro(fecha);
						listadoProducto.add(facturaProducto);
						Integer stockNuevo=stock - cantidad;
						articulo.setStock(stockNuevo);
						if(stockNuevo==0) {
							articulo.setEstado(false);
						}
						articuloRepository.save(articulo);

					}
				}

			}
			facturaProductoRepository.saveAll(listadoProducto);
			Factura facturaReturn = facturaRepository.findById(facturaCurren.getId()).get();
			return Response.crear(true, "Factura registrada", facturaReturn);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return Response.crear(false, "Error registrando factura", e);
		}

	}

	public Response saveDevolucion(Integer facturaId, List<Devolucion> devoluciones,Integer nuevoTotal) {

		Optional<Factura> factura = facturaRepository.findById(facturaId);
		if (factura.isEmpty()) {
			return Response.crear(false, "La factura no existe ", null);
		}
		Double devolucionTotal = 0.0;
		if (devoluciones.isEmpty()) {
			return Response.crear(false, "La lista de devolucion esta vacia", null);
		}
		Boolean facturaCompra = factura.get().getTipoFactura().getId() == 2 ? true : false;
		for (Devolucion devolucion : devoluciones) {

			Optional<FacturaProducto> facturaProducto = facturaProductoRepository
					.findById(devolucion.getFacturaProducto().getId());
			if (facturaProducto.isPresent()) {
				
				Optional<Articulo> articulo = articuloRepository.findById(facturaProducto.get().getArticulo().getId());
				Integer stockActual = articulo.get().getStock();
				Integer cantidadDevolver = devolucion.getCantidadDevolucion();

				devolucionTotal += devolucion.getPrecio()*devolucion.getCantidadDevolucion();
				Articulo articuloReturn = articulo.get();
				Integer stockNew=0;
				if (facturaCompra) {
					stockNew= stockActual - cantidadDevolver;
					
				} else {
					stockNew= stockActual + cantidadDevolver;
				}
				if(stockNew<=0) {
					articuloReturn.setEstado(false);
					articuloReturn.setStock(0);
				}
				devolucion.setFacturaProducto(facturaProducto.get());
				articuloReturn.setStock(stockNew);
				articuloRepository.save(articuloReturn);
				devolucionRepository.save(devolucion);

			}

		}

		Factura facturaCurrent = factura.get();
		System.err.println(nuevoTotal);
		if(facturaCompra) {
			
			facturaCurrent.setPrecioTotal((double)nuevoTotal);
		}
		facturaCurrent.setDevolucion(true);
		facturaCurrent.setDevolucionTotal(facturaCurrent.getDevolucionTotal() + devolucionTotal);
		facturaRepository.save(facturaCurrent);
		Factura facturaReturn = facturaRepository.findById(facturaCurrent.getId()).get();

		return Response.crear(true, "Devolucion registrada", facturaReturn);

	}
	
	
	public Response updateFactura(Factura factura) {
		
		Optional<Factura>facturaOptional=facturaRepository.findById(factura.getId());
		if(facturaOptional.isEmpty()) {
			return Response.crear(true, "Factura no esta registrada", facturaOptional);
		}
		Date fecha = new Date();
		Factura facturaCurren =facturaOptional.get();
		Boolean facturaCompra = facturaCurren.getTipoFactura().getId() == 2 ? true : false;
		Double totalNuevo=factura.getPrecioTotal();
		facturaCurren.setPrecioTotal(totalNuevo);
		facturaCurren.setPrecioBase(totalNuevo*0.81);
		facturaCurren.setIva(totalNuevo*0.19);
		
		if(factura.getCodigoFactura()!=null) {
			facturaCurren.setCodigoFactura(factura.getCodigoFactura());
		}
		Factura facturaR=facturaRepository.save(facturaCurren);
		//Eliminos los productos viejos
		for (FacturaProducto facturaProductoOld : facturaCurren.getProductos()) {
			Articulo articulo=facturaProductoOld.getArticulo();
			Integer cantidad=facturaProductoOld.getCantidad();
			Integer stockActual=articulo.getStock();
			Integer stockNuevo=0;
			if(facturaCompra) {
				stockNuevo=stockActual-cantidad;
			}else {
				stockNuevo=stockActual+cantidad;
			}
			if(stockNuevo>=0) {
				
				articulo.setStock(stockNuevo);
				if(stockNuevo==0) {
					articulo.setEstado(false);
				}else {
					articulo.setEstado(true);
				}
				articuloRepository.save(articulo);
				facturaProductoRepository.deleteById(facturaProductoOld.getId());
				
			}
			
			
		}
		
		//Guardo los productos nuevos
		List<FacturaProducto> listadoProducto = new ArrayList<>();
		
		for (FacturaProducto facturaProducto : factura.getProductos()) {

			Articulo articulo = articuloRepository.findById(facturaProducto.getArticulo().getId()).get();
			Integer stock = articulo.getStock();
			Integer cantidad = facturaProducto.getCantidad();
			if (facturaCompra) {
				facturaProducto.setFacturaId(facturaCurren.getId());
				facturaProducto.setFechaRegistro(fecha);
				listadoProducto.add(facturaProducto);
				articulo.setStock(stock + cantidad);
				if(!articulo.getEstado()) {
					articulo.setEstado(true);
				}
				articuloRepository.save(articulo);

			} else {
				if (stock >= cantidad) {
					facturaProducto.setFacturaId(facturaCurren.getId());
					facturaProducto.setFechaRegistro(fecha);
					listadoProducto.add(facturaProducto);
					Integer stockNuevo=stock - cantidad;
					articulo.setStock(stockNuevo);
					if(stockNuevo==0) {
						articulo.setEstado(false);
					}
					articuloRepository.save(articulo);

				}
			}

		}
		
		facturaProductoRepository.saveAll(listadoProducto);
		Factura facturaReturn = facturaRepository.findById(facturaCurren.getId()).get();
		return Response.crear(true, "Factura registrada", facturaReturn);
		
		
	}

}
