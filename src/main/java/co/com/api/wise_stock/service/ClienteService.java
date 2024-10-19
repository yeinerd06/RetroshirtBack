package co.com.api.wise_stock.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.entity.Cliente;
import co.com.api.wise_stock.repository.ClienteRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class ClienteService {
	
	@Autowired
	ClienteRepository clienteRepository;
	
	public Response findByDocumento(String documento) {
		Optional<Cliente>cliente=clienteRepository.findByDocumento(documento);
		if(cliente.isEmpty()) {
			return Response.crear(false, "cliente no registrado", null);
		}
		return Response.crear(true, "cliente encontrado", cliente.get());
	}

}
