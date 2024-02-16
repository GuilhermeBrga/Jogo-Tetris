// Declara��o do pacote ao qual a classe pertence
package br.com.mvbos.lgj;

// Importa��o das classes necess�rias
import java.awt.Graphics2D;
import br.com.mvbos.lgj.base.CenarioPadrao;
import br.com.mvbos.lgj.base.Menu;
import br.com.mvbos.lgj.base.Texto;
import br.com.mvbos.lgj.base.Util;

// Defini��o da classe InicioCenario que herda de CenarioPadrao
public class InicioCenario extends CenarioPadrao {

	// Declara��o de vari�veis de inst�ncia da classe
	private Texto texto = new Texto(20);
	private Menu menuJogo;

	// Construtor da classe InicioCenario
	public InicioCenario(int largura, int altura) {
		super(largura, altura);
	}

	// Sobrescrita do m�todo carregar da classe pai
	@Override
	public void carregar() {
		// Cria��o e configura��o do menu de jogo
		menuJogo = new Menu("Aperte enter para iniciar! Escolha o nivel ");
		menuJogo.addOpcoes("1");
		Util.centraliza(menuJogo, largura, altura);
		menuJogo.setAtivo(true);
		menuJogo.setSelecionado(true);
	}

	// Sobrescrita do m�todo descarregar da classe pai
	@Override
	public void descarregar() {
		// Atualiza o n�vel do jogo com base na op��o selecionada no menu
		Jogo.nivel = menuJogo.getOpcaoId() + 1;
	}

	// Sobrescrita do m�todo atualizar da classe pai
	@Override
	public void atualizar() {
		// Verifica se as teclas de seta esquerda ou direita foram pressionadas para trocar a op��o do menu
		if (Jogo.controleTecla[Jogo.Tecla.ESQUERDA.ordinal()] || Jogo.controleTecla[Jogo.Tecla.DIREITA.ordinal()]) {
			menuJogo.setTrocaOpcao(Jogo.controleTecla[Jogo.Tecla.ESQUERDA.ordinal()]);
		}

		// Libera todas as teclas ap�s a verifica��o
		Jogo.liberaTeclas();
	}

	// Sobrescrita do m�todo desenhar da classe pai
	@Override
	public void desenhar(Graphics2D g) {
		// Desenha o menu de jogo
		menuJogo.desenha(g);
	}
}
