package co.com.api.wise_stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.api.wise_stock.repository.DevolucionRepository;
import co.com.api.wise_stock.util.Response;

@Service
public class DevolucionService {
    
    @Autowired
    DevolucionRepository devolucionRepository;

    public Response rechazarPedido(Integer idPedido) {
        return Response.crear(null, null, idPedido);
    }
}
