package co.com.api.wise_stock.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "proveedor_articulo")
public class ProveedorArticulo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer id;
	
	@Column(name="proveedor_id")
	private Integer proveedor;

	@ManyToOne
	@JoinColumn(name = "articulo_id")
	private Articulo articulo;
	
	private Integer precioCompra;
	private Date fechaRegistro;
	@Column(nullable = true)
	private Boolean estado;

}
