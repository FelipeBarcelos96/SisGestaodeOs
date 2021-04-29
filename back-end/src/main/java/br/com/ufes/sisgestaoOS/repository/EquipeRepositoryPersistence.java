package br.com.ufes.sisgestaoOS.repository;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.ufes.sisgestaoOS.model.Equipe;
import br.com.ufes.sisgestaoOS.service.novo.NovaEquipe;
import br.com.ufes.sisgestaoOS.dao.EquipeDao;

public class EquipeRepositoryPersistence implements EquipeRepository {

    private static final Map<String, Equipe> EQUIPES_STORE = new ConcurrentHashMap<String, Equipe>();

    @Override
    public int create(NovaEquipe newEquip) {
       // String id = UUID.randomUUID().toString();
        Equipe equip = new Equipe(
        		 newEquip.getSigla()
        		);
        try {
			EquipeDao.getInstance().save(equip);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
        
        int id = equip.getCodEquipe();
        
        EQUIPES_STORE.put(equip.getSigla(), equip);

        return id;
    }
    
    @Override
    public void update(Equipe equip) {
       // String id = UUID.randomUUID().toString();
        try {
        	EquipeDao.getInstance().update(equip);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
    }
}
