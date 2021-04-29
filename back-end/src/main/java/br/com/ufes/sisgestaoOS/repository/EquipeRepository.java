package br.com.ufes.sisgestaoOS.repository;

import br.com.ufes.sisgestaoOS.model.Equipe;
import br.com.ufes.sisgestaoOS.service.novo.NovaEquipe;

public interface EquipeRepository {

    int create(NovaEquipe equip);
    void update(Equipe equip);
}
