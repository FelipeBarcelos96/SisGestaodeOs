package br.com.ufes.sisgestaoOS.main;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import com.sun.net.httpserver.HttpServer;

import br.com.ufes.sisgestaoOS.api.ApiUtils;


public class Testes {

	public static void main(String[] args) {
				
		try {
			int serverPort = 8080;
	        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
	        
	        server.createContext("/api/hello", (exchange -> {
	            if ("GET".equals(exchange.getRequestMethod())) {
	            	Map<String, List<String>> params = ApiUtils.splitQuery(exchange.getRequestURI().getRawQuery());
	            	String semNome = "Anônimo";
	            	String name = params.getOrDefault("name", List.of(semNome)).stream().findFirst().orElse(semNome);
	            	String respText = String.format("Hello %s!", name);
	                exchange.sendResponseHeaders(200, respText.getBytes().length);
	                OutputStream output = exchange.getResponseBody();
	                output.write(respText.getBytes());
	                output.flush();
	            } else {
	                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
	            }
	            exchange.close();
	        }));
	        server.setExecutor(null);
	        server.start();
	        
			
		//	Usuario sup = new Usuario("FELPS", "tecsis", false, false, true, false);
		//	UsuarioDao.getInstance().save(sup);
			
	        System.out.println("Teste concluido com sucesso");
		}catch(Exception e) {
			System.out.println("Error! Alert: ");
			e.printStackTrace();
		}

	}
	
}
