package co.com.api.wise_stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.com.api.wise_stock.entity.ProveedorArticulo;

public interface ProveedorArticuloRepository extends JpaRepository<ProveedorArticulo, Integer> {
	 @Query("SELECT COUNT(pa) > 0 FROM ProveedorArticulo pa WHERE pa.proveedor = :proveedorId AND pa.articulo.id = :articuloId")
	    boolean existsByProveedorAndArticulo(@Param("proveedorId") Integer proveedorId, @Param("articuloId") Integer articuloId);

	 
}
