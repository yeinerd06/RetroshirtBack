package co.com.api.wise_stock.dto;

import lombok.Data;

@Data
public class LoginDTO {

	private String email;
	private String password;
	private Integer codigo;
	private String modulo;

	

}
