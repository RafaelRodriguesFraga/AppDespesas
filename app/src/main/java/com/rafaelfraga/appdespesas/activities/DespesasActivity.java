package com.rafaelfraga.appdespesas.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.config.FirebaseConfig;
import com.rafaelfraga.appdespesas.fragments.DatePickerFragment;
import com.rafaelfraga.appdespesas.helpers.Base64Helper;
import com.rafaelfraga.appdespesas.helpers.DataHelper;
import com.rafaelfraga.appdespesas.models.Movimentacao;
import com.rafaelfraga.appdespesas.models.Usuario;

import java.text.DateFormat;
import java.util.Calendar;

public class DespesasActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener {

    private TextInputEditText mValor;
    private TextInputEditText mData;
    private TextInputEditText mDescricao;
    private Button mSalvar;
    private Movimentacao mMovimentacao;

    private DatabaseReference mRef = FirebaseConfig.getFirebaseReference();
    private FirebaseAuth mAuth = FirebaseConfig.getFirebaseAuth();

    private Double mDespesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        mValor = findViewById(R.id.tietValor);
        mData = findViewById(R.id.tietData);
        mDescricao = findViewById(R.id.tietDescricao);
        mSalvar = findViewById(R.id.btnSalvarDespesa);

        mData.setText(DataHelper.recuperarDataAtual());

        recuperarDespesaTotal();

        mSalvar.setOnClickListener(this);
        mData.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tietData:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                break;

            case R.id.btnSalvarDespesa:
                salvarDespesa();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.YEAR, year);
        calendario.set(Calendar.MONTH, month);
        calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String dataAtual = DateFormat.getDateInstance(DateFormat.SHORT).format(calendario.getTime());

        mData.setText(dataAtual);
    }

    public void salvarDespesa() {
        String valor = mValor.getText().toString();
        String data = mData.getText().toString();
        String descricao = mDescricao.getText().toString();

        if (valor.isEmpty() || data.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        double valorConvertido = Double.parseDouble(valor);

        mMovimentacao = new Movimentacao();
        mMovimentacao.setValor(valorConvertido);
        mMovimentacao.setData(data);
        mMovimentacao.setDescricao(descricao);
        mMovimentacao.setTipo("D");

        Double despesaAtualizada = mDespesaTotal + valorConvertido;
        atualizarDespesa(despesaAtualizada);

        mMovimentacao.salvar(data);

        Toast.makeText(this, "Despesa salva com sucesso", Toast.LENGTH_SHORT).show();

        finish();

    }

    public void recuperarDespesaTotal() {
        String id = Base64Helper.codificarBase64(mAuth.getCurrentUser().getEmail());
        DatabaseReference reference = mRef.child("usuarios").child(id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                mDespesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDespesa(Double despesa) {
        String id = Base64Helper.codificarBase64(mAuth.getCurrentUser().getEmail());
        DatabaseReference reference = mRef.child("usuarios").child(id);

        reference.child("despesaTotal").setValue(despesa);

    }
}
