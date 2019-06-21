package sprites;

import java.awt.Graphics;

import main.Pacman;

public class Fantasma_Aleatorio extends Fantasma
{
	private static final long serialVersionUID = 1L;

	public Fantasma_Aleatorio(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void tick()
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
	}
	
	@Override
	public void render(Graphics graficos)
	{
		if (direcao == 0)
			graficos.drawImage(Pacman.malha.getSprite(66,112), x,y, width,height, null);
		else if (direcao == 1)
			graficos.drawImage(Pacman.malha.getSprite(2,112), x,y, width,height, null);
		else if (direcao == 2)
			graficos.drawImage(Pacman.malha.getSprite(98,112), x,y, width,height, null);
		else
			graficos.drawImage(Pacman.malha.getSprite(34,112), x,y, width,height, null);
	}

}
