package com.rafaelfraga.appdespesas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.config.FirebaseConfig;
import com.rafaelfraga.appdespesas.models.Usuario;

public class CadastrarActivity extends AppCompatActivity {

    private EditText mNome;
    private EditText mEmail;
    private EditText mSenha;
    private Button mCadastrar;
    private FirebaseAuth mAuth;
    private Usuario mUsuario;

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

                mUsuario = new Usuario();
                mUsuario.setNome(nome);
                mUsuario.setEmail(email);
                mUsuario.setSenha(senha);

                cadastrarUsuario();
            }
        });
    }

    public void validaCampos(String nome, String email, String senha) {
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void cadastrarUsuario() {
        mAuth = FirebaseConfig.getFirebaseAuth();
        mAuth.createUserWithEmailAndPassword(mUsuario.getEmail(), mUsuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CadastrarActivity.this,
                                    R.string.mensagem_sucesso_cadastro_usuario
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                excecao = "Sua senha deve conter no mínimo 6 caracteres";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "Digite um email válido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                excecao = "Já existe um usuário cadastrado com este email";
                            } catch (Exception e) {
                                excecao = "Erro ao cadastrar o usuário: " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastrarActivity.this, excecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
