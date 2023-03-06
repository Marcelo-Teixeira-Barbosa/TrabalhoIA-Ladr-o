package algoritmo;

import java.util.ArrayList;
import java.util.List;
public class Ladrao extends ProgramaLadrao {
	public static int[] exp = new int[] {0,0,0,0};
	
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
	
	
	
	
//	int[] up = new int[]{0,1,2,3,4,5,6,7,8,9};
//	int[] down = new int[]{14,15,16,17,18,19,20,21,22,23};
//	int[] right = new int[]{3,4,8,9,12,13,17,18,22,23};
//	int[] left = new int[]{0,1,5,6,10,11,14,15,19,20};
//	int[] outcircle = new int[]{0,1,2,3,4,5,9,10,13,14,18,19,20,21,22,23};
//	int[] innercircle = new int[] {6,7,8,11,12,15,16,17};
//	
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
	
	public int stealProtocol() {
		// formar o algoritimo para randomizar o movimento;
		// movValue index:
//		0 == up
//		1 == down
//		2 == right
//		3 == left
		
		int[] movValue = new int[]{0,0,0,0};
		
		
		int[] vision = this.sensor.getVisaoIdentificacao();
		for (int x=0; x < vision.length; x++) {
			if(vision[x]>=100 && vision[x]<200) {
				if(upList.contains(x)) {
					movValue[0]+=5;
					if(innercircle.contains(x)) {
						movValue[0]+=15;
						}
				}
				if(downList.contains(x)) {
					movValue[1]+=5;
					if(innercircle.contains(x)) {
						movValue[1]+=15;
						}
				}
				if(rightList.contains(x)) {
					movValue[2]+=5;
					if(innercircle.contains(x)) {
						movValue[2]+=15;
						}
				}
				if(downList.contains(x)) {
					movValue[3]+=5;
					if(innercircle.contains(x)) {
						movValue[3]+=15;
						}
				}
			}
				return stealProtocol();
			}
		
		// checar se o ladrão que eu vi esta indo para essa direção.
		return (int) (Math.random() * 5);
	}
	
	public int searchProtocol() {
		int[] vision = this.sensor.getVisaoIdentificacao();
		for (int x=0;x < vision.length; x++) {
			if(vision[x]>=100 && vision[x]<200) {
				return stealProtocol();
			}else {
				return (int) (Math.random() * 5);
			}
		}
		
		return 0; 
	}
	
	public int acao() {
		return (int) (Math.random() * 5);
	}

}