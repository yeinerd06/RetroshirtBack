package co.com.api.wise_stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.com.api.wise_stock.util.AWSS3Service;
import co.com.api.wise_stock.util.Response;

@Service
public class ComprobanteService {
    

    @Autowired
	AWSS3Service awss3Service;
	private static final String rutaComprobante = "/wise-stock/comprobante/";

    public Response guardarComprobante(MultipartFile file) {
        String url = awss3Service.createFolderFile(rutaComprobante, file);
        return Response.crear(true, "Comprobante de pago guardado exitosamente", url);
    }
}
