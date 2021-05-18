package br.com.ufes.sisgestaoOS.api.handlers.ordens;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import br.com.ufes.sisgestaoOS.api.Constants;
import br.com.ufes.sisgestaoOS.api.ResponseEntity;
import br.com.ufes.sisgestaoOS.api.StatusCode;
import br.com.ufes.sisgestaoOS.api.handlers.Handler;
import br.com.ufes.sisgestaoOS.erros.ApplicationExceptions;
import br.com.ufes.sisgestaoOS.erros.GlobalExceptionHandler;
import br.com.ufes.sisgestaoOS.service.OrdemService;
import br.com.ufes.sisgestaoOS.utils.ManipuladorArquivo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class OrdemArquivoHandler extends Handler {

    public OrdemArquivoHandler(OrdemService ordemService, ObjectMapper objectMapper,
                               GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response = null;
        if ("POST".equals(exchange.getRequestMethod())) {
            @SuppressWarnings("rawtypes")
			ResponseEntity e = doPost(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.getResponseHeaders()
            .add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
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

    private ResponseEntity<OrdemArquivoResponse> doPost(InputStream is) {
        OrdemArquivoRequest arquivoRequest = super.readRequest(is, OrdemArquivoRequest.class);
        
        String nome = arquivoRequest.getNome();
        String pasta = "ordens";
        String id = Integer.toString(arquivoRequest.getCodOS());

        ManipuladorArquivo.getInstance().criaArquivo(pasta, id, nome, arquivoRequest.getDados(), arquivoRequest.getInfo());

        return new ResponseEntity<>(null,
            getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
        
    @SuppressWarnings("unchecked")
	private ResponseEntity<OrdemArquivoResponse> doOption(InputStream is) {
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
