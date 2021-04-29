package br.com.ufes.sisgestaoOS.repository;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.ufes.sisgestaoOS.model.Status;
import br.com.ufes.sisgestaoOS.service.novo.NovoStatus;
import br.com.ufes.sisgestaoOS.dao.StatusDao;

public class StatusRepositoryPersistence implements StatusRepository {

    private static final Map<String, Status> STATUS_STORE = new ConcurrentHashMap<String, Status>();

    @Override
    public int create(NovoStatus novoStatus) {
    	Status status = new Status(
    			novoStatus.getStatus()
        		);
        try {
        	StatusDao.getInstance().save(status);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
        
        int codStatus = status.getCodStatus();
        
        STATUS_STORE.put(status.getStatus(), status);

        return codStatus;
    }
    
    @Override
    public void update(Status status) {
        try {
        	StatusDao.getInstance().update(status);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
    }
}
