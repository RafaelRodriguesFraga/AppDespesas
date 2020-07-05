package com.rafaelfraga.appdespesas.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.rafaelfraga.appdespesas.config.FirebaseConfig;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private Double receitaTotal = 0.00;
    private Double despesaTotal = 0.00;

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario() {
    }

    public void cadastrar() {
        DatabaseReference reference = FirebaseConfig.getFirebaseReference();

        //cria um nó chamado usuarios
        reference.child("usuarios")
                .child(this.id)
                .setValue(this);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public Double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(Double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }
}
