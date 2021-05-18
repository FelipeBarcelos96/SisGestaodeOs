package br.com.ufes.sisgestaoOS.api.handlers.login;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.ufes.sisgestaoOS.api.Constants;
import br.com.ufes.sisgestaoOS.api.ResponseEntity;
import br.com.ufes.sisgestaoOS.api.StatusCode;
import br.com.ufes.sisgestaoOS.api.handlers.Handler;
import br.com.ufes.sisgestaoOS.dao.UsuarioDao;
import br.com.ufes.sisgestaoOS.erros.ApplicationExceptions;
import br.com.ufes.sisgestaoOS.erros.GlobalExceptionHandler;
import br.com.ufes.sisgestaoOS.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class LoginHandler extends Handler {

    public LoginHandler(ObjectMapper objectMapper,
                               GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            @SuppressWarnings("rawtypes")
			ResponseEntity e = doPost(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        } else if("OPTIONS".equals(exchange.getRequestMethod())) {
        	response = null;
        	@SuppressWarnings("rawtypes")
			ResponseEntity e = doOption(exchange.getRequestBody());
        	
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_CREDENTIALS, Constants.FALSE);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_REQUEST_HEADERS, Constants.ASTERISC);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_EXPOSE_HEADERS, Constants.HEADERS);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_METHODS, Constants.METHODS);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_HEADERS, "Content-type");
        	exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
        	response = super.writeResponse(e.getBody());
        }
        else {
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
            throw ApplicationExceptions.methodNotAllowed(
                "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private ResponseEntity<LoginResponse> doPost(InputStream is) {
        LoginRequest registerRequest = super.readRequest(is, LoginRequest.class);
        Boolean resp = Boolean.FALSE;
        String url = null;
        int id = 0;
        try {
			ArrayList<Usuario> usuarios = UsuarioDao.getInstance().getAll();
			for(Usuario user : usuarios) {
				if(user.getNome().toUpperCase().equals(registerRequest.getLogin().toUpperCase()) 
						&& user.getPass().equals(registerRequest.getPassword())
						) {
					resp = Boolean.TRUE;
					url = Constants.URL_IP+":"+ 
					"8180" +"/login/"
							+ Integer.toString(user.getId());
					id = user.getId();
				}
			}
		} catch (SQLException e) {
			System.out.println("Alert!! Error: ");
			e.printStackTrace();
		}
        
        LoginResponse response = LoginResponse.builder().response(resp).url(url).id(id).build();
        
        if(resp) {
        return new ResponseEntity<>(response,
            getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
        }else {
        	return new ResponseEntity<>(response,
                    getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.UNAUTHORIZED);
        }
    }
    
    @SuppressWarnings("unchecked")
	private ResponseEntity<LoginResponse> doOption(InputStream is) {
    		@SuppressWarnings("rawtypes")
			ResponseEntity re = new ResponseEntity<>(null,
                    getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    		re.getHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
    		re.getHeaders().add(Constants.ACESS_CONTROL_ALLOW_CREDENTIALS, Constants.FALSE);
    		re.getHeaders().add(Constants.ACESS_CONTROL_REQUEST_HEADERS, Constants.ASTERISC);
    		re.getHeaders().add(Constants.ACESS_CONTROL_EXPOSE_HEADERS, Constants.HEADERS);
    		re.getHeaders().add(Constants.ACESS_CONTROL_ALLOW_METHODS, Constants.METHODS);
    	//	re.getHeaders().add(Constants.ACESS_CONTROL_ALLOW_HEADERS, Constants.HEADERS);
    	return re;
    }
    
}
