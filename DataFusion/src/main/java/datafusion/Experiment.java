package main.java.datafusion;

import main.java.datafusion.datastructures.Algorithm;
import main.java.datafusion.datastructures.RecordCollection;
import main.java.datafusion.datastructures.Result;
import main.java.datafusion.datastructures.Source;
import main.java.datafusion.evaluation.EvaluationMetrics;
import main.java.datafusion.load.LoadTSVFile;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

public class Experiment {

	protected Algorithm algorithm;
	protected RecordCollection inputData;
	protected RecordCollection gold;
	protected File outputDir;
	protected LoadTSVFile loader;

	private double precision;
	private double recall;
	private double errorRate;
	private double mad;
	private double mnad;

	public Experiment(Algorithm algorithm, LoadTSVFile loader, RecordCollection inputData,
			RecordCollection gold, File outputDir) {
		this.algorithm = algorithm;
		this.loader = loader;
		this.inputData = inputData;
		this.gold = gold;
		this.outputDir = outputDir;
	}

	public Experiment(Algorithm algorithm, LoadTSVFile loader, File inputFile,
			File goldFile, File outputDir) {
		this(algorithm, loader, loader.load(inputFile), loader.loadGold(goldFile), outputDir);
	}

	public void run() {
		// Run the algorithm
		ArrayList<Result> results = algorithm.execute(inputData);

		EvaluationMetrics evaluator = new EvaluationMetrics(results, gold);

		// Evaluate the data
		evaluator.calcMetrics();
		//evaluator.calcAccuracy();
		evaluator.calcErrorRate();
		evaluator.calcMAD();
		evaluator.calcMNAD();
		evaluator.printResults();
		precision = evaluator.getPrecision();
		recall = evaluator.getRecall();
		errorRate = evaluator.getErrorRate();
		mad = evaluator.getMAD();
		mnad = evaluator.getMNAD();

		// Write the output
		outputDir.mkdirs();
		RecordCollection resultsCollection = algorithm.convert(results);
		resultsCollection.writeToTSVFile(new File(outputDir, "output.tsv"), loader.getOrderedAttributeNames());
		writeScoreFile(outputDir, evaluator.resultsString());

	}

	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}
	
	public double getFmeasure() {
		return (2.0 * precision * recall / (precision + recall));
	}

	public double getErrorRate() {
		return errorRate;
	}

	public double getMad() {
		return mad;
	}

	public double getMnad() {
		return mnad;
	}
	
	public Algorithm getAlgorithm() {
		return algorithm;
	}
	
	public Set<Source> getSources() {
		return inputData.getSources();
	}

	public void writeScoreFile(File outputDir, String scoreString) {
		try {
			PrintWriter writer = new PrintWriter(new File(outputDir, "score.txt"));
			writer.println(scoreString);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
