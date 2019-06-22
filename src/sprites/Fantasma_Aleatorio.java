package sprites;

import java.awt.Graphics;
import java.util.Random;

public class Fantasma_Aleatorio extends Fantasma
{
	private static final long serialVersionUID = 1L;
	
	private Random aleatorio = new Random();

	public Fantasma_Aleatorio(int x, int y) {
		super(x, y);
		velocidade = 2;
		direcao = aleatorio.nextInt(4);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if (morto)
			return;
		
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
	
	public void render(Graphics graficos)
	{
		super.render(graficos, 112);
	}

}
