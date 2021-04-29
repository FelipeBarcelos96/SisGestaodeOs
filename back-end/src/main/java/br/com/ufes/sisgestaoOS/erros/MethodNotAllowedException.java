package br.com.ufes.sisgestaoOS.erros;

@SuppressWarnings("serial")
class MethodNotAllowedException extends ApplicationException {

    MethodNotAllowedException(int code, String message) {
        super(code, message);
    }
}
