package co.com.api.wise_stock.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;






/**
 * @Clase: AWSS3ServiceImpl
 * @author : Ciro Quintero
 * @Descripción: Implementacion del servicio AWSS3Service para gestionar las
 *               operaciones relacionadas con Amazon S3.
 */
@Service
public class AWSS3ServiceImpl implements AWSS3Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3ServiceImpl.class);

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucketName;


	@Override
	public String uploadFile(String ruta, MultipartFile file) {
	    // Remove leading and trailing slashes from ruta
	    String folderKey = ruta.replaceAll("^/+", "").replaceAll("/+$", "");
	    if (!folderKey.isEmpty()) {
	        folderKey += "/";
	    }
	    String newFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	    
	    LOGGER.info("Subiendo archivo con el nombre... " + newFileName);
	    ObjectMetadata metadata = new ObjectMetadata();
	    metadata.setContentLength(file.getSize());
	    metadata.setContentType(file.getContentType());
	    try (InputStream inputStream = file.getInputStream()) {
	        PutObjectRequest request = new PutObjectRequest(bucketName, folderKey + newFileName, inputStream, metadata);
	        amazonS3.putObject(request);
	        
	        // Return the URL of the uploaded file
	        return amazonS3.getUrl(bucketName, folderKey + newFileName).toString();
	    } catch (IOException e) {
	        LOGGER.error(e.getMessage(), e);
	    }
	    return null;
	}



	@Override
	public InputStream downloadFile(String ruta, String key) {

		String filePath = ruta.endsWith("/") ? ruta + key : ruta + "/" + key;
		S3Object object = amazonS3.getObject(bucketName, filePath);
		return object.getObjectContent();
	}

	@Override
	public boolean deleteFile(String ruta, String key) {
	    String filePath = ruta.endsWith("/") ? ruta + key : ruta + "/" + key;
	    LOGGER.info("Eliminando archivo con la clave... " + filePath);
	    try {
	        amazonS3.deleteObject(bucketName, filePath);
	        LOGGER.info("Archivo eliminado exitosamente: " + filePath);
	        return true;
	    } catch (AmazonS3Exception e) {
	        LOGGER.error("Error al eliminar el archivo de S3: " + e.getErrorMessage(), e);
	        return false;
	    } catch (SdkClientException e) {
	        LOGGER.error("Error en la solicitud HTTP: " + e.getMessage(), e);
	        return false;
	    }
	}


	@Override
	public String createFolderFile(String ruta, MultipartFile file) {

		String folderKey = ruta;

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderKey, emptyContent, metadata);
		amazonS3.putObject(putObjectRequest);
		return uploadFile(folderKey, file);

	}



}
