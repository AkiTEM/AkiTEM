package br.uninove.akitem;

import android.app.Application;

public class GlobalClass extends Application{

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
