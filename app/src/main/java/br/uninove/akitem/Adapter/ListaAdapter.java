package br.uninove.akitem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.uninove.akitem.Entidades.Lista;
import br.uninove.akitem.Model.Estabelecimento;
import br.uninove.akitem.R;

public class ListaAdapter extends ArrayAdapter<Lista> {

    private ArrayList<Lista> listas;
    private Context context;

    public ListaAdapter(Context c, ArrayList<Lista> objects) {
        super(c, 0, objects);

        this.context = c;
        this.listas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (!listas.isEmpty()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_itens, parent, false);

            TextView textViewMarca = (TextView) view.findViewById(R.id.textViewMarca);
            TextView textViewProduto = (TextView) view.findViewById(R.id.textViewProduto);
            TextView textViewValor = (TextView) view.findViewById(R.id.textViewValor);
            ImageView imagem = (ImageView) view.findViewById(R.id.imagem_estabelecimento);

            Lista lista = listas.get(position);
            textViewMarca.setText(lista.getMarca());
            if (!"".equals(lista.getProduto()))
                textViewProduto.setText(lista.getProduto());
            else
                textViewProduto.setText("");
            textViewValor.setText("R$ " + Double.valueOf(lista.getValor()).toString().replace(".", ",").concat("0"));

            String estabelecimento = "";

            if (!"".equals(lista.getEstabaleciomento()))
                estabelecimento = lista.getEstabaleciomento().toUpperCase();

            if (!estabelecimento.isEmpty()) {

                if ("ASSAI".equals(estabelecimento)) {
                    imagem.setImageResource(R.drawable.assai);
                } else if ("ATACADAO".equals(estabelecimento)) {
                    imagem.setImageResource(R.drawable.atacadao);
                } else if ("CARREFOUR".equals(estabelecimento)) {
                    imagem.setImageResource(R.drawable.carrefour);
                } else if ("EXTRA".equals(estabelecimento)) {
                    imagem.setImageResource(R.drawable.extra);
                } else if ("ROLDAO".equals(estabelecimento)) {
                    imagem.setImageResource(R.drawable.roldao);
                }
            }
        }

        return view;
    }
}