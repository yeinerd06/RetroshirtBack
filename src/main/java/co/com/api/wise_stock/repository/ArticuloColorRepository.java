package co.com.api.wise_stock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.ArticuloColor;
import co.com.api.wise_stock.entity.Color;

public interface ArticuloColorRepository  extends JpaRepository<ArticuloColor, Integer>{
	Optional<ArticuloColor> findByArticuloAndColor(Integer articuloId, Color color);

}
