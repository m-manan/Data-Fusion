package main.java.datafusion.load;

import main.java.datafusion.datastructures.*;

public class LoadStocks extends LoadTSVFile {

	public static String[] names = { "Percent Change", "Last Trading Price", "Open Price", "Change $", "Volume",
			"Today's High", "Today's Low", "Previous Close", "52wk High", "52wk Low", "Shares Outstanding", "P/E",
			"Market Cap", "Yield", "Dividend", "EPS" };
	// similarity
	public static AttributeDataType[] dataTypes = { AttributeDataType.FLOAT, AttributeDataType.FLOAT,
			AttributeDataType.FLOAT, AttributeDataType.FLOAT, AttributeDataType.FLOAT, AttributeDataType.FLOAT,
			AttributeDataType.FLOAT, AttributeDataType.FLOAT, AttributeDataType.FLOAT, AttributeDataType.FLOAT,
			AttributeDataType.FLOAT, AttributeDataType.FLOAT, AttributeDataType.FLOAT, AttributeDataType.FLOAT,
			AttributeDataType.FLOAT, AttributeDataType.FLOAT };
	// evaluation
	public static AttributeType[] types = { AttributeType.CATEGORICAL, AttributeType.CATEGORICAL,
			AttributeType.CATEGORICAL, AttributeType.CATEGORICAL, AttributeType.CONTINUOUS, AttributeType.CATEGORICAL,
			AttributeType.CATEGORICAL, AttributeType.CATEGORICAL, AttributeType.CATEGORICAL, AttributeType.CATEGORICAL,
			AttributeType.CONTINUOUS, AttributeType.CATEGORICAL, AttributeType.CONTINUOUS, AttributeType.CATEGORICAL,
			AttributeType.CATEGORICAL, AttributeType.CATEGORICAL };

	public LoadStocks() {
		super(names, dataTypes, types);
	}
}
