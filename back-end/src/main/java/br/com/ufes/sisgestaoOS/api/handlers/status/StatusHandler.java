package br.com.ufes.sisgestaoOS.api.handlers.status;

import static br.com.ufes.sisgestaoOS.api.ApiUtils.splitQuery;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

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
				exchange.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), -1);
				System.out.println("Error! Alert: ");
				e.printStackTrace();
			}

			exchange.sendResponseHeaders(200, resp.getBytes().length);
			OutputStream output = exchange.getResponseBody();
			output.write(resp.getBytes());
			output.flush();
		} else if("DELETE".equals(exchange.getRequestMethod())) {
			
			Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
			String noParamText = "-1";
			String id = params.getOrDefault("id", List.of(noParamText)).stream().findFirst().orElse(noParamText);
			try {
				if(Integer.parseInt(id) > 0) {
					StatusDao.getInstance().delete(Integer.parseInt(id));					
				}
			}catch(Exception e) {
				exchange.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), -1);
				System.out.println("Error! Alert: ");
				e.printStackTrace();
			}

			exchange.sendResponseHeaders(StatusCode.ACCEPTED.getCode(), -1);
			
			OutputStream output = exchange.getResponseBody();
		
			output.flush();
		}else {
			exchange.sendResponseHeaders(StatusCode.METHOD_NOT_ALLOWED.getCode(), -1);// 405 Method Not Allowed
		}
		exchange.close();
	}

}
