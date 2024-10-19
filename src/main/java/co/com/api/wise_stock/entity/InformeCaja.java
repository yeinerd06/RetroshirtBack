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
@Table(name="informe_caja")
public class InformeCaja {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	private Integer numeroFacturas;
	private String startDate;
	private String endDate;
	private Double total;
	private Double totalCompra;
	private Double iva;
	private Double precioBase;
	private Date fechaRegistro;

}
