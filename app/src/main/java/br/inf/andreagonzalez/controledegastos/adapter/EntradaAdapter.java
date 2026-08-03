package br.inf.andreagonzalez.controledegastos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import br.inf.andreagonzalez.controledegastos.R;
import br.inf.andreagonzalez.controledegastos.model.Entrada;
import br.inf.andreagonzalez.controledegastos.util.DateCustomUtil;

public class EntradaAdapter
        extends RecyclerView.Adapter<EntradaAdapter.EntradaViewHolder> {

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    private ArrayList<Entrada> listaEntradas;
    private OnItemLongClickListener longClickListener;

    public EntradaAdapter(ArrayList<Entrada> listaEntradas) {
        this.listaEntradas = listaEntradas;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public EntradaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entrada, parent, false);

        return new EntradaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull EntradaViewHolder holder,
            int position
    ) {

        Entrada entrada = listaEntradas.get(position);

        holder.textDescricao.setText(
                entrada.getDescricao()
        );

        holder.textValor.setText(
                formatarMoeda(entrada.getValor())
        );

        holder.textData.setText(
                "Data: " + DateCustomUtil.toDisplayFormat(entrada.getData())
        );

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
        return listaEntradas.size();
    }

    private String formatarMoeda(double valor) {

        NumberFormat formatoBrasil =
                NumberFormat.getCurrencyInstance(
                        new Locale("pt", "BR")
                );

        return formatoBrasil.format(valor);
    }

    public static class EntradaViewHolder
            extends RecyclerView.ViewHolder {

        TextView textDescricao;
        TextView textValor;
        TextView textData;

        public EntradaViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            textDescricao =
                    itemView.findViewById(
                            R.id.textDescricaoEntrada
                    );

            textValor =
                    itemView.findViewById(
                            R.id.textValorEntrada
                    );

            textData =
                    itemView.findViewById(
                            R.id.textDataEntrada
                    );
        }
    }
}