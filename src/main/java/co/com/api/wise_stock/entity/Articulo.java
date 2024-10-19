package co.com.api.wise_stock.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Articulo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String descripcion;
	private String genero;
	@ManyToOne
	@JoinColumn(name="categoria_id")
	private Categoria categoria;
	@Column(unique = true)
	private String codigo;
	private Double precio;
	private Integer cantidadMinima;
	private String imagen;
	private Integer stock;
	private Boolean estado;

	private Date fechaResgistro;
	@OneToMany(mappedBy = "articulo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ArticuloColor> colores;

	

}
