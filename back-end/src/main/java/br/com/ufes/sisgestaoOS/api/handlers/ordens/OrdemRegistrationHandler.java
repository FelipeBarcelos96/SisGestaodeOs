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
import br.com.ufes.sisgestaoOS.model.Ordem;
import br.com.ufes.sisgestaoOS.service.OrdemService;
import br.com.ufes.sisgestaoOS.service.novo.NovaOrdem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class OrdemRegistrationHandler extends Handler {

    private final OrdemService ordemService;

    public OrdemRegistrationHandler(OrdemService ordemService, ObjectMapper objectMapper,
                               GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.ordemService = ordemService;
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

    private ResponseEntity<OrdemRegistrationResponse> doPost(InputStream is) {
        OrdemRegistrationRequest registerRequest = super.readRequest(is, OrdemRegistrationRequest.class);

        NovaOrdem ordem = NovaOrdem.builder()
                .solicitante(registerRequest.getSolicitante().getUsuario())
                .encarregado(registerRequest.getEncarregado().getUsuario())
                .requisito(registerRequest.getRequisito().getRequisito())
                .status(registerRequest.getStatus().getStatus())
                .prioridade(registerRequest.getPrioridade().getPrioridade())
                .equipe(registerRequest.getEquipe().getEquipe())
                .emissao(registerRequest.getEmissao())
                .descricao(registerRequest.getDescricao())
                .esforco(registerRequest.getEsforco())
                .entrega(registerRequest.getEntrega())
                .vlrEstimado(registerRequest.getVlrEstimado())
                .build();
        
        int codOrdem = ordemService.create(ordem);

        OrdemRegistrationResponse response = new OrdemRegistrationResponse(
        		codOrdem
        		,registerRequest.getSolicitante()
        		,registerRequest.getEncarregado()
        		,registerRequest.getRequisito()
        		,registerRequest.getStatus()
        		,registerRequest.getPrioridade()
        		,registerRequest.getEquipe()
        		,registerRequest.getEmissao()
        		,registerRequest.getDescricao()
        		,registerRequest.getEsforco()
        		,registerRequest.getEntrega()
        		,registerRequest.getVlrEstimado()
        		);

        return new ResponseEntity<>(response,
            getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
    
    private ResponseEntity<OrdemUpdateResponse> doPut(InputStream is) {
    	OrdemUpdateRequest updateRequest = super.readRequest(is, OrdemUpdateRequest.class);

    	Ordem ordem = new Ordem(
    			updateRequest.getCodOs()	
    			,updateRequest.getSolicitante().getUsuario()
    			, updateRequest.getEncarregado().getUsuario()
    			, updateRequest.getRequisito().getRequisito()
    			, updateRequest.getStatus().getStatus()
    			, updateRequest.getPrioridade().getPrioridade()
    			,updateRequest.getEquipe().getEquipe()
    			, updateRequest.getEmissao()
    			, updateRequest.getDescricao()
    			, updateRequest.getEsforco()
    			, updateRequest.getEntrega()
    			, updateRequest.getVlrEstimado()
    			);    			
        
        ordemService.update(ordem);

        return new ResponseEntity<>(null,
        		 getHeaders(Constants.ACESS_CONTROL_ALLOW_HEADERS, Constants.HEADERS), StatusCode.OK);
    }
    
    @SuppressWarnings("unchecked")
	private ResponseEntity<OrdemRegistrationResponse> doOption(InputStream is) {
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
