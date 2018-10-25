package br.uninove.akitem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import br.uninove.akitem.DAO.ConfiguracaoFirebase;
import br.uninove.akitem.Fragment.FotosTelaInicialFragment;
import br.uninove.akitem.GlobalClass;
import br.uninove.akitem.R;

public class MainActivity extends AppCompatActivity {

    private Button btnAbrirActivityLogin;
    private FirebaseAuth autenticacao;
    private FotosTelaInicialFragment fotosTelaInicialFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.logo);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String email = globalVariable.getEmail();

        verifaUsuarioLogado(email);

        btnAbrirActivityLogin = (Button) findViewById(R.id.btnFazerLogin);

        btnAbrirActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAbrirTelaLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentAbrirTelaLogin);
            }
        });

        carregarFragmentTelaInicial();
    }

    private void verifaUsuarioLogado(String email) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {

            Intent intent;
            if (email != null) {
                intent = new Intent(MainActivity.this, PrincipalActivity.class);
                startActivity(intent);
                finish();
            } else
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
        }
    }

    public void carregarFragmentTelaInicial(){
        fotosTelaInicialFragment = new FotosTelaInicialFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.framLayoutTelaInicial, fotosTelaInicialFragment, "FragmentTelaInicial");

        fragmentTransaction.commit();
    }
}
