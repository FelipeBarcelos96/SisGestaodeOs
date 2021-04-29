package br.com.ufes.sisgestaoOS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.ufes.sisgestaoOS.SQLUtils.OracleConnector;
import br.com.ufes.sisgestaoOS.model.Prioridade;

public class PrioridadeDao implements IDAO<Prioridade> {
	
	private Connection conn = OracleConnector.getInstance().connect();
	
    private PrioridadeDao() {
    }
    
    public static PrioridadeDao getInstance() {
        return PrioridadeDaoHolder.INSTANCE;
    }
    
    private static class PrioridadeDaoHolder {
        private static final PrioridadeDao INSTANCE = new PrioridadeDao();
    } 
    

	@Override
	public Prioridade get(int id) throws SQLException {
		String sql = "SELECT * FROM PRIORIDADES WHERE ID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
             System.out.println("Prioridade Inválida!");
        
        rs = stmt.executeQuery(); 
        rs.next();
        Prioridade prioridade = new Prioridade(rs.getInt("ID")
        		, rs.getString("NOME")
        		); 
        prioridade.setId(id);
     
        return prioridade;
	}

	@Override
	public Prioridade get(String nome) throws SQLException {
		String sql = "SELECT * FROM PRIORIDADES WHERE NOME = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nome);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
             System.out.println("Prioridade Não Encontrada!");
        
        rs = stmt.executeQuery(); 
        rs.next();
        Prioridade prioridade = new Prioridade(rs.getInt("ID")
        		, rs.getString("NOME")
        		); 
        prioridade.setId(rs.getInt("ID"));
      
        return prioridade;
	}

	@Override
	public void save(Prioridade prioridade) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM PRIORIDADES WHERE NOME = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, prioridade.getNome().toUpperCase());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 != rs.getInt("EXISTE"))
        	System.out.println("Já Existe um Prioridade Cadastrada com esta Sigla!");
        else{
          sql = "INSERT INTO PRIORIDADES (NOME) VALUES (?)";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, prioridade.getNome().toUpperCase());
          stmt.execute(); 
          
          sql = "SELECT ID FROM PRIORIDADES WHERE NOME = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, prioridade.getNome().toUpperCase());
        
        rs = stmt.executeQuery();
        rs.next();
        prioridade.setId(rs.getInt("ID"));
        }
	}

	@Override
	public void update(Prioridade prioridade) throws SQLException {
		
		String sql = "SELECT COUNT(*) AS EXISTE FROM PRIORIDADES WHERE ID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, prioridade.getId());
        ResultSet rs = stmt.executeQuery();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Prioridade Não Encontrada!");
        else{
           sql = "UPDATE PRIORIDADES SET NOME = ? WHERE ID = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, prioridade.getNome().toUpperCase());
          stmt.setInt(2, prioridade.getId());
          stmt.executeUpdate();
        }
	}

	@Override
	public void delete(int id) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM PRIORIDADES WHERE ID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Prioridade Não Encontrada!");
        else{
            sql = "DELETE FROM PRIORIDADES WHERE ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
        }
	}

	@Override
	public ArrayList<Prioridade> getAll() throws SQLException {
		ArrayList<Prioridade> prioridades = new ArrayList<Prioridade>();
	       Statement stmt = conn.createStatement();
	       ResultSet rs = stmt.executeQuery("SELECT * FROM PRIORIDADES");
	       while(rs.next()){
	    	   Prioridade prioridade = new Prioridade(rs.getInt("ID")
	        		   , rs.getString("NOME") 		  
	        		   );
	
	    	   prioridades.add(prioridade);
	           
	       }
	       
	       return prioridades;
	}

}
