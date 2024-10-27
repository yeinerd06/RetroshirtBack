package co.com.api.wise_stock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class ProveedorPedidoArticulo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="proveedor_pedido_id")
	private Integer proveedorPedido;
	@ManyToOne
	@JoinColumn(name="proveedor_articulo_id")
	private ProveedorArticulo proveedorArticulo;
	@ManyToOne(optional = true)
	@JoinColumn(name="devolucion_proveedor_id")
	private DevolucionProveedor devolucionProveedor;
	@ManyToOne
	@JoinColumn(name="color_id")
	private Color color;
	@ManyToOne
	@JoinColumn(name="talla_id")
	private Talla talla;
	private Integer cantidad;
	@Column(nullable = true)
	private Boolean confirmado;
	@Column(nullable = true)
	private Boolean devolucion;
	@Column(nullable = true)
	private Integer cantidadDevolucion;
	@Column(nullable = true)
	private String motivoDevolucion;

}
