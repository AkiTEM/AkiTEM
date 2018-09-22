package br.uninove.akitem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.uninove.akitem.Entidades.Produtos;
import br.uninove.akitem.R;

public class ProdutosAdapter extends ArrayAdapter<Produtos> {

    private ArrayList<Produtos> produto;
    private Context context;

    public ProdutosAdapter(Context c, ArrayList<Produtos> objects) {
        super(c, 0, objects);

        this.context = c;
        this.produto = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (!produto.isEmpty()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_produtos, parent, false);

            TextView textViewLocal = (TextView) view.findViewById(R.id.textViewLocal);
            TextView textViewProduto = (TextView) view.findViewById(R.id.textViewProduto);
            TextView textViewValor = (TextView) view.findViewById(R.id.textViewValor);

            Produtos produtos2 = produto.get(position);
            textViewLocal.setText(produtos2.getEstabaleciomento());
            textViewProduto.setText(produtos2.getProduto());
            textViewValor.setText(Double.valueOf(produtos2.getValor()).toString());
        }

        return view;
    }
}
