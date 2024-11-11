package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.com.api.wise_stock.service.ComprobanteService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/comprobante")
@CrossOrigin
public class ComprobanteController {
    
    @Autowired
    ComprobanteService comprobanteService;

    @PostMapping
    public Response guardarComprobante(@RequestPart("file") MultipartFile file) {
        return comprobanteService.guardarComprobante(file);
    }
}
