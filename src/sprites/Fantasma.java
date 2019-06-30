package sprites;


import main.Pacman;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Fantasma extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	protected int velocidade=1;
	protected int direcao=0; // 0: cima | 1: direita | 2: baixo | 3: esquerda
	
	protected int iAnimacao=0, tempoAnimacao=15;
	protected int indiceAnimacao=0;
	
	protected int iMorto=0, tempoMorto=60*10;
	
	public boolean morto = false;

	public Fantasma(int x, int y)
	{
		setBounds(x,y, 32,32);
	}
	
	protected boolean podeMover(int x, int y)
	// Checamos se o próximo ladrilho está vazio para podermos mover
	{
		Rectangle colisao = new Rectangle(x,y, width,height); // Retângulo invisível à frente do fantasma, para checar a colisão
		
		if (x < 0 || y < 0 || x > Pacman.LARGURA - 32 || y > Pacman.ALTURA - 32)
			return false;
		
		for (int i=0; i < Pacman.mapa.ladrilhos.length; i++)
			for (int j=0; j < Pacman.mapa.ladrilhos[0].length; j++)
				if (Pacman.mapa.ladrilhos[i][j] != null)
					if (colisao.intersects(Pacman.mapa.ladrilhos[i][j]))
							return false; // Se colidir com algum dos ladrilhos do mapa, o fantasma não pode mover
		
		return true;
	}
	
	public void tick()
	{
		if (morto)
		{
			iMorto++;
			
			if (iMorto >= tempoMorto)
			{
				morto = false;
				iMorto = 0; /*Retorna ao tempo inicial de animação*/
			}
		}
		
		iAnimacao++;
		if (iAnimacao >= tempoAnimacao)
		{
			iAnimacao = 0;
			indiceAnimacao++;
		}
	}
	
	public abstract void render(Graphics graficos);
	
	public void render(Graphics graficos, int y_sprite)
	{
		if (!morto)
		{
			if (direcao == 0)
				if (indiceAnimacao % 2 == 0)
					graficos.drawImage(Pacman.malha.getSprite(66,y_sprite), x,y, width,height, null);
				else
					graficos.drawImage(Pacman.malha.getSprite(82,y_sprite), x,y, width,height, null);
		
			else if (direcao == 1)
				if (indiceAnimacao % 2 == 0)
					graficos.drawImage(Pacman.malha.getSprite(2,y_sprite), x,y, width,height, null);
				else
					graficos.drawImage(Pacman.malha.getSprite(18,y_sprite), x,y, width,height, null);
				
			else if (direcao == 2)
				if (indiceAnimacao % 2 == 0)
					graficos.drawImage(Pacman.malha.getSprite(98,y_sprite), x,y, width,height, null);
				else
					graficos.drawImage(Pacman.malha.getSprite(114,y_sprite), x,y, width,height, null);
			else
				if (indiceAnimacao % 2 == 0)
					graficos.drawImage(Pacman.malha.getSprite(34,y_sprite), x,y, width,height, null);
				else
					graficos.drawImage(Pacman.malha.getSprite(50,y_sprite), x,y, width,height, null);
		
		} else
			if (indiceAnimacao % 4 == 0)
				graficos.drawImage(Pacman.malha.getSprite(130,64), x,y, width,height, null);
			else if (indiceAnimacao % 4 == 1)
				graficos.drawImage(Pacman.malha.getSprite(146,64), x,y, width,height, null);
			else if (indiceAnimacao % 4 == 2)
				graficos.drawImage(Pacman.malha.getSprite(162,64), x,y, width,height, null);
			else
				graficos.drawImage(Pacman.malha.getSprite(178,64), x,y, width,height, null);
	}
}
