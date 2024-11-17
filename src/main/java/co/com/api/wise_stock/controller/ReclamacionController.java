package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.dto.ConfirmarReclamacionDTO;
import co.com.api.wise_stock.dto.ReclamacionDTO;
import co.com.api.wise_stock.service.ReclamacionService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/reclamacion")
@CrossOrigin
public class ReclamacionController {
    
    @Autowired
    ReclamacionService reclamacionService;

    @GetMapping
    public Response listarReclamaciones() {
        return reclamacionService.listarReclamaciones();
    }

    @PostMapping
    public Response crearReclamacion(@RequestBody ReclamacionDTO reclamacionDTO) {
        return reclamacionService.crearReclamacion(reclamacionDTO);
    }

    @PostMapping("/confirmar/{reclamacionId}")
    public Response confirmarReclamacion(@PathVariable("reclamacionId") Integer reclamacionId, @RequestBody ConfirmarReclamacionDTO confirmarReclamacionDTO) {
        return reclamacionService.confirmarReclamacion(reclamacionId, confirmarReclamacionDTO);
    }
}
