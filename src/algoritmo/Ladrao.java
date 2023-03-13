package algoritmo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
public class Ladrao extends ProgramaLadrao {
	public static int[] exp = new int[] {0,0,0,0};
	private int[][] mapMov = new int[30][30];
	private int[][] mapObject = new int[30][30];
	private Point position = new Point();
	
	private boolean jackPot = false;
	private int jackPotCount = 0;
	private int currentCoins = 0;
	
	private ArrayList<Integer> upList = new ArrayList<Integer>(
            List.of(0,1,2,3,4,5,6,7,8,9));
	private ArrayList<Integer> downList = new ArrayList<Integer>(
            List.of(14,15,16,17,18,19,20,21,22,23));
	private ArrayList<Integer> rightList = new ArrayList<Integer>(
            List.of(3,4,8,9,12,13,17,18,22,23));
	private ArrayList<Integer> leftList = new ArrayList<Integer>(
            List.of(0,1,5,6,10,11,14,15,19,20));
	private ArrayList<Integer> outcircle = new ArrayList<Integer>(
            List.of(0,1,2,3,4,5,9,10,13,14,18,19,20,21,22,23));
	private ArrayList<Integer> innercircle = new ArrayList<Integer>(
            List.of(6,7,8,11,12,15,16,17));
	
	
	private ArrayList<Integer> smellUpList = new ArrayList<Integer>(
            List.of(0, 1, 2));
	private ArrayList<Integer> smellDownList = new ArrayList<Integer>(
            List.of(5, 6, 7));
	private ArrayList<Integer> smellRightList = new ArrayList<Integer>(
            List.of(2, 4, 7));
	private ArrayList<Integer> smellLeftList = new ArrayList<Integer>(
            List.of(0, 3, 5));
	
	
	// Cada ladrão pode ter varios mapas e grafos na cabeça dele. Isso pode ajudar a ele tomar uma decisão aleatória 
	//inteligente.
	
	//Criar a função de busca primeiro. É importante que a função de busca seja utilizando um mapa que grava onde ele passou
	// e evite ficar andando em apenas um canto. Criar um grafo que de preferencia a locais que ele não passou.
	
	// Funções para adicionar :
	//0.1. Criar mapas mentais sobre o local que o jogo esta contecendo.
	//0.2. Criar grafo com movimentos ponderados ligando possiveis caminhos que ele pode ir.
	//1. Ir para o banco;
	//2. Ir para moedas;
	//3. Afastar do poupador roubado;
	
	/*	
	 * 
	 *  -2 			Sem visão do local
	 * 	-1 			Fora do ambiente do jogo
	 *   0 			Vazio
	 * 	 1 			Parede
	 * 	 3 			Banco
	 * 	 4 			Moeda
	 * 	 5 			Partilha do poder
	 *   100~199 	Poupador
	 *   200~299 	Ladão
	 * 
	 * */
	public int SearchPoupadorByVision() {
		// Retorna a direção mais favorável.
		int[] vision = sensor.getVisaoIdentificacao();
		int bestDirectionValue = 0;
		int bestDirection = 0;
		int[] directionValues = {0,0,0,0,0};
		//		0 == Random direction
		//		1 == up
		//		2 == down
		//		3 == right
		//		4 == left
		
		
		/* for para buscar o poupador e avaliar a jogada se vale a pena.*/
		
		for(int i = 0;i<vision.length;i++) {
			if(upList.contains(i)) {
				directionValues[1] = evaluateMove(vision[i], i);
			}else if(downList.contains(i)){
				directionValues[2] = evaluateMove(vision[i], i);
			}else if(rightList.contains(i)){
				directionValues[3] = evaluateMove(vision[i], i);
			}else if(leftList.contains(i)){
				directionValues[4] = evaluateMove(vision[i], i);
			}
		}
		//depois de checar os valores de ganho em cada direção, vamos separar a melhor
		// ainda falta fazer ele distinguir entre valores de igual peso.
		for(int i = 0; i < directionValues.length;i++) {
			if(directionValues[i]> bestDirectionValue) {
				bestDirectionValue = directionValues[i];
				bestDirection = i;
			}
		}
		return bestDirection;
	}
	public int evaluateMove(int object, int position) {
		/*Avalia o movimento com a ideia de que um poupador pode esta na região*/
		
		int value = 0;
		if(object>= 100 && object<200) {
			
			value += 1;
			if(innercircle.contains(position)) {
				value += 1;
			}
		}
		return value;
		
		
		
	}
	public int SearchPoupadorBySmell() {
		
		/*
		 * Tem de ser feito diferente da visão.
		 * Tendo em vista que a leitura dos valores do olfato significa que tal agente passou por la a "x" turnos:
		 * conseguimos deduzir que o poupador vai esta mais perto do valor mais baixo.
		 * Todavia, vale lembrar que o valor 0 significa sem cheiro.
		 * 
		 * Então tem de ser o valor mais baixo e diferente de 0;
		 *
		 * */
		
		int[] poupadorSmell = sensor.getAmbienteOlfatoPoupador();
		int bestDirectionValue = 0;
		int bestDirection = 0;
		int[] values = new int[5];
		//		0 == Random direction
		//		1 == up
		//		2 == down
		//		3 == right
		//		4 == left
		
		for(int i = 0; i< poupadorSmell.length;i++) {
			if(poupadorSmell[i]>=1) {
				if(smellUpList.contains(i)) {
					values[1] = compareSmellValues(values[1],poupadorSmell[i]);
				}else if(smellDownList.contains(i)) {
					values[2] = compareSmellValues(values[2],poupadorSmell[i]);
				}else if(smellRightList.contains(i)) {
					values[3] = compareSmellValues(values[3],poupadorSmell[i]);
				}else if(smellLeftList.contains(i)) {
					values[4] = compareSmellValues(values[4],poupadorSmell[i]);
				}
			}
		}
		
		for(int i = 0; i < values.length; i++) {
			if(values[i] != 0 && (values[i] < bestDirectionValue || bestDirectionValue == 0)) {
				bestDirectionValue = values[i];
				bestDirection = i;
			}
		}
		return bestDirection;
		
		
	}
	
	public int compareSmellValues(int lastValue, int newValue) {
	if(newValue < lastValue && newValue != 0 || lastValue == 0) {
		return newValue;
	}
	return lastValue;
		
	}
	public int searchProtocol() {
		return (int)(Math.random() * 5);
		
		
	}
	public int evaluateSearchMovement(int[]  vision, int directionValue){
		
		// para avaliar, eu preciso do ponto no mapa. Tendo em vista que pode ficar muito grande a quantidade de "if's" e
		// tendo em vista que eu so posso mover 1 casa, apesar da visão ser de duas casas em cada direção, eu so posso ir para
		// cima(7), baixo(16), direita(12) e esquerda(11).
		
		// Se tiver mais tempo, eu penso em outra forma de mapear avaliando o resto da visão
		
		int value = 0;
		Point mapCoordinatesOfMoviment= position;
		int houseDataCode = 0; // O que é encontrado na casa em questão
		
		
		if(directionValue == 1) {
			houseDataCode = vision[7];
			mapCoordinatesOfMoviment = new Point( position.x-1, position.y);
		
		}else if (directionValue == 2) {
			houseDataCode = vision[16];	
			mapCoordinatesOfMoviment = new Point( position.x+1, position.y);
		
		}else if (directionValue == 3) {
			houseDataCode = vision[12];
			mapCoordinatesOfMoviment = new Point( position.x, position.y+1);
		
		}else if (directionValue == 4) {
			houseDataCode = vision[11];
			mapCoordinatesOfMoviment = new Point( position.x, position.y-1);
		}
		
		value -= mapMov[mapCoordinatesOfMoviment.x][mapCoordinatesOfMoviment.y];
		
		if(houseDataCode == 0) {
			value+=2;
		}else if(houseDataCode == -2 || houseDataCode == -1 || houseDataCode == 1 || houseDataCode == 3 || houseDataCode == 4 ||houseDataCode == 5) {
			value-=2;
		}
		
		return value;
		/*	
		 * 
		 *  -2 			Sem visão do local
		 * 	-1 			Fora do ambiente do jogo
		 *   0 			Vazio
		 * 	 1 			Parede
		 * 	 3 			Banco
		 * 	 4 			Moeda
		 * 	 5 			Partilha do poder
		 *   100~199 	Poupador
		 *   200~299 	Ladão
		 * 
		 * */
	}

	public void mappingMoves(Point position) {
		mapMov[position.x][position.y]+=1;
	}
	public void VerifyJackPot() {
		if(currentCoins < sensor.getNumeroDeMoedas()) {
			System.out.println("ACHOU");
			currentCoins = sensor.getNumeroDeMoedas();
			jackPot = true;
		}
	}
	
	public int acao() {
		
		VerifyJackPot();
		
		mappingMoves(position);
		position = new Point(sensor.getPosicao().x, sensor.getPosicao().y);
		
		// backawayProtocol after stealing coins.
		
		if(jackPot) {
			System.out.println(jackPotCount);
			if(jackPotCount >= 3) {
				jackPot = false;
				jackPotCount = 0;
			}else {
				jackPotCount ++;
				
				return searchProtocol();
			}
		}
		
		/*Buscar o Poupador pelos sensores do agente*/
		int resolveByVision = SearchPoupadorByVision(); // Grava a direção que foi visto o poupador ou se não foi visto
		int resolveBySmell = SearchPoupadorBySmell();   // Grava a direção que o cheiro do poupador foi sentido por ultimo ou se não
		
		
		if(resolveByVision > 0) {
			//achou poupador com a visão
			return(resolveByVision);
		}else if(resolveBySmell >0) {
			// achou poupador com o olfato
			return(resolveBySmell);
		}else {
			// Não achou poupador
			return (searchProtocol());
		}
	}

}