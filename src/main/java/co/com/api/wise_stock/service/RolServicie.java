package co.com.api.wise_stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.repository.RolRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class RolServicie {
	
	@Autowired
	RolRepository rolRepository;
	
	public Response listadoRoles() {
		return Response.crear(true, "Lista de roles", rolRepository.findAll());
	}

}
