package com.projeto_venda.venda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProdutoDAO {
    Conexao minhaConexao;
    private static final String INCLUIRPRODUTO = "insert into \"produto\" (\"nome\", \"marca\", \"modelo\", \"preco\", \"idProduto\") values (?,?,?,?,?)";
    private static final String QTDPRODUTOS = "select count(*) from \"produto\"";
    private static final String RELATORIOPRODUTOS = "select * from \"produto\"";
    private static final String BUSCAPRODUTO = "select * from \"produto\" where \"idProduto\"=?";

    public ProdutoDAO() {
        minhaConexao = new Conexao("jdbc:postgresql://localhost:5432/BDLoja", "postgres", "eryc");
    }

    public ArrayList<Produto> BuscarGeral() {
        ArrayList<Produto> lista_produto = new ArrayList<>();
        Produto p = null;
        try {
            minhaConexao.conectar();
            Statement instrucao = minhaConexao.getConexao().createStatement();
            ResultSet rs = instrucao.executeQuery(RELATORIOPRODUTOS);
            while (rs.next()) {
                p = new Produto(rs.getString("nome"), rs.getString("marca"), rs.getString("modelo"),
                        rs.getFloat("preco"), rs.getInt("idProduto"));

                lista_produto.add(p);
            }
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na busca geral: " + e.getMessage());
        }
        return lista_produto;
    }

    public void addProduto(Produto produto) {
        try {
            minhaConexao.conectar();
            PreparedStatement instrucao = minhaConexao.getConexao().prepareStatement(INCLUIRPRODUTO);
            instrucao.setString(1, produto.getNome());
            instrucao.setString(2, produto.getMarca());
            instrucao.setString(3, produto.getModelo());
            instrucao.setFloat(4, produto.getPreco());
            instrucao.setInt(5, produto.getId());
            instrucao.execute();
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na Inclusão: " + e.getMessage());
        }
    }

    public int qtdProdutos() {
        int qtd = 0;
        try {
            minhaConexao.conectar();
            Statement instrucao = minhaConexao.getConexao().createStatement();
            ResultSet rs = instrucao.executeQuery(QTDPRODUTOS);
            if (rs.next()) {
                qtd = rs.getInt("count");
            }
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return qtd;
    }

    public Produto buscarProduto(Integer idProduto) {

        Produto produto = null;
        try {
            minhaConexao.conectar();
            PreparedStatement instrucao = minhaConexao.getConexao().prepareStatement(BUSCAPRODUTO);
            instrucao.setInt(1, idProduto);
            ResultSet rs = instrucao.executeQuery();
            if (rs.next()) {
                produto = new Produto(rs.getString("nome"), rs.getString("marca"), rs.getString("modelo"),
                        rs.getFloat("preco"),
                        rs.getInt("idProduto"));
            }
            minhaConexao.desconectar();
        } catch (SQLException e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return produto;
    }
}
