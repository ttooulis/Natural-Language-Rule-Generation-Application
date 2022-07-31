package cy.ac.ouc.cognition.nlrg.app;

import static cy.ac.ouc.cognition.nlrg.lib.NLRGTrace.outln;

import cy.ac.ouc.cognition.nlrg.lib.NLRGParameter;
import cy.ac.ouc.cognition.nlrg.lib.NLRGTrace;

public class NLRGParameterApp extends NLRGParameter {

	/********************************
	 * NLRG App Parameters
	 ********************************/
	public static String NLRGThing_LineSeperator = System.getProperty("line.separator");
	public static String NLRGPipeline_ParseDataFile = "ParseData.txt";
	public static String NLRGPipeline_MetaPredicatesFile = "Predicates.txt";
	public static String NLRGPipeline_MetaQueryFile = "Query.txt";

	
	public static void Load(String settingsFile) {

		Initialize(settingsFile);
		
		outln(NLRGTrace.TraceLevel.IMPORTANT, "\nSetting NLRG App parameters...");

		NLRGThing_LineSeperator = ReadParameterTrace("LineSeperator", System.getProperty("line.separator"));
		NLRGPipeline_ParseDataFile = ReadParameterTrace("ParseDataFile", "ParseData.txt");
		NLRGPipeline_MetaPredicatesFile = ReadParameterTrace("MetaPredicatesFile", "Predicates.txt");
		NLRGPipeline_MetaQueryFile = ReadParameterTrace("MetaQueryFile", "Query.txt");
		
		outln(NLRGTrace.TraceLevel.IMPORTANT, "NLRG App Parameters Set!");
	}

}
