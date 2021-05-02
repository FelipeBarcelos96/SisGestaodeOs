package br.com.ufes.sisgestaoOS.api.handlers.status;

import static br.com.ufes.sisgestaoOS.api.ApiUtils.splitQuery;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import br.com.ufes.sisgestaoOS.api.Constants;
import br.com.ufes.sisgestaoOS.api.ResponseEntity;
import br.com.ufes.sisgestaoOS.api.StatusCode;
import br.com.ufes.sisgestaoOS.api.handlers.Handler;
import br.com.ufes.sisgestaoOS.dao.StatusDao;
import br.com.ufes.sisgestaoOS.erros.GlobalExceptionHandler;
import br.com.ufes.sisgestaoOS.model.Status;

public class StatusHandler extends Handler {

	public StatusHandler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
		super(objectMapper, exceptionHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void execute(HttpExchange exchange) throws Exception {
		@SuppressWarnings("unused")
		byte[] response;
		if ("GET".equals(exchange.getRequestMethod())) {
			Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
			String noParamText = "-1";
			String id = params.getOrDefault("id", List.of(noParamText)).stream().findFirst().orElse(noParamText);
			String resp = "";
			try {
				if(Integer.parseInt(id) >= 0) {
					Status status = StatusDao.getInstance().get(Integer.parseInt(id));
					resp = new Gson().toJson(status);
				}else {
					ArrayList<Status> statuss = StatusDao.getInstance().getAll();
					
						resp = new Gson().toJson(statuss);
					
				}
			}catch(Exception e) {
				exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
				exchange.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), -1);
				System.out.println("Error! Alert: ");
				e.printStackTrace();
			}
			exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
			exchange.sendResponseHeaders(200, resp.getBytes().length);
			OutputStream output = exchange.getResponseBody();
			output.write(resp.getBytes());
			output.flush();
		} else if("DELETE".equals(exchange.getRequestMethod())) {
			String resp = "";
			Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
			String noParamText = "-1";
			String id = params.getOrDefault("id", List.of(noParamText)).stream().findFirst().orElse(noParamText);
			try {
				if(Integer.parseInt(id) > 0) {
					StatusDao.getInstance().delete(Integer.parseInt(id));					
				}
			}catch(Exception e) {
				resp = e.getMessage();
				exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
				exchange.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), resp.getBytes().length);
				OutputStream output = exchange.getResponseBody();
				output.write(resp.getBytes());
				output.flush();
				System.out.println("Error! Alert: ");
				e.printStackTrace();
			}
			exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
			exchange.sendResponseHeaders(StatusCode.ACCEPTED.getCode(), -1);
			
			OutputStream output = exchange.getResponseBody();
		
			output.flush();
		}else if("OPTIONS".equals(exchange.getRequestMethod())) {
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
        }else {
        	exchange.getResponseHeaders().add(Constants.ACESS_CONTROL_ALLOW_ORIGIN, Constants.ASTERISC);
			exchange.sendResponseHeaders(StatusCode.METHOD_NOT_ALLOWED.getCode(), -1);// 405 Method Not Allowed
		}
		exchange.close();
	}

	@SuppressWarnings("unchecked")
	private ResponseEntity<StatusRegistrationResponse> doOption(InputStream is) {
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
