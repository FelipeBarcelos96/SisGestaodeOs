package br.com.ufes.sisgestaoOS.api.handlers.prioridades;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.ufes.sisgestaoOS.api.Constants;
import br.com.ufes.sisgestaoOS.api.ResponseEntity;
import br.com.ufes.sisgestaoOS.api.StatusCode;
import br.com.ufes.sisgestaoOS.api.handlers.Handler;
import br.com.ufes.sisgestaoOS.erros.ApplicationExceptions;
import br.com.ufes.sisgestaoOS.erros.GlobalExceptionHandler;
import br.com.ufes.sisgestaoOS.model.Prioridade;
import br.com.ufes.sisgestaoOS.service.PrioridadeService;
import br.com.ufes.sisgestaoOS.service.novo.NovaPrioridade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class PrioridadeRegistrationHandler extends Handler {

    private final PrioridadeService prioridadeService;

    public PrioridadeRegistrationHandler(PrioridadeService prioridadeService, ObjectMapper objectMapper,
                               GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.prioridadeService = prioridadeService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            @SuppressWarnings("rawtypes")
			ResponseEntity e = doPost(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
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

    private ResponseEntity<PrioridadesRegistrationResponse> doPost(InputStream is) {
        PrioridadesRegistrationRequest registerRequest = super.readRequest(is, PrioridadesRegistrationRequest.class);

        NovaPrioridade prioridade = NovaPrioridade.builder()
                .nome(registerRequest.getNome())
                .build();
        
        int codPrioridade = prioridadeService.create(prioridade);

        PrioridadesRegistrationResponse response = new PrioridadesRegistrationResponse(
        		codPrioridade
        		, prioridade.getNome()
        		);

        return new ResponseEntity<>(response,
            getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
    
    private ResponseEntity<PrioridadesUpdateResponse> doPut(InputStream is) {
    	PrioridadesUpdateRequest updateRequest = super.readRequest(is, PrioridadesUpdateRequest.class);

    	Prioridade prioridade = new Prioridade(
    			updateRequest.getId()
    			,updateRequest.getNome()		
    			);    			
        
        prioridadeService.update(prioridade);

        return new ResponseEntity<>(null,
        		 getHeaders(Constants.ACESS_CONTROL_ALLOW_HEADERS, Constants.HEADERS), StatusCode.OK);
    }
    
    @SuppressWarnings("unchecked")
	private ResponseEntity<PrioridadesRegistrationResponse> doOption(InputStream is) {
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
