package br.com.ufes.sisgestaoOS.api.handlers.usuario;

import br.com.ufes.sisgestaoOS.service.json.EquipeJson;
import lombok.Value;

@Value
class UsuarioRegistrationResponse {

    int id;
    String login;
    String password;
    Boolean ehAdm;
    Boolean ehGestor;
    Boolean ehDev;
    Boolean ehAnal;
    EquipeJson equipe;
}