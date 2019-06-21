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
		super.tick();
		if (morto)
			return;
		
		tempo++;
		if (tempo >= 60*3) // 3 segundos
		{
			do
			{
				do
					x = aleatorio.nextInt(Pacman.LARGURA/32) * 32;
				while (Math.abs(Pacman.jogador.x - x) < 16 || Math.abs(Pacman.jogador.x - x) >= 192);
				
				do
					y = aleatorio.nextInt(Pacman.ALTURA/32) * 32;
				while (Math.abs(Pacman.jogador.y - y) < 16 || Math.abs(Pacman.jogador.y - y) >= 192);
			
			} while (Pacman.mapa.ladrilhos[x/32][y/32] != null); 
			
			direcao = aleatorio.nextInt(4);
			tempo = 0;
		}
	}
	
	@Override
	public void render(Graphics graficos)
	{
		super.render(graficos, 80);
	}
}
