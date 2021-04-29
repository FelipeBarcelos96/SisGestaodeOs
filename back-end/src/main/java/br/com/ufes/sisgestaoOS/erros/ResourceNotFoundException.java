package br.com.ufes.sisgestaoOS.erros;

@SuppressWarnings("serial")
class ResourceNotFoundException extends ApplicationException {

    ResourceNotFoundException(int code, String message) {
        super(code, message);
    }
}
