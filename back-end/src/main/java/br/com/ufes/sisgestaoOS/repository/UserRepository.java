package br.com.ufes.sisgestaoOS.repository;

import br.com.ufes.sisgestaoOS.model.Usuario;
import br.com.ufes.sisgestaoOS.service.novo.NovoUsuario;

public interface UserRepository {

    int create(NovoUsuario user);
    void update(Usuario user);
}
