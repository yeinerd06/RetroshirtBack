package co.com.api.wise_stock.entity;

import java.util.Date;
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

import lombok.Data;

@Entity
@Data
public class ProveedorPedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String uuid;
	@Column(nullable = true)
	private Integer codigoFactura;
	private Integer total;
	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	@ManyToOne
	@JoinColumn(name="proveedor_id")
	private Proveedor proveedor;
	@Column(nullable = true)
	private Date fechaPedido;
	@Column(nullable = true)
	private Date fechaEntrega;
	@Column(nullable = true)
	private Boolean pedidoPendiente;
	private Boolean estadoPedido;
	private String nota;
	private Date fechaRegistro;
	@OneToMany(mappedBy = "proveedorPedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ProveedorPedidoArticulo> productos;
	@Column(nullable = true)
	private Boolean devolucion;
	

}
