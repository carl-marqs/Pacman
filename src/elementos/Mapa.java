package elementos;

import sprites.Ladrilho;
import sprites.Pastilha;
import main.Pacman;
import sprites.Fantasma;
import sprites.Fantasma_Aleatorio;
import sprites.Fantasma_Evasivo;
import sprites.Fantasma_Perseguidor;
import sprites.Fantasma_Prestigiador;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
public class Mapa
{
	public int largura, altura;
	public Ladrilho[][] ladrilhos;
	public List<Pastilha> pastilhas;
	public List<Fantasma> fantasmas;
	
	public Mapa()
	// Construir um novo mapa aleatoriamente
	{
		Random aleatorio = new Random(); // cria nova variável para aleatorização
		
		// Definir dimensões do mapa
		largura = 37;
		altura = 19;
		
		// Inicializar listas e vetores
		pastilhas = new ArrayList<>();
		fantasmas = new ArrayList<>();
		ladrilhos = new Ladrilho[largura][altura];
		
		// Escolher as paredes (ladrilhos) e pastilhas aleatoriamente
		for (int x=0; x < largura; x++)
			for (int y=0; y < altura; y++)
			{
				if ( !( (x == 0 || x == largura-1) && (y == 0 || y == altura-1) ) && // preencher as quinas
					 ( (x == 0 || x == largura/2 || x == largura-1) &&
					   (y == 0 || y == altura/2 || y == altura-1) ) || // fazer as 4 aberturas laterais
					 ( (y == altura-2 || y == 1) && (x != 0 && x != largura-1)  ) ||
					 ( (x == largura-2 || x == 1) && (y != 0 && y != altura-1) )  ) // colocar pastilhas nas posições adjacentes aos cantos
				{
					pastilhas.add(new Pastilha(x*32,y*32, false));
					continue;
				}
				
				// Preencher os cantos
				if (x == 0 || y == 0 || x == largura-1 || y == altura-1)
					ladrilhos[x][y] = new Ladrilho(x*32, y*32);
				
				else
					// Colocar paredes (ladrilhos) intercalando linhas
					if (y % 2 == 0)
					{
						// 1/4 de chance de não conter uma parede
						if (aleatorio.nextInt(4) % 4 == 0)
							pastilhas.add(new Pastilha(x*32,y*32, false));
						else
							ladrilhos[x][y] = new Ladrilho(x*32, y*32);
					
					} else
						pastilhas.add(new Pastilha(x*32,y*32, false));
			}

		// Escolher posições aleatórias para os fantasmas
		for(int contFantasma=0; contFantasma < 4; contFantasma++)
		{
			// Escolher uma posição aleatória
			int x,y;
			do
			{
				x = aleatorio.nextInt(largura);
				y = aleatorio.nextInt(altura);
			
			} while (ladrilhos[x][y] != null); // repetir até não colidir com uma parede
			
			// Colocar todos os 4 tipos diferentes de fantasma
			if (contFantasma == 0)
				fantasmas.add(new Fantasma_Aleatorio(x*32,y*32));
			else if (contFantasma == 1)
				fantasmas.add(new Fantasma_Perseguidor(x*32,y*32));
			else if (contFantasma == 2)
				fantasmas.add(new Fantasma_Evasivo(x*32,y*32));
			else
				fantasmas.add(new Fantasma_Prestigiador(x*32,y*32));
		}
		
		// Escolher posições aleatórias para as pastilhas especiais
		for(int contPastilha=0; contPastilha < 6; contPastilha++)
		{
			// Escolher uma posição aleatória
			int x,y;
			do
			{
				x = aleatorio.nextInt(largura);
				y = aleatorio.nextInt(altura);
			
			} while (ladrilhos[x][y] != null); // repetir até não colidir com uma parede
			
			// Colocar a pastilha especial nessa posição
			pastilhas.add(new Pastilha(x*32,y*32, true));
		}
		
		// Escolher uma posição aleatória para o jogador
		int x,y;
		do
		{
			x = aleatorio.nextInt(largura);
			y = aleatorio.nextInt(altura);
		
		} while (ladrilhos[x][y] != null); // repetir até não colidir com uma parede
		
		// Colocar o jogador nessa posição
		Pacman.jogador.x = x*32;
		Pacman.jogador.y = y*32;
		
	}
	
	public Mapa(String caminho)
	// Construir o mapa a partir do caminho de um arquivo
	{
		// Inicializar as listas
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
			
			int contFantasma=0; // Indicar qual o tipo de fantasma inserir
			
			// Ler cada elemento do vetor pixels
			for (int x=0; x < largura; x++)
				for (int y=0; y < altura; y++)
				{
					int pixel = pixels[x+(y*largura)]; // Armazena a cor do pixel atual
					
					if (pixel == 0xFF000000) // Se o pixel for preto, é um ladrilho
					{
						ladrilhos[x][y] = new Ladrilho(x*32, y*32);
					
					} else if (pixel == 0xFF0000FF) // Se for azul, é onde o jogador deve nascer
					{
						Pacman.jogador.x = x*32;
						Pacman.jogador.y = y*32;
					
					} else if (pixel == 0xFFFF0000) // Se for vermelho, é um inimigo
					{
						pastilhas.add(new Pastilha(x*32,y*32, false));
						
						if (contFantasma == 0)
							fantasmas.add(new Fantasma_Aleatorio(x*32,y*32));
						else if (contFantasma == 1)
							fantasmas.add(new Fantasma_Perseguidor(x*32,y*32));
						else if (contFantasma == 2)
							fantasmas.add(new Fantasma_Evasivo(x*32,y*32));
						else
							fantasmas.add(new Fantasma_Prestigiador(x*32,y*32));
						
						contFantasma++;
						
					} else if (pixel == 0xFF00FF00) // Se for verde, é uma pastilha especial
					{
						pastilhas.add(new Pastilha(x*32,y*32, true));
						
					} else // Se não, é uma pastilha comum
					{
						pastilhas.add(new Pastilha(x*32,y*32, false));
					}
				}
			
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void atualizar()
	// Calcula a lógica do mapa
	{
		// Atualizar todos os fantasmas
		for (int i=0; i < fantasmas.size(); i++)
			fantasmas.get(i).atualizar();
	}
	
	public void renderizar(Graphics graficos)
	{
		// Renderizar os ladrilhos
		for (int x=0; x < largura; x++)
			for (int y=0; y < altura; y++)
				if (ladrilhos[x][y] != null)
					ladrilhos[x][y].render(graficos);
		
		// Renderizar as pastilhas
		for (int i=0; i < pastilhas.size(); i++)
			pastilhas.get(i).render(graficos);
		
		// Renderizar os fantasmas
		for (int i=0; i < fantasmas.size(); i++)
			fantasmas.get(i).render(graficos);
		
	}
}
