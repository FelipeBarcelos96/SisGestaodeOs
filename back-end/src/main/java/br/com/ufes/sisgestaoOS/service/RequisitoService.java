package br.com.ufes.sisgestaoOS.service;

import br.com.ufes.sisgestaoOS.model.Requisito;
import br.com.ufes.sisgestaoOS.repository.RequisitoRepository;
import br.com.ufes.sisgestaoOS.service.novo.NovoRequisito;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RequisitoService {

    private final RequisitoRepository requisitoRepository;

    public int create(NovoRequisito requisito) {
        return requisitoRepository.create(requisito);
    }
    
    public void update(Requisito requisito) {
    	requisitoRepository.update(requisito);
    }

}