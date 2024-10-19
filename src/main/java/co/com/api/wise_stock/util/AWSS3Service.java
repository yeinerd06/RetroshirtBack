package co.com.api.wise_stock.util;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Interface: AWSS3Service
 * @author : Ciro Quintero
 * @Descripción: Interfaz servicio AWSS3Service con los metodos para gestionar
 *               las operaciones relacionadas con Amazon S3.
 */
public interface AWSS3Service {

	public String uploadFile(String ruta, MultipartFile file);

	public InputStream downloadFile(String  ruta, String key);

	public boolean deleteFile(String  ruta, String key);

	public String createFolderFile(String  ruta, MultipartFile file);
}
