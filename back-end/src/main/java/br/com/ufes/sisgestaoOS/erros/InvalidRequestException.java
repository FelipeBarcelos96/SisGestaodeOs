package br.com.ufes.sisgestaoOS.erros;

class InvalidRequestException extends ApplicationException {

    public InvalidRequestException(int code, String message) {
        super(code, message);
    }
}
