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

import java.util.ArrayList;

import br.uninove.akitem.Adapter.ProdutosAdapter;
import br.uninove.akitem.DAO.ConfiguracaoFirebase;
import br.uninove.akitem.Entidades.Lista;
import br.uninove.akitem.Entidades.Produtos;
import br.uninove.akitem.GlobalClass;
import br.uninove.akitem.Helper.Base64Custom;
import br.uninove.akitem.R;

import static android.widget.AdapterView.OnItemClickListener;

public class ProdutosActivity extends AppCompatActivity {

    private FirebaseAuth usuarioFirebase;
    private ListView listView;
    private ArrayAdapter<Produtos> adapter;
    private ArrayList<Produtos> produtos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerProdutos;
    private Button btnVoltarTelaInicial;
    private String marca, produto;
    private Lista lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Intent intent = getIntent();
        marca = replace_to(intent.getStringExtra("marca").toUpperCase());
        produto = replace_to(intent.getStringExtra("produto").toUpperCase());

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String email  = globalVariable.getEmail();

        produtos = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewProdutos);
        adapter = new ProdutosAdapter(this, produtos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (email != null) {
                    lista = new Lista();
                    String idItemProduto = Base64Custom.codificarBase64(email.concat(produtos.get(position).getEstabaleciomento()).concat(produtos.get(position).getMarca()).concat(produtos.get(position).getProduto()));
                    lista.setId(idItemProduto);
                    lista.setEmail(email);
                    lista.setEstabaleciomento(produtos.get(position).getEstabaleciomento());
                    lista.setMarca(produtos.get(position).getMarca());
                    lista.setProduto(produtos.get(position).getProduto());
                    lista.setValor(produtos.get(position).getValor());

                    salvarLista(lista, idItemProduto);
                } else
                    Toast.makeText(ProdutosActivity.this, "Favor logar novamente", Toast.LENGTH_SHORT).show();
            }
        });

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
                int tamMarca = 1;
                int tamMarca2;
                int tamProduto = 1;
                int tamProduto2;
                String valueMarca;
                String valueProduto;

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Produtos produtosNovo = dados.getValue(Produtos.class);

                    first = false;

                    if (!"".equals(marca))
                        tamMarca = marca.length();
                        tamMarca2 = produtosNovo.getMarca().length();
                        if (tamMarca > tamMarca2)
                            tamMarca = tamMarca2;
                            valueMarca = produtosNovo.getMarca().substring(0,tamMarca);
                            valueMarca = replace_to(valueMarca.toUpperCase());

                    if (!"".equals(produto))
                        tamProduto = produto.length();
                        tamProduto2 = produtosNovo.getProduto().length();
                        if (tamProduto > tamProduto2)
                            tamProduto = tamProduto2;
                            valueProduto = produtosNovo.getProduto().substring(0,tamProduto);
                            valueProduto = replace_to(valueProduto.toUpperCase());

                    if (!"".equals(marca) && !"".equals(produto)) {
                        if (valueMarca.equals(marca) && valueProduto.equals(produto))
                            first = true;
                    } else if (!"".equals(marca)) {
                        if (valueMarca.equals(marca))
                            first = true;
                    } else if (!"".equals(produto)) {
                        if (valueProduto.equals(produto))
                            first = true;
                    } else if ("".equals(marca) && "".equals(produto))
                        first = true;

                    if (first)
                        produtos.add(produtosNovo);
                }
                if (produtos.isEmpty()) {
                    Toast.makeText(ProdutosActivity.this, "Não há resultado para busca!", Toast.LENGTH_SHORT).show();
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

    public String replace_to(String item) {
        item = item.replace("Ã", "A");
        item = item.replace("Õ", "O");
        item = item.replace("Ç", "C");
        item = item.replace("Á", "A");
        item = item.replace("Ó", "O");

        return item;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.produtos_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.carrinho) {
            verLista();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void voltarTelaInicial() {
        Intent intent = new Intent(ProdutosActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean salvarLista(Lista lista, String idItemProduto) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("Lista");
            firebase.child(idItemProduto).setValue(lista);
            Toast.makeText(ProdutosActivity.this, "Item inserido com sucesso", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    private void verLista() {
        Intent intent = new Intent(ProdutosActivity.this, ListaActivity.class);
        intent.putExtra("marca", marca);
        intent.putExtra("produto", produto);
        startActivity(intent);
        finish();
    }

    private void logout() {
        usuarioFirebase.signOut();
        Intent intent = new Intent(ProdutosActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
