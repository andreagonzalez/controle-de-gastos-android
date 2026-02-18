package br.inf.andreagonzalez.controledegastos;

public class Gasto {

    private String descricao;
    private double valor;

    public Gasto(String descricao, double valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }
}
