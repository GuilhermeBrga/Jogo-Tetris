package br.com.mvbos.lgj;

public class Jogador implements Comparable<Jogador> {

    private String nome;
    private int pontosAcumulados;

    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }

    public int getPontosAcumulados() {
        return pontosAcumulados;
    }
    public void setPontosAcumulados(int pontosAcumulados) {
        this.pontosAcumulados = pontosAcumulados;
    }

    @Override
    public int compareTo(Jogador o) {
        return o.getPontosAcumulados()-pontosAcumulados;
    }
}