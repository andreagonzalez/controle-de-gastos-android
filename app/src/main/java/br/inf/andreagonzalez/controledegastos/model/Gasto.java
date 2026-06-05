package br.inf.andreagonzalez.controledegastos.model;

public class Gasto {

    private String descricao;
    private double valor;
    private String categoria;
    private String formaPagamento;


    public Gasto(String descricao,
                 double valor,
                 String categoria,
                 String formaPagamento
    ) {
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.formaPagamento = formaPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public String getCategoria() {
        return categoria;
    }
    public String getFormaPagamento() {
        return formaPagamento;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}