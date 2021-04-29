package br.com.ufes.sisgestaoOS.service;

import br.com.ufes.sisgestaoOS.model.Equipe;
import br.com.ufes.sisgestaoOS.repository.EquipeRepository;
import br.com.ufes.sisgestaoOS.service.novo.NovaEquipe;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EquipeService {

    private final EquipeRepository equipeRepository;

    public int create(NovaEquipe equipe) {
        return equipeRepository.create(equipe);
    }
    
    public void update(Equipe equipe) {
    	equipeRepository.update(equipe);
    }

}