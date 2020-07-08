package com.rafaelfraga.appdespesas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.config.FirebaseConfig;
import com.rafaelfraga.appdespesas.helpers.Base64Helper;
import com.rafaelfraga.appdespesas.helpers.DataHelper;
import com.rafaelfraga.appdespesas.models.Movimentacao;
import com.rafaelfraga.appdespesas.models.Usuario;

public class ReceitasActivity extends AppCompatActivity {

    private EditText mValor;
    private TextInputEditText mData;
    private TextInputEditText mCategoria;
    private TextInputEditText mDescricao;
    private FloatingActionButton mSalvar;
    private Movimentacao mMovimentacao;

    private DatabaseReference mRef = FirebaseConfig.getFirebaseReference();
    private FirebaseAuth mAuth = FirebaseConfig.getFirebaseAuth();

    private Double mReceitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        mValor = (EditText) findViewById(R.id.etDinheiro);
        mData = (TextInputEditText) findViewById(R.id.tietData);
        mCategoria = (TextInputEditText) findViewById(R.id.tietCategoria);
        mDescricao = (TextInputEditText) findViewById(R.id.tietDescricao);
        mSalvar = (FloatingActionButton) findViewById(R.id.fabSalvar);

        mData.setText(DataHelper.recuperarDataAtual());

        recuperarReceitaTotal();

        mSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarReceita();
            }
        });
    }

    public void salvarReceita() {
        String valor = mValor.getText().toString();
        String data = mData.getText().toString();
        String categoria = mCategoria.getText().toString();
        String descricao = mDescricao.getText().toString();

        if (valor.isEmpty() || data.isEmpty() || categoria.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        double valorConvertido = Double.parseDouble(valor);

        mMovimentacao = new Movimentacao();
        mMovimentacao.setValor(valorConvertido);
        mMovimentacao.setData(data);
        mMovimentacao.setCategoria(categoria);
        mMovimentacao.setDescricao(descricao);
        mMovimentacao.setTipo("R");

        Double receitaAtualizada = mReceitaTotal + valorConvertido;
        atualizarReceita(receitaAtualizada);

        mMovimentacao.salvar(data);
        finish();

    }

    public void recuperarReceitaTotal() {
        String id = Base64Helper.codificarBase64(mAuth.getCurrentUser().getEmail());
        DatabaseReference reference = mRef.child("usuarios").child(id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                mReceitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita(Double receita) {
        String id = Base64Helper.codificarBase64(mAuth.getCurrentUser().getEmail());
        DatabaseReference reference = mRef.child("usuarios").child(id);

        reference.child("receitaTotal").setValue(receita);
    }


}
