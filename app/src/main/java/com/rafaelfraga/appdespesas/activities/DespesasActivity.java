package com.rafaelfraga.appdespesas.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.helpers.DataHelper;
import com.rafaelfraga.appdespesas.models.Movimentacao;

public class DespesasActivity extends AppCompatActivity {
    EditText mValor;
    TextInputEditText mData;
    TextInputEditText mCategoria;
    TextInputEditText mDescricao;
    FloatingActionButton mSalvar;

    private Movimentacao mMovimentacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        mValor = (EditText) findViewById(R.id.etDinheiro);
        mData = (TextInputEditText) findViewById(R.id.tietData);
        mCategoria = (TextInputEditText) findViewById(R.id.tietCategoria);
        mDescricao = (TextInputEditText) findViewById(R.id.tietDescricao);
        mSalvar = (FloatingActionButton) findViewById(R.id.fabSalvar);

        mData.setText(DataHelper.recuperarDataAtual());

        mSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDespesa();
            }
        });

    }

    public void salvarDespesa() {
        String valor = mValor.getText().toString();
        String data = mData.getText().toString();
        String categoria = mCategoria.getText().toString();
        String descricao = mDescricao.getText().toString();

        if(valor.isEmpty() || data.isEmpty() || categoria.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        mMovimentacao = new Movimentacao();
        mMovimentacao.setValor(Double.parseDouble(valor));
        mMovimentacao.setData(data);
        mMovimentacao.setCategoria(categoria);
        mMovimentacao.setDescricao(descricao);
        mMovimentacao.setTipo("D");

        mMovimentacao.salvar(data);

    }
}
