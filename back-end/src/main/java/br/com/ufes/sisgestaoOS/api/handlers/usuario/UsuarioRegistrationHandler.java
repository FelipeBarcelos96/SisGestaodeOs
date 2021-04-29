package br.com.ufes.sisgestaoOS.api.handlers.usuario;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.ufes.sisgestaoOS.api.Constants;
import br.com.ufes.sisgestaoOS.api.ResponseEntity;
import br.com.ufes.sisgestaoOS.api.StatusCode;
import br.com.ufes.sisgestaoOS.api.handlers.Handler;
import br.com.ufes.sisgestaoOS.erros.ApplicationExceptions;
import br.com.ufes.sisgestaoOS.erros.GlobalExceptionHandler;
import br.com.ufes.sisgestaoOS.model.Equipe;
import br.com.ufes.sisgestaoOS.model.Usuario;
import br.com.ufes.sisgestaoOS.service.UserService;
import br.com.ufes.sisgestaoOS.service.json.EquipeJson;
import br.com.ufes.sisgestaoOS.service.novo.NovoUsuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class UsuarioRegistrationHandler extends Handler {

    private final UserService userService;

    public UsuarioRegistrationHandler(UserService userService, ObjectMapper objectMapper,
                               GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.userService = userService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity e = doPost(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        } else if("OPTIONS".equals(exchange.getRequestMethod())) {
        	response = null;
        	ResponseEntity e = doOption(exchange.getRequestBody());
        	
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_CREDENTIALS, Constants.FALSE);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_REQUEST_HEADERS, Constants.ASTERISC);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_EXPOSE_HEADERS, Constants.HEADERS);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_METHODS, Constants.METHODS);
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_HEADERS, "Content-type");
        	exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
        	response = super.writeResponse(e.getBody());
        }else if("PUT".equals(exchange.getRequestMethod())) {
        	ResponseEntity e = doPut(exchange.getRequestBody());
        	exchange.getResponseHeaders().putAll(e.getHeaders());
        	exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
        	response = super.writeResponse(e.getBody());
        }
        else {
            throw ApplicationExceptions.methodNotAllowed(
                "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private ResponseEntity<UsuarioRegistrationResponse> doPost(InputStream is) {
        UsuarioRegistrationRequest registerRequest = super.readRequest(is, UsuarioRegistrationRequest.class);

        NovoUsuario user = NovoUsuario.builder()
                .login(registerRequest.getLogin())
                .password(registerRequest.getPassword())
                .ehAdm(registerRequest.getEhAdm())
                .ehGestor(registerRequest.getEhGestor())
                .ehDev(registerRequest.getEhDev())
                .ehAnal(registerRequest.getEhAnal())
                .equipe(new Equipe(registerRequest.getEquipe().getCodEquipe(), registerRequest.getEquipe().getSigla()))
                .build();
        
        int userId = userService.create(user);

        UsuarioRegistrationResponse response = new UsuarioRegistrationResponse(
        		userId
        		, user.getLogin()
        		, user.getPassword()
        		, user.getEhAdm()
        		, user.getEhGestor()
        		, user.getEhDev()
        		, user.getEhAnal()
        		, EquipeJson.builder().codEquipe(user.getEquipe().getCodEquipe()).sigla(user.getEquipe().getSigla()).build()
        		);

        return new ResponseEntity<>(response,
            getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
    
    private ResponseEntity<UsuarioUpdateResponse> doPut(InputStream is) {
    	UsuarioUpdateRequest updateRequest = super.readRequest(is, UsuarioUpdateRequest.class);

    	Usuario user = new Usuario(
    			updateRequest.getId()
    			,updateRequest.getLogin()
    			,updateRequest.getPassword()
    			,updateRequest.getEhAdm()
    			,updateRequest.getEhGestor()
    			,updateRequest.getEhDev()
    			,updateRequest.getEhAnal()
    			,new Equipe(updateRequest.getEquipe().getCodEquipe(), updateRequest.getEquipe().getSigla())   			
    			);    			
        
        userService.update(user);

        return new ResponseEntity<>(null,
        		 getHeaders(Constants.ACESS_CONTROL_ALLOW_HEADERS, Constants.HEADERS), StatusCode.OK);
    }
    
    private ResponseEntity<UsuarioRegistrationResponse> doOption(InputStream is) {
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
