package com.rafaelfraga.appdespesas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.config.FirebaseConfig;
import com.rafaelfraga.appdespesas.helpers.Base64Helper;
import com.rafaelfraga.appdespesas.models.Usuario;

import java.text.DecimalFormat;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton mDespesa;
    private FloatingActionButton mReceita;
    private MaterialCalendarView mCalendarView;
    private TextView mSaudacao;
    private TextView mSaldo;
    private ImageView mSair;

    private FirebaseAuth mAuth = FirebaseConfig.getFirebaseAuth();
    private DatabaseReference mRef = FirebaseConfig.getFirebaseReference();
    private DatabaseReference mUsuarioRef;
    private ValueEventListener mValueEventListener;

    private Double mDespesaTotal = 0.00;
    private Double mReceitaTotal = 0.00;
    private Double mCalculoSaldo = 0.00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mDespesa = findViewById(R.id.fabDespesa);
        mReceita = findViewById(R.id.fabReceita);
        mSaudacao = findViewById(R.id.tvSaudacao);
        mSaldo = findViewById(R.id.tvSaldo);
        mCalendarView = findViewById(R.id.calendarView);
        mSair = findViewById(R.id.ivSair);

        configurarCalendarView();


        mDespesa.setOnClickListener(this);
        mReceita.setOnClickListener(this);
        mSair.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUsuarioRef.removeEventListener(mValueEventListener);
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

            case R.id.ivSair:
                mAuth.signOut();

                Intent intentMain = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intentMain);
                finish();
                break;
        }
    }

    public void configurarCalendarView() {
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
                "Agosto", "Setmbro", "Outubro", "Novembro", "Dezembro"};
        mCalendarView.setTitleMonths(meses);

        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });
    }

    public void recuperarResumo() {
        String id = Base64Helper.codificarBase64(mAuth.getCurrentUser().getEmail());
        mUsuarioRef = mRef.child("usuarios").child(id);

        mValueEventListener = mUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                mDespesaTotal = usuario.getDespesaTotal();
                mReceitaTotal = usuario.getReceitaTotal();

                mCalculoSaldo = mReceitaTotal - mDespesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                String calculoSaldoFormatado = decimalFormat.format(mCalculoSaldo);

                mSaudacao.setText("Olá, "+usuario.getNome());
                mSaldo.setText("R$ "+calculoSaldoFormatado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
