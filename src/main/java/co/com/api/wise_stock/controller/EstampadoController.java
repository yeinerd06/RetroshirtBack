package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.com.api.wise_stock.dto.AlertaTintaDTO;
import co.com.api.wise_stock.service.EstampadoService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/estampado")
@CrossOrigin
public class EstampadoController {
    
    @Autowired
    EstampadoService estampadoService;

    @PostMapping
    public Response guardarEstampado(@RequestPart("nombre") String nombre, @RequestPart("precio") String precio, @RequestPart("file") MultipartFile file) {
        return estampadoService.guardarEstampado(nombre, precio, file);
    }

    @GetMapping
    public Response listarEstampados() {
        return estampadoService.listarEstampados();
    }

    @PostMapping("/alerta")
    public Response alertaTinta(@RequestBody AlertaTintaDTO alertaTintaDTO ) {
        return estampadoService.alertaTintaEstampar(alertaTintaDTO);
    }
}
