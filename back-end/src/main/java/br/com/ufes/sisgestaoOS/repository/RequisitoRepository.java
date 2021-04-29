package br.com.ufes.sisgestaoOS.repository;

import br.com.ufes.sisgestaoOS.model.Requisito;
import br.com.ufes.sisgestaoOS.service.novo.NovoRequisito;

public interface RequisitoRepository {

    int create(NovoRequisito req);
    void update(Requisito req);
}
