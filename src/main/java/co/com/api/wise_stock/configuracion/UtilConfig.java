package co.com.api.wise_stock.configuracion;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de todas las utilidades del proyecto
 * @author Julian Camilo Riveros Fonseca
 */
@Configuration
public class UtilConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    
}
