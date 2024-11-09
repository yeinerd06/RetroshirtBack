package co.com.api.wise_stock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.com.api.wise_stock.entity.EstadoPedido;
import co.com.api.wise_stock.enums.EstadoEnum;

public interface EstadoPedidoRepository extends JpaRepository <EstadoPedido, Integer> {

    List<EstadoPedido> findByPedidoId(Integer pedidoId);

    Optional<EstadoPedido> findByPedidoIdAndEstado(Integer pedidoId, EstadoEnum estado);

    @Query("SELECT ep FROM EstadoPedido ep WHERE ep.fechaCambio = (SELECT MAX(e.fechaCambio) FROM EstadoPedido e WHERE e.pedido.id = ep.pedido.id)")
    List<EstadoPedido> findLastEstadoForEachPedido();
}
