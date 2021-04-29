package br.com.ufes.sisgestaoOS.api.handlers.equipes;

import lombok.Value;

@Value
class EquipeUpdateRequest {

    int codEquipe;
    String sigla;

}