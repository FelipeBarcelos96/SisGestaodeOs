package br.com.ufes.sisgestaoOS.service;

import br.com.ufes.sisgestaoOS.model.Usuario;
import br.com.ufes.sisgestaoOS.repository.UserRepository;
import br.com.ufes.sisgestaoOS.service.novo.NovoUsuario;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public int create(NovoUsuario user) {
        return userRepository.create(user);
    }
    
    public void update(Usuario user) {
       userRepository.update(user);
    }

}