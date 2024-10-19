package co.com.api.wise_stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.entity.Color;
import co.com.api.wise_stock.repository.CategoriaRepository;
import co.com.api.wise_stock.repository.ColorRepository;
import co.com.api.wise_stock.repository.TallaRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class CategoriaColorTallaService {
	
	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Autowired
	ColorRepository colorRepository;
	
	@Autowired
	TallaRepository tallaRepository;
	
	public Response listaCategorias() {
		return Response.crear(true, "Lista Categorias", categoriaRepository.findAll());
	}
	
	public Response listaColores() {
		return Response.crear(true, "Lista Colores", colorRepository.findAll());
	}
	
	public Response listaTallas() {
		return Response.crear(true, "Lista Tallas", tallaRepository.findAll());
	}
	
	

}
