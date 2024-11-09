package co.com.api.wise_stock.entity;

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
@Table(name="pedido_articulo")
public class PedidoArticulo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
	@JoinColumn(name="pedido_id")
	private Pedido pedido;

    @ManyToOne
	@JoinColumn(name="articulo_id")
	private Articulo articulo;

    @ManyToOne
	@JoinColumn(name="estampado_id")
	private Estampado estampado;

    private Integer cantidad;
}
