package br.uninove.akitem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import br.uninove.akitem.Entidades.Produtos;
import br.uninove.akitem.Model.Estabelecimento;
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

            TextView textViewMarca = (TextView) view.findViewById(R.id.textViewMarca);
            TextView textViewProduto = (TextView) view.findViewById(R.id.textViewProduto);
            TextView textViewValor = (TextView) view.findViewById(R.id.textViewValor);
            ImageView imagem = (ImageView) view.findViewById(R.id.imagem_estabelecimento);

            Produtos produtos = produto.get(position);
            if (!"".equals(produtos.getMarca()))
                textViewMarca.setText(produtos.getMarca());
            else
                textViewMarca.setText("");
            textViewProduto.setText(produtos.getProduto());

            BigDecimal roundVal = new BigDecimal(produtos.getValor());
            roundVal = roundVal.setScale(2,BigDecimal.ROUND_HALF_UP);
            textViewValor.setText("R$ " + roundVal);

            String estabelecimento = produtos.getEstabaleciomento().toUpperCase();

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