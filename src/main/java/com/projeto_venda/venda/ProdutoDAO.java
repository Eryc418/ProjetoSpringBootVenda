package com.projeto_venda.venda;

public class ProdutoDAO {
    Conexao minhaConexao;
    private static final String INCLUIRPRODUTO = "insert into \"produto\" (\"nome\", \"marca\", \"modelo\", \"preco\", \"idProduto\") values (?,?,?,?,?)";
    private static final String QTDPRODUTOS = "select count(*) from \"produto\"";
    private static final String RELATORIOPRODUTOS = "select * from \"produto\"";

    public ProdutoDAO() {
        minhaConexao = new Conexao("jdbc:postgresql://localhost:5432/BDLoja", "postgres", "eryc");
    }

}
