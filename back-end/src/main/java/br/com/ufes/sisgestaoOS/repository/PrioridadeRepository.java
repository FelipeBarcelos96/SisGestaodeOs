package br.com.ufes.sisgestaoOS.repository;

import br.com.ufes.sisgestaoOS.model.Prioridade;
import br.com.ufes.sisgestaoOS.service.novo.NovaPrioridade;

public interface PrioridadeRepository {

    int create(NovaPrioridade prioridade);
    void update(Prioridade prioridade);
}
