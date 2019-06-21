package sprites;

import java.awt.Graphics;
import java.util.Random;

import main.Pacman;

public class Fantasma_Prestigiador extends Fantasma
{
	private static final long serialVersionUID = 1L;
	
	private int tempo=0;
	private Random aleatorio;

	public Fantasma_Prestigiador(int x, int y)
	{
		super(x,y);
		
		aleatorio = new Random();
	}
	
	@Override
	public void tick()
	{
		tempo++;
		if (tempo >= 60*5) // 5 segundos
		{
			do
			{
				x = aleatorio.nextInt(Pacman.LARGURA/32) * 32;
				y = aleatorio.nextInt(Pacman.ALTURA/32) * 32;
				direcao = aleatorio.nextInt(4);
			
			} while (Pacman.mapa.ladrilhos[x/32][y/32] != null && x != Pacman.jogador.x && y != Pacman.jogador.y); 
			
			tempo = 0;
		}
	}
	
	@Override
	public void render(Graphics graficos)
	{
		if (direcao == 0)
			graficos.drawImage(Pacman.malha.getSprite(66,80), x,y, width,height, null);
		else if (direcao == 1)
			graficos.drawImage(Pacman.malha.getSprite(2,80), x,y, width,height, null);
		else if (direcao == 2)
			graficos.drawImage(Pacman.malha.getSprite(98,80), x,y, width,height, null);
		else
			graficos.drawImage(Pacman.malha.getSprite(34,80), x,y, width,height, null);
	}
}
