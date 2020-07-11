package com.rafaelfraga.appdespesas.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.rafaelfraga.appdespesas.config.FirebaseConfig;
import com.rafaelfraga.appdespesas.helpers.Base64Helper;
import com.rafaelfraga.appdespesas.helpers.DataHelper;

public class Movimentacao {
    private String data;
    private String descricao;
    private String tipo;
    private String chave;
    private Double valor;

    public Movimentacao() {
    }

    public void salvar(String data) {
        FirebaseAuth auth = FirebaseConfig.getFirebaseAuth();
        String id = Base64Helper.codificarBase64(auth.getCurrentUser().getEmail());
        String mesAno = DataHelper.converterDataParaMesAno(data);

        DatabaseReference reference = FirebaseConfig.getFirebaseReference();
        reference.child("movimentacoes")
                .child(id)
                .child(mesAno)
                .push() //Cria um id no Firebase
                .setValue(this);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
