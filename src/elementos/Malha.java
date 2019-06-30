package elementos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Malha
{
	public static BufferedImage[] jogador;
	public BufferedImage malha;
	
	public Malha(String caminho)
	{
		try
		{
			malha = ImageIO.read(getClass().getResource(caminho));
			
		} catch (IOException e) { e.printStackTrace(); }
		
		jogador = new BufferedImage[2];
		jogador[0] = getSprite(0,0);
		jogador[1] = getSprite(16,0);
	}
	
	public void girarJogador(int direcao)
	{
		if (direcao == 0)
		{
			jogador[0] = getSprite(2,32);
			jogador[1] = getSprite(18,32);
			
		} else if (direcao == 1)
		{
			jogador[0] = getSprite(0,0);
			jogador[1] = getSprite(16,0);
			
		} else if (direcao == 2)
		{
			jogador[0] = getSprite(2,48);
			jogador[1] = getSprite(18,48);
			
		} else
		{
			jogador[0] = getSprite(2,16);
			jogador[1] = getSprite(18,16);
		}
	}
	
	public BufferedImage getSprite(int x, int y)
	{
		return malha.getSubimage(x,y, 16,16);
	}
}
