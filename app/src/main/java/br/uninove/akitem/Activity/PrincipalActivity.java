package br.uninove.akitem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.uninove.akitem.DAO.ConfiguracaoFirebase;
import br.uninove.akitem.R;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth usuarioFirebase;
    private EditText edtMarca, edtProduto;
    private Button btnVerProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        edtMarca = (EditText) findViewById(R.id.edtMarca);
        edtProduto = (EditText) findViewById(R.id.edtProduto);
        btnVerProduto = (Button) findViewById(R.id.btnVerProdutos);

        btnVerProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verProdutos();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal_activity, menu);
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
        if (id == R.id.action_update_login) {
            updateLogin();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update_password) {
            updatePassword();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove_user) {
            remove_user();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void verProdutos() {
        edtMarca = (EditText) findViewById(R.id.edtMarca);
        edtProduto = (EditText) findViewById(R.id.edtProduto);
        final CheckBox ck_lista =(CheckBox)findViewById(R.id.ckLista);

        Intent intent = new Intent(PrincipalActivity.this, ProdutosActivity.class);
        intent.putExtra("marca", edtMarca.getText().toString());
        intent.putExtra("produto", edtProduto.getText().toString());

        if ((!edtMarca.getText().toString().isEmpty() || !edtProduto.getText().toString().isEmpty()) || (edtMarca.getText().toString().isEmpty() && edtProduto.getText().toString().isEmpty() && ck_lista.isChecked())) {
            if (!edtMarca.getText().toString().isEmpty() || !edtProduto.getText().toString().isEmpty())
                if (ck_lista.isChecked())
                    Toast.makeText(PrincipalActivity.this, "Será considerado o filtro de busca", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        } else
            Toast.makeText(PrincipalActivity.this, "Favor preencher os campos para busca", Toast.LENGTH_SHORT).show();
    }

    private void verLista() {
        edtMarca = (EditText) findViewById(R.id.edtMarca);
        edtProduto = (EditText) findViewById(R.id.edtProduto);

        Intent intent = new Intent(PrincipalActivity.this, ListaActivity.class);
        intent.putExtra("marca", edtMarca.getText().toString());
        intent.putExtra("produto", edtProduto.getText().toString());
        startActivity(intent);
        finish();
    }

    public void updateLogin() {
        Intent intent = new Intent(PrincipalActivity.this, UpdateLoginActivity.class);
        startActivity(intent);
    }

    public void updatePassword() {
        Intent intent = new Intent(PrincipalActivity.this, UpdatePasswordActivity.class);
        startActivity(intent);
    }

    private void remove_user() {
        Intent intent = new Intent(PrincipalActivity.this, RemoveUserActivity.class);
        startActivity(intent);
    }

    private void logout() {
        usuarioFirebase.signOut();
        Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
