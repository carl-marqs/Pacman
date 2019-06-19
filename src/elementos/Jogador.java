package elementos;

import main.Pacman;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Jogador extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	public boolean cima, direita, baixo, esquerda;
	public int velocidade = 1;
	
	private int tempo = 0, tempoAnimacao = 10;
	public int indiceAnimacao = 0;
	private int direcao = 3;

	public Jogador(int x, int y)
	{
		setBounds(x,y, 32,32); // Definir tamanho e posição do retângulo
	}
	
	private boolean podeMover(int x, int y)
	// Checamos se o próximo ladrilho está vazio para podermos mover
	{
		Rectangle colisao = new Rectangle(x,y, width,height); // Retângulo invisível à frente do player, para checar a colisão
		
		for (int i=0; i < Pacman.mapa.ladrilhos.length; i++)
			for (int j=0; j < Pacman.mapa.ladrilhos[0].length; j++)
				if (Pacman.mapa.ladrilhos[i][j] != null)
					if (colisao.intersects(Pacman.mapa.ladrilhos[i][j]))
						return false; // Se colidir com algum dos ladrilhos do mapa, o player não pode mover
		
		return true;
	}
	
	public void tick()
	{
		// Movimentação do jogador
		if (cima && podeMover(x,y-velocidade))
		{
			y -= velocidade;
			direcao = 0;
		}
		if (direita && podeMover(x+velocidade,y))
		{
			x += velocidade;
			direcao = 1;
		}
		if (baixo && podeMover(x,y+velocidade))
		{
			y += velocidade;
			direcao = 2;
		}
		if (esquerda && podeMover(x-velocidade,y))
		{
			x -= velocidade;
			direcao = 3;
		}
		
		for (int i=0; i < Pacman.mapa.pastilhas.size(); i++)
		{
			if (intersects(Pacman.mapa.pastilhas.get(i))) // Se colidir com uma pastilha...
			{
				Pacman.mapa.pastilhas.remove(i); // Deleta a pastilha
				break;
			}
		}
		
		if (Pacman.mapa.pastilhas.size() == 0) // Se o jogador comer todas as pastilhas, é vencedor
		{
			Pacman.ESTADO = Pacman.PAUSADO;
			Pacman.jogador = new Jogador((Pacman.LARGURA/2)-16, (Pacman.ALTURA/2)-16); // Insere o jogador no meio do mapa
			Pacman.mapa = new Mapa("/mapas/mapa1.png"); // Começar com um mapa gerado a partir de um arquivo
			return;
		}
		
		for (int i=0; i < Pacman.mapa.fantasmas.size(); i++)
		{
			if (Pacman.mapa.fantasmas.get(i).intersects(this))
			{
				Pacman.ESTADO = Pacman.PAUSADO;
				Pacman.jogador = new Jogador((Pacman.LARGURA/2)-16, (Pacman.ALTURA/2)-16); // Insere o jogador no meio do mapa
				Pacman.mapa = new Mapa("/mapas/mapa1.png"); // Começar com um mapa gerado a partir de um arquivo
			}
		}
		
		tempo++;
		if (tempo >= tempoAnimacao)
		{
			tempo = 0;
			indiceAnimacao++;
		}
	}
	
	public void render(Graphics graficos)
	{
		if (direcao == 0) // cima
			Pacman.malha.rotacionar(direcao);
		else if (direcao == 1) // direita
			Pacman.malha.rotacionar(direcao);
		else if (direcao == 2) //baixo
			Pacman.malha.rotacionar(direcao);
		else
			Pacman.malha.rotacionar(direcao);
		
		graficos.drawImage(Malha.animacao[indiceAnimacao%2], x,y, width,height, null);
	}
}
