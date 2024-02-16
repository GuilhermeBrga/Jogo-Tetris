package br.com.mvbos.lgj;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Ranking {
    ArrayList<Jogador> ranking = new ArrayList<>();

    public void addJogadores(Jogador jogadores){
        ranking.add(jogadores);
    }

    public void carregaRanking(){
        Path Ranking = Paths.get("C:\\Users\\guilh\\IdeaProjects\\TetrisGame\\src\\br\\com\\mvbos\\lgj\\RankingOFC.txt");
        try(BufferedReader read = new BufferedReader(new FileReader(Ranking.toFile()))) {
            if(!Files.exists(Ranking)){
                Files.createFile(Ranking);
            }

            String Linha;
            while ((Linha = read.readLine()) !=null){
                String[] NonPon = Linha.split(",");
                Jogador jogadores = new Jogador();
                jogadores.setNome(NonPon[0]);
                jogadores.setPontosAcumulados(Integer.parseInt(NonPon[1]));
                ranking.add(jogadores);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void organizar(){
        Collections.sort(ranking);
    }

    public void Top10(){
        while (ranking.size()>10){
            ranking.remove(ranking.size()-1);
        }
    }

    public  void abrirMelhores() {
        JFrame showRankFrame = new JFrame("Melhores no Ranking");
        showRankFrame.setSize(500,672);

        JTextArea ShowRankText = new JTextArea();

        Font font = new Font("Arial",Font.PLAIN,15);
        ShowRankText.setFont(font);

        for (int i = 0; i < ranking.size(); i++) {
            Jogador jogador = ranking.get(i);
            ShowRankText.append((i + 1) + "° lugar - " + jogador.getNome() + " / " + "Pontos: " + jogador.getPontosAcumulados());
            ShowRankText.append("\n");
            Top10();
        }

        showRankFrame.add(ShowRankText);
        showRankFrame.setVisible(true);
    }

    public void saveInfo() {
        Path top10 = Paths.get("C:\\Users\\guilh\\IdeaProjects\\TetrisGame\\src\\br\\com\\mvbos\\lgj\\RankingOFC.txt");
        try(BufferedWriter escrever = new BufferedWriter(new FileWriter(top10.toFile(),false))) {
            for(Jogador jogadores : ranking){
                escrever.write(jogadores.getNome()+","+jogadores.getPontosAcumulados()+"\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}