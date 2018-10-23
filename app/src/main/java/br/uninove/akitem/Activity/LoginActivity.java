package br.uninove.akitem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.uninove.akitem.DAO.ConfiguracaoFirebase;
import br.uninove.akitem.Entidades.Usuarios;
import br.uninove.akitem.GlobalClass;
import br.uninove.akitem.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private ValueEventListener valueEventListener;
    private DatabaseReference firebase;
    private TextView tvAbreCadastro;
    private Button btnLogar;
    private FirebaseAuth autenticacao;
    private Usuarios usuarios;
    private String idenficadorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        tvAbreCadastro = (TextView) findViewById(R.id.tvAbreCadastro);
        btnLogar = (Button) findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {

                    if (edtEmail.getText().toString().equals("admin") && edtSenha.getText().toString().equals("admin")) {
                        cadastrarProdutos();
                    } else {
                        usuarios = new Usuarios();
                        usuarios.setEmail(edtEmail.getText().toString());
                        usuarios.setSenha(edtSenha.getText().toString());

                        globalVariable.setEmail(edtEmail.getText().toString());

                        validarLogin();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Preencha os campos de e-mail e senha!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvAbreCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreCadastroUsuario();
            }
        });
    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuarios.getEmail(), usuarios.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset) {
            reset();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove_user) {
            //remove_user();
            Toast.makeText(LoginActivity.this, "Em implementação...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirTelaPrincipal() {
        Intent intentAbrirTelaPrincipal = new Intent(LoginActivity.this, PrincipalActivity.class);
        startActivity(intentAbrirTelaPrincipal);
    }

    public void abreCadastroUsuario() {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    private void cadastrarProdutos() {
        Intent intent = new Intent(LoginActivity.this, CadastroProdutos.class);
        startActivity(intent);
        finish();
    }

    private void reset() {
        Intent intent = new Intent(LoginActivity.this, ResetActivity.class);
        startActivity(intent);
    }

    private void remove_user() {
        Intent intent = new Intent(LoginActivity.this, RemoveUserActivity.class);
        startActivity(intent);
    }
}