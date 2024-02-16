// Declaração do pacote ao qual a classe pertence
package br.com.mvbos.lgj;

// Importação das classes necessárias
import java.awt.Graphics2D;
import br.com.mvbos.lgj.base.CenarioPadrao;
import br.com.mvbos.lgj.base.Menu;
import br.com.mvbos.lgj.base.Texto;
import br.com.mvbos.lgj.base.Util;

// Definição da classe InicioCenario que herda de CenarioPadrao
public class InicioCenario extends CenarioPadrao {

	// Declaração de variáveis de instância da classe
	private Texto texto = new Texto(20);
	private Menu menuJogo;

	// Construtor da classe InicioCenario
	public InicioCenario(int largura, int altura) {
		super(largura, altura);
	}

	// Sobrescrita do método carregar da classe pai
	@Override
	public void carregar() {
		// Criação e configuração do menu de jogo
		menuJogo = new Menu("Aperte enter para iniciar! Escolha o nivel ");
		menuJogo.addOpcoes("1");
		Util.centraliza(menuJogo, largura, altura);
		menuJogo.setAtivo(true);
		menuJogo.setSelecionado(true);
	}

	// Sobrescrita do método descarregar da classe pai
	@Override
	public void descarregar() {
		// Atualiza o nível do jogo com base na opção selecionada no menu
		Jogo.nivel = menuJogo.getOpcaoId() + 1;
	}

	// Sobrescrita do método atualizar da classe pai
	@Override
	public void atualizar() {
		// Verifica se as teclas de seta esquerda ou direita foram pressionadas para trocar a opção do menu
		if (Jogo.controleTecla[Jogo.Tecla.ESQUERDA.ordinal()] || Jogo.controleTecla[Jogo.Tecla.DIREITA.ordinal()]) {
			menuJogo.setTrocaOpcao(Jogo.controleTecla[Jogo.Tecla.ESQUERDA.ordinal()]);
		}

		// Libera todas as teclas após a verificação
		Jogo.liberaTeclas();
	}

	// Sobrescrita do método desenhar da classe pai
	@Override
	public void desenhar(Graphics2D g) {
		// Desenha o menu de jogo
		menuJogo.desenha(g);
	}
}
