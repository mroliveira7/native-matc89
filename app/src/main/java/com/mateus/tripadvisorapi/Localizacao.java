package com.mateus.tripadvisorapi;

/**
 * Created by Lucas on 13/12/2017.
 */

public class Localizacao {
    private String pais;
    private String estado;
    private String cidade;
    private String estabelecimento;
    private String avaliacao;  // TEM QUE SER INT, TA STRING SO PRA TESTE

    public Localizacao(String pais, String estado, String cidade, String estabelecimento, String avaliacao) {
        this.pais =  pais;
        this.estado = estado;
        this.cidade = cidade;;
        this.estabelecimento = estabelecimento;
        this.avaliacao = avaliacao;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }
}
