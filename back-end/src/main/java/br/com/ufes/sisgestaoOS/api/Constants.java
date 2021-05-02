package br.com.ufes.sisgestaoOS.api;

import br.com.ufes.sisgestaoOS.utils.ManipuladorDePropriedades;

public class Constants {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";    
    public static final String ASTERISC = "*";
    public static final String FALSE = "false";
    public static final String HEADERS = "Date, Transfer-encoding, Content-type, application/json";
    public static final String METHODS = "GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH";
    public static final String ACESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String ACESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    public static final String ACESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final int SERVER_PORT = 8080;
    public static final String URL_IP = ManipuladorDePropriedades.getProp().getProperty("ip");
}
