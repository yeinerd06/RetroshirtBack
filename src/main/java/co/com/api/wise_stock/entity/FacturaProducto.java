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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
@Table(name="factura_producto")
public class FacturaProducto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer facturaId;
	@ManyToOne
	@JoinColumn(name="articulo_id")
	private Articulo articulo;
	private Integer cantidad;
	private Double precio;
	@Column(nullable = true)
	private Boolean devolucion;
	private Date fechaRegistro;
	@OneToMany(mappedBy = "facturaProducto", fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Devolucion>devoluciones;
	

}
