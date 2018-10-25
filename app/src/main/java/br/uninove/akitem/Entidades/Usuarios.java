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
    public static String PROVIDER = "br.uninove.akitem.Activity.Entidades.Usuarios.PROVIDER";

    private String id;
    private String email;
    private String senha;
    private String newPassword;

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

    private void setEmailInMap( Map<String, Object> map ) {
        if( getEmail() != null ){
            map.put( "email", getEmail() );
        }
    }

    public void setEmailIfNull(String email) {
        if( this.email == null ){
            this.email = email;
        }
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Exclude
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void updateDB( DatabaseReference.CompletionListener... completionListener ){

        DatabaseReference firebase = LibraryClass.getFirebase().child("Usuario");

        Map<String, Object> map = new HashMap<>();
        setEmailInMap(map);

        if( map.isEmpty() ){
            return;
        }

        if( completionListener.length > 0 ){
            firebase.updateChildren(map, completionListener[0]);
        }
        else{
            firebase.updateChildren(map);
        }
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
