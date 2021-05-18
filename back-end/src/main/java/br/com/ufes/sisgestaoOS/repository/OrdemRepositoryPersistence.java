package br.com.ufes.sisgestaoOS.repository;

import java.sql.SQLException;
import br.com.ufes.sisgestaoOS.model.Ordem;
import br.com.ufes.sisgestaoOS.service.novo.NovaOrdem;
import br.com.ufes.sisgestaoOS.dao.OrdemDao;

public class OrdemRepositoryPersistence implements OrdemRepository {

    //private static final Map<BigDecimal, Ordem> ORDENS_STORE = new ConcurrentHashMap<BigDecimal, Ordem>();
    
    @Override
    public int create(NovaOrdem novaOrdem) {
    	Ordem ordem = new Ordem(
    			 novaOrdem.getSolicitante()
    			,novaOrdem.getEncarregado()
    			,novaOrdem.getRequisito()
    			,novaOrdem.getStatus()
    			,novaOrdem.getPrioridade()
    			,novaOrdem.getEquipe()
    			,novaOrdem.getEmissao()
    			,novaOrdem.getDescricao()
    			,novaOrdem.getEsforco()
    			,novaOrdem.getEntrega()
    			,novaOrdem.getVlrEstimado()
        		);
        try {
        	OrdemDao.getInstance().save(ordem);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
        
        //ORDENS_STORE.put(new BigDecimal(ordem.getCodOs()), ordem);

        return ordem.getCodOs();
    }
    
    @Override
    public void update(Ordem ordem) {
       // String id = UUID.randomUUID().toString();
        try {
        	OrdemDao.getInstance().update(ordem);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
    }
}
