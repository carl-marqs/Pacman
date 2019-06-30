package sprites;

import main.Pacman;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Fantasma extends Rectangle {
	private static final long serialVersionUID = 1L;

	// Movimentação
	protected int velocidade = 1; // o fantasma anda 60 pixels a cada segundo (60fps * 1 pixel/segundo)
	protected int direcao = 0; // 0: cima | 1: direita | 2: baixo | 3: esquerda

	// Estado do fantasma
	protected int iMorto = 0;
	protected int tempoMorto = 60 * 10; // 10 segundos (60fps * 10s)
	public boolean morto = false;
	
	// Animação da sprite
	protected int iAnimacao = 0, tempoAnimacao = 15;
	protected int indiceAnimacao = 0;

	public Fantasma(int x, int y)
	// Criar novo quadrado 32x32px a partir de uma posição
	{ setBounds(x, y, 32, 32); }

	protected boolean podeMover(int x, int y)
	// Checamos se o próximo ladrilho está vazio para podermos mover
	{
		Rectangle colisao = new Rectangle(x, y, width, height); // retângulo invisível à frente do fantasma, para checar a colisão
		
		// Não se mover para fora do mapa
		if (x < 0 || y < 0 || x > Pacman.LARGURA - 32 || y > Pacman.ALTURA - 32)
			return false;

		for (int i = 0; i < Pacman.mapa.ladrilhos.length; i++)
			for (int j = 0; j < Pacman.mapa.ladrilhos[0].length; j++) // verifica cada posição do mapa...
				if (Pacman.mapa.ladrilhos[i][j] != null) // se possuir um ladrilho nessa posição...
					if (colisao.intersects(Pacman.mapa.ladrilhos[i][j])) // e se estiver colidindo com ele...
						return false; // não pode mover

		return true;
	}

	public void atualizar()
	// Calcula a lógica do fantasma
	{
		mexer();
		
		// Se estiver morto, checar se deve voltar ao normal
		if (morto)
		{
			iMorto++;

			if (iMorto >= tempoMorto)
			{
				morto = false;
				iMorto = 0; /* Retorna ao tempo inicial de animação */
			}
		}
	}
	
	// Cada tipo de fantasma deve mostrar como renderizar sua imagem
	public abstract void render(Graphics graficos);

	public void render(Graphics graficos, int y_sprite)
	{
		// Se estiver vivo
		if (!morto)
			if (direcao == 0)
				if (indiceAnimacao % 2 == 0)
					graficos.drawImage(Pacman.malha.cortar(66, y_sprite), x, y, width, height, null);
				else
					graficos.drawImage(Pacman.malha.cortar(82, y_sprite), x, y, width, height, null);

			else if (direcao == 1)
				if (indiceAnimacao % 2 == 0)
					graficos.drawImage(Pacman.malha.cortar(2, y_sprite), x, y, width, height, null);
				else
					graficos.drawImage(Pacman.malha.cortar(18, y_sprite), x, y, width, height, null);

			else if (direcao == 2)
				if (indiceAnimacao % 2 == 0)
					graficos.drawImage(Pacman.malha.cortar(98, y_sprite), x, y, width, height, null);
				else
					graficos.drawImage(Pacman.malha.cortar(114, y_sprite), x, y, width, height, null);
			else if (indiceAnimacao % 2 == 0)
				graficos.drawImage(Pacman.malha.cortar(34, y_sprite), x, y, width, height, null);
			else
				graficos.drawImage(Pacman.malha.cortar(50, y_sprite), x, y, width, height, null);
			
		// Se estiver morto
		else if (indiceAnimacao % 4 == 0)
			graficos.drawImage(Pacman.malha.cortar(130, 64), x, y, width, height, null);
		else if (indiceAnimacao % 4 == 1)
			graficos.drawImage(Pacman.malha.cortar(146, 64), x, y, width, height, null);
		else if (indiceAnimacao % 4 == 2)
			graficos.drawImage(Pacman.malha.cortar(162, 64), x, y, width, height, null);
		else
			graficos.drawImage(Pacman.malha.cortar(178, 64), x, y, width, height, null);
	}
	
	private void mexer()
	// Mudar a imagem da animação do sprite
	{
		// Incrementa o tempo de exibição da imagem atual
		iAnimacao++;
		
		// Se tiver sido exibida tempo suficiente, mudar para a próxima imagem
		if (iAnimacao >= tempoAnimacao)
		{
			iAnimacao = 0;
			indiceAnimacao++;
		}
	}
}
