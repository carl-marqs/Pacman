import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Fantasma extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	public Random aleatorio;
	
	private int velocidade = 2;
	
	private int modo=0; // 0: aleatorio | 1: inteligente
	private int direcao=0; // 0: cima | 1: direita | 2: baixo | 3: esquerda

	public Fantasma(int x, int y)
	{
		aleatorio = new Random();
		
		setBounds(x,y, 32,32);
		direcao = aleatorio.nextInt(4);
	}
	
	private boolean podeMover(int x, int y)
	// Checamos se o próximo ladrilho está vazio para podermos mover
	{
		Rectangle colisao = new Rectangle(x,y, width,height); // Retângulo invisível à frente do fantasma, para checar a colisão
		
		for (int i=0; i < Pacman.mapa.ladrilhos.length; i++)
			for (int j=0; j < Pacman.mapa.ladrilhos[0].length; j++)
				if (Pacman.mapa.ladrilhos[i][j] != null)
					if (colisao.intersects(Pacman.mapa.ladrilhos[i][j]))
						return false; // Se colidir com algum dos ladrilhos do mapa, o fantasma não pode mover
		
		return true;
	}
	
	public void tick()
	{
		if (modo == 0) // Se está no modo aleatório...
		{
			if (direcao == 0)
			{
				if (podeMover(x,y-velocidade))
					y -= velocidade;
				else
					direcao = aleatorio.nextInt(4);
				
			} else if (direcao == 1)
			{
				if (podeMover(x+velocidade,y))
					x += velocidade;
				else
					direcao = aleatorio.nextInt(4);
				
			} else if (direcao == 2)
			{
				if (podeMover(x,y+velocidade))
					y += velocidade;
				else
					direcao = aleatorio.nextInt(4);
				
			} else if (direcao == 3)
			{
				if (podeMover(x-velocidade,y))
					x -= velocidade;
				else
					direcao = aleatorio.nextInt(4);
			}
			
		} else
		{
			
		}
	}
	
	public void render(Graphics graficos)
	{
		graficos.drawImage(Pacman.malha.getSprite(2,64), x,y, width,height, null);
	}
}
