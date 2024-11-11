package co.com.api.wise_stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.Devolucion;

public interface DevolucionRepository extends JpaRepository <Devolucion, Integer>{
    
}
