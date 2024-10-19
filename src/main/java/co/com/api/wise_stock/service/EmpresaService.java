package co.com.api.wise_stock.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.entity.Empresa;
import co.com.api.wise_stock.repository.EmpresaRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class EmpresaService {
	
	@Autowired
	EmpresaRepository empresaRepository;
	
	public Response informacionEmpresa() {
		return  Response.crear(true, "Información de la empresa", empresaRepository.findById(1));
	}
	
	public Response updateInformacionEmpresa(Empresa empresa) {
		
		Optional<Empresa>empresaCurrent=empresaRepository.findById(1);
		if(!empresaCurrent.isPresent()) {
			return Response.crear(false, "Error buscando empresa", null);
		}
		Empresa empresaReturn=empresaCurrent.get();
		
		empresaReturn.setNombre(empresa.getNombre().toUpperCase());
		empresaReturn.setDireccion(empresa.getDireccion());
		empresaReturn.setCelular(empresa.getCelular());
		empresaReturn.setWhatsapp(empresa.getWhatsapp());
		
		
		
		return Response.crear(true, "Actualizar informacion de la empresa",empresaRepository.save(empresaReturn) );
	}

}
