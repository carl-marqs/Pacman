package sprites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pastilha extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	public boolean especial; // indica se é uma pastilha especial

	public Pastilha(int x, int y, boolean especial)
	// Criar novo quadrado a partir de uma posição
	{
		this.especial = especial;
		
		// Define o tamanho do retângulo
		if (especial)
			setBounds(x+10,y+10, 12,12);
		else
			setBounds(x+12,y+12, 6,6);
	}
	
	public void render(Graphics graficos)
	{
		graficos.setColor(Color.WHITE);
		graficos.fillRect(x,y, width,height);
	}
}
