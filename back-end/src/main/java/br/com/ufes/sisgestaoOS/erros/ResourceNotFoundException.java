package br.com.ufes.sisgestaoOS.erros;

class ResourceNotFoundException extends ApplicationException {

    ResourceNotFoundException(int code, String message) {
        super(code, message);
    }
}
