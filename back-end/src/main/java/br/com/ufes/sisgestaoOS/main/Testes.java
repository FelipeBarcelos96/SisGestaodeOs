package br.com.ufes.sisgestaoOS.main;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.sun.net.httpserver.HttpServer;

import br.com.ufes.sisgestaoOS.SQLUtils.OracleConnector;
import br.com.ufes.sisgestaoOS.api.ApiUtils;
import br.com.ufes.sisgestaoOS.model.Equipe;
import br.com.ufes.sisgestaoOS.model.Requisito;
import br.com.ufes.sisgestaoOS.model.Usuario;


public class Testes {

	public static void main(String[] args) {
		
		Date time;
		time = new Date(Timestamp.valueOf(LocalDateTime.now()).getTime());
		System.out.println(time);
		Connection conn = OracleConnector.getInstance().connect();
		Requisito req = new Requisito(
		new Usuario(9, "ANALISTA 2", "tecsis", Boolean.FALSE, Boolean.FALSE,Boolean.FALSE,Boolean.TRUE,
				new Equipe(
						2, "ANALISTAS "
						)
				)
		, "X", "X", new Timestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
		);
		try {
		String sql = "SELECT COUNT(*) AS EXISTE FROM REQUISITOS WHERE NOMEREQ = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, req.getTitulo().toUpperCase());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 != rs.getInt("EXISTE"))
        	System.out.println("Já Existe um Requisito Cadastrado com este Título!");
        else{
          sql = "INSERT INTO REQUISITOS (CODANALISTA, NOMEREQ, DESCRREQ, PRAZO) VALUES (?,?,?,?)";
          stmt = conn.prepareStatement(sql);
          System.out.println(
        		  req.getAnalista().getId()+" "+
        		  req.getTitulo().toUpperCase()+" "+
        		  req.getDescricao()+" "+
        		  req.getPrazo().toString()+" "
        		  );
          stmt.setInt(1, req.getAnalista().getId());
          stmt.setString(2, req.getTitulo().toUpperCase());
          stmt.setString(3, req.getDescricao());
          stmt.setDate(4, null);
          stmt.execute(); 
          
          sql = "SELECT CODREQ FROM REQUISITOS WHERE NOMEREQ = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, req.getTitulo().toUpperCase());
        
        rs = stmt.executeQuery();
        rs.next();
        req.setCodReq(rs.getInt("CODREQ"));
        }
        
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Erro: ");
			e.printStackTrace();
		}
		
		/*
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
*/
	}
	
}
