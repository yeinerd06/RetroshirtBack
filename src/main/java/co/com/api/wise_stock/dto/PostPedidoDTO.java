package co.com.api.wise_stock.dto;

import java.util.List;

import co.com.api.wise_stock.enums.TipoEntregaEnum;
import lombok.Data;

@Data
public class PostPedidoDTO {
    
    private Integer idUsuario;
    private String direccionEntrega;
    private TipoEntregaEnum tipoEntrega;
    private String comprobantePago;
    private String fechaEstimadaEntrega;
    private List<PostArticuloDTO> articulos;
}
