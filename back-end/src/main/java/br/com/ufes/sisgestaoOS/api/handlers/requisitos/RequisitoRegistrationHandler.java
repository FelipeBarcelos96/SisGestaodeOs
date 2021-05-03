package br.com.ufes.sisgestaoOS.api.handlers.requisitos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.ufes.sisgestaoOS.api.Constants;
import br.com.ufes.sisgestaoOS.api.ResponseEntity;
import br.com.ufes.sisgestaoOS.api.StatusCode;
import br.com.ufes.sisgestaoOS.api.handlers.Handler;
import br.com.ufes.sisgestaoOS.erros.ApplicationExceptions;
import br.com.ufes.sisgestaoOS.erros.GlobalExceptionHandler;
import br.com.ufes.sisgestaoOS.model.Requisito;
import br.com.ufes.sisgestaoOS.service.RequisitoService;
import br.com.ufes.sisgestaoOS.service.json.EquipeJson;
import br.com.ufes.sisgestaoOS.service.json.UsuarioJson;
import br.com.ufes.sisgestaoOS.service.novo.NovoRequisito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class RequisitoRegistrationHandler extends Handler {

    private final RequisitoService requisitoService;

    public RequisitoRegistrationHandler(RequisitoService requisitoService, ObjectMapper objectMapper,
                               GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.requisitoService = requisitoService;
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
        }else if("PUT".equals(exchange.getRequestMethod())) {
        	@SuppressWarnings("rawtypes")
			ResponseEntity e = doPut(exchange.getRequestBody());
        	exchange.getResponseHeaders().putAll(e.getHeaders());
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
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

    private ResponseEntity<RequisitoRegistrationResponse> doPost(InputStream is) {
    	
        RequisitoRegistrationRequest registerRequest = super.readRequest(is, RequisitoRegistrationRequest.class);
        
        System.out.println(registerRequest.getPrazo());
        
        NovoRequisito requisito;
        
			requisito = NovoRequisito.builder()
			        .analista(registerRequest.getAnalista().getUsuario())			        		
			        .titulo(registerRequest.getTitulo())
			        .descricao(registerRequest.getDescricao())
			        .prazo(registerRequest.getPrazo())
			        .build();			
		
        int requisitoId = requisitoService.create(requisito);

        RequisitoRegistrationResponse response = new RequisitoRegistrationResponse(
        		requisitoId
        		, UsuarioJson.builder()
        		.id(requisito.getAnalista().getId())
        		.login(requisito.getAnalista().getNome())
        		.password(requisito.getAnalista().getPass())
        		.ehAdm(requisito.getAnalista().getEhAdm())
        		.ehGestor(requisito.getAnalista().getEhGestor())
        		.ehDev(requisito.getAnalista().getEhDev())
        		.ehAnal(requisito.getAnalista().getEhAnal())
        		.equipe(EquipeJson.builder()
        				.codEquipe(requisito.getAnalista().getEquipe().getCodEquipe())
        				.sigla(requisito.getAnalista().getEquipe().getSigla())
        				.build()
        				).build()
        		, requisito.getTitulo()
        		, requisito.getDescricao()
        		, requisito.getPrazo()
        		);  

        return new ResponseEntity<>(response,
            getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);        
    }
    
    private ResponseEntity<RequisitoUpdateResponse> doPut(InputStream is) {
    	RequisitoUpdateRequest updateRequest = super.readRequest(is, RequisitoUpdateRequest.class);
    	System.out.println(updateRequest.getPrazo());
    	Requisito requisito = new Requisito(
    			updateRequest.getCodReq()
    			, updateRequest.getAnalista().getUsuario()
    			, updateRequest.getTitulo()
    			, updateRequest.getDescricao()
    			, updateRequest.getPrazo()
    		//	, new Timestamp(new Date(Date.parse(updateRequest.getPrazo())).getTime())
    			);    			
        
        requisitoService.update(requisito);

        return new ResponseEntity<>(null,
        		 getHeaders(Constants.ACESS_CONTROL_ALLOW_HEADERS, Constants.HEADERS), StatusCode.OK);
    }
    
    @SuppressWarnings("unchecked")
	private ResponseEntity<RequisitoRegistrationResponse> doOption(InputStream is) {
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
