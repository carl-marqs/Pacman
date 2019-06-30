package sprites;

import java.awt.Graphics;
import java.util.Random;

import main.Pacman;

public class Fantasma_Prestigiador extends Fantasma
{
	private static final long serialVersionUID = 1L;
	
	private int tempo=0;
	private Random aleatorio= new Random();

	public Fantasma_Prestigiador(int x, int y)
	{ super(x,y); }
	
	@Override
	public void atualizar()
	// Calcula o comportamento do fantasma
	{
		super.atualizar();
		if (morto) return; // se está morto, não se mexer
		
		// A cada 3 segundos, escolher uma nova posição aleatória próxima, mas não muito, do jogador
		tempo++;
		if (tempo >= 60*3) // (60fps * 3s)
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
	// Diz qual o índice do seu sprite para a classe pai renderizar
	{ super.render(graficos, 80); }
}
