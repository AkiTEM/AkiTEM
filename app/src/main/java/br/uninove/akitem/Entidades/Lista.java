package br.uninove.akitem.Entidades;

public class Lista {

    private String id;
    private String email;
    private String estabaleciomento;
    private String marca;
    private String produto;
    private Double valor;

    public Lista() {
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

    public String getEstabaleciomento() {
        return estabaleciomento;
    }

    public void setEstabaleciomento(String estabaleciomento) {
        this.estabaleciomento = estabaleciomento;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
