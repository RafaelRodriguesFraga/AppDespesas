package com.rafaelfraga.appdespesas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.rafaelfraga.appdespesas.R;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton mDespesa;
    private FloatingActionButton mReceita;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mDespesa = findViewById(R.id.fabDespesa);
        mReceita = findViewById(R.id.fabReceita);

        mDespesa.setOnClickListener(this);
        mReceita.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabDespesa:
                Toast.makeText(this, "Clicou no botao despesa", Toast.LENGTH_SHORT).show();
            break;

            case R.id.fabReceita:
                Toast.makeText(this, "Clicou no botao receita", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
