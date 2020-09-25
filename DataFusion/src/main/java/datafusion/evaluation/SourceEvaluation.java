package main.java.datafusion.evaluation;

import java.util.HashMap;

import main.java.datafusion.datastructures.Attribute;
import main.java.datafusion.datastructures.Entity;
import main.java.datafusion.datastructures.RecordCollection;
import main.java.datafusion.datastructures.Result;
import main.java.datafusion.datastructures.Source;

public class SourceEvaluation extends EvaluationMetrics {
	Source source;
	
	public SourceEvaluation(RecordCollection sourceRecords, RecordCollection collection, Source source) {
		super(sourceRecords, collection);
		this.source = source;
	}
	
	@Override
	protected HashMap<Entity, HashMap<String, Attribute>> getResultHash() {
		HashMap<Entity, HashMap<String, Attribute>> resultHash = new HashMap<Entity, HashMap<String, Attribute>>();
		for (Result r : resultRecords) {
			if(r.getSource().equals(source))
				resultHash.put(r.getEntity(), r.getAttributes());
		}
		return resultHash;
	}

}
