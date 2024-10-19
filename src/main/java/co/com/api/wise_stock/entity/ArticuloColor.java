package co.com.api.wise_stock.entity;


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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
public class ArticuloColor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	//@ManyToOne
	@Column(name="articulo_id")
	private Integer articulo;
	@ManyToOne
	@JoinColumn(name="color_id")
	private Color color;
	 // Nueva relación OneToMany con ArticuloColorTalla
	 @OneToMany(mappedBy = "articuloColor")
	    @JsonIgnoreProperties("articuloColor") 
    private List<ArticuloColorTalla> tallas;

}
