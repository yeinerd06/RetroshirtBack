package co.com.api.wise_stock.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.entity.Proveedor;
import co.com.api.wise_stock.repository.ProveedorRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class ProveedorService {

		@Autowired
		ProveedorRepository proveedorRepository;
		
		
		public Response listaProveedores() {
			return Response.crear(true, "Lista", proveedorRepository.findAll());
		}
		
		public Response saveProveedor(Proveedor proveedor) {
			proveedor.setEstado(true);
			proveedor.setFecha(new Date());
			return Response.crear(true, "Save Proveedor", proveedorRepository.save(proveedor));
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
