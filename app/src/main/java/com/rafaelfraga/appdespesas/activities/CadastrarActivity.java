package com.rafaelfraga.appdespesas.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rafaelfraga.appdespesas.R;
public class CadastrarActivity extends AppCompatActivity {

    private EditText mNome;
    private EditText mEmail;
    private EditText mSenha;
    private Button mCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        mNome = findViewById(R.id.etNome);
        mEmail = findViewById(R.id.etEmail);
        mSenha = findViewById(R.id.etSenha);
        mCadastrar = findViewById(R.id.btnCadastrar);

        mCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = mNome.getText().toString();
                String email = mEmail.getText().toString();
                String senha = mSenha.getText().toString();

                validaCampos(nome, email, senha);
                cadastrarUsuario();


            }
        });
    }

    public void validaCampos(String nome, String email, String senha) {
        if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void cadastrarUsuario() {

    }
}
