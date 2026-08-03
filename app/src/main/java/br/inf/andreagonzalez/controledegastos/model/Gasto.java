package br.inf.andreagonzalez.controledegastos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gasto")
public class Gasto {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String descricao;
    private double valor;
    private String categoria;
    private String formaPagamento;
    private String data;

    // Construtor principal
    public Gasto(String descricao,
                 double valor,
                 String categoria,
                 String formaPagamento,
                 String data) {
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.formaPagamento = formaPagamento;
        this.data = data;
    }

    // Getter e Setter para id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters
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

    public String getData() {
        return data;
    }

    // Setters
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

    public void setData(String data) {
        this.data = data;
    }
}
