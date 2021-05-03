package br.com.ufes.sisgestaoOS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.ufes.sisgestaoOS.SQLUtils.OracleConnector;
import br.com.ufes.sisgestaoOS.model.Ordem;

public class OrdemDao implements IDAO<Ordem> {
	
private Connection conn = OracleConnector.getInstance().connect();
    
    private OrdemDao() {
    }
    
    public static OrdemDao getInstance() {
        return OrdemDaoHolder.INSTANCE;
    }
    
    private static class OrdemDaoHolder {

        private static final OrdemDao INSTANCE = new OrdemDao();
    }

	@Override
	public Ordem get(int id) throws SQLException {
		String sql = "SELECT * FROM ORDENS WHERE CODOS = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
             System.out.println("Ordem de Serviço Inválida!");
        
        rs = stmt.executeQuery(); 
        rs.next();
        Ordem ord = new Ordem(rs.getInt("CODOS")
        		, (rs.getInt("CODSOLICITANTE") <= 0 ? null : UsuarioDao.getInstance().get(rs.getInt("CODSOLICITANTE")))
        		, (rs.getInt("CODENCARREGADO") <= 0 ? null : UsuarioDao.getInstance().get(rs.getInt("CODENCARREGADO")))
        		, (rs.getInt("CODREQ") <= 0 ? null : RequisitoDao.getInstance().get(rs.getInt("CODREQ")))
        		, (rs.getInt("CODSTATUS") <= 0 ? null : StatusDao.getInstance().get(rs.getInt("CODSTATUS")))
        		, (rs.getInt("PRIORIDADE") <= 0 ? null : PrioridadeDao.getInstance().get(rs.getInt("PRIORIDADE")))
        		, (rs.getInt("CODEQUIPE") <= 0 ? null : EquipeDao.getInstance().get(rs.getInt("CODEQUIPE")))
        		, rs.getTimestamp("EMISSAO")
        		, rs.getString("DESCRICAO")
        		, rs.getBigDecimal("ESFORCO")
        		, rs.getTimestamp("ENTREGA")
        		, rs.getBigDecimal("VLRESTIMADO")
        		);
        stmt.close();
        return ord;
	}

	@Override
	public Ordem get(String descr) throws SQLException {
		String sql = "SELECT * FROM ORDENS WHERE DESCRICAO = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, descr);
        ResultSet rs = stmt.executeQuery();        
        if(!rs.next())
        	 System.out.println("Ordem de Serviço Inválida!");
        
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, descr);
        rs = stmt.executeQuery();
        rs.next();
        Ordem ord = new Ordem(rs.getInt("CODOS")
        		, (rs.getInt("CODSOLICITANTE") <= 0 ? null : UsuarioDao.getInstance().get(rs.getInt("CODSOLICITANTE")))
        		, (rs.getInt("CODENCARREGADO") <= 0 ? null : UsuarioDao.getInstance().get(rs.getInt("CODENCARREGADO")))
        		, (rs.getInt("CODREQ") <= 0 ? null : RequisitoDao.getInstance().get(rs.getInt("CODREQ")))
        		, (rs.getInt("CODSTATUS") <= 0 ? null : StatusDao.getInstance().get(rs.getInt("CODSTATUS")))
        		, (rs.getInt("PRIORIDADE") <= 0 ? null : PrioridadeDao.getInstance().get(rs.getInt("PRIORIDADE")))
        		, (rs.getInt("CODEQUIPE") <= 0 ? null : EquipeDao.getInstance().get(rs.getInt("CODEQUIPE")))
        		, rs.getTimestamp("EMISSAO")
        		, rs.getString("DESCRICAO")
        		, rs.getBigDecimal("ESFORCO")
        		, rs.getTimestamp("ENTREGA")
        		, rs.getBigDecimal("VLRESTIMADO")
        		);
        stmt.close();
        return ord;
	}

	@Override
	public void save(Ordem ord) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM ORDENS WHERE DESCRICAO = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, ord.getDescricao());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 != rs.getInt("EXISTE"))
        	System.out.println("Já Existe uma Ordem de Serviço com essa Descrição!");
        else{
          sql = "INSERT INTO ORDENS (CODSOLICITANTE, CODENCARREGADO, CODREQ, CODSTATUS, PRIORIDADE, CODEQUIPE, EMISSAO, DESCRICAO, ESFORCO, ENTREGA, VLRESTIMADO) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
          stmt = conn.prepareStatement(sql);
          stmt.setInt(1, ord.getSolicitante().getId());
          stmt.setInt(2, ord.getEncarregado().getId());
          stmt.setInt(3, ord.getRequisito().getCodReq());
          stmt.setInt(4, ord.getStatus().getCodStatus());
          stmt.setInt(5, ord.getPrioridade().getId());
          stmt.setInt(6, ord.getEquipe().getCodEquipe());
          stmt.setTimestamp(7, ord.getEmissao());
          stmt.setString(8, ord.getDescricao());
          stmt.setBigDecimal(9, ord.getEsforco());
          stmt.setTimestamp(10, ord.getEntrega());
          stmt.setBigDecimal(11, ord.getVlrEstimado());
          stmt.execute(); 
          
          sql = "SELECT CODOS FROM ORDENS WHERE DESCRICAO = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, ord.getDescricao());
        
        rs = stmt.executeQuery();
        rs.next();
        ord.setCodOs(rs.getInt("CODOS"));
        }		
        stmt.close();
	}

	@Override
	public void update(Ordem ord) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM ORDENS WHERE CODOS = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, ord.getCodOs());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Ordem de Serviço Não Encontrada!");
        else{
           sql = "UPDATE ORDENS SET CODSOLICITANTE = ? , CODENCARREGADO = ? , CODREQ = ? , CODSTATUS = ?, PRIORIDADE = ? , CODEQUIPE = ?,"
           		+ " EMISSAO = ?, DESCRICAO = ?, ESFORCO = ? , ENTREGA = ?, VLRESTIMADO = ? WHERE CODOS = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setInt(1, ord.getSolicitante().getId());
          stmt.setInt(2, ord.getEncarregado().getId());
          stmt.setInt(3, ord.getRequisito().getCodReq());
          stmt.setInt(4, ord.getStatus().getCodStatus());
          stmt.setInt(5, ord.getPrioridade().getId());
          stmt.setInt(6, ord.getEquipe().getCodEquipe());
          stmt.setTimestamp(7, ord.getEmissao());
          stmt.setString(8, ord.getDescricao());
          stmt.setBigDecimal(9, ord.getEsforco());
          stmt.setTimestamp(10, ord.getEntrega());
          stmt.setBigDecimal(11, ord.getVlrEstimado());
          stmt.setInt(12, ord.getCodOs());
          stmt.executeUpdate();
        }
        stmt.close();
	}

	@Override
	public void delete(int id) throws SQLException {
		String sql = "SELECT COUNT(*) AS EXISTE FROM ORDENS WHERE CODOS = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Ordem de Serviço Não Encontrada!");
        else{
            sql = "DELETE FROM ORDENS WHERE CODOS = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();        
        }	
        stmt.close();
	}

	@Override
	public ArrayList<Ordem> getAll() throws SQLException {
		ArrayList<Ordem> ordens = new ArrayList<Ordem>();
	       Statement stmt = conn.createStatement();
	       ResultSet rs = stmt.executeQuery("SELECT * FROM ORDENS");
	       while(rs.next()){
	    	   Ordem ord = new Ordem(rs.getInt("CODOS")
	        		   , (rs.getInt("CODSOLICITANTE") <= 0  ? null : UsuarioDao.getInstance().get(rs.getInt("CODSOLICITANTE")))
	        		   , (rs.getInt("CODENCARREGADO") <= 0  ? null : UsuarioDao.getInstance().get(rs.getInt("CODENCARREGADO")))
	        		   , (rs.getInt("CODREQ") <= 0  ? null : RequisitoDao.getInstance().get(rs.getInt("CODREQ")))
	        		   , (rs.getInt("CODSTATUS") <= 0  ? null : StatusDao.getInstance().get(rs.getInt("CODSTATUS")))
	        		   , (rs.getInt("PRIORIDADE") <= 0  ? null : PrioridadeDao.getInstance().get(rs.getInt("PRIORIDADE")))
	        		   , (rs.getInt("CODEQUIPE") <= 0  ? null : EquipeDao.getInstance().get(rs.getInt("CODEQUIPE")))
	        		   , rs.getTimestamp("EMISSAO")
	        		   , rs.getString("DESCRICAO")
	        		   , rs.getBigDecimal("ESFORCO")
	        		   , rs.getTimestamp("ENTREGA")
	        		   , rs.getBigDecimal("VLRESTIMADO")
	        		   );
	         
	    	   ordens.add(ord);
	           
	       }
	       stmt.close();
	       return ordens;
	
	}

}
