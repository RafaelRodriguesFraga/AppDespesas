package com.rafaelfraga.appdespesas.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.adapters.MovimentacaoAdapter;
import com.rafaelfraga.appdespesas.config.FirebaseConfig;
import com.rafaelfraga.appdespesas.helpers.Base64Helper;
import com.rafaelfraga.appdespesas.models.Movimentacao;
import com.rafaelfraga.appdespesas.models.Usuario;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton mDespesa;
    private FloatingActionButton mReceita;
    private FloatingActionMenu mMenu;
    private MaterialCalendarView mCalendarView;
    private TextView mSaudacao;
    private TextView mSaldo;
    private ImageView mSair;
    private RecyclerView mRecyclerMovimentacao;
    private MovimentacaoAdapter mAdapter;
    private List<Movimentacao> mMovimentacoes = new ArrayList<>();

    private FirebaseAuth mAuth = FirebaseConfig.getFirebaseAuth();
    private DatabaseReference mRef = FirebaseConfig.getFirebaseReference();
    private DatabaseReference mMovimentacaoRef;
    private DatabaseReference mUsuarioRef;
    private ValueEventListener mValueEventListenerUsuario;
    private ValueEventListener mValueEventListenerMovimentacao;


    private Double mDespesaTotal = 0.00;
    private Double mReceitaTotal = 0.00;
    private Double mCalculoSaldo = 0.00;
    private String mesAnoSelecionado;
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mDespesa = findViewById(R.id.fabDespesa);
        mReceita = findViewById(R.id.fabReceita);
        mMenu = findViewById(R.id.famMenu);
        mSaudacao = findViewById(R.id.tvSaudacao);
        mSaldo = findViewById(R.id.tvSaldo);
        mCalendarView = findViewById(R.id.calendarView);
        mSair = findViewById(R.id.ivSair);

        configurarCalendarView();
        prepararRecyclerView();
        deslizar();

        mDespesa.setOnClickListener(this);
        mReceita.setOnClickListener(this);
        mSair.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUsuarioRef.removeEventListener(mValueEventListenerUsuario);
        mUsuarioRef.removeEventListener(mValueEventListenerMovimentacao);
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

        CalendarDay dataAtual = mCalendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", dataAtual.getMonth());
        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + dataAtual.getYear());

        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%02d", date.getMonth());
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());

                mMovimentacaoRef.removeEventListener(mValueEventListenerMovimentacao);
                recuperarMovimentacoes();
            }
        });
    }

    public void recuperarResumo() {
        String id = Base64Helper.codificarBase64(mAuth.getCurrentUser().getEmail());
        mUsuarioRef = mRef.child("usuarios").child(id);

        mValueEventListenerUsuario = mUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                mDespesaTotal = usuario.getDespesaTotal();
                mReceitaTotal = usuario.getReceitaTotal();

                mCalculoSaldo = mReceitaTotal - mDespesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                String calculoSaldoFormatado = decimalFormat.format(mCalculoSaldo);

                mSaudacao.setText("Olá, " + usuario.getNome());
                mSaldo.setText("R$ " + calculoSaldoFormatado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void prepararRecyclerView() {
        mAdapter = new MovimentacaoAdapter(mMovimentacoes, this);

        mRecyclerMovimentacao = findViewById(R.id.rvMovimentacao);
        mRecyclerMovimentacao.setHasFixedSize(true);
        mRecyclerMovimentacao.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerMovimentacao.setAdapter(mAdapter);

        mRecyclerMovimentacao.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy <= 0) {
                    mMenu.showMenu(true);
                } else {
                    mMenu.hideMenu(true);
                }
            }
        });
    }

    public void recuperarMovimentacoes() {
        String id = Base64Helper.codificarBase64(mAuth.getCurrentUser().getEmail());

        mMovimentacaoRef = mRef.child("movimentacoes")
                .child(id)
                .child(mesAnoSelecionado);

        mValueEventListenerMovimentacao = mMovimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMovimentacoes.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    movimentacao = dados.getValue(Movimentacao.class);
                    movimentacao.setChave(dados.getKey());
                    mMovimentacoes.add(movimentacao);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deslizar() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                    viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(mRecyclerMovimentacao);
    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Movimentação");
        alertDialog.setMessage("Tem certeza que deseja excluir?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacao = mMovimentacoes.get(position);

                String id = Base64Helper.codificarBase64(mAuth.getCurrentUser().getEmail());

                mMovimentacaoRef = mRef.child("movimentacoes")
                        .child(id)
                        .child(mesAnoSelecionado);

                mMovimentacaoRef.child(movimentacao.getChave()).removeValue();
                mAdapter.notifyItemRemoved(position);
                atualizarSaldo();
                Toast.makeText(DashboardActivity.this, "Excluído com sucesso", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DashboardActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    public void atualizarSaldo() {
        String id = Base64Helper.codificarBase64(mAuth.getCurrentUser().getEmail());
        mUsuarioRef = mRef.child("usuarios").child(id);

        if(movimentacao.getTipo().equals("R")) {
            mReceitaTotal = mReceitaTotal - movimentacao.getValor();
            mUsuarioRef.child("receitaTotal").setValue(mReceitaTotal);
        }

        if(movimentacao.getTipo().equals("D")) {
            mDespesaTotal = mDespesaTotal - movimentacao.getValor();
            mUsuarioRef.child("despesaTotal").setValue(mDespesaTotal);
        }
    }
}
