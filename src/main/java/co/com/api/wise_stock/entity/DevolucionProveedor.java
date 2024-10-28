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

public class DevolucionProveedor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer cantidad;
	private String motivo;
	private Boolean estadoDevolucion;
	private Date fechaDevolucion;
	@Column(nullable = true)
	private Boolean devolucion;
	@Column(nullable = true)
	private Integer cantidadDevolucion;
	@Column(nullable = true)
	private String motivoDevolucion;
	@Column(nullable = true)
	private Date fechaConfirmarDevolucion;

}
