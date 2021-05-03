package br.com.ufes.sisgestaoOS.api.handlers.login;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
class LoginResponse {
     Boolean response;
     String url;
     int id;
}