package com.rafaelfraga.appdespesas.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                Intent intentDespesa = new Intent(DashboardActivity.this, DespesasActivity.class);
                startActivity(intentDespesa);
            break;

            case R.id.fabReceita:
                Intent imtentReceita = new Intent(DashboardActivity.this, ReceitasActivity.class);
                startActivity(imtentReceita);
                break;
        }
    }
}
