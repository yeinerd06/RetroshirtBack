package co.com.api.wise_stock.dto;

import lombok.Data;

@Data
public class EmailDTO {
	private String titulo;
	private String detalle;
	private int codigo;
	private String asunto;
}
