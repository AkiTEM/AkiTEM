package br.uninove.akitem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;

import br.uninove.akitem.DAO.ConfiguracaoFirebase;
import br.uninove.akitem.R;

public class ResetActivity extends AppCompatActivity {

    private Button btnVoltarTelaInicial;
    private AutoCompleteTextView email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnVoltarTelaInicial = (Button) findViewById(R.id.btnVoltarTelaInicial);

        btnVoltarTelaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarTelaInicial();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        email = (AutoCompleteTextView) findViewById(R.id.email);
    }

    public void reset( View view ){
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(ResetActivity.this, "Preencha o campo de e-mail", Toast.LENGTH_SHORT).show();
        } else
            firebaseAuth
                .sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if( task.isSuccessful() ){
                            email.setText("");
                            Toast.makeText(
                                    ResetActivity.this,
                                    "Recuperação de acesso iniciada. Email enviado.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        else{
                            Toast.makeText(
                                    ResetActivity.this,
                                    "Falhou! Tente novamente",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report( e );
                    }
                });
    }

    private void voltarTelaInicial() {
        Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}