package co.com.api.wise_stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name="devolucion")
public class Devolucion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer cantidadDevolucion;
	@ManyToOne
	@JoinColumn(name="factura_producto_id")
	@JsonBackReference
	private FacturaProducto facturaProducto;
	private Integer usuario;
	private Double precio;
	@Column(nullable = true)
	private String motivo;
	private Integer fecha;

}
