package com.rafaelfraga.appdespesas.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafaelfraga.appdespesas.R;
import com.rafaelfraga.appdespesas.models.Movimentacao;

import java.util.List;

public class MovimentacaoAdapter extends RecyclerView.Adapter<MovimentacaoAdapter.MovimentacaoViewHolder> {

    private List<Movimentacao> mMovimentacoes;
    private Context mContext;

    public MovimentacaoAdapter(List<Movimentacao> mMovimentacoes, Context mContext) {
        this.mMovimentacoes = mMovimentacoes;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MovimentacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimentacao, parent,
               false);
       return new MovimentacaoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimentacaoViewHolder holder, int position) {
        Movimentacao movimentacao = mMovimentacoes.get(position);

        holder.mTitulo.setText(movimentacao.getDescricao());
        holder.mData.setText(movimentacao.getData());
        holder.mValor.setText(String.valueOf(movimentacao.getValor()));
        holder.mValor.setTextColor(mContext.getResources().getColor(R.color.colorAccentReceita));

        if(movimentacao.getTipo().equals("D")) {
            holder.mValor.setTextColor(mContext.getResources().getColor(R.color.colorDanger));
            holder.mValor.setText("-"+movimentacao.getValor());
        }
    }

    @Override
    public int getItemCount() {
        return mMovimentacoes.size();
    }

    public class MovimentacaoViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitulo;
        private TextView mData;
        private TextView mValor;

        public MovimentacaoViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitulo = itemView.findViewById(R.id.tvTitulo);
            mData = itemView.findViewById(R.id.tvData);
            mValor = itemView.findViewById(R.id.tvValor);
        }
    }
}
