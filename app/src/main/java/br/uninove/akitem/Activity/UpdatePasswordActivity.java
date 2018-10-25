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
import com.google.firebase.database.ValueEventListener;

import br.uninove.akitem.Entidades.Usuarios;
import br.uninove.akitem.GlobalClass;
import br.uninove.akitem.R;

public class UpdatePasswordActivity extends AppCompatActivity implements ValueEventListener {

    private Button btnVoltarTelaInicial6;
    private Usuarios usuarios;
    private EditText newPassword;
    private EditText password;
    private FirebaseAuth autenticacao;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        email = globalVariable.getEmail();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnVoltarTelaInicial6 = (Button) findViewById(R.id.btnVoltarTelaInicial6);

        btnVoltarTelaInicial6.setOnClickListener(new View.OnClickListener() {
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
        newPassword = (EditText) findViewById(R.id.new_password);
        password = (EditText) findViewById(R.id.password);

        usuarios = new Usuarios();
        usuarios.setId(autenticacao.getCurrentUser().getUid());
        usuarios.contextDataDB( this );
    }

    public void update( View view ){
        usuarios.setNewPassword(newPassword.getText().toString());
        usuarios.setSenha(password.getText().toString());
        usuarios.setEmail(email);

        if (!newPassword.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            reauthenticate();
        } else
            Toast.makeText(
                    UpdatePasswordActivity.this,
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
                                UpdatePasswordActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void updateData(){
        usuarios.setNewPassword(newPassword.getText().toString());
        usuarios.setSenha(password.getText().toString());

        FirebaseUser firebaseUser = autenticacao.getCurrentUser();

        if( firebaseUser == null ){
            return;
        }

        firebaseUser
                .updatePassword(usuarios.getNewPassword())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if( task.isSuccessful() ){
                            newPassword.setText("");
                            password.setText("");

                            Toast.makeText(
                                    UpdatePasswordActivity.this,
                                    "Senha atualizada com sucesso",
                                    Toast.LENGTH_SHORT
                            ).show();

                            autenticacao.signOut();
                            Intent intent = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
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
                                UpdatePasswordActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
        usuarios.setEmail(usuarios.getEmail());
    }

    @Override
    public void onCancelled(DatabaseError firebaseError) {
        FirebaseCrash.report(firebaseError.toException());
    }

    private void voltarTelaInicial() {
        Intent intent = new Intent(UpdatePasswordActivity.this, PrincipalActivity.class);
        startActivity(intent);
        finish();
    }
}
