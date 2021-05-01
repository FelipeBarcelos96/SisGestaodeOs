package br.com.ufes.sisgestaoOS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.ufes.sisgestaoOS.SQLUtils.OracleConnector;
import br.com.ufes.sisgestaoOS.model.Status;

public class StatusDao implements IDAO<Status> {
	
	private Connection conn = OracleConnector.getInstance().connect();
	
    private StatusDao() {
    }
    
    public static StatusDao getInstance() {
        return StatusDaoHolder.INSTANCE;
    }
    
    private static class StatusDaoHolder {
        private static final StatusDao INSTANCE = new StatusDao();
    } 
    

	@Override
	public Status get(int codStatus) throws SQLException {
		String sql = "SELECT * FROM STATUS WHERE CODSTATUS = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, codStatus);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
             System.out.println("Status Inválido!");
        
        rs = stmt.executeQuery(); 
        rs.next();
        Status status = new Status(rs.getInt("CODSTATUS")
        		, rs.getString("DESCRSTATUS")
        		); 
        status.setCodStatus(codStatus);
        stmt.close();
        return status;
	}

	@Override
	public Status get(String nome) throws SQLException {
		String sql = "SELECT * FROM STATUS WHERE DESCRSTATUS = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nome);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
             System.out.println("Status Não Encontrado!");
        
        rs = stmt.executeQuery(); 
        rs.next();
        Status status = new Status(rs.getInt("CODSTATUS")
        		, rs.getString("DESCRSTATUS")
        		); 
        status.setCodStatus(rs.getInt("CODSTATUS"));
        stmt.close();
        return status;
	}

	@Override
	public void save(Status status) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM STATUS WHERE DESCRSTATUS = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, status.getStatus().toUpperCase());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 != rs.getInt("EXISTE"))
        	System.out.println("Já Existe um Status Cadastrada com esta Sigla!");
        else{
          sql = "INSERT INTO STATUS (DESCRSTATUS) VALUES (?)";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, status.getStatus().toUpperCase());
          stmt.execute(); 
          
          sql = "SELECT CODSTATUS FROM STATUS WHERE DESCRSTATUS = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, status.getStatus().toUpperCase());
        
        rs = stmt.executeQuery();
        rs.next();
        status.setCodStatus(rs.getInt("CODSTATUS"));
        }
        stmt.close();
	}

	@Override
	public void update(Status status) throws SQLException {
		
		String sql = "SELECT COUNT(*) AS EXISTE FROM STATUS WHERE CODSTATUS = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, status.getCodStatus());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Status Não Encontrado!");
        else{
           sql = "UPDATE STATUS SET DESCRSTATUS = ? WHERE CODSTATUS = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, status.getStatus().toUpperCase());
          stmt.setInt(2, status.getCodStatus());
          stmt.executeUpdate();
        }
        stmt.close();
	}

	@Override
	public void delete(int codStatus) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM STATUS WHERE CODSTATUS = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, codStatus);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Status Não Encontrado!");
        else{
            sql = "DELETE FROM STATUS WHERE CODSTATUS = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, codStatus);
            stmt.execute();
        }
        stmt.close();
	}

	@Override
	public ArrayList<Status> getAll() throws SQLException {
		ArrayList<Status> statuss = new ArrayList<Status>();
	       Statement stmt = conn.createStatement();
	       ResultSet rs = stmt.executeQuery("SELECT * FROM STATUS");
	       while(rs.next()){
	    	   Status status = new Status(rs.getInt("CODSTATUS")
	        		   , rs.getString("DESCRSTATUS") 		  
	        		   );
	
	    	   statuss.add(status);
	           
	       }
	       stmt.close();
	       return statuss;
	}

}
