package br.com.mvbos.lgj.base;

import java.awt.Color;
import java.awt.Graphics2D;

public class Menu extends Texto {

	private short idx;
	private String rotulo;
	private String[] opcoes;
	private boolean selecionado;

	public Menu(String rotulo) {
		super();

		this.rotulo = rotulo;
		setLargura(350);
		setAltura(-500);
		setCor(Color.WHITE);
	}

	//niveis infinitos
	public void addOpcoes(String... opcao) {
		opcoes = opcao;
	}

	@Override
	public void desenha(Graphics2D g) {
		if (opcoes == null)
			return;

		g.setColor(getCor());
		super.desenha(g, String.format("%s: <%s>", getRotulo(), opcoes[idx]), getPx(), getPy() + getAltura());



	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;

	}

	public int getOpcaoId() {
		return idx;
	}

	public String getOpcaoTexto() {
		return opcoes[idx];
	}

	public void setTrocaOpcao(boolean esquerda) {
		if (!isSelecionado() || !isAtivo())
			return;

		idx += esquerda ? -1 : 1;

		if(!esquerda){
			String[] novaOpcoes = new String[opcoes.length + 1];
			for(int i=0; i < opcoes.length; i++){
				novaOpcoes[i] = opcoes[i];
			}
			int proximaOpcao = Integer.parseInt(opcoes[opcoes.length-1])+1;
			novaOpcoes[opcoes.length] = String.valueOf(proximaOpcao);
			opcoes = novaOpcoes;
		}


		if (idx < 0)
			idx = (short) (opcoes.length - 1);
		else if (idx == opcoes.length)
			idx = 0;

	}

}
