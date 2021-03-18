import java.util.ArrayList;

public class Algoritmo {

	static int tamPoblacion = 6;
	static int numGenes = 4;
	static int minimos[] = new int[] { 1, 1, 1, 1 };
	static int maximos[] = new int[] { 156, 1576, 1476, 5976 };
	static double porMutacion = 0.1;
	static int totalIteracciones = 5;
	static int elitismo = 1;

	static boolean verCruzar = false;
	static boolean verMutar = false;
	static boolean verEvaluar = false;
	static boolean verSeleccionar = true;
	static boolean verPoblacionInicial = false;
	static boolean mutacionGen = false;

	static int[] mejor;
	static ArrayList<Integer> mejores = new ArrayList<Integer>();
	static ArrayList<Float> medias = new ArrayList<Float>();

	@SuppressWarnings("unused")
	public static void main(String[] args) {
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

	private static void crearPoblacionInicial(int[][] poblacion) {
		for (int i = 0; i < poblacion.length; i++) {
			for (int j = 0; j < (poblacion[i].length) - 1; j++) {
				int minimo = minimos[j];
				int maximo = maximos[j];
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
			int corte = (int) Math.floor(Math.random() * (numGenes - 1 - 0 + 1));
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
		int[] mejorPoblacion = obtnerMejor(poblacion);
		aptitudes = new int[aux.length];
		aptitudes[0] = aux[0][numGenes];

		for (int i = 1; i < aptitudes.length; i++) {
			aptitudes[i] = aux[i][numGenes] + aptitudes[i - 1];
		}

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
		for (int i = 0; i < elitismo; i++) {
			int numeroAleatorio = (int) (Math.random() * numGenes - 1);
			poblacion[numeroAleatorio] = mejorPoblacion.clone();
		}
		if (verSeleccionar)
			imprimirPoblacion(poblacion);
	}

	private static void mutacionCromosoma(int[][] poblacion) {
		for (int i = 0; i < poblacion.length; i++) {
			if ((Math.random()) < porMutacion) { // Se muta
				if (verMutar) {
					System.out.println("Se muta el cromosoma:");
					imprimeCromosoma(poblacion[i], false);
					int genMutado = (int) Math.floor(Math.random() * numGenes);
					poblacion[i][genMutado] = (int) Math
							.floor(Math.random() * (maximos[genMutado] - minimos[genMutado] + 1) + minimos[genMutado]);
					System.out.println("Se ha mutado el gen: " + (genMutado + 1));
					imprimeCromosoma(poblacion[i], false);
				} else {
					int genMutado = (int) Math.floor(Math.random() * numGenes);
					poblacion[i][genMutado] = (int) Math
							.floor(Math.random() * (maximos[genMutado] - minimos[genMutado] + 1) + minimos[genMutado]);
				}
			}
		}

	}

	private static void mutacionGen(int[][] poblacion) {
		for (int i = 0; i < poblacion.length; i++) {
			for (int j = 0; j < numGenes; j++) {
				if ((Math.random() + 100) < porMutacion) { // Se muta
					if (verMutar) {
						System.out.println("Se muta el cromosoma:");
						imprimeCromosoma(poblacion[i], false);
						int genMutado = j;
						poblacion[i][genMutado] = (int) Math.floor(
								Math.random() * (maximos[genMutado] - minimos[genMutado] + 1) + minimos[genMutado]);
						System.out.println("Se ha mutado el gen: " + (genMutado + 1));
						imprimeCromosoma(poblacion[i], false);
					} else {
						int genMutado = (int) Math.floor(Math.random() * numGenes);
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
	
	private static void obtenerAptitudMasterMind(int[] cromosoma) {
		
		
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
