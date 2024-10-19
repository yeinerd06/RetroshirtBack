package co.com.api.wise_stock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.RolUsuario;
import co.com.api.wise_stock.entity.Usuario;



public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Integer> {
	List<RolUsuario> findByRolId(Integer rolId);
	Optional<RolUsuario>findByUsuario(Usuario usuario);
}
