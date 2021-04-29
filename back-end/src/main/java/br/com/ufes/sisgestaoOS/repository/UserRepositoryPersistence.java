package br.com.ufes.sisgestaoOS.repository;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.ufes.sisgestaoOS.model.Usuario;
import br.com.ufes.sisgestaoOS.service.novo.NovoUsuario;
import br.com.ufes.sisgestaoOS.dao.UsuarioDao;

public class UserRepositoryPersistence implements UserRepository {

    private static final Map<String, Usuario> USERS_STORE = new ConcurrentHashMap<String, Usuario>();

    @Override
    public int create(NovoUsuario newUser) {
 
        Usuario user = new Usuario(newUser.getLogin()
        		, newUser.getPassword()
        		, newUser.getEhAdm()
        		, newUser.getEhGestor()
        		, newUser.getEhDev()
        		, newUser.getEhAnal()
        		, newUser.getEquipe()
        		);
        try {
			UsuarioDao.getInstance().save(user);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
        
        int id = user.getId();
        
        USERS_STORE.put(newUser.getLogin(), user);

        return id;
    }
    
    @Override
    public void update(Usuario user) {
       // String id = UUID.randomUUID().toString();
        try {
			UsuarioDao.getInstance().update(user);
		} catch (SQLException e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}
    }
}
