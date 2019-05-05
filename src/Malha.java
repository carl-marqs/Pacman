import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Malha
{
	private BufferedImage malha;
	
	public Malha(String caminho)
	{
		try
		{
			malha = ImageIO.read(getClass().getResource(caminho));
			
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public BufferedImage getSprite(int x, int y)
	{
		return malha.getSubimage(x,y, 16,16);
	}
}
