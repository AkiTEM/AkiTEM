package br.uninove.akitem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.uninove.akitem.DAO.ConfiguracaoFirebase;
import br.uninove.akitem.Entidades.Usuarios;
import br.uninove.akitem.GlobalClass;
import br.uninove.akitem.R;

public class UpdateLoginActivity extends AppCompatActivity implements ValueEventListener {

    private Button btnVoltarTelaInicial5;
    private Usuarios usuarios;
    private AutoCompleteTextView newEmail;
    private EditText password;
    private String email;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_login);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        email = globalVariable.getEmail();

        btnVoltarTelaInicial5 = (Button) findViewById(R.id.btnVoltarTelaInicial5);

        btnVoltarTelaInicial5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarTelaInicial();
            }
        });

        autenticacao = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        password = (EditText) findViewById(R.id.password);
        newEmail = (AutoCompleteTextView) findViewById(R.id.email);

        usuarios = new Usuarios();
        usuarios.setId(autenticacao.getCurrentUser().getUid());
        usuarios.contextDataDB( this );
    }

    public void update( View view ){
        usuarios.setEmail(email);
        usuarios.setSenha(password.getText().toString());

        if (!newEmail.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            reauthenticate();
        } else
            Toast.makeText(
                    UpdateLoginActivity.this,
                    "Preencha todos os campos para atualização",
                    Toast.LENGTH_SHORT
            ).show();
    }

    private void reauthenticate(){
        FirebaseUser firebaseUser = autenticacao.getCurrentUser();

        if( firebaseUser == null ){
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(
                usuarios.getEmail(),
                usuarios.getSenha()
        );

        firebaseUser.reauthenticate( credential )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if( task.isSuccessful() ){
                            updateData();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report( e );
                        Toast.makeText(
                                UpdateLoginActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void updateData(){
        usuarios.setSenha(password.getText().toString());

        FirebaseUser firebaseUser = autenticacao.getCurrentUser();

        if( firebaseUser == null ){
            return;
        }

        firebaseUser
                .updateEmail(newEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if( task.isSuccessful() ){
                            usuarios.setEmail( newEmail.getText().toString() );
                            usuarios.updateDB();

                            Toast.makeText(
                                    UpdateLoginActivity.this,
                                    "Email de login atualizado com sucesso",
                                    Toast.LENGTH_SHORT
                            ).show();

                            autenticacao.signOut();
                            Intent intent = new Intent(UpdateLoginActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report( e );
                        Toast.makeText(
                                UpdateLoginActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
        newEmail.setText(usuarios.getEmail());
        usuarios.setEmail(usuarios.getEmail());
    }

    @Override
    public void onCancelled(DatabaseError firebaseError) {
        FirebaseCrash.report(firebaseError.toException());
    }

    private void voltarTelaInicial() {
        Intent intent = new Intent(UpdateLoginActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
