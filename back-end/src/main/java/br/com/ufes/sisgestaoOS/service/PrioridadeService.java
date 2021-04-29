package br.com.ufes.sisgestaoOS.service;

import br.com.ufes.sisgestaoOS.model.Prioridade;
import br.com.ufes.sisgestaoOS.repository.PrioridadeRepository;
import br.com.ufes.sisgestaoOS.service.novo.NovaPrioridade;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PrioridadeService {

    private final PrioridadeRepository prioridadeRepository;

    public int create(NovaPrioridade prioridade) {
        return prioridadeRepository.create(prioridade);
    }
    
    public void update(Prioridade prioridade) {
    	prioridadeRepository.update(prioridade);
    }

}