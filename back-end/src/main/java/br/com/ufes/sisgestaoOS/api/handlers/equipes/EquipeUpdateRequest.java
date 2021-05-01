package br.com.ufes.sisgestaoOS.api.handlers.equipes;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
class EquipeUpdateRequest {

    int codEquipe;
    String sigla;

}