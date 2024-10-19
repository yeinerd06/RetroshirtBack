package co.com.api.wise_stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.Categoria;

public interface CategoriaRepository  extends JpaRepository<Categoria, Integer>{

}
