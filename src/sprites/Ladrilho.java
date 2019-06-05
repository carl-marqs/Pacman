package sprites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Ladrilho extends Rectangle
{
	private static final long serialVersionUID = 1L;

	public Ladrilho(int x, int y)
	{
		setBounds(x,y, 32,32); // Define o tamanho e posição do retângulo
	}
	
	public void render(Graphics graficos)
	{
		graficos.setColor(new Color(0,0,255)); // Definir a cor de fundo
		graficos.fillRect(x,y, width,height); // Define onde renderizar o ladrilho
	}
}
