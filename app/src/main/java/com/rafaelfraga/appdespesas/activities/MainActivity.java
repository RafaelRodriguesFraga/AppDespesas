package com.rafaelfraga.appdespesas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.config.FirebaseConfig;

public class MainActivity extends IntroActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adicionarSlides();
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    private void adicionarSlides() {

        setButtonNextVisible(false);
        setButtonBackVisible(true);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build());
    }

    private void verificarUsuarioLogado() {
        mAuth = FirebaseConfig.getFirebaseAuth();

        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void entrar(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }

    public void cadastrar(View view) {
        startActivity(new Intent(MainActivity.this, CadastrarActivity.class));
    }
}
