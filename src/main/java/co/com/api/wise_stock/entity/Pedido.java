package co.com.api.wise_stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.com.api.wise_stock.enums.TipoEntregaEnum;
import lombok.Data;

@Entity
@Data
@Table(name="pedido")
public class Pedido {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;

    @Column( name = "fecha_pedido")
	private String fechaPedido;
    
	@Column( name = "fecha_estimada")
	private String fechaEstimadaEntrega;

    @Column( name = "fecha_entrega")
	private String fechaEntrega;

    @Column( name = "direccion_entrega")
	private String direccionEntrega;

    @Column( name = "tipo_entrega")
	private TipoEntregaEnum tipoEntrega;

    @Column( name = "comprobante_pago")
	private String comprobantePago;

    @ManyToOne
	@JoinColumn(name="operario_asignado")
	private Usuario operarioAsignado;

}
