package co.com.api.wise_stock.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import lombok.Data;

@Entity
@Data
public class Articulo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	@Column(unique = true)
	private String codigo;
	private String marca;
	private Double precio;
	private Integer cantidadMinima;
	private Integer stock;
	private String categoria;
	private String imagen;
	private Boolean estado;

	private Date fechaResgistro;
	
	/*
	 	@OneToMany(mappedBy = "articulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProveedorArticulo> proveedorArticulos = new HashSet<>();
	 */

}
