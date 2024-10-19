package co.com.api.wise_stock.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.com.api.wise_stock.entity.Articulo;
import co.com.api.wise_stock.entity.ArticuloColor;
import co.com.api.wise_stock.repository.ArticuloColorRepository;
import co.com.api.wise_stock.repository.ArticuloRepository;
import co.com.api.wise_stock.repository.ProveedorRepository;
import co.com.api.wise_stock.util.AWSS3Service;
import co.com.api.wise_stock.util.Response;

@Service
public class ArticuloService {
	@Autowired
	ArticuloRepository articuloRepository;
	@Autowired
	ArticuloColorRepository articuloColorRepository;

	@Autowired
	ProveedorRepository proveedorRepository;

	@Autowired
	AWSS3Service awss3Service;
	private static final String rutaArticulo = "/wise-stock/articulo/";
	
	
	public Response listadoArticulos() {
		return Response.crear(true, "Listado articulos", articuloRepository.findAll());
	}

	public Response saveArticulo(Articulo articulo, MultipartFile file) {
		try {

			if (file != null) {
				String urlImagen = awss3Service.createFolderFile(rutaArticulo, file);
				articulo.setImagen(urlImagen);

			}
			articulo.setCategoria(articulo.getCategoria());
			articulo.setFechaResgistro(new Date());
			Articulo articuloReturn=articuloRepository.save(articulo);
			for (ArticuloColor articuloColor : articulo.getColores()) {
				articuloColor.setArticulo(articuloReturn.getId());
				articuloColorRepository.save(articuloColor);
				
			}
			return Response.crear(true, "Articulo registrado",articuloReturn );
		} catch (Exception e) {
			System.err.print(e);
			return Response.crear(false, "error registrando articulo", e);
			// TODO: handle exception
		}

	}
	
	public Response updateArticulo(Articulo articulo, MultipartFile file) {
	    try {
	        // Buscar el artículo actual por su ID
	        Optional<Articulo> articuloCurrent = articuloRepository.findById(articulo.getId());
	        if (articuloCurrent.isEmpty()) {
	            return Response.crear(false, "Error: El artículo no está registrado", null);
	        }

	        Articulo articuloReturn = articuloCurrent.get();

	        // Actualizar los atributos del artículo
	        articuloReturn.setCategoria(articulo.getCategoria());
	        articuloReturn.setNombre(articulo.getNombre());
	        articuloReturn.setPrecio(articulo.getPrecio());
	        articuloReturn.setStock(articulo.getStock());
	        articuloReturn.setCantidadMinima(articulo.getCantidadMinima());
	        articuloReturn.setEstado(articulo.getEstado());
	        articuloReturn.setCodigo(articulo.getCodigo());

	        // Verificar si se ha subido un archivo y actualizar la imagen
	        if (file != null) {
	            String url = awss3Service.createFolderFile(rutaArticulo, file);
	            articuloReturn.setImagen(url);
	        }

	        // Iterar sobre los colores enviados en la solicitud
	        for (ArticuloColor nuevoColor : articulo.getColores()) {
	            // Verificar si el color ya existe para el artículo
	            Optional<ArticuloColor> colorExistente = articuloColorRepository.findByArticuloAndColor(articuloReturn.getId(), nuevoColor.getColor());
	            
	            if (!colorExistente.isPresent()) {
	            	 // Si no existe, agregar el nuevo color al artículo
	                nuevoColor.setArticulo(articuloReturn.getId());
	                articuloColorRepository.save(nuevoColor);
	            } 
	        }

	        // Guardar el artículo actualizado
	        return Response.crear(true, "Artículo actualizado correctamente", articuloRepository.save(articuloReturn));

	    } catch (Exception e) {
	        // Manejar errores
	        return Response.crear(false, "Error actualizando artículo", e);
	    }
	}


}
