package elementos;

import sprites.Ladrilho;
import sprites.Pastilha;
import sprites.Pacman;
import sprites.Fantasma;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
public class Mapa
{
	public int largura, altura;
	public Ladrilho[][] ladrilhos;
	public List<Pastilha> pastilhas;
	public List<Fantasma> fantasmas;
	
	public Mapa(String caminho)
	{
		pastilhas = new ArrayList<>();
		fantasmas = new ArrayList<>();
		
		try
		{
			BufferedImage mapa = ImageIO.read(getClass().getResource(caminho)); // Ler a imagem do mapa
			largura = mapa.getWidth(); // Armazenar a largura da imagem
			altura = mapa.getHeight(); // Armazenar a altura da imagem
			int[] pixels = new int[largura*altura]; // Guarda as informações dos pixels da imagem
			
			ladrilhos = new Ladrilho[largura][altura]; // Inicializa o vetor com as dimensões da imagem
			mapa.getRGB(0,0, largura,altura, pixels, 0, largura); // Pega a cor de cada pixel
			
			// Ler cada elemento do vetor pixels
			for (int x=0; x < largura; x++)
			{
				for (int y=0; y < altura; y++)
				{
					int pixel = pixels[x+(y*largura)]; // Armazena a cor do pixel atual
					
					if (pixel == 0xFF000000) // Se o pixel for preto, é um ladrilho
					{
						ladrilhos[x][y] = new Ladrilho(x*32, y*32);
					
					} else if (pixel == 0xFF0000FF) // Se for azul, é onde o jogador deve nascer
					{
						// QUEBRA DE ENCAPSULAMENTO: variáveis públicas
						Pacman.jogador.x = x*32;
						Pacman.jogador.y = y*32;
					
					} else if (pixel == 0xFFFF0000) // Se for vermelho, é um inimigo
					{
						fantasmas.add(new Fantasma(x*32,y*32));
						pastilhas.add(new Pastilha(x*32,y*32));
						
					} else if (pixel == 0x00FF00) // Se for verde, é um bônus
					{
						
					} else // Se não, é uma pastilha
					{
						pastilhas.add(new Pastilha(x*32,y*32));
					}
				}
			}
			
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void tick()
	{
		for (int i=0; i < fantasmas.size(); i++)
			fantasmas.get(i).tick();
	}
	
	public void render(Graphics graficos)
	{
		// Renderizar os ladrilhos
		for (int x=0; x < largura; x++)
		{
			for (int y=0; y < altura; y++)
			{
				if (ladrilhos[x][y] != null)
					ladrilhos[x][y].render(graficos);
			}
		}
		
		// Renderizar as pastilhas
		for (int i=0; i < pastilhas.size(); i++)
			pastilhas.get(i).render(graficos);
		
		// Renderizar os fantasmas
		for (int i=0; i < fantasmas.size(); i++)
			fantasmas.get(i).render(graficos);
		
	}
}
