package br.uninove.akitem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;

import br.uninove.akitem.Adapter.ListaAdapter;
import br.uninove.akitem.Adapter.ProdutosAdapter;
import br.uninove.akitem.DAO.ConfiguracaoFirebase;
import br.uninove.akitem.Entidades.Lista;
import br.uninove.akitem.Entidades.Produtos;
import br.uninove.akitem.GlobalClass;
import br.uninove.akitem.R;

import static android.widget.AdapterView.OnItemClickListener;

public class ListaActivity extends AppCompatActivity {

    private FirebaseAuth usuarioFirebase;
    private ListView listView;
    private ArrayAdapter<Lista> adapter;
    private ArrayList<Lista> lista;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerLista;
    private Button btnVoltarTelaInicial;
    private String marca, produto;
    private Lista itemTot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Intent intent = getIntent();
        marca = intent.getStringExtra("marca").toUpperCase();
        produto = intent.getStringExtra("produto").toUpperCase();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String email = globalVariable.getEmail();

        lista = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewLista);
        adapter = new ListaAdapter(this, lista);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deletaItemLista(lista.get(position).getId());
            }
        });

        firebase = ConfiguracaoFirebase.getFirebase().child("Lista");

        valueEventListenerLista = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();
                double totItens = 0;

                if (email == null) {
                    Toast.makeText(ListaActivity.this, "Favor logar novamente", Toast.LENGTH_LONG).show();
                    usuarioFirebase.signOut();
                    Intent intent = new Intent(ListaActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Lista listanova = dados.getValue(Lista.class);

                    if (email.equals(listanova.getEmail())) {
                        totItens += listanova.getValor();
                        lista.add(listanova);
                    }
                }

                itemTot = new Lista();
                itemTot.setEmail(email);
                itemTot.setEstabaleciomento("");
                itemTot.setMarca("Total");
                itemTot.setProduto("");
                itemTot.setValor(totItens);
                lista.add(itemTot);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        btnVoltarTelaInicial = (Button) findViewById(R.id.btnVoltarTelaInicial3);
        btnVoltarTelaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarTelaInicial();
            }
        });

    }

    private boolean deletaItemLista(String idItemProduto) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("Lista").child(idItemProduto);
            firebase.removeValue();
            Toast.makeText(ListaActivity.this, "Item excluido com sucesso", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void voltarTelaInicial() {
        Intent intent = new Intent(ListaActivity.this, ProdutosActivity.class);
        intent.putExtra("marca", marca);
        intent.putExtra("produto", produto);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerLista);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerLista);
    }

}