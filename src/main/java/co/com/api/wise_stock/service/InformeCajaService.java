package co.com.api.wise_stock.service;


import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.entity.InformeCaja;
import co.com.api.wise_stock.repository.InformeCajaRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class InformeCajaService {

	@Autowired
	InformeCajaRepository informeCajaRepository;
	
	public Response listaInforme() {
		return Response.crear(true, "Save Informe Caja", informeCajaRepository.findAll());
	}
	
	public Response saveInformeCaja(InformeCaja informeCaja) {
		
		informeCaja.setFechaRegistro(new Date());
		
		return Response.crear(true, "Save Informe Caja", informeCajaRepository.save(informeCaja));
	}
	
	public Response deleteInformeCaja(Integer id) {
		Optional<InformeCaja>informe=informeCajaRepository.findById(id);
		if(informe.isEmpty()) {
			return Response.crear(false, "Informe no esta registrado", null);
		}
		informeCajaRepository.deleteById(id);
		return Response.crear(true, "Informe eliminado", true);
	}
}
