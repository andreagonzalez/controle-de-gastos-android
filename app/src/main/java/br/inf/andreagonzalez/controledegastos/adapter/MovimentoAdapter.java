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
import br.inf.andreagonzalez.controledegastos.model.Movimento;

public class MovimentoAdapter
        extends RecyclerView.Adapter<MovimentoAdapter.MovimentoViewHolder> {

    private ArrayList<Movimento> listaMovimentos;

    public MovimentoAdapter(ArrayList<Movimento> listaMovimentos) {
        this.listaMovimentos = listaMovimentos;
    }

    @NonNull
    @Override
    public MovimentoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movimento, parent, false);

        return new MovimentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MovimentoViewHolder holder,
            int position
    ) {

        Movimento movimento = listaMovimentos.get(position);

        holder.textDescricao.setText(movimento.getDescricao());
        holder.textTipo.setText(movimento.getTipo());
        holder.textValor.setText(
                formatarMoeda(movimento.getValor())
        );
    }

    @Override
    public int getItemCount() {
        return listaMovimentos.size();
    }

    private String formatarMoeda(double valor) {

        NumberFormat formatoBrasil =
                NumberFormat.getCurrencyInstance(
                        new Locale("pt", "BR")
                );

        return formatoBrasil.format(valor);
    }

    public static class MovimentoViewHolder
            extends RecyclerView.ViewHolder {

        TextView textDescricao;
        TextView textTipo;
        TextView textValor;

        public MovimentoViewHolder(@NonNull View itemView) {

            super(itemView);

            textDescricao =
                    itemView.findViewById(R.id.textDescricaoMovimento);

            textTipo =
                    itemView.findViewById(R.id.textTipoMovimento);

            textValor =
                    itemView.findViewById(R.id.textValorMovimento);
        }
    }
}