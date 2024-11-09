package co.com.api.wise_stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.com.api.wise_stock.dto.EstampadoDTO;
import co.com.api.wise_stock.entity.Estampado;
import co.com.api.wise_stock.repository.EstampadoRepository;
import co.com.api.wise_stock.util.AWSS3Service;
import co.com.api.wise_stock.util.Response;

@Service
public class EstampadoService {
    
    @Autowired
    EstampadoRepository estampadoRepository;

    @Autowired
	AWSS3Service awss3Service;

	private static final String rutaEstampado = "/wise-stock/estampado/";

    public Response guardarEstampado(String nombre, String precio, MultipartFile file) {
        Estampado estampado = new Estampado();
        if (!file.isEmpty()) {
            String url = awss3Service.createFolderFile(rutaEstampado, file);
            estampado.setImagen(url);
        }
        estampado.setNombre(nombre);
        estampado.setPrecio(precio);
        estampadoRepository.save(estampado);

        return Response.crear(true, "Estampado guardado exitosamente", null);
    }

    public Response listarEstampados() {
        return Response.crear(true, "Listado de estampados", estampadoRepository.findAll());
    }
}
