package co.com.api.wise_stock.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.com.api.wise_stock.dto.AlertaTintaDTO;
import co.com.api.wise_stock.dto.EmailDTO;
import co.com.api.wise_stock.entity.Estampado;
import co.com.api.wise_stock.entity.RolUsuario;
import co.com.api.wise_stock.repository.EstampadoRepository;
import co.com.api.wise_stock.repository.RolUsuarioRepository;
import co.com.api.wise_stock.util.AWSS3Service;
import co.com.api.wise_stock.util.EmailService;
import co.com.api.wise_stock.util.Response;

@Service
public class EstampadoService {
    
    @Autowired
    EstampadoRepository estampadoRepository;

    @Autowired
	EmailService emailService;

    @Autowired
    RolUsuarioRepository rolUsuarioRepository;

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

    public Response alertaTintaEstampar(AlertaTintaDTO alertaTintaDTO) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es"));
        LocalDateTime fechaActual = LocalDateTime.now();
        String fechaFormateada = fechaActual.format(formatter);

        List<RolUsuario> admins = rolUsuarioRepository.findByRolId(1);
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAsunto("Alerta por tinta para estampar");
        emailDTO.setTitulo("Se ha generado una alerta el " + fechaFormateada);
        emailDTO.setDetalle(alertaTintaDTO.getMsg());
        emailService.sendEmailPedidoListo(emailDTO, admins.get(0).getUsuario().getEmail());

        return Response.crear(true, "Alerta generada exitosamente", null);
    }

}
