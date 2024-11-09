package co.com.api.wise_stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.PedidoArticulo;

public interface PedidoArticuloRepository extends JpaRepository<PedidoArticulo, Integer> {
    
}
