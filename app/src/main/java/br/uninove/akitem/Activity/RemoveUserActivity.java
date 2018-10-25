package br.uninove.akitem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.uninove.akitem.Entidades.Usuarios;
import br.uninove.akitem.R;

import br.uninove.akitem.Activity.Domain.User;

public class RemoveUserActivity extends AppCompatActivity
        implements ValueEventListener, DatabaseReference.CompletionListener {

    private Button btnVoltarTelaInicial4;
    private Usuarios usuarios;
    private EditText password;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnVoltarTelaInicial4 = (Button) findViewById(R.id.btnVoltarTelaInicial4);

        btnVoltarTelaInicial4.setOnClickListener(new View.OnClickListener() {
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

        usuarios = new Usuarios();
        usuarios.setId( autenticacao.getCurrentUser().getUid() );
        usuarios.contextDataDB( this );
    }

    public void update( View view ){
        usuarios.setSenha(password.getText().toString());

        reauthenticate();
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
                            deleteUser();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report( e );
                Toast.makeText(
                        RemoveUserActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void deleteUser(){
        FirebaseUser firebaseUser = autenticacao.getCurrentUser();

        if( firebaseUser == null ){
            return;
        }

        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if( !task.isSuccessful() ){
                    return;
                }

                usuarios.removeDB( RemoveUserActivity.this );
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report( e );
                        Toast.makeText(
                                RemoveUserActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Usuarios u = dataSnapshot.getValue( Usuarios.class );
        usuarios.setEmail( u.getEmail() );
    }

    @Override
    public void onCancelled(DatabaseError firebaseError) {
        FirebaseCrash.report( firebaseError.toException() );
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if( databaseError != null ){
            FirebaseCrash.report( databaseError.toException() );
        }

        Toast.makeText(
                RemoveUserActivity.this,
                "Conta removida com sucesso",
                Toast.LENGTH_SHORT
        ).show();
        finish();
    }

    private void voltarTelaInicial() {
        Intent intent = new Intent(RemoveUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}