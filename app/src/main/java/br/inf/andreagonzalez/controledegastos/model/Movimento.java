package br.inf.andreagonzalez.controledegastos.model;

public class Movimento {

    private String descricao;
    private double valor;
    private String tipo;

    public Movimento(
            String descricao,
            double valor,
            String tipo
    ) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }
}