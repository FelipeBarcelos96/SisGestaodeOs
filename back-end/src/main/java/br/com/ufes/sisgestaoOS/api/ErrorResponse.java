package br.com.ufes.sisgestaoOS.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {

    int code;
    String message;
}
