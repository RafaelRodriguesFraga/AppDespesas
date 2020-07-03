package com.rafaelfraga.appdespesas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.config.FirebaseConfig;
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

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Todos os " +
                            "campos são obrigatórios", Toast.LENGTH_SHORT).show();
                    return;
                }

                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);

                entrar();


            }
        });
    }

    public void entrar() {
        mAuth = FirebaseConfig.getFirebaseAuth();
        mAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                           Intent intent = new Intent(LoginActivity.this, DespesasActivity.class);
                           startActivity(intent);
                           finish();
                        }else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                excecao = "Usuário inválido ou desabilitado";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "Usuário ou senha inválidos";
                            } catch (Exception e) {
                                excecao = "Erro ao cadastrar o usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
