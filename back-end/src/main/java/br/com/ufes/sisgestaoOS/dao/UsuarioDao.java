/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufes.sisgestaoOS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.ufes.sisgestaoOS.SQLUtils.OracleConnector;
import br.com.ufes.sisgestaoOS.model.Usuario;

/**
 *
 * @author Felps
 */
public class UsuarioDao implements IDAO<Usuario>{
	
	private Connection conn = OracleConnector.getInstance().connect();
    private Interpretador interpretador = Interpretador.getInstance();
    
    private UsuarioDao() {
    }
    
    public static UsuarioDao getInstance() {
        return UsuarioDaoHolder.INSTANCE;
    }
    
    private static class UsuarioDaoHolder {

        private static final UsuarioDao INSTANCE = new UsuarioDao();
    }
    
    public Usuario get(int id) throws SQLException {
        String sql = "SELECT * FROM USUARIOS WHERE CODUSU = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if(!rs.next())
             System.out.println("Usuário Inválido!");
        
        rs = stmt.executeQuery(); 
        rs.next();
        Usuario usu = new Usuario(rs.getString("NOMEUSU")
        		, rs.getString("SENHA")
        		, interpretador.interpreta(rs.getString("ADMINISTRADOR"))
        		, interpretador.interpreta(rs.getString("GESTOR"))
        		, interpretador.interpreta(rs.getString("DESENVOLVEDOR"))
        		, interpretador.interpreta(rs.getString("ANALISTA"))
        		, (rs.getInt("CODEQUIPE") < 0 ? null : EquipeDao.getInstance().get(rs.getInt("CODEQUIPE")))
        		); 
        usu.setId(id);
      //  usu.setPermissoes(PermissoesDao.getInstance().get(codUsu));
        return usu;
    }
    
    public Usuario get(String nome) throws SQLException {
        String sql = "SELECT * FROM USUARIOS WHERE NOMEUSU = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nome.toUpperCase());
        ResultSet rs = stmt.executeQuery();        
        if(!rs.next())
        	 System.out.println("Usuário Inválido!");
        
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, nome.toUpperCase());
        rs = stmt.executeQuery();
        rs.next();
        Usuario usu = new Usuario(rs.getString("NOMEUSU")
        		, rs.getString("SENHA")
        		, interpretador.interpreta(rs.getString("ADMINISTRADOR"))
        		, interpretador.interpreta(rs.getString("GESTOR"))
        		, interpretador.interpreta(rs.getString("DESENVOLVEDOR"))
        		, interpretador.interpreta(rs.getString("ANALISTA"))
        		, (rs.getInt("CODEQUIPE") < 0  ? null : EquipeDao.getInstance().get(rs.getInt("CODEQUIPE")))
        		); 
        usu.setId(rs.getInt("CODUSU"));
      //  System.out.println(usu.getIsAdmin().toString()+" , "+ usu.getPermissoes().getVisualizar().toString()+" , "+usu.getPermissoes().getExcluir().toString()+","+usu.getPermissoes().getCompartilhar().toString());
        return usu;
    }

    public void save(Usuario usuario) throws SQLException {
        String sql = "SELECT COUNT(*) AS EXISTE FROM USUARIOS WHERE NOMEUSU = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, usuario.getNome().toUpperCase());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 != rs.getInt("EXISTE"))
        	System.out.println("Já Existe um Usuário Cadastrado com este nome!");
        else{
          sql = "INSERT INTO USUARIOS (CODEQUIPE, NOMEUSU, SENHA, ADMINISTRADOR, GESTOR, DESENVOLVEDOR, ANALISTA) VALUES (?,?,?,?,?,?,?)";
          stmt = conn.prepareStatement(sql);
          stmt.setInt(1, usuario.getEquipe().getCodEquipe());
          stmt.setString(2, usuario.getNome().toUpperCase());
          stmt.setString(3, usuario.getPass());
          stmt.setString(4, interpretador.interpreta(usuario.getEhAdm()));
          stmt.setString(5, interpretador.interpreta(usuario.getEhGestor()));
          stmt.setString(6, interpretador.interpreta(usuario.getEhDev()));
          stmt.setString(7, interpretador.interpreta(usuario.getEhAnal()));
          stmt.execute(); 
          
          sql = "SELECT CODUSU FROM USUARIOS WHERE NOMEUSU = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, usuario.getNome().toUpperCase());
        
        rs = stmt.executeQuery();
        rs.next();
        usuario.setId(rs.getInt("CODUSU"));
        //PermissoesDao.getInstance().save(usuario);
        }
    }

    
    public void update(Usuario usuario) throws SQLException {
      //  System.out.println(Integer.toString(usuario.getId()));
        String sql = "SELECT COUNT(*) AS EXISTE FROM USUARIOS WHERE CODUSU = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, usuario.getId());
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Usuário¡rio Não Encontrado!");
        else{
           sql = "UPDATE USUARIOS SET NOMEUSU = ? , SENHA = ? , ADMINISTRADOR = ? , GESTOR = ? , DESENVOLVEDOR = ? , ANALISTA = ?, CODEQUIPE = ? WHERE CODUSU = ?";
          stmt = conn.prepareStatement(sql);
          stmt.setString(1, usuario.getNome());
          stmt.setString(2, usuario.getPass());
          stmt.setString(3, interpretador.interpreta(usuario.getEhAdm()));
          stmt.setString(4, interpretador.interpreta(usuario.getEhGestor()));
          stmt.setString(5, interpretador.interpreta(usuario.getEhDev()));
          stmt.setString(6, interpretador.interpreta(usuario.getEhAnal()));
          stmt.setInt(7, usuario.getEquipe().getCodEquipe());
          stmt.setInt(8, usuario.getId());
          stmt.executeUpdate();
         
        }
    }

    
    public void delete(int id) throws SQLException {
        String sql = "SELECT COUNT(*) AS EXISTE FROM USUARIOS WHERE CODUSU = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        if(0 == rs.getInt("EXISTE"))
        	System.out.println("Usuário¡rio Não Encontrado!");
        else{
            sql = "DELETE FROM USUARIOS WHERE CODUSU = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
          // PermissoesDao.getInstance().delete(usuario);
        }
    }

    
    public ArrayList<Usuario> getAll() throws SQLException {
       ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
       Statement stmt = conn.createStatement();
       ResultSet rs = stmt.executeQuery("SELECT * FROM USUARIOS");
       /*if(rs.next()){
           Usuario usu = new Usuario(rs.getInt("CODUSU"), rs.getString("NOMEUSU"), rs.getString("PASS"), interpretador.interpreta(rs.getString("ADM")));
           usu.setPermissoes(PermissoesDao.getInstance().get(rs.getInt("CODUSU")));
           usuarios.add(usu);
       }*/
       while(rs.next()){
           Usuario usu = new Usuario(rs.getInt("CODUSU")
        		   , rs.getString("NOMEUSU")
        		   , rs.getString("SENHA")
        		   , interpretador.interpreta(rs.getString("ADMINISTRADOR"))
        		   , interpretador.interpreta(rs.getString("GESTOR"))
        		   , interpretador.interpreta(rs.getString("DESENVOLVEDOR"))
        		   , interpretador.interpreta(rs.getString("ANALISTA"))
        		   , (rs.getInt("CODEQUIPE") < 0  ? null : EquipeDao.getInstance().get(rs.getInt("CODEQUIPE")))
        		   );
           //usu.setPermissoes(PermissoesDao.getInstance().get(rs.getInt("CODUSU")));
           usuarios.add(usu);
           
       }
       
       return usuarios;
    }   
    
}
