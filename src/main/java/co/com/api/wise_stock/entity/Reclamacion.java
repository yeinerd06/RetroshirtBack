package co.com.api.wise_stock.entity;

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
@Table(name="reclamacion")
public class Reclamacion {
        @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @ManyToOne
	@JoinColumn(name="pedido_id")
	private Pedido pedido;
    
	private String motivo;

	private Integer cantidad;

    @Column( name = "fecha_reclamacion")
    private String fechaReclamacion;

	@Column( name = "fecha_conrfirmarreclamacion", nullable = true)
	private String fechaConfirmarReclamacion;

	private Boolean reclamacion;

	private String motivoReclamacion;
	
	@Column(nullable = true)
	private Integer cantidadReclamacion;
}
