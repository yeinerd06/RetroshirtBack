package co.com.api.wise_stock.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data

public class Proveedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer id;

	@Column(unique = true)
	private String documento;

	private String nombre;
	private String apellido;
	private String telefono;
	private String email;
	private String direccion;
	private String barrio;
	@Column(name="estado",nullable = true)
	private Boolean estado;
	private Date fecha;
	

}
