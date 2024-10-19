package co.com.api.wise_stock.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import antlr.collections.List;
import co.com.api.wise_stock.entity.Rol;
import lombok.Data;

@Data
public class UsuarioDto {

	private Integer id;
	private String nombre;
	private String apellido;
	private String email;
	private String password;
	private String imagen;
	private Boolean estado;
	private Date fecha;
	private Rol rol;
}
