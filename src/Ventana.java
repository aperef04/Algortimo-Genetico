
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Ventana extends JFrame {

	/**
	 * Constructor de Ventana Si tipoGrafica es 0, se creará la ventana de las
	 * aptitudes de la población Si tipoGrafica es 1, se creará la ventana de las
	 * aptitudes del mejor cromosoma
	 * 
	 * @param aptitudes
	 * @param tipoGrafica
	 */
	public Ventana(ArrayList<Integer> aptitudes, ArrayList<Float> medias ) {

		initAptitudesPoblacion(aptitudes,medias);

	}
	
	private void initAptitudesPoblacion(ArrayList aptitudesPoblacion, ArrayList medias) {
		 DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < aptitudesPoblacion.size()-1; i++) {
			dataset.addValue((int) aptitudesPoblacion.get(i), "aptitud", "" + i);
			dataset.addValue((float) medias.get(i), "medias", "" + i);
		}

		
		JFreeChart chart = ChartFactory.createLineChart("Evolucion aptitudes", "Generacion", "Aptitud",
				dataset, PlotOrientation.VERTICAL, true, true, false);

		ChartPanel panel = new ChartPanel(chart);
		JFrame ventana = new JFrame("Aptitudes de la Población");
		ventana.getContentPane().add(panel);
		ventana.pack();
		ventana.setVisible(true);
		ventana.setSize(800, 600);
		ventana.setLocationRelativeTo(null);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}


}