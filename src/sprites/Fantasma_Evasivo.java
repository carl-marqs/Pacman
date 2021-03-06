package sprites;

import java.awt.Graphics;
import java.util.Random;

import main.Pacman;

public class Fantasma_Evasivo extends Fantasma
{
	private static final long serialVersionUID = 1L;
	
	private int modo=1;
	private int tempo=0;
	private Random aleatorio = new Random();

	public Fantasma_Evasivo(int x, int y)
	{
		super(x, y);
		velocidade = 2; // é mais rápido que os outros fantasmas
	}
	
	@Override
	public void atualizar()
	// Calcula o comportamento do fantasma
	// Faz basicamente o oposto do que o perseguidor faz com o jogador, com o ponto médio da posição dos outros fantasmas
	{
		super.atualizar();
		if (morto) return; // se está morto, não se mexer
		
		// Calcular a posição média de todos os fantasmas, menos si mesmo
		float x_medio = Pacman.mapa.fantasmas.get(0).x;
		float y_medio = Pacman.mapa.fantasmas.get(0).y;
		for (int i=1; i < Pacman.mapa.fantasmas.size()-1; i++)
		{
			x_medio = (x_medio + Pacman.mapa.fantasmas.get(i).x)/2;
			y_medio = (y_medio + Pacman.mapa.fantasmas.get(i).y)/2;
		}
		
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
			if (tempo >= 60*1)
			{
				modo = 1;
				tempo = 0;
			}
			
		} else if (modo == 1) // Se está no modo inteligente
		{
			boolean movendo = false;
			
			if (y < y_medio)
				if (podeMover(x,y-velocidade))
				{
					y -= velocidade;
					movendo = true;
					direcao = 0;
				}
			
			if (x > x_medio)
				if (podeMover(x+velocidade,y))
				{
					x += velocidade;
					movendo = true;
					direcao = 1;
				}
			
			if (y > y_medio)
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
			
			if (x == x_medio && y == y_medio)
				movendo = true;
			
			if (!movendo)
				modo = 2; // Não está movendo! Precisa encontrar um caminho.
		
		} else if (modo == 2) // Está preso, precisa encontrar um caminho
		{
			if (direcao == 0)
			{
				if (x > x_medio)
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
				if (y > y_medio)
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
				if (x > x_medio)
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
				if (y > y_medio)
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
	
	@Override
	public void render(Graphics graficos)
	// Diz qual o índice do seu sprite para a classe pai renderizar
	{ super.render(graficos, 96); }

}
