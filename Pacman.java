import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Pacman extends Canvas implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	public static int LARGURA = 640, ALTURA = 480; // Resolução do jogo
	
	private boolean estaRodando = false; // Armazena o estado do jogo
	private Thread thread;
	
	public static Jogador jogador; // Armazena a variável do jogador
	
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
	}
	
	public synchronized void start()
	{
		if (estaRodando) return; // Se já estiver rodando, não fazer nada
		estaRodando = true; // O jogo começou a ser executado nesse frame
		
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()
	{
		if (!estaRodando) return; // Se não está rodando, não fazer nada
		estaRodando = false; // O jogo foi finalizado
		
		try { thread.join(); }
		catch (InterruptedException e) { e.printStackTrace(); } 
	}
	
	private void tick()
	// Calcula toda a lógica do jogo
	{
		jogador.tick(); // Calcula a lógica do jogador
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
		jogador.render(graficos); // Renderizar o jogador também
		
		graficos.dispose();
		buffer.show();
	}

	@Override
	public void run()
	// Game loop
	{
		double tickSegundo = 60; // Velocidade de atualização do jogo
		int fps = 0;
		
		double delta = 0;
		double timer = System.currentTimeMillis(); // Contador do tempo
		long ultimoTempo = System.nanoTime(); // Tempo desde a última atualização
		double intervalo = 1000000000/tickSegundo;
		
		requestFocus(); // Ao iniciar o jogo, trazê-lo para primeiro plano
		
		while (estaRodando)
		{
			long agora = System.nanoTime(); // Armazena o tempo atual
			delta += (agora-ultimoTempo)/intervalo; // Se o tempo atual for maior do que deveria ser, delta vai "compensar" esse valor
			ultimoTempo = agora; // Já calculamos o tempo atual, portanto, é o último tempo calculado
			
			while (delta >= 1)
			// Mantém o jogo rodando à velocidade tickSegundo
			{
				tick(); // Lógica do jogo
				render();
				fps++; // Conta os frames desse segundo
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000)
			{
				System.out.println(fps);
				fps = 0; // Reseta a contagem para o próximo segundo
				timer += 1000; // Acrescenta 1s ao tempo atual
			}
		}
		
		stop(); // Encerra o jogo
	}

	@Override
	public void keyPressed(KeyEvent tecla)
	// Ao pressionar uma tecla...
	{
		// Se for uma tecla de movimentação, mover-se
		if (tecla.getKeyCode() == KeyEvent.VK_UP) jogador.cima = true;
		if (tecla.getKeyCode() == KeyEvent.VK_RIGHT) jogador.direita = true;
		if (tecla.getKeyCode() == KeyEvent.VK_DOWN) jogador.baixo = true;
		if (tecla.getKeyCode() == KeyEvent.VK_LEFT) jogador.esquerda = true;
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
