package main.java.datafusion.model;

import main.java.datafusion.datastructures.*;

import java.util.ArrayList;
import java.util.Set;

public class Voting extends Algorithm {

	public Voting() {
		super("Voting");
	}

	@Override
	public ArrayList<Result> execute(RecordCollection recordCollection) {
		// Print an info message about the algorithm
		System.out.println(infoString(recordCollection));
		Set<Entity> entities = recordCollection.getEntities();
		ArrayList<Result> resultingTable = new ArrayList<Result>(entities.size());
		for (Entity e : entities) {
			Result result = new Result(source, e);
			Set<String> attributeNames = recordCollection.getAttributes(e);
			ArrayList<Record> recordsForE = recordCollection.getRecords(e);
			for (String attrName : attributeNames) {
				result.addAttribute(getMajorityVote(getCount(recordsForE, attrName)));
			}
			resultingTable.add(result);
		}
		System.out.println("\nDone running algorithm.");
		return resultingTable;
	}
}
