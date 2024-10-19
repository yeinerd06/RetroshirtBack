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
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="factura")
public class Factura {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = true,unique = true)
	private Integer facturaId;
	@Column(nullable = true)
	private String descripcion;
	@ManyToOne
	@JoinColumn(name="estado_factura_id")
	private EstadoFactura estadoFactura;
	@Column(nullable = true)
	private Date fechaPedido;
	@Column(nullable = true)
	private Date fechaEntrega;
	@ManyToOne
	@JoinColumn(name="tipo_factura_id")
	private TipoFactura tipoFactura;
	@ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = true) 
	private Proveedor proveedor;
	private Double precioBase;
	private Double precioTotal;
	private Double iva;
	@Column(nullable = true)
	private Double devolucionTotal;
	@Column(nullable = true)
	private Boolean devolucion;
	private Boolean pagoEfectivo;
	@Column(nullable = true)
	private String codigoFactura;
	private Date fechaPago;
	@ManyToOne
    @JoinColumn(name = "cliente_id", nullable = true) 
    private Cliente cliente;
	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	@Column(nullable = true)
	private String devolucionMotivo;
	private Date fechaRegistro;
	@OneToMany(mappedBy = "facturaId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<FacturaProducto>productos;
	
	

}
