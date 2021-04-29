package br.com.ufes.sisgestaoOS.repository;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.ufes.sisgestaoOS.model.Prioridade;
import br.com.ufes.sisgestaoOS.service.novo.NovaPrioridade;
import br.com.ufes.sisgestaoOS.dao.PrioridadeDao;

public class PrioridadeRepositoryPersistence implements PrioridadeRepository {

    private static final Map<String, Prioridade> PRIORIDADES_STORE = new ConcurrentHashMap<String, Prioridade>();

    @Override
    public int create(NovaPrioridade novaPrioridade) {
    	Prioridade prioridade = new Prioridade(
    			novaPrioridade.getNome()
        		);
        try {
        	PrioridadeDao.getInstance().save(prioridade);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
        
        int id = prioridade.getId();
        
        PRIORIDADES_STORE.put(prioridade.getNome(), prioridade);

        return id;
    }
    
    @Override
    public void update(Prioridade prio) {
        try {
        	PrioridadeDao.getInstance().update(prio);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
    }
}
