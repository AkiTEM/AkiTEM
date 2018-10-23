package br.uninove.akitem.Entidades;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import br.uninove.akitem.Activity.Domain.Util.LibraryClass;
import br.uninove.akitem.DAO.ConfiguracaoFirebase;

public class Usuarios {

    private String id;
    private String email;
    private String senha;

    public Usuarios() {
    }

    public void salvar() {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("Usuario").child(String.valueOf(getId())).setValue(this);
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());

        return hashMapUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void removeDB( DatabaseReference.CompletionListener completionListener ){

        DatabaseReference firebase = LibraryClass.getFirebase().child("Usuario");
        firebase.setValue(null, completionListener);
    }

    public void contextDataDB( Context context ){

        DatabaseReference firebase = LibraryClass.getFirebase().child("Usuario");
        firebase.addListenerForSingleValueEvent( (ValueEventListener) context );
    }
}
