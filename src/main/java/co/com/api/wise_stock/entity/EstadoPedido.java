package co.com.api.wise_stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.com.api.wise_stock.enums.EstadoEnum;
import lombok.Data;

@Entity
@Data
@Table(name="estado_pedido")
public class EstadoPedido {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="pedido_id")
	private Pedido pedido;

    private EstadoEnum estado;

    @Column( name = "fecha_cambio")
    private String fechaCambio;
}
