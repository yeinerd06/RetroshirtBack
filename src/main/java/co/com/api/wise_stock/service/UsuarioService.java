package co.com.api.wise_stock.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.com.api.wise_stock.dto.EmailDTO;
import co.com.api.wise_stock.dto.UsuarioDto;
import co.com.api.wise_stock.entity.CodigoCambio;
import co.com.api.wise_stock.entity.Rol;
import co.com.api.wise_stock.entity.RolUsuario;
import co.com.api.wise_stock.entity.Usuario;
import co.com.api.wise_stock.repository.CodigoCambioRepository;
import co.com.api.wise_stock.repository.RolRepository;
import co.com.api.wise_stock.repository.RolUsuarioRepository;
import co.com.api.wise_stock.repository.UsuarioReporitory;
import co.com.api.wise_stock.util.AWSS3Service;
import co.com.api.wise_stock.util.EmailService;
import co.com.api.wise_stock.util.Response;

@Service
public class UsuarioService {

	@Autowired
	UsuarioReporitory usuarioReporitory;
	@Autowired
	RolRepository rolRepository;
	@Autowired
	RolUsuarioRepository rolUsuarioRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	EmailService emailService;
	@Autowired
	CodigoCambioRepository codigoCambioRepository;
	@Autowired
	AWSS3Service awss3Service;
	private static final String rutaPerfil = "/wise-stock/perfil/";

	public Response saveUsuario(UsuarioDto registroDTO, MultipartFile file) {

		Optional<Usuario> usuarioOptional = usuarioReporitory.findByEmail(registroDTO.getEmail().toUpperCase());

		if (usuarioOptional.isPresent()) {
			return Response.crear(false, "Usuario ya esta registrado", null);
		}

		Usuario usuario = new Usuario();
		usuario.setNombre(registroDTO.getNombre());
		usuario.setApellido(registroDTO.getApellido());
		usuario.setEmail(registroDTO.getEmail().toUpperCase());
		usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
		usuario.setFecha(new Date());
		usuario.setEstado(true);
		Optional<Rol> rol = rolRepository.findById(registroDTO.getRol().getId());
		if (rol.isPresent()) {
			if (!file.isEmpty()) {
				String url = awss3Service.createFolderFile(rutaPerfil, file);
				usuario.setImagen(url);
			}
			Usuario usuarioReturn = usuarioReporitory.save(usuario);
			RolUsuario rolUsuario = new RolUsuario();
			rolUsuario.setUsuario(usuarioReturn);
			rolUsuario.setRolId(rol.get().getId());
			rolUsuarioRepository.save(rolUsuario);
			// Agrega el rol a la lista de roles del usuario
			usuarioReturn.getRoles().add(rol.get());
			return Response.crear(true, "Usuario registrado", usuarioReturn);

		}

		return Response.crear(false, "Error", null);
	}

	public Response updateUsuario(UsuarioDto usuario, MultipartFile file) {
		
	    
		Optional<Usuario>usuarioOptional=usuarioReporitory.findById(usuario.getId());
		 if (usuarioOptional.isEmpty()) {
	            return Response.crear(false, "Usuario no existe", null);
	        }

        Optional<RolUsuario> rolUsuarioOptional = rolUsuarioRepository.findByUsuario(usuarioOptional.get());

        if (rolUsuarioOptional.isEmpty()) {
            return Response.crear(false, "UsuarioRol no existe", null);
        }

        RolUsuario rolUsuario = rolUsuarioOptional.get();
        Usuario usuarioActualizado =usuarioOptional.get();

       

        // Actualizar los campos del usuario
        usuarioActualizado.setNombre(usuario.getNombre());
        usuarioActualizado.setApellido(usuario.getApellido());
        usuarioActualizado.setEmail(usuario.getEmail().toUpperCase());
        usuarioActualizado.setEstado(usuario.getEstado());
        if (file != null) {
            if (usuarioActualizado.getImagen() != null) {
                // Obtener la key de la imagen existente
                String[] urlParts = usuarioActualizado.getImagen().split("/");
                String key = urlParts[urlParts.length - 1]; // Obtener el nombre del archivo

               // awss3Service.deleteFile(rutaPerfil, key);
            }

          
            String url = awss3Service.createFolderFile(rutaPerfil, file);
            usuarioActualizado.setImagen(url);
        }

        // Actualizar el rol si es diferente
        
        boolean actualizoRol=false;
        if (!usuario.getRol().getId().equals(rolUsuario.getRolId())) {
           
            rolUsuario.setRolId(usuario.getRol().getId());
            rolUsuarioRepository.save(rolUsuario);
            actualizoRol=true;
        }

        // Guardar el usuario actualizado
        Usuario usuarioReturn = usuarioReporitory.save(usuarioActualizado);
        if(actualizoRol) {
        	usuarioReturn.getRoles().clear();
        	usuarioReturn.getRoles().add(usuario.getRol());
        }
        return Response.crear(true, "Usuario Actualizado", usuarioReturn);
	}

	public Response deleteUsuario(Integer id) {
		Optional<Usuario>usuario=usuarioReporitory.findById(id);
		if(usuario.isEmpty()) {
			return Response.crear(false, "Id no esta registrado", null);
		}
		Usuario usuarioReturn=usuario.get();
		usuarioReturn.setEstado(false);
		usuarioReporitory.save(usuarioReturn);
		return Response.crear(true,	"Usuario eliminado", null);
	}
	
	public Response listadoUsuarios() {
		return Response.crear(true, "listado de usuarios", usuarioReporitory.findAll());
	}

	public Response sendCodigoCambio(CodigoCambio codigoCambio) {

		Optional<Usuario> usuario = usuarioReporitory.findByEmail(codigoCambio.getEmail().toUpperCase());

		if (!usuario.isPresent()) {
			return Response.crear(false, "Usario no esta registrado", null);
		}

		Integer codigo = generarCodigo(6);
		String uuid = UUID.randomUUID().toString();
		codigoCambio.setCodigo(codigo + "");
		codigoCambio.setUuid(uuid);
		codigoCambio.setFecha(new Date());

		CodigoCambio codigoReturn = codigoCambioRepository.save(codigoCambio);

		if (codigoReturn.getId() != null) {
			EmailDTO emailDTO = new EmailDTO();
			emailDTO.setCodigo(codigo);
			emailDTO.setAsunto("WISE STOCK - RECUPERAR CONTRASEÑA");
			emailDTO.setDetalle("Este codigo es para verificar su email y cambiar su contraseña");
			emailDTO.setTitulo("WISE STOCK ");

			if (emailService.sendEmailCambio(codigoCambio, emailDTO)) {
				return Response.crear(true, "Email enviado", uuid);
			}

		}

		return Response.crear(false, "Error ", null);

	}

	public Response validarCodigoCambioPassword(String uuid, String codigo, CodigoCambio codigoCurrent) {
		Optional<CodigoCambio> codigoRegistro = codigoCambioRepository.findByUuid(uuid);

		if (codigoRegistro.isEmpty()) {
			return Response.crear(false, "uuid no registrado", null);
		}
		if (!codigoRegistro.get().getCodigo().equals(codigo)) {
			return Response.crear(false, "Codigo incorrecto", null);
		}

		Optional<Usuario> usuarioOptional = usuarioReporitory.findByEmail(codigoCurrent.getEmail().toUpperCase());
		if (usuarioOptional.isEmpty()) {
			return Response.crear(false, "Usuario no encontrado", null);
		}

		Usuario usuario = usuarioOptional.get();
		usuario.setPassword(passwordEncoder.encode(codigoCurrent.getPassword()));
		usuarioReporitory.save(usuario);

		return Response.crear(true, "Contraseña actualizada", null);
	}

	public int generarCodigo(int longitud) {
		Random random = new Random();
		int min = (int) Math.pow(10, longitud - 1);
		int max = (int) Math.pow(10, longitud) - 1;
		return random.nextInt(max - min + 1) + min;
	}

	public Response registrarCliente(UsuarioDto usuarioDto, MultipartFile file) {
		Optional<Usuario> usuarioOptional = usuarioReporitory.findByEmail(usuarioDto.getEmail().toUpperCase());

		if (usuarioOptional.isPresent()) {
			return Response.crear(false, "Usuario ya esta registrado", null);
		}

		Usuario usuario = new Usuario();
		usuario.setNombre(usuarioDto.getNombre());
		usuario.setApellido(usuarioDto.getApellido());
		usuario.setEmail(usuarioDto.getEmail().toUpperCase());
		usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
		usuario.setFecha(new Date());
		usuario.setEstado(true);
		if (!file.isEmpty()) {
			String url = awss3Service.createFolderFile(rutaPerfil, file);
			usuario.setImagen(url);
		}
		Optional<Rol> rol = rolRepository.findById(2);
		Usuario usuarioReturn = usuarioReporitory.save(usuario);
		RolUsuario rolUsuario = new RolUsuario();
		rolUsuario.setUsuario(usuarioReturn);
		rolUsuario.setRolId(rol.get().getId());
		rolUsuarioRepository.save(rolUsuario);

		return Response.crear(true, "Usuario registrado exitosamente", null);
	}

}
