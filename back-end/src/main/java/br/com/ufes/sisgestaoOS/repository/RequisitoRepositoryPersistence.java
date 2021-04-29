package br.com.ufes.sisgestaoOS.repository;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.ufes.sisgestaoOS.model.Requisito;
import br.com.ufes.sisgestaoOS.model.Status;
import br.com.ufes.sisgestaoOS.service.novo.NovoRequisito;
import br.com.ufes.sisgestaoOS.service.novo.NovoStatus;
import br.com.ufes.sisgestaoOS.dao.RequisitoDao;
import br.com.ufes.sisgestaoOS.dao.StatusDao;

public class RequisitoRepositoryPersistence implements RequisitoRepository {

    private static final Map<String, Requisito> REQUISITOS_STORE = new ConcurrentHashMap<String, Requisito>();

    @Override
    public int create(NovoRequisito novoRequisito) {
    	Requisito requisito = new Requisito(
    			novoRequisito.getAnalista()
    			,novoRequisito.getTitulo()
    			,novoRequisito.getDescricao()
    			,novoRequisito.getPrazo()
        		);
        try {
        	RequisitoDao.getInstance().save(requisito);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
        
        int codStatus = requisito.getCodReq();
        
        REQUISITOS_STORE.put(requisito.getTitulo(), requisito);

        return codStatus;
    }
    
    @Override
    public void update(Requisito requisito) {
        try {
        	RequisitoDao.getInstance().update(requisito);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
    }
}
