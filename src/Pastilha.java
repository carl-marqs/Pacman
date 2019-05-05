import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Pastilha extends Rectangle
{
	private static final long serialVersionUID = 1L;

	public Pastilha(int x, int y)
	{
		setBounds(x+12,y+12, 6,6);
	}
	
	public void render(Graphics graficos)
	{
		graficos.setColor(Color.WHITE);
		graficos.fillRect(x,y, width,height);
	}
}
