package com.rafaelfraga.appdespesas.helpers;

import java.text.SimpleDateFormat;

public class DataHelper {
    public static String recuperarDataAtual() {
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataAtual = simpleDateFormat.format(data);

        return dataAtual;
    }

    public static String converterDataParaMesAno(String data) {
        String dataSeparada[] = data.split("/");

        String dia = dataSeparada[0]; // dia
        String mes = dataSeparada[1]; // mes
        String ano = dataSeparada[2]; // ano

        String mesAno = mes + ano;

        return mesAno;

    }
}
