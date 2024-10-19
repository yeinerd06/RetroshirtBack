package co.com.api.wise_stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.api.wise_stock.entity.Empresa;

public interface EmpresaRepository  extends JpaRepository<Empresa, Integer>{

}
