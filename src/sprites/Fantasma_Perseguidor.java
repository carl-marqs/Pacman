package sprites;

import java.awt.Graphics;

import main.Pacman;

public class Fantasma_Perseguidor extends Fantasma
{
	private static final long serialVersionUID = 1L;
	
	private int modo=0;
	private int tempo=0;

	public Fantasma_Perseguidor(int x, int y)
	{
		super(x, y);
	}
	
	@Override
	public void tick()
	{
		if (modo == 0) // Se está no modo inteligente
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
				modo = 1; // Não está movendo! Precisa encontrar um caminho.
			
			tempo++;
			if (tempo >= 60*8)
			{
				modo = 0;
				tempo = 0;
			}
		
		} else if (modo == 1) // Está preso, precisa encontrar um caminho
		{
			if (direcao == 0)
			{
				if (x < Pacman.jogador.x)
				{
					if (podeMover(x+velocidade, y))
					{
						x += velocidade;
						modo = 0; // Encontramos o caminho!
					}
				
				} else if (podeMover(x-velocidade, y))
				{
					x -= velocidade;
					modo = 0; // Encontramos o caminho!
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
						modo = 0; // Encontramos o caminho!
					}
				
				} else if (podeMover(x, y-velocidade))
				{
					y -= velocidade;
					modo = 0; // Encontramos o caminho!
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
						modo = 0; // Encontramos o caminho!
					}
				
				} else if (podeMover(x-velocidade, y))
				{
					x -= velocidade;
					modo = 0; // Encontramos o caminho!
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
						modo = 0; // Encontramos o caminho!
					}
				
				} else if (podeMover(x, y-velocidade))
				{
					y -= velocidade;
					modo = 0; // Encontramos o caminho!
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
	
	@Override
	public void render(Graphics graficos)
	{
		if (direcao == 0)
			graficos.drawImage(Pacman.malha.getSprite(66,64), x,y, width,height, null);
		else if (direcao == 1)
			graficos.drawImage(Pacman.malha.getSprite(2,64), x,y, width,height, null);
		else if (direcao == 2)
			graficos.drawImage(Pacman.malha.getSprite(98,64), x,y, width,height, null);
		else
			graficos.drawImage(Pacman.malha.getSprite(34,64), x,y, width,height, null);
	}

}
