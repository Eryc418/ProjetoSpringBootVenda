package com.projeto_venda.venda;

public class User {
    private String nome;
    private String senha;
    private String email;
    private Integer id;
    private Integer tipoUser;

    User() {

    }

    User(String nome, String senha, String email, Integer id, Integer tipoUser) {
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.id = id;
        this.tipoUser = tipoUser;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTipoUser(Integer tipoUser) {
        this.tipoUser = tipoUser;
    }

    public String getNome() {
        return this.nome;
    }

    public String getSenha() {
        return this.senha;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getTipoUser() {
        return this.tipoUser;
    }
}
