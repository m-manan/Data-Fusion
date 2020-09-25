package main.java.datafusion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Set;

import main.java.datafusion.datastructures.Source;
import main.java.datafusion.model.TruthFinder;
import main.java.datafusion.model.Voting;
/**
 * Main class for running the experiment with
 * TruthFinder and Voting algorithm on stocks data for every day.
 * Save result and performance in "output/".
 */
public class MainForExperiment {

	public static String[] filesToTest = { "stock-2011-07-01.txt", "stock-2011-07-04.txt", "stock-2011-07-05.txt",
			"stock-2011-07-06.txt", "stock-2011-07-07.txt", "stock-2011-07-08.txt", "stock-2011-07-11.txt",
			"stock-2011-07-12.txt", "stock-2011-07-13.txt", "stock-2011-07-14.txt", "stock-2011-07-15.txt",
			"stock-2011-07-18.txt", "stock-2011-07-19.txt", "stock-2011-07-20.txt", "stock-2011-07-21.txt",
			"stock-2011-07-22.txt", "stock-2011-07-25.txt", "stock-2011-07-26.txt", "stock-2011-07-27.txt",
			"stock-2011-07-28.txt", "stock-2011-07-29.txt" };

	public static String[] nasdaqFiles = { "stock-2011-07-01-nasdaq-com.txt", "stock-2011-07-04-nasdaq-com.txt",
			"stock-2011-07-05-nasdaq-com.txt", "stock-2011-07-06-nasdaq-com.txt", "stock-2011-07-07-nasdaq-com.txt",
			"stock-2011-07-08-nasdaq-com.txt", "stock-2011-07-11-nasdaq-com.txt", "stock-2011-07-12-nasdaq-com.txt",
			"stock-2011-07-13-nasdaq-com.txt", "stock-2011-07-14-nasdaq-com.txt", "stock-2011-07-15-nasdaq-com.txt",
			"stock-2011-07-18-nasdaq-com.txt", "stock-2011-07-19-nasdaq-com.txt", "stock-2011-07-20-nasdaq-com.txt",
			"stock-2011-07-21-nasdaq-com.txt", "stock-2011-07-22-nasdaq-com.txt", "stock-2011-07-25-nasdaq-com.txt",
			"stock-2011-07-26-nasdaq-com.txt", "stock-2011-07-27-nasdaq-com.txt", "stock-2011-07-28-nasdaq-com.txt",
			"stock-2011-07-29-nasdaq-com.txt" };
	
	public static String[] outdir =  {"2011-07-01", "2011-07-04", "2011-07-05",
			"2011-07-06", "2011-07-07", "2011-07-08", "2011-07-11",
			"2011-07-12", "2011-07-13", "2011-07-14", "2011-07-15",
			"2011-07-18", "2011-07-19", "2011-07-20", "2011-07-21",
			"2011-07-22", "2011-07-25", "2011-07-26", "2011-07-27",
			"2011-07-28", "2011-07-29" 
			
	};

	public static String path = "C:/Users/ponti/Desktop/workspace/data_integration/eclipse_workspace/DataFusion/data";

	public static void main(String[] args) {
		double precision = 0;
		double recall = 0;
		double fmeasure = 0;
		double errorRate = 0;
		double MAD = 0;
		double MNAD = 0;
		int mnadCount = 0;
		
		/*
		 * VOTING EXPERIMENT
		 */
		for (int i = 0; i < filesToTest.length; i++) {
			Experiment stocks = ExperimentSetups.getStockExperiment(new Voting(),
					new File(new File(path, "clean_stock"), filesToTest[i]),
					new File(new File(path, "nasdaq_truth"), nasdaqFiles[i]),
					new File(new File("data", "output/voting"), outdir[i]));
			stocks.run();
			precision += stocks.getPrecision();
			recall += stocks.getRecall();
			fmeasure += stocks.getFmeasure();
			errorRate += stocks.getErrorRate();
			MAD += stocks.getMad();
			if(! Double.isNaN(stocks.getMnad())) {
				MNAD += stocks.getMnad();
				mnadCount++;
			}
			System.out.println();
			stocks = null;
			System.gc();
		}
		
		precision /= filesToTest.length;
		recall /= filesToTest.length;
		fmeasure /= filesToTest.length;
		errorRate /= filesToTest.length;
		MAD /= filesToTest.length;
		MNAD /= mnadCount;
		
		// Save mean performance
		try {
			File outfile = new File("data/output/voting/voting_mean_perf.txt");
			outfile.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(outfile, "UTF-8");
			out.println("Algorithm\tPrecision\tRecall\tFmeasure\tErrorRate\tMAD\tMNAD");
			
			StringBuilder sb = new StringBuilder(1000);
			sb.append("Voting");	sb.append("\t");
			sb.append(precision); sb.append("\t");
			sb.append(recall); sb.append("\t");
			sb.append(fmeasure); sb.append("\t");
			sb.append(errorRate); sb.append("\t");
			sb.append(MAD);	sb.append("\t");
			sb.append(MNAD);
			out.println(sb.toString());
			out.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		precision = 0;
		recall = 0;
		fmeasure = 0;
		errorRate = 0;
		MAD = 0;
		MNAD = 0;
		mnadCount = 0;
		
		HashMap<Source, Double> sourceTrustWorthiness = new HashMap<Source, Double>();
		Set<Source> sources = null;
		/*
		 * TRUTH FINDER EXPERIMENT
		 */
		for (int i = 0; i < filesToTest.length; i++) {
			Experiment stocks = ExperimentSetups.getStockExperiment(new TruthFinder(),
					new File(new File(path, "clean_stock"), filesToTest[i]),
					new File(new File(path, "nasdaq_truth"), nasdaqFiles[i]),
					new File(new File("data", "output_test/truth_finder"), outdir[i]));
			stocks.run();
			// Compute trustworthiness source mean
			TruthFinder tf = (TruthFinder) stocks.getAlgorithm();
			sources = stocks.getSources();
			for(Source s : sources) {
				double tmp;
				if(i == 0)
					tmp = tf.getSourceTrustworthiness(s);
				else
					tmp = sourceTrustWorthiness.get(s) + tf.getSourceTrustworthiness(s);
				sourceTrustWorthiness.put(s, tmp);
			}
			precision += stocks.getPrecision();
			recall += stocks.getRecall();
			fmeasure += stocks.getFmeasure();
			errorRate += stocks.getErrorRate();
			MAD += stocks.getMad();
			if(! Double.isNaN(stocks.getMnad())) {
				MNAD += stocks.getMnad();
				mnadCount++;
			}
			System.out.println();
			stocks = null;
			System.gc();
		}
		
		precision /= filesToTest.length;
		recall /= filesToTest.length;
		fmeasure /= filesToTest.length;
		errorRate /= filesToTest.length;
		MAD /= filesToTest.length;
		MNAD /= filesToTest.length;
		
		for(Source s : sources) {
			double tmp = sourceTrustWorthiness.get(s) / filesToTest.length;
			sourceTrustWorthiness.put(s, tmp);
			System.out.println(s + "\tTRUST:\t" + tmp);
		}
		
		// Save mean performance
		try {
			File outfile = new File("data/output_test/truth_finder/tf_mean_perf.txt");
			outfile.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(outfile, "UTF-8");
			out.println("Algorithm\tPrecision\tRecall\tFmeasure\tErrorRate\tMAD\tMNAD");
			
			StringBuilder sb = new StringBuilder(1000);
			sb.append("TruthFinder");	sb.append("\t");
			sb.append(precision); sb.append("\t");
			sb.append(recall); sb.append("\t");
			sb.append(fmeasure); sb.append("\t");
			sb.append(errorRate); sb.append("\t");
			sb.append(MAD);	sb.append("\t");
			sb.append(MNAD);
			out.println(sb.toString());
			out.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}

}
