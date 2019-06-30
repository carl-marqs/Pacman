package elementos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Malha
{
	public static BufferedImage[] jogador;
	public BufferedImage malha;
	
	public Malha(String caminho)
	// Define uma nova malha de sprites a partir do caminho de um arquivo
	{
		// Ler o arquivo como imagem
		try
		{ malha = ImageIO.read(getClass().getResource(caminho)); }
		catch (IOException e) { e.printStackTrace(); }
		
		// Inicializar cada imagem da animação do jogador
		jogador = new BufferedImage[2];
		jogador[0] = cortar(0,0);
		jogador[1] = cortar(16,0);
	}
	
	public void girarJogador(int direcao)
	// Girar a sprite do jogador dada sua direção
	{
		if (direcao == 0) // cima
		{
			jogador[0] = cortar(2,32);
			jogador[1] = cortar(18,32);
			
		} else if (direcao == 1) // direita
		{
			jogador[0] = cortar(0,0);
			jogador[1] = cortar(16,0);
			
		} else if (direcao == 2) // baixo
		{
			jogador[0] = cortar(2,48);
			jogador[1] = cortar(18,48);
			
		} else // esquerda
		{
			jogador[0] = cortar(2,16);
			jogador[1] = cortar(18,16);
		}
	}
	
	public BufferedImage cortar(int x, int y)
	// Corta um quadrado de 16px a partir da posição inicial
	{ return malha.getSubimage(x,y, 16,16); }
}
