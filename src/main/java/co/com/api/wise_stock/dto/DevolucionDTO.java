package co.com.api.wise_stock.dto;

import lombok.Data;

@Data
public class DevolucionDTO {
	private Integer pedidoId;    
	private String motivo;
    private Integer cantidad;
}
