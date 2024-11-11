package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.service.DevolucionService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/devolucion")
@CrossOrigin
public class Devolucioncontroller {
    
    @Autowired
    DevolucionService devolucionService;

    public Response rechazarPedido(@PathVariable("idPedido") Integer idPedido) {
        return devolucionService.rechazarPedido(idPedido);
    }
}
