package algoritmo;

public class Ladrao extends ProgramaLadrao {
	public static int[] exp = new int[] {0,0,0,0};
	int[] up = new int[]{0,1,2,3,4,5,6,7,8,9};
	int[] down = new int[]{14,15,16,17,18,19,20,21,22,23};
	int[] right = new int[]{3,4,8,9,12,13,17,18,22,23};
	int[] left = new int[]{0,1,5,6,10,11,14,15,19,20};
	
	
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
		int[] vision = this.sensor.getVisaoIdentificacao();
		
		// checar se o ladrão que eu vi esta indo para essa direção.
		return (int) (Math.random() * 5);
	}
	
	public int searchProtocol() {
		int[] vision = this.sensor.getVisaoIdentificacao();
		int isPoupador = 0;
		int validMoves[];
		for (int x=0;x < vision.length; x++) {
			if(vision[x]>=100 && vision[x]<200) {
				isPoupador++;
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