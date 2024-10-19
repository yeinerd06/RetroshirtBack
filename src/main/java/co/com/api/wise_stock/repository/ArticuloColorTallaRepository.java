package co.com.api.wise_stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.ArticuloColor;
import co.com.api.wise_stock.entity.ArticuloColorTalla;
import co.com.api.wise_stock.entity.Talla;

public interface ArticuloColorTallaRepository extends JpaRepository<ArticuloColorTalla, Integer> {
	
	Optional<ArticuloColorTalla> findByArticuloColorAndTalla(ArticuloColor articuloColor, Talla talla);
}
