package co.com.api.wise_stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Empresa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = true)
	private String logo;
	@Column(nullable = true)
	private String nombre;
	@Column(nullable = true)
	private String direccion;
	@Column(nullable = true)
	private String celular;
	@Column(nullable = true)
	private String whatsapp;

}
