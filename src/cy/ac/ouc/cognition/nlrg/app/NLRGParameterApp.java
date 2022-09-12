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
	public static String NLRGPipeline_PredicatesFile = "Predicates.txt";
	public static String NLRGPipeline_ContextFile = "Context.txt";

	
	public static void Load(String settingsFile) {

		Initialize(settingsFile);
		
		outln(NLRGTrace.TraceLevel.IMPORTANT, "\nSetting NLRG App parameters...");

		NLRGThing_LineSeperator = ReadParameterTrace("LineSeperator", System.getProperty("line.separator"));
		NLRGPipeline_ParseDataFile = ReadParameterTrace("ParseDataFile", "ParseData.txt");
		NLRGPipeline_PredicatesFile = ReadParameterTrace("PredicatesFile", "Predicates.txt");
		NLRGPipeline_ContextFile = ReadParameterTrace("ContextFile", "Context.txt");
		
		outln(NLRGTrace.TraceLevel.IMPORTANT, "NLRG App Parameters Set!");
	}

}
