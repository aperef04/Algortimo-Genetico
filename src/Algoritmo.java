import java.util.ArrayList;

public class Algoritmo {

	static int tamPoblacion = 16;
	static int colores = 9;
	static double porMutacion = 2;
	
	static int elitismo = 2;
	static int numGenes = 10;
	
	//Debe escribirse la solución, y el tamaño que sea igual que el numero de genes
	static int solucionMasterMind[] = new int [] {1, 2, 4, 4, 5, 8, 1, 8, 9, 7};
	
	static int minimos[] = new int[] { 1, 1, 1, 1, 1, 1};
	static int maximos[] = new int[] { 900, 471, 92, 985, 49, 529};
	static int totalIteracciones = 1000;
	
	//Parámetros a modificar para ver por terminal las diferentes funciones
	static boolean verCruzar = false;
	static boolean verMutar = false;
	static boolean verEvaluar = false;
	static boolean verSeleccionar = false;
	static boolean verPoblacionInicial = false;
	static boolean mutacionGen = false;
	static boolean masterMind = true;
	
	static int minimosMasterMind[];
	static int maximosMasterMind[];
	static int[] mejor;
	static ArrayList<Integer> mejores = new ArrayList<Integer>();
	static ArrayList<Float> medias = new ArrayList<Float>();

	public static void main(String[] args) {
		
		if(masterMind)
			algoritmoMasterMind();
		else
			algoritmoNormal();
	}



	private static void algoritmoNormal() {
		int poblacion[][];
		poblacion = new int[tamPoblacion][numGenes + 1];
		
		
		crearPoblacionInicial(poblacion);
		evaluar(poblacion);
		int numIteracciones = 0;

		while (numIteracciones < totalIteracciones) {

			seleccion(poblacion);
			cruzar(poblacion);
			if (mutacionGen)
				mutacionGen(poblacion);
			else
				mutacionCromosoma(poblacion);
			evaluar(poblacion);
			numIteracciones++;
		}
		System.out.println("El mejor es:");
		imprimeCromosoma(mejor, true);
		Ventana ventana = new Ventana(mejores, medias);
	}

	private static void algoritmoMasterMind() {
		if(solucionMasterMind.length != numGenes) {
			System.out.println("Comprueba la solución");
			throw new Error();
		}
		int poblacion[][];
		poblacion = new int[tamPoblacion][numGenes + 1];
		
		rellenarColores(); 
		int maximo = obtnerMaximoMasterMind(); 
		crearPoblacionInicial(poblacion);
		evaluarMasterMind(poblacion);
		int numIteracciones = 0;
		
		while (mejor[numGenes] != maximo) {
			seleccion(poblacion);
			cruzar(poblacion);
			if (mutacionGen)
				mutacionGen(poblacion);
			else
				mutacionCromosoma(poblacion);
			evaluarMasterMind(poblacion);
			numIteracciones++;
		}
		System.out.println("El mejor es:");
		imprimeCromosoma(mejor, true);
		System.out.println("Se ha obtnenido en: "+ numIteracciones);
		
	}
	
	/**
	 * Función que rellena los array de maximo y minimo para el master mind
	 */
	private static void rellenarColores() {
		minimosMasterMind = new int[numGenes];
		maximosMasterMind = new int[numGenes];
		for (int i = 0; i < numGenes; i++) {
			minimosMasterMind[i] = 1;
			maximosMasterMind[i] = colores;
		}
		
	}

	/**
	 * Se busca cual es la aptitud maximo que puede tener el cromosoma. Es decir acertar todas
	 * 
	 */

	private static int obtnerMaximoMasterMind() {
		int maximo =  0;
		for (int i = 1; i <= (numGenes-1); i++) {
			maximo+= i;
		}
		maximo += 2*numGenes;
		return maximo;
	}



	private static void crearPoblacionInicial(int[][] poblacion) {
		int minimo;
		int maximo;
		for (int i = 0; i < poblacion.length; i++) {
			for (int j = 0; j < (poblacion[i].length) - 1; j++) {
				if(masterMind) {
					minimo = minimosMasterMind[j];
					maximo = maximosMasterMind[j];
				}else {
					minimo = minimos[j];
					maximo = maximos[j];
				}
				//Se guarda en la posición un numero aleatorio entre el máximo y el minimo
				poblacion[i][j] = (int) Math.floor(Math.random() * (maximo - minimo + 1) + minimo);
			}
		}
		mejor = poblacion[0].clone();

		if (verPoblacionInicial)
			imprimirPoblacion(poblacion);
	}

	private static void evaluar(int[][] poblacion) {
		float media = 0;
		int[] mejorPoblacion;
		//Se recoore la población para obtener la aptitud de cada cromosoma
		//La función de aptitud es una suma de los genes
		for (int i = 0; i < poblacion.length; i++) {
			poblacion[i][numGenes] = 0;
			for (int j = 0; j < poblacion[i].length - 1; j++) {
				poblacion[i][numGenes] += poblacion[i][j];
			}
			media += poblacion[i][numGenes];
		}
		mejorPoblacion = obtnerMejor(poblacion);
		if (mejor[numGenes] < mejorPoblacion[numGenes])
			mejor = mejorPoblacion.clone();
		
		media = media / (float) poblacion.length;
		
		mejores.add(mejorPoblacion[numGenes]);
		medias.add(media);
		if (verEvaluar) {
			System.out.println("-------------Evaluacion--------------");
			imprimirPoblacion(poblacion);
		}

	}

	private static void cruzar(int[][] poblacion) {
		for (int i = 0; i < poblacion.length; i += 2) {
			int[] aux = poblacion[i + 1].clone();
			int corte = (int) Math.floor(Math.random() * (numGenes));
			if (verCruzar) {
				System.out.println("---------------Se van a cruzar----------------- con corte en " + corte);
				imprimeCromosoma(poblacion[i], false);
				imprimeCromosoma(poblacion[i + 1], false);
				for (int j = corte; j < numGenes; j++) {
					poblacion[i + 1][j] = poblacion[i][j];
					poblacion[i][j] = aux[j];
				}
				System.out.println("---------------Resultado del cruzamiento-----------------");
				imprimeCromosoma(poblacion[i], false);
				imprimeCromosoma(poblacion[i + 1], false);
			} else {
				for (int j = corte; j < numGenes; j++) {
					poblacion[i + 1][j] = poblacion[i][j];
					poblacion[i][j] = aux[j];
				}

			}

		}

	}

	private static void seleccion(int[][] poblacion) {
		if (verSeleccionar) {
			imprimirPoblacion(poblacion);
			System.out.println("-------------Seleccion--------------");
		}
		int aptitudes[];
		int[][] aux = poblacion.clone();
		//Se guarda al mejor de la población para el elitismo
		
		int[] mejorPoblacion = obtnerMejor(poblacion);
		
		//Array para la ruleta con pesos
		aptitudes = new int[aux.length];
		aptitudes[0] = aux[0][numGenes];

		for (int i = 1; i < aptitudes.length; i++) {
			aptitudes[i] = aux[i][numGenes] + aptitudes[i - 1];
		}
		//Ruleta con pesos
		for (int i = 0; i < aptitudes.length; i++) {
			int aleatorio = (int) Math.floor(Math.random() * (aptitudes[aux.length - 1] + 1));
			boolean encontrado = false;
			int j = 0;
			while (!encontrado) {
				if (aleatorio <= aptitudes[j]) {
					encontrado = true;
					poblacion[i] = aux[j].clone();
				} else {
					j++;
				}
			}
		}
		//Se introduce tantas veces como marque el elitismo al mejor de la población en lugares aleatorios
		for (int i = 0; i < elitismo; i++) {
			int numeroAleatorio = (int) (Math.random() * numGenes - 1);
			poblacion[numeroAleatorio] = mejorPoblacion.clone();
		}
		if (verSeleccionar)
			imprimirPoblacion(poblacion);
	}

	private static void mutacionCromosoma(int[][] poblacion) {
		for (int i = 0; i < poblacion.length; i++) {
			if (Math.floor(Math.random() * (100) ) < porMutacion) { // Se muta
				if (verMutar) {
					System.out.println("Se muta el cromosoma:");
					imprimeCromosoma(poblacion[i], false);
					int genMutado = (int) Math.floor(Math.random() * numGenes);
					if(masterMind)
						poblacion[i][genMutado] = (int) Math.floor(
								Math.random() * (maximosMasterMind[genMutado] - minimosMasterMind[genMutado] + 1) + minimosMasterMind[genMutado]);
					else
						poblacion[i][genMutado] = (int) Math.floor(
							Math.random() * (maximos[genMutado] - minimos[genMutado] + 1) + minimos[genMutado]);
					System.out.println("Se ha mutado el gen: " + (genMutado + 1));
					System.out.println("--------------------------");
					imprimeCromosoma(poblacion[i], false);
				} else {
					int genMutado = (int) Math.floor(Math.random() * numGenes);
					if(masterMind)
						poblacion[i][genMutado] = (int) Math.floor(
								Math.random() * (maximosMasterMind[genMutado] - minimosMasterMind[genMutado] + 1) + minimosMasterMind[genMutado]);
					else
						poblacion[i][genMutado] = (int) Math.floor(
							Math.random() * (maximos[genMutado] - minimos[genMutado] + 1) + minimos[genMutado]);
				}
			}
		}

	}

	private static void mutacionGen(int[][] poblacion) {
		for (int i = 0; i < poblacion.length; i++) {
			for (int j = 0; j < numGenes; j++) {
				if ((Math.random()) < porMutacion/numGenes) { // Se muta
					if (verMutar) {
						System.out.println("Se muta el cromosoma:");
						imprimeCromosoma(poblacion[i], false);
						int genMutado = j;
						if(masterMind)
							poblacion[i][genMutado] = (int) Math.floor(
									Math.random() * (maximosMasterMind[genMutado] - minimosMasterMind[genMutado] + 1) + minimosMasterMind[genMutado]);
						else
							poblacion[i][genMutado] = (int) Math.floor(
								Math.random() * (maximos[genMutado] - minimos[genMutado] + 1) + minimos[genMutado]);
						System.out.println("Se ha mutado el gen: " + (genMutado + 1));
						imprimeCromosoma(poblacion[i], false);
					} else {
						int genMutado = (int) Math.floor(Math.random() * numGenes);
						if(masterMind)
							poblacion[i][genMutado] = (int) Math.floor(
									Math.random() * (maximosMasterMind[genMutado] - minimosMasterMind[genMutado] + 1) + minimosMasterMind[genMutado]);
						else
							poblacion[i][genMutado] = (int) Math.floor(
								Math.random() * (maximos[genMutado] - minimos[genMutado] + 1) + minimos[genMutado]);
					}
				}
			}

		}
	}
	
	

	private static void evaluarMasterMind(int[][] poblacion) {
		float media = 0;
		int[] mejorPoblacion;
		for (int i = 0; i < poblacion.length; i++) {
			obtenerAptitudMasterMind(poblacion[i]);
			media += poblacion[i][numGenes];
		}
		if (verEvaluar) {
			System.out.println("-------------Evaluacion--------------");
			imprimirPoblacion(poblacion);
		}
		mejorPoblacion = obtnerMejor(poblacion);
		if (mejor[numGenes] < mejorPoblacion[numGenes])
			mejor = mejorPoblacion.clone();
		media = media / (float) poblacion.length;
		mejores.add(mejorPoblacion[numGenes]);
		medias.add(media);
		

	}
	/**
	 * Calcula la aptitud del cromosoma usando la fucnion vista en clase 
	 * @param cromosoma
	 */
	private static void obtenerAptitudMasterMind(int[] cromosoma) {
		int negras = 0;
		int blancas = 0;
		int aptitud = 0;
		int[] croAux = cromosoma.clone();
		int [] solAux = solucionMasterMind.clone();
		for (int i = 0; i < solAux.length; i++) {
			if(croAux[i] == solAux[i]) {
				negras++;
				croAux[i] = -1;
				solAux[i] = -1;
			}
		}
		for (int i = 0; i < croAux.length-1; i++) {
			for (int j = 0; j < solAux.length; j++) {
				if(croAux[i] != -1 && solAux[j] != -1) {
					if(croAux[i] == solAux[j]) {
						blancas++;
						croAux[i] = -1;
						solAux[j] = -1;
					}
				}
			}
		}
		for (int i = 1; i <= (blancas+negras-1); i++) {
			aptitud+= i;
		}
		aptitud += 2*negras+blancas;
		cromosoma[numGenes] = aptitud;
	}
	
	private static void imprimeCromosoma(int[] m, boolean evaluacion) {
		if (evaluacion) {
			for (int i = 0; i < m.length; i++) {
				System.out.print(" " + m[i]);
			}
			System.out.println();
		} else {
			for (int i = 0; i < m.length - 1; i++) {
				System.out.print(" " + m[i]);
			}
			System.out.println();
		}

	}

	private static int[] obtnerMejor(int[][] poblacion) {
		int[] mejor = poblacion[0].clone();
		for (int i = 0; i < poblacion.length; i++) {
			if (poblacion[i][numGenes] > mejor[numGenes])
				mejor = poblacion[i].clone();
		}
		return mejor;
	}

	private static void imprimirPoblacion(int[][] matriz) {
		System.out.println("---------------Poblacion-----------------");
		for (int x = 0; x < matriz.length; x++) {
			System.out.print("|");
			for (int y = 0; y < matriz[x].length; y++) {
				System.out.print(matriz[x][y]);
				if (y != matriz[x].length - 1)
					System.out.print("\t");
			}
			System.out.println("|");
		}
		System.out.println("------------------------------------------");
	}

}
