package br.uninove.akitem.Activity.Domain;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import br.uninove.akitem.Activity.Domain.Util.LibraryClass;


public class User {
    public static String PROVIDER = "br.uninove.akitem.Activity.Domain.User.PROVIDER";

    private String id;
    private String name;
    private String email;
    private String password;
    private String newPassword;

    public User(){}

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

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void saveProviderSP(Context context, String token ){
        LibraryClass.saveSP( context, PROVIDER, token );
    }
    public String getProviderSP(Context context ){
        return( LibraryClass.getSP( context, PROVIDER) );
    }

    public void saveDB( DatabaseReference.CompletionListener... completionListener ){
        //DatabaseReference firebase = LibraryClass.getFirebase().child("Usuario").child( getId() );
        DatabaseReference firebase = LibraryClass.getFirebase().child("Usuario");

        if( completionListener.length == 0 ){
            firebase.setValue(this);
        }
        else{
            firebase.setValue(this, completionListener[0]);
        }
    }

    public void updateDB( DatabaseReference.CompletionListener... completionListener ){

        //DatabaseReference firebase = LibraryClass.getFirebase().child("Usuarios").child( getId() );
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

        //DatabaseReference firebase = LibraryClass.getFirebase().child("Usuarios").child( getId() );
        DatabaseReference firebase = LibraryClass.getFirebase().child("Usuario");
        firebase.setValue(null, completionListener);
    }

    public void contextDataDB( Context context ){
        //DatabaseReference firebase = LibraryClass.getFirebase().child("Usuarios").child( getId() );
        DatabaseReference firebase = LibraryClass.getFirebase().child("Usuario");

        firebase.addListenerForSingleValueEvent( (ValueEventListener) context );
    }
}
