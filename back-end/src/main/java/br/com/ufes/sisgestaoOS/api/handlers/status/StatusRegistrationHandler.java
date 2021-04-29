package br.com.ufes.sisgestaoOS.api.handlers.status;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.ufes.sisgestaoOS.api.Constants;
import br.com.ufes.sisgestaoOS.api.ResponseEntity;
import br.com.ufes.sisgestaoOS.api.StatusCode;
import br.com.ufes.sisgestaoOS.api.handlers.Handler;
import br.com.ufes.sisgestaoOS.erros.ApplicationExceptions;
import br.com.ufes.sisgestaoOS.erros.GlobalExceptionHandler;
import br.com.ufes.sisgestaoOS.model.Status;
import br.com.ufes.sisgestaoOS.service.StatusService;
import br.com.ufes.sisgestaoOS.service.novo.NovoStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class StatusRegistrationHandler extends Handler {

    private final StatusService statusService;

    public StatusRegistrationHandler(StatusService statusService, ObjectMapper objectMapper,
                               GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.statusService = statusService;
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

    private ResponseEntity<StatusRegistrationResponse> doPost(InputStream is) {
        StatusRegistrationRequest registerRequest = super.readRequest(is, StatusRegistrationRequest.class);

        NovoStatus status = NovoStatus.builder()
                .status(registerRequest.getStatus())
                .build();
        
        int codStatus = statusService.create(status);

        StatusRegistrationResponse response = new StatusRegistrationResponse(
        		codStatus
        		, status.getStatus()
        		);

        return new ResponseEntity<>(response,
            getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
    
    private ResponseEntity<StatusUpdateResponse> doPut(InputStream is) {
    	StatusUpdateRequest updateRequest = super.readRequest(is, StatusUpdateRequest.class);

    	Status status = new Status(
    			updateRequest.getCodStatus()
    			,updateRequest.getStatus() 			
    			);    			
        
        statusService.update(status);

        return new ResponseEntity<>(null,
        		 getHeaders(Constants.ACESS_CONTROL_ALLOW_HEADERS, Constants.HEADERS), StatusCode.OK);
    }
    
    private ResponseEntity<StatusRegistrationResponse> doOption(InputStream is) {
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
