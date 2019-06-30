package sprites;

import java.awt.Graphics;
import java.util.Random;

import main.Pacman;

public class Fantasma_Perseguidor extends Fantasma
{
	private static final long serialVersionUID = 1L;
	
	private int modo=1; // 0: aleatorio | 1: inteligente | 2: preso
	private int tempo=0;

	private Random aleatorio = new Random();

	public Fantasma_Perseguidor(int x, int y)
	{ super(x, y); }
	
	@Override
	public void atualizar()
	// Calcula o comportamento do fantasma
	// Começa no modo inteligente, no qual fica perseguindo o jogador, e fica por 12s.
	// Depois de 12s, para dar chance do jogador fugir, ele fica no modo aleatório por 2s.
	// Quando não sabe o que fazer, ele muda pro modo "preso", que escolhe outra posição pra seguir.
	{
		super.atualizar();
		if (morto) return; // se está morto, não se mexer
		
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
			if (tempo >= 60*2)
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
			
			// Se estiver alinhado com o jogador, está se movendo, teoricamente
			if (x == Pacman.jogador.x && y == Pacman.jogador.y)
				movendo = true;
			
			if (!movendo)
				modo = 2; // Não está movendo! Precisa encontrar um caminho.
			
			tempo++;
			if (tempo >= 60*12)
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
	
	@Override
	public void render(Graphics graficos)
	// Diz qual o índice do seu sprite para a classe pai renderizar
	{ super.render(graficos, 64); }

}
