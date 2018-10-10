package br.uninove.akitem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.uninove.akitem.DAO.ConfiguracaoFirebase;
import br.uninove.akitem.Entidades.Produtos;
import br.uninove.akitem.R;

public class CadastroProdutos extends AppCompatActivity {

    private Button btnGravar, btnVoltarTelaInicial;
    private EditText edtEstabelecimento, edtMarca, edtProduto, edtValor;
    private Produtos produtos;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produtos);

        edtEstabelecimento = (EditText) findViewById(R.id.edtEstabelecimento);
        edtMarca = (EditText) findViewById(R.id.edtMarca);
        edtProduto = (EditText) findViewById(R.id.edtProduto);
        edtValor = (EditText) findViewById(R.id.edtValor);
        btnGravar = (Button) findViewById(R.id.btnGravar);
        btnVoltarTelaInicial = (Button) findViewById(R.id.btnVoltarTelaInicial);

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                produtos = new Produtos();
                produtos.setEstabaleciomento(edtEstabelecimento.getText().toString());
                produtos.setMarca(edtMarca.getText().toString());
                produtos.setProduto(edtProduto.getText().toString());
                if (!edtValor.getText().toString().isEmpty())
                    produtos.setValor(Double.valueOf(edtValor.getText().toString()));

                if (!edtEstabelecimento.getText().toString().isEmpty() && !edtMarca.getText().toString().isEmpty() && !edtProduto.getText().toString().isEmpty() && !edtValor.getText().toString().isEmpty()) {
                    salvarProduto(produtos);
                } else
                    Toast.makeText(CadastroProdutos.this, "Favor preencher todos os campos!", Toast.LENGTH_LONG).show();
            }
        });

        btnVoltarTelaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarTelaInicial();
            }
        });
    }

    private boolean salvarProduto(Produtos produtos) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("Produtos");
            firebase.child("").push().setValue(produtos);
            Toast.makeText(CadastroProdutos.this, "Produto inserido com sucesso", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void voltarTelaInicial() {
        Intent intent = new Intent(CadastroProdutos.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
