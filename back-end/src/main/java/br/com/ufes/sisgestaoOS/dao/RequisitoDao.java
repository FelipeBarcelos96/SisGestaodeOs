package br.com.ufes.sisgestaoOS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.ufes.sisgestaoOS.SQLUtils.OracleConnector;
import br.com.ufes.sisgestaoOS.model.Requisito;

public class RequisitoDao implements IDAO<Requisito> {
	
	private Connection conn = OracleConnector.getInstance().connect();
    
    private RequisitoDao() {
    }
    
    public static RequisitoDao getInstance() {
        return RequisitoDaoHolder.INSTANCE;
    }
    
    private static class RequisitoDaoHolder {

        private static final RequisitoDao INSTANCE = new RequisitoDao();
    }

	@Override
	public Requisito get(int id) throws SQLException {
		String sql = "SELECT * FROM REQUISITOS WHERE CODREQ = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
             System.out.println("Requisito Inválido!");
        
        rs = stmt.executeQuery(); 
        rs.next();
        Requisito req = new Requisito(rs.getInt("CODREQ")
        		, (rs.getInt("CODANALISTA") < 0 ? null : UsuarioDao.getInstance().get(rs.getInt("CODANALISTA")))
        		, rs.getString("NOMEREQ")
        		, rs.getString("DESCRREQ")
        		, rs.getTimestamp("PRAZO")
        		);
        return req;
	}

	@Override
	public Requisito get(String nome) throws SQLException {
		String sql = "SELECT * FROM REQUISITOS WHERE NOMEREQ = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nome.toUpperCase());
        ResultSet rs = stmt.executeQuery();        
        if(!rs.next())
        	 System.out.println("Requisito Inválido!");
        
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, nome.toUpperCase());
        rs = stmt.executeQuery();
        rs.next();
        Requisito req = new Requisito(rs.getInt("CODREQ")
        		, (rs.getInt("CODANALISTA") < 0 ? null : UsuarioDao.getInstance().get(rs.getInt("CODANALISTA")))
        		, rs.getString("NOMEREQ")
        		, rs.getString("DESCRREQ")
        		, rs.getTimestamp("PRAZO")
        		);
        return req;
	}

	@Override
	public void save(Requisito req) throws SQLException {
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
          stmt.setInt(1, req.getAnalista().getId());
          stmt.setString(2, req.getTitulo().toUpperCase());
          stmt.setString(3, req.getDescricao());
          stmt.setTimestamp(4, req.getPrazo());
          stmt.execute(); 
          
          sql = "SELECT CODREQ FROM REQUISITOS WHERE NOMEREQ = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, req.getTitulo().toUpperCase());
        
        rs = stmt.executeQuery();
        rs.next();
        req.setCodReq(rs.getInt("CODREQ"));
        }
		
	}

	@Override
	public void update(Requisito req) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM REQUISITOS WHERE CODREQ = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, req.getCodReq());
        ResultSet rs = stmt.executeQuery();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Requisito Não Encontrado!");
        else{
           sql = "UPDATE REQUISITOS SET CODANALISTA = ? , NOMEREQ = ? , DESCRREQ = ? , PRAZO = ? WHERE CODREQ = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setInt(1, req.getAnalista().getId());
          stmt.setString(2, req.getTitulo());
          stmt.setString(3, req.getDescricao());
          stmt.setTimestamp(4, req.getPrazo());
          stmt.setInt(5, req.getCodReq());
          stmt.executeUpdate();
        }
	}

	@Override
	public void delete(int id) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM REQUISITOS WHERE CODREQ = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Requisito Não Encontrado!");
        else{
            sql = "DELETE FROM REQUISITOS WHERE CODREQ = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();        
        }		
	}

	@Override
	public ArrayList<Requisito> getAll() throws SQLException {
		ArrayList<Requisito> requisitos = new ArrayList<Requisito>();
	       Statement stmt = conn.createStatement();
	       ResultSet rs = stmt.executeQuery("SELECT * FROM REQUISITOS");
	       while(rs.next()){
	    	   Requisito req = new Requisito(rs.getInt("CODREQ")
	        		   , (rs.getInt("CODANALISTA") < 0  ? null : UsuarioDao.getInstance().get(rs.getInt("CODANALISTA")))
	        		   , rs.getString("NOMEREQ")
	        		   , rs.getString("DESCRREQ")
	        		   , rs.getTimestamp("PRAZO")
	        		   );
	         
	    	   requisitos.add(req);
	           
	       }
	       
	       return requisitos;
	}

}
