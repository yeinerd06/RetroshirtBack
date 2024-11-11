package co.com.api.wise_stock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.Articulo;
import co.com.api.wise_stock.entity.Pedido;
import co.com.api.wise_stock.entity.PedidoArticulo;


public interface PedidoArticuloRepository extends JpaRepository<PedidoArticulo, Integer> {
    
    Optional<PedidoArticulo>  findByPedidoAndArticulo(Pedido pedido, Articulo articulo);

    List<PedidoArticulo> findByPedidoIn(List<Pedido> pedidos);

    List<PedidoArticulo> findByPedido(Pedido pedido);
}
