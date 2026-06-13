package br.inf.andreagonzalez.controledegastos.model;

public class Entrada {

    private String descricao;
    private double valor;
    private String data;

    public Entrada(
            String descricao,
            double valor,
            String data
    ) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public String getData() {
        return data;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    public void setData(String data) {
        this.data = data;
    }
}