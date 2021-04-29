package br.com.ufes.sisgestaoOS.repository;

import br.com.ufes.sisgestaoOS.model.Ordem;
import br.com.ufes.sisgestaoOS.service.novo.NovaOrdem;

public interface OrdemRepository {

    int create(NovaOrdem ordem);
    void update(Ordem ordem);
}
