package com.rafaelfraga.appdespesas.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.models.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mSenha;
    private Button mEntrar;
    private Usuario usuario;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.etEmail);
        mSenha = findViewById(R.id.etSenha);
        mEntrar = findViewById(R.id.btnEntrar);

        mEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String senha = mSenha.getText().toString();

                validaCampos(email, senha);

                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);


            }
        });
    }

    public void validaCampos(String email, String senha) {
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }
    }



}
