package sprites;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pastilha extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	public boolean especial;

	public Pastilha(int x, int y, boolean especial)
	{
		this.especial = especial;
		
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
