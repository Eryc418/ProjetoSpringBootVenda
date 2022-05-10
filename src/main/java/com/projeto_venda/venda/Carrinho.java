package com.projeto_venda.venda;

public class Carrinho {
    private Integer idUser;
    private Integer idProduto;
    private String nomeProduto;

    public Carrinho() {

    }

    public Carrinho(Integer idUser, Integer idProduto, String nomeProduto) {
        this.idUser = idUser;
        this.idProduto = idProduto;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

}
