package main;

import elementos.Jogador;
import elementos.Mapa;
import elementos.Malha;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFrame;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Pacman extends Canvas implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	// Resolução do jogo
	public static int LARGURA = 1184;
	public static int ALTURA = 608;
	
	// Elementos do jogo
	public static Jogador jogador;
	public static Mapa mapa; // Armazena em qual labirinto jogar
	public static Malha malha;
	
	// Controlam o estado de execução do jogo
	private boolean estaJOGANDO = false;
	public static final int PAUSADO = 0, JOGANDO = 1;
	public static int ESTADO = PAUSADO;
	
	private Thread thread;
	
	public static void main(String[] args)
	{
		Pacman jogo = new Pacman();
		
		JFrame frame = new JFrame();
		frame.setTitle("Pacman"); // Define o título do jogo
		frame.add(jogo);
		frame.setResizable(false); // Proibir redimensionar a janela
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Centralizar a janela
		frame.setVisible(true);
		
		jogo.start(); // Inicia o jogo
	}
	
	public Pacman()
	{
		Dimension resolucao = new Dimension(LARGURA, ALTURA); // Define a resolução do jogo
		setPreferredSize(resolucao); // Aplica a resolução definida
		setMinimumSize(resolucao); // Define a resolução mínima da janela
		setMaximumSize(resolucao); // Define a resolução máxima da janela
		
		jogador = new Jogador((LARGURA/2)-16, (ALTURA/2)-16); // Insere o jogador no meio do mapa
		malha = new Malha("/assets/pacman_spritesheet.png"); // Usando a malha de sprites original
		
		tocarMusica("res/sons/pacman_beginning.wav");
		
		addKeyListener(this);
	}
	
	public synchronized void start()
	{
		if (estaJOGANDO) return; // Se já estiver jogando, não fazer nada
		estaJOGANDO = true; // O jogo começou a ser executado nesse frame
		
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()
	{
		if (!estaJOGANDO) return; // Se não está JOGANDO, não fazer nada
		estaJOGANDO = false; // O jogo foi finalizado
		
		try { thread.join(); }
		catch (InterruptedException e) { e.printStackTrace(); } 
	}
	
	private void atualizar()
	// Calcula toda a lógica do jogo
	{
		if (ESTADO == JOGANDO)
		{
			jogador.atualizar(); // Calcula a lógica do jogador
			mapa.atualizar();
		}
	}
	
	private void renderizar()
	{
		BufferStrategy buffer = getBufferStrategy();
		if (buffer == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics graficos = buffer.getDrawGraphics();
		graficos.setColor(Color.BLACK); // Define a cor do fundo
		graficos.fillRect(0, 0, LARGURA, ALTURA); // Retângulo no qual renderizar
		
		if (ESTADO == JOGANDO)
		// Se o jogo não está pausado, renderizar o jogador, o mapa e o texto
		{
			jogador.renderizar(graficos); // Renderizar o jogador também
			mapa.renderizar(graficos);
			
			// Exibir a pontuação
			graficos.setColor(Color.WHITE);
			graficos.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
			graficos.drawString("Pontuação: " + Integer.toString(jogador.pontuacao), LARGURA - 256,24);
			
			// Exibir a quantidade de vida
			graficos.drawString("Vidas: " + Integer.toString(jogador.vidas), 8,24);
		
		} else
		// Se o jogo estiver pausado, exibir uma caixa de texto
		{
			// Variáveis da largura e altura e posições x e y da caixa de texto
			int larguraCaixa = 640, alturaCaixa = 320;
			int pos_x = (Pacman.LARGURA/2) - (larguraCaixa/2);
			int pos_y = (Pacman.ALTURA/2) - (alturaCaixa/2);
			
			// Exibir a caixa de texto
			graficos.setColor(Color.BLUE);
			graficos.fillRect(pos_x, pos_y, larguraCaixa, alturaCaixa);
			
			// Exibir o texto
			graficos.setColor(Color.YELLOW);
			graficos.setFont(new Font(Font.DIALOG, Font.BOLD, 48));
			graficos.drawString("Pacman 322", Pacman.LARGURA/2 - 160, Pacman.ALTURA/2 - 64);
			
			graficos.setColor(Color.WHITE);
			graficos.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
			graficos.drawString("Aperte ENTER para o mapa oficial", Pacman.LARGURA/2 - 280, Pacman.ALTURA/2 - 8);
			graficos.drawString("Aperte ESPAÇO para mapa aleatório", Pacman.LARGURA/2 - 296, Pacman.ALTURA/2 + 16);
		}
		
		graficos.dispose();
		buffer.show();
	}

	@Override
	public void run()
	// Game loop
	{
		double tickSegundo = 60; // Velocidade de atualização do jogo
		
		double delta = 0;
		long ultimoTempo = System.nanoTime(); // Tempo desde a última atualização
		double intervalo = 1e9/tickSegundo;
		
		requestFocus(); // Ao iniciar o jogo, trazê-lo para primeiro plano
		
		while (estaJOGANDO)
		{
			long agora = System.nanoTime(); // Armazena o tempo atual
			delta += (agora-ultimoTempo)/intervalo; // Se o tempo atual for maior do que deveria ser, delta vai "compensar" esse valor
			ultimoTempo = agora; // Já calculamos o tempo atual, portanto, é o último tempo calculado
	
			while (delta >= 1)
			// Mantém o jogo JOGANDO à velocidade tickSegundo
			{
				atualizar(); // Lógica do jogo
				renderizar();
				delta--;
			}
		}
		
		stop(); // Encerra o jogo
	}

	@Override
	public void keyPressed(KeyEvent tecla)
	// Ao pressionar uma tecla...
	{
		if (ESTADO == JOGANDO) // Se o jogo não estiver pausado...
		{
			// Se for uma tecla de movimentação, mover-se
			if (tecla.getKeyCode() == KeyEvent.VK_UP) jogador.cima = true;
			if (tecla.getKeyCode() == KeyEvent.VK_RIGHT) jogador.direita = true;
			if (tecla.getKeyCode() == KeyEvent.VK_DOWN) jogador.baixo = true;
			if (tecla.getKeyCode() == KeyEvent.VK_LEFT) jogador.esquerda = true;
		
		} else
		// Se estiver pausado, esperar o jogador pressionar enter para começá-lo
		{
			if (tecla.getKeyCode() == KeyEvent.VK_ENTER)
			{
				mapa = new Mapa("/mapas/mapa1.png"); // Começar com um mapa gerado a partir de um arquivo
				ESTADO = JOGANDO;
			}
			else if (tecla.getKeyCode() == KeyEvent.VK_SPACE)
			{
				mapa = new Mapa(); // Começar com um mapa gerado a partir de um arquivo
				ESTADO = JOGANDO;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent tecla)
	// Ao soltar a tecla...
	{
		// Se for uma tecla de movimentação, parar de mover
		if (tecla.getKeyCode() == KeyEvent.VK_UP) jogador.cima = false;
		if (tecla.getKeyCode() == KeyEvent.VK_RIGHT) jogador.direita = false;
		if (tecla.getKeyCode() == KeyEvent.VK_DOWN) jogador.baixo = false;
		if (tecla.getKeyCode() == KeyEvent.VK_LEFT) jogador.esquerda = false;
	}

	@Override
	public void keyTyped(KeyEvent tecla) {}
	
	public static void tocarMusica(String caminho)
	// Tocar uma música a partir do caminho do arquivo
	{
		try
		{ AudioPlayer.player.start(new AudioStream(new FileInputStream(new File(caminho)))); }
		catch (Exception e)
		{ System.out.println(e.getMessage()); }
	}
	
	public static void pausar()
	// Encerrar o jogo atual e voltar para a tela inicial
	{
		ESTADO = PAUSADO;
		mapa = new Mapa();
		jogador = new Jogador((Pacman.LARGURA/2)-16, (Pacman.ALTURA/2)-16); // Insere o jogador no meio do mapa
	}
}
