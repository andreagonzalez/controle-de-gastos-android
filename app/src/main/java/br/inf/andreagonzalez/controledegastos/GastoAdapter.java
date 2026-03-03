package br.inf.andreagonzalez.controledegastos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    private ArrayList<Gasto> listaGastos;
    private OnItemLongClickListener longClickListener;

    public GastoAdapter(ArrayList<Gasto> listaGastos) {
        this.listaGastos = listaGastos;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
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
        holder.textValor.setText(formatarMoeda(gasto.getValor()));

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                int posicaoAtual = holder.getAdapterPosition();
                if (posicaoAtual != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClick(posicaoAtual);
                }
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaGastos.size();
    }

    private String formatarMoeda(double valor) {
        NumberFormat formatoBrasil =
                NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatoBrasil.format(valor);
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