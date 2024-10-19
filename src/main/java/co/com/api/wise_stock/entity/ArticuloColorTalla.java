package co.com.api.wise_stock.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
@Entity
@Data
public class ArticuloColorTalla {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	@JoinColumn(name="articulo_color_id")
	private ArticuloColor articuloColor;
	@ManyToOne
	@JoinColumn(name="talla_id")
	private Talla talla;
	private Integer sotck;
	private Double precio;

}
