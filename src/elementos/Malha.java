package elementos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Malha
{
	public static BufferedImage[] animacao;
	public BufferedImage malha;
	
	public Malha(String caminho)
	{
		try
		{
			malha = ImageIO.read(getClass().getResource(caminho));
			
		} catch (IOException e) { e.printStackTrace(); }
		
		animacao = new BufferedImage[2];
		animacao[0] = getSprite(0,0);
		animacao[1] = getSprite(16,0);
	}
	
	public void rotacionar(int direcao)
	{
		if (direcao == 0)
		{
			animacao[0] = getSprite(2,32);
			animacao[1] = getSprite(18,32);
			
		} else if (direcao == 1)
		{
			animacao[0] = getSprite(0,0);
			animacao[1] = getSprite(16,0);
			
		} else if (direcao == 2)
		{
			animacao[0] = getSprite(2,48);
			animacao[1] = getSprite(18,48);
			
		} else
		{
			animacao[0] = getSprite(2,16);
			animacao[1] = getSprite(18,16);
		}
	}
	
	public BufferedImage getSprite(int x, int y)
	{
		return malha.getSubimage(x,y, 16,16);
	}
}
