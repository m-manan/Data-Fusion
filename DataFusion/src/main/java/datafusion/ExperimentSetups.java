package main.java.datafusion;

import main.java.datafusion.datastructures.Algorithm;
import main.java.datafusion.load.LoadStocks;

import java.io.File;

public class ExperimentSetups {

	public static Experiment getStockExperiment(Algorithm algorithm, File inputFile,
			File goldFile, File outputDir) {
		return new Experiment(algorithm, new LoadStocks(), inputFile, goldFile, outputDir);
	}

}
