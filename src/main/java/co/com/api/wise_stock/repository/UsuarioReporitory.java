package co.com.api.wise_stock.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.Usuario;



public interface UsuarioReporitory extends JpaRepository<Usuario, Integer>{
	Optional<Usuario> findByEmail(String email);
	List<Usuario> findByEstado(Boolean estado);
}
