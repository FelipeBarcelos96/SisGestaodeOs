package br.com.ufes.sisgestaoOS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import br.com.ufes.sisgestaoOS.SQLUtils.OracleConnector;
import br.com.ufes.sisgestaoOS.model.Equipe;

public class EquipeDao implements IDAO<Equipe>{
	
	private Connection conn = OracleConnector.getInstance().connect();
    private EquipeDao() {
    }
    
    public static EquipeDao getInstance() {
        return EquipeDaoHolder.INSTANCE;
    }
    
    private static class EquipeDaoHolder {

        private static final EquipeDao INSTANCE = new EquipeDao();
    }

	@Override
	public Equipe get(int id) throws SQLException {
		String sql = "SELECT * FROM EQUIPES WHERE CODEQUIPE = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
             System.out.println("Equipe Inválida!");
        
        rs = stmt.executeQuery(); 
        rs.next();
        Equipe equipe = new Equipe(rs.getInt("CODEQUIPE")
        		, rs.getString("SIGLA")
        		); 
        equipe.setCodEquipe(id);
     
        return equipe;
	}

	@Override
	public Equipe get(String sigla) throws SQLException {
		String sql = "SELECT * FROM EQUIPES WHERE SIGLA = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, sigla);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
             System.out.println("Equipe Não Encontrada!");
        
        rs = stmt.executeQuery(); 
        rs.next();
        Equipe equipe = new Equipe(rs.getInt("CODEQUIPE")
        		, rs.getString("SIGLA")
        		); 
        equipe.setCodEquipe(rs.getInt("CODEQUIPE"));
      
        return equipe;
	}

	@Override
	public void save(Equipe equipe) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM EQUIPES WHERE SIGLA = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, equipe.getSigla().toUpperCase());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 != rs.getInt("EXISTE"))
        	System.out.println("Já Existe um Equipe Cadastrada com esta Sigla!");
        else{
          sql = "INSERT INTO EQUIPES (SIGLA) VALUES (?)";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, equipe.getSigla().toUpperCase());
          stmt.execute(); 
          
          sql = "SELECT CODEQUIPE FROM EQUIPES WHERE SIGLA = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, equipe.getSigla().toUpperCase());
        
        rs = stmt.executeQuery();
        rs.next();
        equipe.setCodEquipe(rs.getInt("CODEQUIPE"));
        //PermissoesDao.getInstance().save(usuario);
        }
		
	}
	
	@Override
	public void update(Equipe equipe) throws SQLException {
		
	//  System.out.println(Integer.toString(usuario.getId()));
        String sql = "SELECT COUNT(*) AS EXISTE FROM EQUIPES WHERE CODEQUIPE = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, equipe.getCodEquipe());
        ResultSet rs = stmt.executeQuery();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Equipe Não Encontrada!");
        else{
           sql = "UPDATE EQUIPES SET SIGLA = ? WHERE CODEQUIPE = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, equipe.getSigla().toUpperCase());
          stmt.setInt(2, equipe.getCodEquipe());
          stmt.executeUpdate();
         
        }
		
	}

	@Override
	public void delete(int id) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM EQUIPES WHERE CODEQUIPE = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Equipe Não Encontrada!");
        else{
            sql = "DELETE FROM EQUIPES WHERE CODEQUIPE = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
          // PermissoesDao.getInstance().delete(usuario);
        }		
	}

	@Override
	public ArrayList<Equipe> getAll() throws SQLException {
		ArrayList<Equipe> equipes = new ArrayList<Equipe>();
	       Statement stmt = conn.createStatement();
	       ResultSet rs = stmt.executeQuery("SELECT * FROM EQUIPES");
	       while(rs.next()){
	    	   Equipe equipe = new Equipe(rs.getInt("CODEQUIPE")
	        		   , rs.getString("SIGLA") 		  
	        		   );
	
	    	   equipes.add(equipe);
	           
	       }
	       
	       return equipes;
	}	

}
