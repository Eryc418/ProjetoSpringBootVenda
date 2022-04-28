package com.projeto_venda.venda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {
    Conexao minhaConexao;
    private final String INCLUIRUSUARIO = "insert into \"usuario\" (\"Nome\", \"Senha\", \"Email\", \"IdUser\",  \"TipoUsuario\") values (?,?,?,?,?)";
    private final String QTDUSUARIOS = "select count(*) from \"usuario\"";

    public UsuarioDAO() {
        minhaConexao = new Conexao("jdbc:postgresql://localhost:5432/BDLoja", "postgres", "eryc");
    }

    public void addUser(User usuario) {
        try {
            minhaConexao.conectar();
            PreparedStatement instrucao = minhaConexao.getConexao().prepareStatement(INCLUIRUSUARIO);
            instrucao.setString(1, usuario.getNome());
            instrucao.setString(2, usuario.getSenha());
            instrucao.setString(3, usuario.getEmail());
            instrucao.setInt(4, usuario.getId());
            instrucao.setInt(5, usuario.getTipoUser());
            instrucao.execute();
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na Inclusão: " + e.getMessage());
        }
    }

    public int qtdUsuario() {
        int qtd = 0;
        try {
            minhaConexao.conectar();
            Statement instrucao = minhaConexao.getConexao().createStatement();
            ResultSet rs = instrucao.executeQuery(QTDUSUARIOS);
            if (rs.next()) {
                qtd = rs.getInt("count");
            }
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return qtd;
    }
}