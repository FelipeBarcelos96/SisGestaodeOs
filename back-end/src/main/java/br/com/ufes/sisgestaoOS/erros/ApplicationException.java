package br.com.ufes.sisgestaoOS.erros;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ApplicationException extends RuntimeException {

    private final int code;

    ApplicationException(int code, String message) {
        super(message);
        this.code = code;
    }
}