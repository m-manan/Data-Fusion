package main.java.datafusion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import main.java.datafusion.datastructures.RecordCollection;
import main.java.datafusion.datastructures.Source;
import main.java.datafusion.evaluation.SourceEvaluation;
import main.java.datafusion.load.LoadStocks;
import main.java.datafusion.load.LoadTSVFile;
/**
 * Main class for compute the performance measure
 * of each source.
 * Save result and performance in "output/".
 */
public class MainForInitialAnalysis {

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

	public static void main(String[] args) {
		LoadTSVFile load = new LoadStocks();
		
		String[] sourceName = new String[55];
		double[] precision = new double[55];
		double[] precisionCount = new double[55];
		double[] recall = new double[55];
		double[] fmeasure = new double[55];
		double[] errorRate = new double[55];
		double[] MAD = new double[55];
		double[] madCount = new double[55];
		double[] MNAD = new double[55];
		double[] mnadCount = new double[55];
		
		for(int i = 0; i < filesToTest.length; i++) {
			System.out.println("Evaluate: " + filesToTest[i]);
			
			File outfile = new File("data/output/sources/sources_perf_" + filesToTest[i]);
			outfile.getParentFile().mkdirs();
			
			RecordCollection dataset = load.load(new File("data/clean_stock/" + filesToTest[i]));
			RecordCollection gold = load.loadGold(new File("data/nasdaq_truth/" + nasdaqFiles[i]));
			
			// Get all sources
			Set<Source> sources = dataset.getSources();
			
			try {
				PrintWriter out = new PrintWriter(outfile, "UTF-8");
				out.println("Source\tPrecision\tRecall\tFmeasure\tErrorRate\tMAD\tMNAD");
				int j = 0;
				for (Source s : sources) {
					// Compute evaluation metrics
					SourceEvaluation se = new SourceEvaluation(dataset, gold, s);
					se.calcMetrics();
					se.calcErrorRate();
					se.calcMAD();
					se.calcMNAD();
					System.out.println("\n" + s);
					se.printResults();
					System.out.println();
					// Save result in TSV file
					StringBuilder sb = new StringBuilder(1000);
					sb.append(s.getName());	sb.append("\t");
					sb.append(se.getPrecision()); sb.append("\t");
					sb.append(se.getRecall()); sb.append("\t");
					sb.append(se.getFmeasure()); sb.append("\t");
					sb.append(se.getErrorRate()); sb.append("\t");
					sb.append(se.getMAD());	sb.append("\t");
					sb.append(se.getMNAD());
					out.println(sb.toString());
					// Compute mean of performance of each day
					sourceName[j] = s.getName();
					if(! Double.isNaN(se.getPrecision())) {
						precision[j] += se.getPrecision();
						precisionCount[j]++;
					}
					recall[j] += se.getRecall();
					fmeasure[j] += se.getFmeasure();
					errorRate[j] += se.getErrorRate();
					if(! Double.isNaN(se.getMAD())) {
						MAD[j] += se.getMAD();
						madCount[j]++;
					}
					if(! Double.isNaN(se.getMNAD())) {
						MNAD[j] += se.getMNAD();
						mnadCount[j]++;
					}				
					j++;
				}
				out.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		try {
			File outfile = new File("data/output/sources/source_mean_perf.txt");
			outfile.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(outfile, "UTF-8");
			out.println("Source\tPrecision\tRecall\tFmeasure\tErrorRate\tMAD\tMNAD");
			for(int i = 0; i < precision.length; i++) {
				precision[i] /= precisionCount[i];
				recall[i] /= filesToTest.length;
				fmeasure[i] /= filesToTest.length;
				errorRate[i] /= filesToTest.length;
				MAD[i] /= madCount[i];
				MNAD[i] /= mnadCount[i];
				// Save result in TSV file
				StringBuilder sb = new StringBuilder(1000);
				sb.append(sourceName[i]);	sb.append("\t");
				sb.append(precision[i]); sb.append("\t");
				sb.append(recall[i]); sb.append("\t");
				sb.append(fmeasure[i]); sb.append("\t");
				sb.append(errorRate[i]); sb.append("\t");
				sb.append(MAD[i]);	sb.append("\t");
				sb.append(MNAD[i]);
				out.println(sb.toString());
			}
			
			out.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		

	}
}
