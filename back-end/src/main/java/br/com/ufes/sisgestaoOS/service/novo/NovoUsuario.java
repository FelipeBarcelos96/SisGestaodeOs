package br.com.ufes.sisgestaoOS.service.novo;

import br.com.ufes.sisgestaoOS.model.Equipe;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NovoUsuario {
    Equipe equipe;
    String login;
    String password;
    Boolean ehAdm;
    Boolean ehGestor;
    Boolean ehDev;
    Boolean ehAnal;
}
