package br.com.ufes.sisgestaoOS.repository;

import br.com.ufes.sisgestaoOS.model.Status;
import br.com.ufes.sisgestaoOS.service.novo.NovoStatus;

public interface StatusRepository {

    int create(NovoStatus status);
    void update(Status status);
}
