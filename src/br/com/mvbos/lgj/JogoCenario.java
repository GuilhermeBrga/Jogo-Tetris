package br.com.mvbos.lgj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.Random;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import br.com.mvbos.lgj.base.CenarioPadrao;
import br.com.mvbos.lgj.base.Texto;

public class JogoCenario extends CenarioPadrao {

	enum Estado {
		JOGANDO, GANHOU, PERDEU
	}

	private static final int ESPACAMENTO = 2;

	private static final int ESPACO_VAZIO = -1;

	private static final int LINHA_COMPLETA = -2;

	private int largBloco, altBloco;

	private int ppx, ppy;

	private final int[][] grade = new int[10][16];

	private double temporizador = 0.0;

	private Texto texto = new Texto(20);

	private Random rand = new Random();

	private int idPeca = -1;
	private int idPrxPeca = -1;

	private int idPrxPeca1 = -1;

	private int idPrxPeca2 = -1;
	private Color corPeca;
	private int[][] peca;

	private int nivel = Jogo.nivel;
	private int pontos;
	private int linhasFeitas;

	private boolean animar;
	private boolean depurar;

	private Estado estado = Estado.JOGANDO;

	// Som
	private AudioInputStream as;

	private Clip clipAdicionarPeca;

	private Clip clipMarcarLinha;

	private Sequencer seqSomDeFundo;
	public int getPontos(){
		return pontos;
	}

	private int[] contagemPecas = new int[Peca.PECAS.length];
	private int totalPecas = 0;

	public JogoCenario(int largura, int altura) {
		super(largura, altura);
	}

	@Override
	public void carregar() {
		largBloco = largura / grade.length;
		altBloco = altura / grade[0].length;

		for (int i = 0; i < grade.length; i++) {
			for (int j = 0; j < grade[0].length; j++) {
				grade[i][j] = ESPACO_VAZIO;
			}
		}

		Type[] audioFileTypes = AudioSystem.getAudioFileTypes();
		for (Type t : audioFileTypes) {
			System.out.println(t.getExtension());
		}

		try {
			as = AudioSystem.getAudioInputStream(new File("C:\\Users\\guilh\\IdeaProjects\\TetrisGame\\src\\br\\com\\mvbos\\lgj\\adiciona_peca.wav"));
			clipAdicionarPeca = AudioSystem.getClip();
			clipAdicionarPeca.open(as);

			as = AudioSystem.getAudioInputStream(new File("C:\\Users\\guilh\\IdeaProjects\\TetrisGame\\src\\br\\com\\mvbos\\lgj\\109662_grunz_success.wav"));
			clipMarcarLinha = AudioSystem.getClip();
			clipMarcarLinha.open(as);

			seqSomDeFundo = MidiSystem.getSequencer();
			seqSomDeFundo.setSequence(MidiSystem.getSequence(new File("C:\\Users\\guilh\\IdeaProjects\\TetrisGame\\src\\br\\com\\mvbos\\lgj\\piano_quebrado.mid")));
			seqSomDeFundo.open();

			seqSomDeFundo.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);

			seqSomDeFundo.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

		adicionaPeca();
	}

	@Override
	public void descarregar() {
		popUpNomeJogador();
		if (clipAdicionarPeca != null) {
			clipAdicionarPeca.stop();
			clipAdicionarPeca.close();
		}

		if (clipMarcarLinha != null) {
			clipMarcarLinha.stop();
			clipMarcarLinha.close();
		}

		if (seqSomDeFundo != null) {
			seqSomDeFundo.stop();
			seqSomDeFundo.close();
		}
	}

	@Override
	public void atualizar() {

		if (estado != Estado.JOGANDO) {
			return;
		}

		if (Jogo.controleTecla[Jogo.Tecla.ESQUERDA.ordinal()]) {
			if (validaMovimento(peca, ppx - 1, ppy))
				ppx--;

		} else if (Jogo.controleTecla[Jogo.Tecla.DIREITA.ordinal()]) {
			if (validaMovimento(peca, ppx + 1, ppy))
				ppx++;
		}

		if (Jogo.controleTecla[Jogo.Tecla.CIMA.ordinal()]) {
			girarReposicionarPeca(false);

			//tecla Z
		} else if (Jogo.controleTecla[Jogo.Tecla.Z.ordinal()]) {
			girarReposicionarPeca(true);

		}else if(Jogo.controleTecla[Jogo.Tecla.BAIXO.ordinal()]) {
			if (validaMovimento(peca, ppx, ppy + 1))
				ppy++;
				pontos++;
		} else if(Jogo.controleTecla[Jogo.Tecla.SPACE.ordinal()]){
			while(validaMovimento(peca, ppx, ppy + 1)){
				ppy++;
				pontos+=2;
			}
		}

		if (depurar && Jogo.controleTecla[Jogo.Tecla.BC.ordinal()]) {
			if (++idPeca == Peca.PECAS.length)
				idPeca = 0;

			peca = Peca.PECAS[idPeca];
			corPeca = Peca.Cores[idPeca];
		}

		Jogo.liberaTeclas();

		if (animar && temporizador >= 5.0) {
			animar = false;

			descerColunas();
			adicionaPeca();

		} else if (temporizador >= 100.0) {
			temporizador = 0.0;

			if (colidiu(ppx, ppy + 1)) {

				if (clipAdicionarPeca != null) {
					clipAdicionarPeca.setFramePosition(0);
					clipAdicionarPeca.start();
				}

				if (!parouForaDaGrade()) {
					adicionarPecaNaGrade();
					animar = marcarLinha();

					peca = null;

					if (!animar)
						adicionaPeca();

				} else {
					estado = Estado.PERDEU;
				}

			} else
				ppy++;

		} else
			temporizador += 1 + ((nivel-1)*0.1);
			//aumenta a velocidade em 0.1 por nivel
	}

	public void adicionaPeca() {

		ppy = -2;
		ppx = grade.length / 2 - 1;

		if (idPeca == -1)
			idPeca = rand.nextInt(Peca.PECAS.length);
		idPrxPeca = rand.nextInt(Peca.PECAS.length);
		if(idPeca == idPrxPeca){
			idPrxPeca = rand.nextInt(Peca.PECAS.length);
		}
		else{
			idPeca = idPrxPeca;
		}
		if(idPrxPeca1 == -1){
			idPrxPeca1 = rand.nextInt(Peca.PECAS.length);
		}else{
			idPrxPeca = idPrxPeca1;
			idPrxPeca1 = idPrxPeca2;
		}
		idPrxPeca2 = rand.nextInt(Peca.PECAS.length);

		peca = Peca.PECAS[idPeca];
		corPeca = Peca.Cores[idPeca];

		contagemPecas[idPeca]++;
		totalPecas++;

	}

	private void adicionarPecaNaGrade() {

		for (int col = 0; col < peca.length; col++) {
			for (int lin = 0; lin < peca[col].length; lin++) {

				if (peca[lin][col] != 0) {

					grade[col + ppx][lin + ppy] = idPeca;

				}
			}
		}
	}

	private boolean validaMovimento(int[][] peca, int px, int py) {

		if (peca == null)
			return false;

		for (int col = 0; col < peca.length; col++) {
			for (int lin = 0; lin < peca[col].length; lin++) {
				if (peca[lin][col] == 0)
					continue;

				int prxPx = col + px; // Proxima posicao peca x
				int prxPy = lin + py; // Proxima posicao peca y

				if (prxPx < 0 || prxPx >= grade.length)
					return false;

				if (prxPy >= grade[0].length)
					return false;

				if (prxPy < 0)
					continue;

				// Colidiu com uma peca na grade
				if (grade[prxPx][prxPy] > ESPACO_VAZIO)
					return false;

			}
		}

		return true;
	}

	private boolean parouForaDaGrade() {

		if (peca == null)
			return false;

		for (int lin = 0; lin < peca.length; lin++) {
			for (int col = 0; col < peca[lin].length; col++) {
				if (peca[lin][col] == 0)
					continue;
				// Fora da grade
				if (lin + ppy < 0)
					return true;
			}
		}

		return false;
	}

	private boolean colidiu(int px, int py) {

		if (peca == null)
			return false;

		for (int col = 0; col < peca.length; col++) {
			for (int lin = 0; lin < peca[col].length; lin++) {
				if (peca[lin][col] == 0)
					continue;

				int prxPx = col + px;
				int prxPy = lin + py;

				if (depurar) {
					if (prxPx < 0 || prxPx >= grade.length)
						return false;
				}
				// Chegou na base da grade
				if (prxPy == grade[0].length)
					return true;

				// Fora da grade
				if (prxPy < 0)
					continue;

				// Colidiu com uma peca na grade
				if (grade[prxPx][prxPy] > ESPACO_VAZIO)
					return true;
			}
		}

		return false;
	}

	//linhas completas
	private boolean marcarLinha() {
		int multPontos = 0;

		for (int lin = grade[0].length - 1; lin >= 0; lin--) {
			boolean linhaCompleta = true;

			for (int col = grade.length - 1; col >= 0; col--) {
				if (grade[col][lin] == ESPACO_VAZIO) {
					linhaCompleta = false;
					break;
				}
			}

			if (linhaCompleta) {
				multPontos++;
				for (int col = grade.length - 1; col >= 0; col--) {
					grade[col][lin] = LINHA_COMPLETA;
				}
			}
		}

		pontos += calculaPontosGanhos(multPontos, nivel);
		Jogo.jogador.setPontosAcumulados(pontos);
		linhasFeitas += multPontos;

		//subir de nivel
		if (linhasFeitas == 10) {
			nivel++;
			linhasFeitas = 0;
		}

		return multPontos > 0;
	}

	private void descerColunas() {
		for (int col = 0; col < grade.length; col++) {
			for (int lin = grade[0].length - 1; lin >= 0; lin--) {

				if (grade[col][lin] == LINHA_COMPLETA) {
					int moverPara = lin;
					int prxLinha = lin - 1;

					for (; prxLinha > -1; prxLinha--) {
						if (grade[col][prxLinha] == LINHA_COMPLETA)
							continue;
						else
							break;

					}

					for (; moverPara > -1; moverPara--, prxLinha--) {

						if (prxLinha > -1)
							grade[col][moverPara] = grade[col][prxLinha];
						else
							grade[col][moverPara] = ESPACO_VAZIO;

					}
				}
			}
		}

		if (clipMarcarLinha != null) {
			clipMarcarLinha.setFramePosition(0);
			clipMarcarLinha.start();
		}

	}

	protected void girarPeca(boolean sentidoHorario) {
		if (peca == null)
			return;

		final int[][] temp = new int[peca.length][peca.length];

		for (int i = 0; i < peca.length; i++) {
			for (int j = 0; j < peca.length; j++) {
				if (sentidoHorario)
					temp[j][peca.length - i - 1] = peca[i][j];
				else
					temp[peca.length - j - 1][i] = peca[i][j];
			}
		}

		System.out.println("Antes:");
		imprimirArray(peca);
		System.out.println("Depois:");
		imprimirArray(temp);

		if (validaMovimento(temp, ppx, ppy)) {
			peca = temp;
		}
	}

	private void imprimirArray(int[][] arr) {
		for (int lin = 0; lin < arr.length; lin++) {
			for (int col = 0; col < arr[lin].length; col++) {
				System.out.print(arr[lin][col] + "\t");
			}

			System.out.println();
		}
	}

	private void girarReposicionarPeca(boolean sentidoHorario) {
		if (peca == null)
			return;

		int tempPx = ppx;
		final int[][] tempPeca = new int[peca.length][peca.length];

		for (int i = 0; i < peca.length; i++) {
			for (int j = 0; j < peca.length; j++) {
				if (sentidoHorario)
					tempPeca[j][peca.length - i - 1] = peca[i][j];
				else
					tempPeca[peca.length - j - 1][i] = peca[i][j];
			}
		}

		// Reposiciona peca na tela
		for (int i = 0; i < tempPeca.length; i++) {
			for (int j = 0; j < tempPeca.length; j++) {
				if (tempPeca[j][i] == 0) {
					continue;
				}

				int prxPx = i + tempPx;

				if (prxPx < 0)
					tempPx = tempPx - prxPx;

				else if (prxPx == grade.length)
					tempPx = tempPx - 1;

			}
		}

		if (validaMovimento(tempPeca, tempPx, ppy)) {
			peca = tempPeca;
			ppx = tempPx;
		}
	}
	@Override
	public void desenhar(Graphics2D g) {

		for (int col = 0; col < grade.length; col++) {
			for (int lin = 0; lin < grade[0].length; lin++) {
				int valor = grade[col][lin];

				if (valor == ESPACO_VAZIO)
					continue;

				if (valor == LINHA_COMPLETA)
					g.setColor(Color.RED);
				else
					g.setColor(Peca.Cores[valor]);

				int x = 250 + col * largBloco + ESPACAMENTO;
				int y = lin * altBloco + ESPACAMENTO;

				g.fillRect(x, y, largBloco - ESPACAMENTO, altBloco - ESPACAMENTO);

			}
		}

		if (peca != null) {
			g.setColor(corPeca);

			for (int col = 0; col < peca.length; col++) {
				for (int lin = 0; lin < peca[col].length; lin++) {
					if (peca[lin][col] != 0) {

						int x = 250 + (col + ppx) * largBloco + ESPACAMENTO;
						int y = (lin + ppy) * altBloco + ESPACAMENTO;

						g.fillRect(x, y, largBloco - ESPACAMENTO, altBloco - ESPACAMENTO);

					} else if (depurar) {
						g.setColor(Color.PINK);
						int x = 250 + (col + ppx) * largBloco + ESPACAMENTO;
						int y = (lin + ppy) * altBloco + ESPACAMENTO;

						g.fillRect(x, y, largBloco - ESPACAMENTO, altBloco - ESPACAMENTO);

						g.setColor(corPeca);
					}
				}
			}
		}

		int miniatura = largBloco / 4;
		int[][] prxPeca = Peca.PECAS[idPrxPeca];
		int[][] prxPeca1 = Peca.PECAS[idPrxPeca1];
		int[][] prxPeca2 = Peca.PECAS[idPrxPeca2];


		g.setColor(Peca.Cores[idPrxPeca]);

		for (int col = 0; col < prxPeca.length; col++) {
			for (int lin = 0; lin < prxPeca[col].length; lin++) {
				if (prxPeca[lin][col] == 0)
					continue;

				int x = col * miniatura + ESPACAMENTO + 850;
				int y = lin * miniatura + ESPACAMENTO;

				g.fillRect(x, y, miniatura - ESPACAMENTO, miniatura - ESPACAMENTO);

			}
		}

		g.setColor(Peca.Cores[idPrxPeca1]);

		for (int col = 0; col < prxPeca1.length; col++) {
			for (int lin = 0; lin < prxPeca1[col].length; lin++) {
				if (prxPeca1[lin][col] == 0)
					continue;

				int x = col * miniatura + ESPACAMENTO + 850;
				int y = (lin + 20) * miniatura + ESPACAMENTO;

				g.fillRect(x, y, miniatura - ESPACAMENTO, miniatura - ESPACAMENTO);

			}
		}

		g.setColor(Peca.Cores[idPrxPeca2]);

		for (int col = 0; col < prxPeca2.length; col++) {
			for (int lin = 0; lin < prxPeca2[col].length; lin++) {
				if (prxPeca2[lin][col] == 0)
					continue;

				int x = col * miniatura + ESPACAMENTO + 850;
				int y = (lin + 40) * miniatura + ESPACAMENTO;

				g.fillRect(x, y, miniatura - ESPACAMENTO, miniatura - ESPACAMENTO);

			}
		}

		texto.setCor(Color.WHITE);
		texto.desenha(g, "Level: " + nivel,0 , 20);
		texto.desenha(g,  "Linhas feitas: " + linhasFeitas,0, 40);
		texto.desenha(g, "Pontos: " + pontos, 0, 60);
		texto.desenha(g, "Pecas verdes: " + contagemPecas[0], 0, 230);
		texto.desenha(g, "Pecas laranjas: " + contagemPecas[1], 0, 260);
		texto.desenha(g, "Pecas amarelas: " + contagemPecas[2], 0, 290);
		texto.desenha(g, "Pecas ciano: " + contagemPecas[3], 0, 320);
		texto.desenha(g, "Pecas azuis: " + contagemPecas[4], 0, 350);
		texto.desenha(g, "Pecas rosas: " + contagemPecas[5], 0, 380);
		texto.desenha(g, "Pecas brancas: " + contagemPecas[6], 0, 410);
		texto.desenha(g, "Total de pecas: " + totalPecas, 0, 460);

		if (estado != Estado.JOGANDO) {
			texto.setCor(Color.WHITE);

			if (estado == Estado.GANHOU)
				texto.desenha(g, "Finalmente!", 180, 180);
		}

		if (estado != Estado.JOGANDO) {
			texto.setCor(Color.WHITE);

			if (estado != Estado.GANHOU) {
				texto.desenha(g, "Deu ruim :(", 500, 170);
				texto.desenha(g, "Aperte Esc", 500, 200);
			}
		}
	}

	//calculo dos pontos
	private int calculaPontosGanhos(int linhasEliminadas, int nivel){
		if(linhasEliminadas == 1)
			return nivel*100;
		else if(linhasEliminadas == 2)
			return nivel*300;
		else if(linhasEliminadas == 3)
			return nivel*500;
		else if(linhasEliminadas >= 4)
			return nivel*800;
		return 0;
	}

	private void popUpNomeJogador() {

		Jogador jogador = new Jogador();
		Ranking ranking = new Ranking();


		String apelido = JOptionPane.showInputDialog("Digite seu apelido:");
		jogador.setNome(apelido);

		int pontosDoJogo = getPontos();

		jogador.setPontosAcumulados(pontosDoJogo);

		ranking.carregaRanking();


		ranking.addJogadores(jogador);


		ranking.organizar();

		JOptionPane.showMessageDialog(null, jogador.getNome() + " - " + jogador.getPontosAcumulados() + " pontos");
		ranking.saveInfo();
		ranking.abrirMelhores();
	}
}