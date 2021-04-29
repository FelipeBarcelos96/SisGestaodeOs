package br.com.ufes.sisgestaoOS.erros;

class MethodNotAllowedException extends ApplicationException {

    MethodNotAllowedException(int code, String message) {
        super(code, message);
    }
}
