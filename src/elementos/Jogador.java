package elementos;

import main.Pacman;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Jogador extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	// Movimentação
	public boolean cima, direita, baixo, esquerda; // direções para qual o jogador quer se mover
	private int velocidade = 1; // o jogador anda 60 pixels a cada segundo (60fps * 1 pixel/segundo)
	
	// Vida e pontuação
	public int vidas = 3; // valor inicial
	public int pontuacao = 0; // valor inicial
	
	// Invencibilidade
	private int iInvencivel = -1; // contará a quanto tempo está invencível
	private int tempoInvencivel = 600; // 10 segundos (60fps * 10s)
	
	// Animação da sprite
	private int direcao = 3; // direção que está se movendo
	private int iAnimacao = 0; // contará quanto tempo está invencível
	private int tempoAnimacao = 10;
	public int indiceAnimacao = 0; // qual sprite da animação deve exibir

	public Jogador(int x, int y)
	// Criar novo quadrado 32x32px a partir de uma posição
	{ setBounds(x,y, 32,32); }
	
	public void atualizar()
	// Calcula a lógica do jogador
	{
		// Se estiver com menos de 0 vidas, encerrar o jogo
		if (vidas <= 0)
			Pacman.pausar();
		
		// Rotina do jogador
		andar();
		comer();
		mexer();
		
		// Se estiver invencível, checar se deve voltar ao normal
		if (iInvencivel >= 0)
		{
			iInvencivel++;
			
			if (iInvencivel > tempoInvencivel)
				iInvencivel = -1;
		}
	}
	
	public void renderizar(Graphics graficos)
	{	
		graficos.drawImage(Malha.jogador[indiceAnimacao%2], x,y, width,height, null);
	}
	
	private boolean podeMover(int x, int y)
	// Checamos se o próximo ladrilho está vazio para podermos mover
	{
		Rectangle colisao = new Rectangle(x,y, width,height); // Retângulo invisível à frente do player, para checar a colisão
		
		for (int i=0; i < Pacman.mapa.ladrilhos.length; i++)
			for (int j=0; j < Pacman.mapa.ladrilhos[0].length; j++) // verifica cada posição do mapa...
				if (Pacman.mapa.ladrilhos[i][j] != null) // se possuir um ladrilho nessa posição...
					if (colisao.intersects(Pacman.mapa.ladrilhos[i][j])) // e se estiver colidindo com ele...
						return false; // não pode mover
		
		return true;
	}
	
	private void andar()
	{
		// Checar para qual direção o jogador deve se deslocar
		if (cima && podeMover(x,y-velocidade))
		{
			y -= velocidade;
			direcao = 0;
			
			// Checa se está fora do mapa e precisa teleportar
			if (y <= -16)
				y = Pacman.ALTURA-16;
		}
		if (direita && podeMover(x+velocidade,y))
		{
			x += velocidade;
			direcao = 1;
			
			// Checa se está fora do mapa e precisa teleportar
			if (x >= Pacman.LARGURA-16)
				x = -16;
		}
		if (baixo && podeMover(x,y+velocidade))
		{
			y += velocidade;
			direcao = 2;
			
			// Checa se está fora do mapa e precisa teleportar
			if (y >= Pacman.ALTURA-16)
				y = -16;
		}
		if (esquerda && podeMover(x-velocidade,y))
		{
			x -= velocidade;
			direcao = 3;
			
			// Checa se está fora do mapa e precisa teleportar
			if (x <= -16)
				x = Pacman.LARGURA-16;
		}
		
		// Verifica se está colidindo com um fantasma
		for (int i=0; i < Pacman.mapa.fantasmas.size(); i++) // selecionar cada fantasma...
			if (Pacman.mapa.fantasmas.get(i).intersects(this)) // se estiverem colidindo...
			{
				if (vidas <= 0) // se
					Pacman.pausar();
					
				else if (!Pacman.mapa.fantasmas.get(i).morto)
				{
					if (iInvencivel == -1) // Se não está invencível...
					{
						vidas --;
						Pacman.tocarMusica("res/sons/pacman_death.wav");
					
					} else
					{
						ganharPontos(100);
						Pacman.tocarMusica("res/sons/pacman_eatghost.wav");
					}
					
					Pacman.mapa.fantasmas.get(i).morto = true;
				}
		}
		
		// Rotacionar sprite para a direção que está andando
		Pacman.malha.girarJogador(direcao);
	}
	
	private void comer()
	{
		for (int i=0; i < Pacman.mapa.pastilhas.size(); i++)
			if (intersects(Pacman.mapa.pastilhas.get(i))) // Se colidir com uma pastilha...
			{
				// Incrementar a pontuação dependendo do tipo da pastilha
				if (Pacman.mapa.pastilhas.get(i).especial == true)
				{
					ganharPontos(10);
					iInvencivel = 0; // entrar no modo invencível
					Pacman.tocarMusica("res/sons/pacman_eatfruit.wav");
				}
				else
				{
					ganharPontos(1);
					Pacman.tocarMusica("res/sons/pacman_chomp.wav");
				}
				
				Pacman.mapa.pastilhas.remove(i); // Tira a pastilha do mapa
				
				break;
			}
		
		if (Pacman.mapa.pastilhas.size() == 0) // Se o jogador comer todas as pastilhas, é vencedor
		{
			Pacman.pausar();
			Pacman.tocarMusica("res/sons/pacman_beginning.wav");
			return;
		}
	}
	
	private void mexer()
	// Mudar a imagem da animação do sprite
	{
		// A sprite ficará acelerada se estiver invencível
		if (iInvencivel == -1) // se não estiver invencível...
			iAnimacao++;
		else
			iAnimacao += 2;
		
		// Se tiver sido exibida tempo suficiente, mudar para a próxima imagem
		if (iAnimacao >= tempoAnimacao)
		{
			iAnimacao = 0;
			indiceAnimacao++;
		}
	}
	
	private void ganharPontos(int pontos)
	{
		pontuacao += pontos;
		
		// Checa se o jogador deve ganhar vida extra
		if (pontuacao % 10000 == 0)
		{
			vidas += 1;
			Pacman.tocarMusica("res/sons/pacman_extrapac.wav");
		}
	}
}
