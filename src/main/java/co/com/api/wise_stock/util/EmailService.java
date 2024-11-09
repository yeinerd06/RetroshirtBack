package co.com.api.wise_stock.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import co.com.api.wise_stock.dto.EmailDTO;
import co.com.api.wise_stock.entity.CodigoCambio;



@Service
public class EmailService  {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private TemplateEngine templateEngine;

	
	public boolean sendEmailCambio(CodigoCambio codigoRegistro,  EmailDTO email) {
		try {

			MimeMessage mimeMessageHelpe = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessageHelpe, "UTF-8");

			// FECHA GENERA EL EMAIL
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es"));
			String fechaFormateada = sdf.format(fecha);

			Context context = new Context();
			context.setVariable("titulo", email.getTitulo());
			context.setVariable("codigo", email.getCodigo());
			context.setVariable("detalle", email.getDetalle());
			//context.setVariable("usuario", usuario);
			context.setVariable("fecha", fechaFormateada);
			String htmlContent = templateEngine.process("email-template", context);
			messageHelper.setTo(codigoRegistro.getEmail());
			messageHelper.setSubject(email.getAsunto());

			messageHelper.setText(htmlContent, true);

			javaMailSender.send(mimeMessageHelpe);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendEmailPedidoListo( EmailDTO email, String emailTo) {
		try {
		
			MimeMessage mimeMessageHelpe = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessageHelpe, "UTF-8");

			// FECHA GENERA EL EMAIL
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es"));
			String fechaFormateada = sdf.format(fecha);

			Context context = new Context();
			context.setVariable("titulo", email.getTitulo());
			context.setVariable("detalle", email.getDetalle());
			context.setVariable("fecha", fechaFormateada);
			String htmlContent = templateEngine.process("email-pedidolisto", context);
			messageHelper.setTo(emailTo);
			messageHelper.setSubject(email.getAsunto());

			messageHelper.setText(htmlContent, true);

			javaMailSender.send(mimeMessageHelpe);

			return true;
		} catch( Exception e ) {
			e.printStackTrace();
			return false;
		}
	}
	
}
