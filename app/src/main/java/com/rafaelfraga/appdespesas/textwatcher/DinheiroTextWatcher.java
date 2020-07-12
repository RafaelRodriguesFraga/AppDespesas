package com.rafaelfraga.appdespesas.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public class DinheiroTextWatcher implements TextWatcher {
   private EditText mValor;
    private String ultimaQuantidade = "";
    private int ultimaPosicaoCursor = -1;

    public DinheiroTextWatcher(EditText editText) {
        this.mValor = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        String valor = s.toString();
        if(!valor.equals("")) {
            String stringLimpa = limparMoedaParaNumero(valor);
            String quantidadeFormatada = transformarEmMoeda(stringLimpa);
            ultimaQuantidade = quantidadeFormatada;
            ultimaPosicaoCursor = mValor.getSelectionStart();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!s.toString().equals(ultimaQuantidade)) {
                String stringLimpa = limparMoedaParaNumero(s.toString());

                try {
                    String quantidadeFormatada = transformarEmMoeda(stringLimpa);
                    mValor.removeTextChangedListener(this);
                    mValor.setText(quantidadeFormatada);
                    mValor.setSelection(quantidadeFormatada.length());
                    mValor.addTextChangedListener(this);

                    if(ultimaPosicaoCursor != ultimaQuantidade.length() && ultimaPosicaoCursor != -1) {
                        int comprimentoDelta = quantidadeFormatada.length() - ultimaQuantidade.length();
                        int novoDeslocamentoCursor = Math.max(0, Math.min(quantidadeFormatada.length(), ultimaPosicaoCursor + comprimentoDelta));
                        mValor.setSelection(novoDeslocamentoCursor);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private String limparMoedaParaNumero(String valorMoeda) {
        String resultado = null;

        if(valorMoeda == null) {
            resultado = "";
        }else {
            resultado = valorMoeda.replaceAll("[(a-z)|(A-Z)|($,.)]", "");
        }

        return resultado;

    }

    private String transformarEmMoeda(String valor) {
        double parsed = Double.parseDouble(valor);
        String formatado = NumberFormat
                .getCurrencyInstance(new Locale("pt", "BR"))
                .format((parsed / 100));
        formatado = formatado.replaceAll("[^(0-9) (.,)]", "");

        return formatado;
    }

}
