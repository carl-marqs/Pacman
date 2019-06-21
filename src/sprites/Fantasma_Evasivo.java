package sprites;

import java.awt.Graphics;

import main.Pacman;

public class Fantasma_Evasivo extends Fantasma
{
	private static final long serialVersionUID = 1L;
	
	private int modo=0;
	private int tempo=0;

	public Fantasma_Evasivo(int x, int y)
	{
		super(x, y);
	}
	
	@Override
	public void tick()
	{
		float x_medio = Pacman.mapa.fantasmas.get(0).x;
		float y_medio = Pacman.mapa.fantasmas.get(0).y;
		
		for (int i=1; i < Pacman.mapa.fantasmas.size()-1; i++)
		{
			x_medio = (x_medio + Pacman.mapa.fantasmas.get(i).x)/2;
			y_medio = (y_medio + Pacman.mapa.fantasmas.get(i).y)/2;
		}
		
		if (modo == 0) // Se está no modo inteligente
		{
			boolean movendo = false;
			
			if (y < y_medio)
				if (podeMover(x,y-velocidade))
				{
					y -= velocidade;
					movendo = true;
					direcao = 0;
				}
			
			if (x >= x_medio)
				if (podeMover(x+velocidade,y))
				{
					x += velocidade;
					movendo = true;
					direcao = 1;
				}
			
			if (y >= y_medio)
				if (podeMover(x,y+velocidade))
				{
					y += velocidade;
					movendo = true;
					direcao = 2;
				}
			
			if (x < x_medio)
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
				if (x > x_medio)
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
				if (y > y_medio)
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
				if (x >= x_medio)
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
				if (y >= y_medio)
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
			graficos.drawImage(Pacman.malha.getSprite(66,96), x,y, width,height, null);
		else if (direcao == 1)
			graficos.drawImage(Pacman.malha.getSprite(2,96), x,y, width,height, null);
		else if (direcao == 2)
			graficos.drawImage(Pacman.malha.getSprite(98,96), x,y, width,height, null);
		else
			graficos.drawImage(Pacman.malha.getSprite(34,96), x,y, width,height, null);
	}

}
