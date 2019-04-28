import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Jogador extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	public boolean cima, direita, baixo, esquerda;
	public int velocidade = 4;

	public Jogador(int x, int y)
	{
		setBounds(x, y, 32, 32); // Definir tamanho e posição do retângulo
	}
	
	public void tick()
	{
		// Movimentação do jogador
		if (cima) y -= velocidade;
		if (direita) x += velocidade;
		if (baixo) y += velocidade;
		if (esquerda) x -= velocidade;
	}
	
	public void render(Graphics graficos)
	{
		graficos.setColor(Color.yellow); // Define a cor do jogador
		graficos.fillRect(x, y, width, height); // Define onde renderizar o jogador
	}
}
