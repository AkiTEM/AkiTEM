package br.uninove.akitem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.uninove.akitem.Adapter.ProdutosAdapter;
import br.uninove.akitem.DAO.ConfiguracaoFirebase;
import br.uninove.akitem.Entidades.Produtos;
import br.uninove.akitem.R;

public class ProdutosActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Produtos> adapter;
    private ArrayList<Produtos> produtos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerProdutos;
    private Button btnVoltarTelaInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        Intent intent = getIntent();
        final String marca = intent.getStringExtra("marca").toUpperCase();
        final String produto = intent.getStringExtra("produto").toUpperCase();

        produtos = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewProdutos);
        adapter = new ProdutosAdapter(this, produtos);
        listView.setAdapter(adapter);

        firebase = ConfiguracaoFirebase.getFirebase().child("Produtos");
        //Query query = FirebaseDatabase.getInstance().getReference("Produtos")
        //        .orderByChild("marca")
        //        .equalTo("HIKARI");
        //query.addListenerForSingleValueEvent(valueEventListenerProdutos);

        //ValueEventListener valueEventListenerProdutos = new ValueEventListener() {
        valueEventListenerProdutos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();
                boolean first;

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Produtos produtosNovo = dados.getValue(Produtos.class);

                    first = false;

                    if (!"".equals(marca) && !"".equals(produto)) {
                        if (produtosNovo.getMarca().toUpperCase().equals(marca) && produtosNovo.getProduto().toUpperCase().equals(produto))
                            first = true;
                    } else if (!"".equals(marca)) {
                        if (produtosNovo.getMarca().toUpperCase().equals(marca))
                            first = true;
                    } else if (!"".equals(produto)) {
                        if (produtosNovo.getProduto().toUpperCase().equals(produto))
                            first = true;
                    }  else if ("".equals(marca) && "".equals(produto))
                        first = true;

                    if (first)
                        produtos.add(produtosNovo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        btnVoltarTelaInicial = (Button) findViewById(R.id.btnVoltarTelaInicial2);
        btnVoltarTelaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarTelaInicial();
            }
        });
    }

    private void voltarTelaInicial() {
        Intent intent = new Intent(ProdutosActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerProdutos);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerProdutos);
    }
}
