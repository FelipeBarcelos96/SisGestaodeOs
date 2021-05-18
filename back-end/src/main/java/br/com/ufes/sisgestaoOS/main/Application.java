package br.com.ufes.sisgestaoOS.main;

import static br.com.ufes.sisgestaoOS.main.Configuration.getErrorHandler;
import static br.com.ufes.sisgestaoOS.main.Configuration.getObjectMapper;
import static br.com.ufes.sisgestaoOS.main.Configuration.getUserService;
import static br.com.ufes.sisgestaoOS.main.Configuration.getEquipeService;
import static br.com.ufes.sisgestaoOS.main.Configuration.getStatusService;
import static br.com.ufes.sisgestaoOS.main.Configuration.getPrioridadeService;
import static br.com.ufes.sisgestaoOS.main.Configuration.getRequisitoService;
import static br.com.ufes.sisgestaoOS.main.Configuration.getOrdemService;
import static br.com.ufes.sisgestaoOS.api.ApiUtils.splitQuery;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import br.com.ufes.sisgestaoOS.api.Constants;
import br.com.ufes.sisgestaoOS.api.handlers.equipes.EquipeHandler;
import br.com.ufes.sisgestaoOS.api.handlers.equipes.EquipeRegistrationHandler;
import br.com.ufes.sisgestaoOS.api.handlers.login.LoginHandler;
import br.com.ufes.sisgestaoOS.api.handlers.ordens.ArquivoHandler;
import br.com.ufes.sisgestaoOS.api.handlers.ordens.OrdemArquivoHandler;
import br.com.ufes.sisgestaoOS.api.handlers.ordens.OrdemHandler;
import br.com.ufes.sisgestaoOS.api.handlers.ordens.OrdemRegistrationHandler;
import br.com.ufes.sisgestaoOS.api.handlers.prioridades.PrioridadeHandler;
import br.com.ufes.sisgestaoOS.api.handlers.prioridades.PrioridadeRegistrationHandler;
import br.com.ufes.sisgestaoOS.api.handlers.requisitos.RequisitoHandler;
import br.com.ufes.sisgestaoOS.api.handlers.requisitos.RequisitoRegistrationHandler;
import br.com.ufes.sisgestaoOS.api.handlers.status.StatusHandler;
import br.com.ufes.sisgestaoOS.api.handlers.status.StatusRegistrationHandler;
import br.com.ufes.sisgestaoOS.api.handlers.usuario.UsuarioHandler;
import br.com.ufes.sisgestaoOS.api.handlers.usuario.UsuarioRegistrationHandler;
import com.sun.net.httpserver.HttpServer;

public class Application {
	static HttpServer server;
	public static void main(String[] args) throws IOException {

		server = HttpServer.create(new InetSocketAddress(Constants.SERVER_PORT), 0);		
		
		UsuarioHandler usuarioHandler = new UsuarioHandler(getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/users", usuarioHandler::handle);
		
		StatusHandler statusHandler = new StatusHandler(getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/status", statusHandler::handle);
		
		PrioridadeHandler prioridadeHandler = new PrioridadeHandler(getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/prioridade", prioridadeHandler::handle);
		
		RequisitoHandler requisitoHandler = new RequisitoHandler(getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/requisito", requisitoHandler::handle);
		
		EquipeHandler equipeHandler = new EquipeHandler(getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/equips", equipeHandler::handle);
		
		OrdemHandler ordemHandler = new OrdemHandler(getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/ordem", ordemHandler::handle);
		
		ArquivoHandler arquivoHandler = new ArquivoHandler(getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/arquivo", arquivoHandler::handle);
		
		UsuarioRegistrationHandler usuarioRegistrationHandler = new UsuarioRegistrationHandler(getUserService(), getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/users/register", usuarioRegistrationHandler::handle);
		
		EquipeRegistrationHandler equipeRegistrationHandler = new EquipeRegistrationHandler(getEquipeService(), getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/equips/register", equipeRegistrationHandler::handle);
		
		StatusRegistrationHandler statusRegistrationHandler = new StatusRegistrationHandler(getStatusService(), getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/status/register", statusRegistrationHandler::handle);
		
		PrioridadeRegistrationHandler prioridadeRegistrationHandler = new PrioridadeRegistrationHandler(getPrioridadeService(), getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/prioridade/register", prioridadeRegistrationHandler::handle);
		
		RequisitoRegistrationHandler requisitoRegistrationHandler = new RequisitoRegistrationHandler(getRequisitoService(), getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/requisito/register", requisitoRegistrationHandler::handle);
		
		OrdemRegistrationHandler ordemRegistrationHandler = new OrdemRegistrationHandler(getOrdemService(), getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/ordem/register", ordemRegistrationHandler::handle);
		
		OrdemArquivoHandler ordemArquivoHandler = new OrdemArquivoHandler(getOrdemService(), getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/ordem/arquivo", ordemArquivoHandler::handle);
		
		LoginHandler loginHandler = new LoginHandler(getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/login", loginHandler::handle);

		server.createContext("/api/hello", (exchange -> {

			if ("GET".equals(exchange.getRequestMethod())) {
				Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
				String noNameText = "Anonymous";
				String name = params.getOrDefault("name", List.of(noNameText)).stream().findFirst().orElse(noNameText);
				String respText = String.format("Hello %s!", name);
				exchange.sendResponseHeaders(200, respText.getBytes().length);
				System.out.println("Chegou Solicitação");
				OutputStream output = exchange.getResponseBody();
				output.write(respText.getBytes());
				output.flush();
			} else {
				exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
			}
			exchange.close();
		}));		

		server.setExecutor(null); // creates a default executor
		server.start();
		System.out.println("Servidor iniciado com sucesso");

	
	}
	
	public static HttpServer getServer() {
		return server;
	}
}
