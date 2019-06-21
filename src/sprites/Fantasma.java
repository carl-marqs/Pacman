package sprites;


import main.Pacman;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Fantasma extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	public Random aleatorio;
	
	private int tempo=0;
	protected int velocidade = 1;
	public int modo=0; // 0: aleatorio | 1: inteligente | 2: preso | 3: morto
	protected int direcao=0; // 0: cima | 1: direita | 2: baixo | 3: esquerda

	public Fantasma(int x, int y)
	{
		aleatorio = new Random();

		setBounds(x,y, 32,32);
		direcao = aleatorio.nextInt(4);
	}
	
	protected boolean podeMover(int x, int y)
	// Checamos se o próximo ladrilho está vazio para podermos mover
	{
		Rectangle colisao = new Rectangle(x,y, width,height); // Retângulo invisível à frente do fantasma, para checar a colisão
		
		for (int i=0; i < Pacman.mapa.ladrilhos.length; i++)
			for (int j=0; j < Pacman.mapa.ladrilhos[0].length; j++)
				if (Pacman.mapa.ladrilhos[i][j] != null)
					if (colisao.intersects(Pacman.mapa.ladrilhos[i][j]))
						return false; // Se colidir com algum dos ladrilhos do mapa, o fantasma não pode mover
		
		return true;
	}
	
	public void tick()
	{
		if (modo == 0) // Se está no modo aleatório...
		{
			if (direcao == 0)
			{
				if (podeMover(x,y-velocidade)) // Se não encontrar obstáculos à sua frente, pode mover
					y -= velocidade;
				else
					direcao = aleatorio.nextInt(4); // Se encontrar, ir pra outra direção aleatoriamente
				
			} else if (direcao == 1)
			{
				if (podeMover(x+velocidade,y))
					x += velocidade;
				else
					direcao = aleatorio.nextInt(4);
				
			} else if (direcao == 2)
			{
				if (podeMover(x,y+velocidade))
					y += velocidade;
				else
					direcao = aleatorio.nextInt(4);
				
			} else if (direcao == 3)
			{
				if (podeMover(x-velocidade,y))
					x -= velocidade;
				else
					direcao = aleatorio.nextInt(4);
			}
			
			tempo++;
			if (tempo >= 60*4)
			{
				modo = 1;
				tempo = 0;
			}
			
		} else if (modo == 1) // Se está no modo inteligente
		{
			boolean movendo = false;
			
			if (y > Pacman.jogador.y)
				if (podeMover(x,y-velocidade))
				{
					y -= velocidade;
					movendo = true;
					direcao = 0;
				}
			
			if (x < Pacman.jogador.x)
				if (podeMover(x+velocidade,y))
				{
					x += velocidade;
					movendo = true;
					direcao = 1;
				}
			
			if (y < Pacman.jogador.y)
				if (podeMover(x,y+velocidade))
				{
					y += velocidade;
					movendo = true;
					direcao = 2;
				}
			
			if (x > Pacman.jogador.x)
				if (podeMover(x-velocidade,y))
				{
					x -= velocidade;
					movendo = true;
					direcao = 3;
				}
			
			if (x == Pacman.jogador.x && y == Pacman.jogador.y)
				movendo = true;
			
			if (!movendo)
				modo = 2; // Não está movendo! Precisa encontrar um caminho.
			
			tempo++;
			if (tempo >= 60*8)
			{
				modo = 0;
				tempo = 0;
			}
		
		} else if (modo == 2) // Está preso, precisa encontrar um caminho
		{
			if (direcao == 0)
			{
				if (x < Pacman.jogador.x)
				{
					if (podeMover(x+velocidade, y))
					{
						x += velocidade;
						modo = 1; // Encontramos o caminho!
					}
				
				} else if (podeMover(x-velocidade, y))
				{
					x -= velocidade;
					modo = 1; // Encontramos o caminho!
				}
				
				if (podeMover(x,y-velocidade))
					y -= velocidade; 
				
			} else if (direcao == 1)
			{
				if (y < Pacman.jogador.y)
				{
					if (podeMover(x, y+velocidade))
					{
						y += velocidade;
						modo = 1; // Encontramos o caminho!
					}
				
				} else if (podeMover(x, y-velocidade))
				{
					y -= velocidade;
					modo = 1; // Encontramos o caminho!
				}
				
				if (podeMover(x+velocidade,y))
					x += velocidade; 
				
			} else if (direcao == 2)
			{
				if (x < Pacman.jogador.x)
				{
					if (podeMover(x+velocidade, y))
					{
						x += velocidade;
						modo = 1; // Encontramos o caminho!
					}
				
				} else if (podeMover(x-velocidade, y))
				{
					x -= velocidade;
					modo = 1; // Encontramos o caminho!
				}
				
				if (podeMover(x,y+velocidade))
					y += velocidade; 
				
			} else if (direcao == 3)
			{
				if (y < Pacman.jogador.y)
				{
					if (podeMover(x, y+velocidade))
					{
						y += velocidade;
						modo = 1; // Encontramos o caminho!
					}
				
				} else if (podeMover(x, y-velocidade))
				{
					y -= velocidade;
					modo = 1; // Encontramos o caminho!
				}
				
				if (podeMover(x-velocidade,y))
					x -= velocidade; 
			}
			
			tempo++;
			if (tempo >= 60*0.05)
			{
				modo = 0;
				tempo = 0;
			}
		}
	}
	
	public void render(Graphics graficos)
	{
		if (direcao == 0)
			graficos.drawImage(Pacman.malha.getSprite(66,64), x,y, width,height, null);
		else if (direcao == 1)
			graficos.drawImage(Pacman.malha.getSprite(2,80), x,y, width,height, null);
		else if (direcao == 2)
			graficos.drawImage(Pacman.malha.getSprite(98,96), x,y, width,height, null);
		else
			graficos.drawImage(Pacman.malha.getSprite(34,112), x,y, width,height, null);
	}
}
