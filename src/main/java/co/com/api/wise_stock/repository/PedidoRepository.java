package co.com.api.wise_stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.com.api.wise_stock.entity.Pedido;
import co.com.api.wise_stock.entity.Usuario;

import java.util.List;


public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
    
    @Query("SELECT p FROM Pedido p WHERE p.operarioAsignado.id = :operarioId AND p.fechaEstimadaEntrega BETWEEN :fechaInicio AND :fechaFin")
    List<Pedido> findPedidosByOperarioAndFechaEstimadaEntregaBetween(
            @Param("operarioId") Integer operarioId,
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );

    List<Pedido> findByOperarioAsignado(Usuario operarioAsignado);
}
