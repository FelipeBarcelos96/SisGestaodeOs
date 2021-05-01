package br.com.ufes.sisgestaoOS.api.handlers.login;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class LoginRequest {
    String login;
    String password;
}
