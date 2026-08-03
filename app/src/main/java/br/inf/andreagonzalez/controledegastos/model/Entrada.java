package br.inf.andreagonzalez.controledegastos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "entrada")
public class Entrada {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String descricao;
    private double valor;
    private String data;

    // Construtor
    public Entrada(String descricao, double valor, String data) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    // Getter e Setter para id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter e Setter para descricao
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Getter e Setter para valor
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    // Getter e Setter para data
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
