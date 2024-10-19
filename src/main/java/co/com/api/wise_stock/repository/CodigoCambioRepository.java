package co.com.api.wise_stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.CodigoCambio;

public interface CodigoCambioRepository  extends JpaRepository<CodigoCambio, Integer>{
	
	Optional<CodigoCambio>findByUuid(String uuid);
}
