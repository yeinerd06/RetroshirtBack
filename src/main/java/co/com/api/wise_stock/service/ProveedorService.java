package co.com.api.wise_stock.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.entity.Articulo;
import co.com.api.wise_stock.entity.Proveedor;
import co.com.api.wise_stock.entity.ProveedorArticulo;
import co.com.api.wise_stock.repository.ArticuloRepository;
import co.com.api.wise_stock.repository.ProveedorArticuloRepository;
import co.com.api.wise_stock.repository.ProveedorRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class ProveedorService {

		@Autowired
		ProveedorRepository proveedorRepository;
		@Autowired
		ProveedorArticuloRepository proveedorArticuloRepository;
		@Autowired
		ArticuloRepository articuloRepository;
		
		
		public Response listaProveedores() {
			return Response.crear(true, "Lista", proveedorRepository.findAll());
		}
		
		public Response saveProveedor(Proveedor proveedor) {
			proveedor.setEstado(true);
			proveedor.setFecha(new Date());
			return Response.crear(true, "Save Proveedor", proveedorRepository.save(proveedor));
		}
		public Response saveProveedorArticulo(ProveedorArticulo proveedorArticulo) {
			System.out.println(proveedorArticulo);
			Optional<Articulo>articulo=articuloRepository.findById(proveedorArticulo.getArticulo().getId());
			if(!articulo.isPresent()) {
				return Response.crear(false, "Articulo no esta registrado", null);
			}
			proveedorArticulo.setFechaRegistro(new Date());
			Boolean existeProveedorArticulo=proveedorArticuloRepository.existsByProveedorAndArticulo(proveedorArticulo.getProveedor(), proveedorArticulo.getArticulo().getId());
			if(existeProveedorArticulo) {
				return Response.crear(false, "Este producto ya ha sido agregado al proveedor. ", null);
			}
			ProveedorArticulo proveedorArticuloReturn=proveedorArticuloRepository.save(proveedorArticulo);
			proveedorArticuloReturn.setArticulo(articulo.get());
			return Response.crear(true, "Articulo Proveedor Registrado",
					proveedorArticuloReturn);
		}
		
		public Response updateProveedor(Proveedor proveedor) {
			
			Optional<Proveedor>proveedorCurrent=proveedorRepository.findById(proveedor.getId());
			if(proveedorCurrent.isEmpty()) {
				return Response.crear(false, "Proveedor no esta registrado", null);
			}
			
		  Proveedor	proveedorReturn=proveedorCurrent.get();
		  proveedorReturn.setNombre(proveedor.getNombre());
		  proveedorReturn.setApellido(proveedor.getApellido());
		  proveedorReturn.setBarrio(proveedor.getBarrio());
		  proveedorReturn.setDireccion(proveedor.getDireccion());
		  proveedorReturn.setEmail(proveedor.getEmail());
		  proveedorReturn.setEstado(proveedor.getEstado());
		  
		  return Response.crear(true, "Porveedor Actualizado", proveedorRepository.save(proveedorReturn));
		  
		}
}
