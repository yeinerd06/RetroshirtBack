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
@Table(name="devolucion")
public class Devolucion {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @ManyToOne
	@JoinColumn(name="pedido_id")
	private Pedido pedido;
    
	private String motivo;

	private Integer cantidad;

    @Column( name = "fecha_devolucion")
    private String fechaDevolucion;

	@Column( name = "fecha_conrfirmardevolucion", nullable = true)
	private String fechaConfirmarDevolucion;

	private Boolean devolucion;

	private String motivoDevolucion;
	
	@Column(nullable = true)
	private Integer cantidadDevolucion;
}
