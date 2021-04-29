package br.com.ufes.sisgestaoOS.service;

import br.com.ufes.sisgestaoOS.model.Status;
import br.com.ufes.sisgestaoOS.repository.StatusRepository;
import br.com.ufes.sisgestaoOS.service.novo.NovoStatus;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public int create(NovoStatus status) {
        return statusRepository.create(status);
    }
    
    public void update(Status status) {
    	statusRepository.update(status);
    }

}