package br.com.ufes.sisgestaoOS.service;

import br.com.ufes.sisgestaoOS.model.Ordem;
import br.com.ufes.sisgestaoOS.repository.OrdemRepository;
import br.com.ufes.sisgestaoOS.service.novo.NovaOrdem;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrdemService {

    private final OrdemRepository ordemRepository;

    public int create(NovaOrdem ordem) {
        return ordemRepository.create(ordem);
    }
    
    public void update(Ordem ordem) {
    	ordemRepository.update(ordem);
    }

}