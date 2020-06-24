package com.example.agendaadmin.model;

public class Empresa {

    private String imagemUrl;
    private String Informacao;
    private String numeroContato;

    public Empresa(){

    }

    public Empresa(String informacao, String numeroContato) {
        this.imagemUrl = imagemUrl;
        this.Informacao = informacao;
        this.numeroContato = numeroContato;

    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getInformacao() {
        return Informacao;
    }

    public void setInformacao(String informacao) {
        Informacao = informacao;
    }

    public String getNumeroContato() {
        return numeroContato;
    }

    public void setNumeroContato(String numeroContato) {
        this.numeroContato = numeroContato;
    }


}
