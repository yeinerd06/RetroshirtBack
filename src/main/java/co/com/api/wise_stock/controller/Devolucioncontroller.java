package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.dto.ConfirmarDevolucionDTO;
import co.com.api.wise_stock.dto.DevolucionDTO;
import co.com.api.wise_stock.service.DevolucionService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/devolucion")
@CrossOrigin
public class Devolucioncontroller {
    
    @Autowired
    DevolucionService devolucionService;

    @GetMapping
    public Response listarDevoluciones() {
        return devolucionService.listarDevoluciones();
    }

    @PostMapping
    public Response crearDevolucion(@RequestBody DevolucionDTO devolucionDTO) {
        return devolucionService.crearDevolucion(devolucionDTO);
    }

    @PostMapping("/confirmar/{devolucionId}")
    public Response confirmarDevolucion(@PathVariable("devolucionId") Integer devolucionId, @RequestBody ConfirmarDevolucionDTO confirmarDevolucionDTO) {
        return devolucionService.confirmarDevolucion(devolucionId, confirmarDevolucionDTO);
    }
}
