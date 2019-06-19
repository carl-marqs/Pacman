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

import javax.swing.JFrame;

public class Pacman extends Canvas implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	public static int LARGURA = 1184, ALTURA = 608; // Resolução do jogo
	
	private boolean estaJOGANDO = false; // Armazena o estado do jogo
	private Thread thread;
	
	public static Jogador jogador;
	public static Mapa mapa; // Armazena em qual labirinto jogar
	public static Malha malha;
	
	public static final int PAUSADO = 0, JOGANDO = 1;
	public static int ESTADO = PAUSADO;
	
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
		
		addKeyListener(this);
		
		jogador = new Jogador((LARGURA/2)-16, (ALTURA/2)-16); // Insere o jogador no meio do mapa
		mapa = new Mapa("/mapas/mapa1.png"); // Começar com um mapa gerado a partir de um arquivo
		malha = new Malha("/assets/pacman_spritesheet.png"); // Usando a malha de sprites original
	}
	
	public synchronized void start()
	{
		if (estaJOGANDO) return; // Se já estiver JOGANDO, não fazer nada
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
	
	private void tick()
	// Calcula toda a lógica do jogo
	{
		if (ESTADO == JOGANDO)
		{
			jogador.tick(); // Calcula a lógica do jogador
			mapa.tick();
		}
	}
	
	private void render()
	{
		BufferStrategy buffer = getBufferStrategy();
		if (buffer == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics graficos = buffer.getDrawGraphics();
		graficos.setColor(Color.black); // Define a cor do fundo
		graficos.fillRect(0, 0, LARGURA, ALTURA); // Retângulo no qual renderizar
		
		if (ESTADO == JOGANDO)
		// Se o jogo não está pausado, renderizar o jogador e o mapa
		{
			jogador.render(graficos); // Renderizar o jogador também
			mapa.render(graficos);
		
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
			graficos.setColor(Color.WHITE);
			graficos.setFont(new Font(Font.DIALOG, Font.BOLD, 32));
			graficos.drawString("Pressione ENTER para começar!", Pacman.LARGURA/2 - 288, Pacman.ALTURA/2 + 8);
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
				tick(); // Lógica do jogo
				render();
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
			if (tecla.getKeyCode() == KeyEvent.VK_ENTER)
				ESTADO = JOGANDO;
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
}
