package br.inf.andreagonzalez.controledegastos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    private ArrayList<Gasto> listaGastos;

    public GastoAdapter(ArrayList<Gasto> listaGastos) {
        this.listaGastos = listaGastos;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = listaGastos.get(position);
        holder.textDescricao.setText(gasto.getDescricao());
        holder.textValor.setText("R$ " + gasto.getValor());
    }

    @Override
    public int getItemCount() {
        return listaGastos.size();
    }

    public static class GastoViewHolder extends RecyclerView.ViewHolder {

        TextView textDescricao;
        TextView textValor;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textValor = itemView.findViewById(R.id.textValor);
        }
    }
}
