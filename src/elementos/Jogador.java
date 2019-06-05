package elementos;

import sprites.Pacman;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Jogador extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	public boolean cima, direita, baixo, esquerda;
	public int velocidade = 1;

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
			y -= velocidade;
		if (direita && podeMover(x+velocidade,y))
			x += velocidade;
		if (baixo && podeMover(x,y+velocidade))
			y += velocidade;
		if (esquerda && podeMover(x-velocidade,y))
			x -= velocidade;
		
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
			Pacman.jogador = new Jogador(0,0);
			Pacman.mapa = new Mapa("/mapas/mapa1.png");
			return;
		}
		
		for (int i=0; i < Pacman.mapa.fantasmas.size(); i++)
		{
			if (Pacman.mapa.fantasmas.get(i).intersects(this))
			{
				System.exit(0);
			}
		}
	}
	
	public void render(Graphics graficos)
	{
		graficos.drawImage(Pacman.malha.getSprite(0,0), x,y, width,height, null);
	}
}
