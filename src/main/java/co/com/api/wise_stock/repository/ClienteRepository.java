package co.com.api.wise_stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.Cliente;

public interface ClienteRepository  extends JpaRepository<Cliente, Integer>{
	
	Optional<Cliente>findByDocumento(String documento);

}
