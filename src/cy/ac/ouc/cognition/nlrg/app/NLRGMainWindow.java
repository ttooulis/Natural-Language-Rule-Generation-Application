package cy.ac.ouc.cognition.nlrg.app;

//import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;

import cy.ac.ouc.cognition.nlrg.lib.NLRGParameterLib;
import cy.ac.ouc.cognition.nlrg.lib.NLRGPipeline;;

public class NLRGMainWindow {


	enum PipeLineState {
		NLP_NOT_LOADED,
		NLP_LOADED,
		NEW_INSTRUCTION,
		NL_PARSED,
		PREDICATES_GENERATED,
		CONTEXT_GENERATED,
		RULE_GENERATED
	}

	
	//private static final int NLP_NOT_LOADED = (-2), NLP_LOADED = (-1), NEW_INSTRUCTION = 0, NL_PARSED = 1, PREDICATES_GENERATED = 2, CONTEXT_GENERATED = 3, RULE_GENERATED = 4;
	private static int MKB_NOT_LOADED = 0, MKB_LOADED = 1;
	
    private static String ls = NLRGParameterApp.NLRGThing_LineSeperator;

	private static PipeLineState currentPipeLineState = PipeLineState.NLP_NOT_LOADED;
	private static int currentMetaKBState = MKB_NOT_LOADED;
	
	/* Controls definition */
	private static Text txtInstruction;
	private static Text txtGeneratedRule;

	private static Button btnMetaKBaseEdit;

	private static Button btnViewParseData;
	private static Button btnNlParseText;

	private static Button btnViewPredicates;
	private static Button btnGeneratePredicates;
	
	private static Button btnViewContext;
	private static Button btnGenerateContext;
	private static Button btnGenerateRule;
	
	private static Button btnAddRuleToKB;

	private static Label lblLblMessageArea;
	private static Label lblMetaKBStatus;

	protected Shell shlRuleGeneration;


	private static NLRGPipeline NLRGPipe;


	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {

			NLRGParameterApp.Load("NLRGApp.xml");
			NLRGPipe = new NLRGPipeline();

			NLRGMainWindow window = new NLRGMainWindow();

			window.createContents();

	    	LoadMetaKnowledgeBase();
			
	    	AdjustControlState(PipeLineState.NLP_NOT_LOADED);

	    	window.open();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}



	static void LoadMetaKnowledgeBase() {
		
		lblMetaKBStatus.setText("Loading...");
		try {
			int loadResult = NLRGPipe.loadMetaKnowledgeBase(null);
			if (loadResult != 0) {
				String mkbVersion = NLRGPipe.getMetaKBVersion(null);
		    	lblMetaKBStatus.setText(mkbVersion);
		    	System.out.println("Meta-Knowledge Base File (" + NLRGParameterLib.NLRGPipeline_MetaKnowledgeBaseFile + ") loaded!");
				System.out.println("Meta-Knowledge Base Version = [" + mkbVersion + "]");
		    	currentMetaKBState = MKB_LOADED;
			}
			else {
				System.err.println("Meta-Knowledge Base File (" + NLRGParameterLib.NLRGPipeline_MetaKnowledgeBaseFile + ") failed to load");
				lblMetaKBStatus.setText("Failed to Load");
		    	currentMetaKBState = MKB_NOT_LOADED;
			}
			
			// CID - Should have different state for NLP and instruction
			if (currentPipeLineState == PipeLineState.NLP_NOT_LOADED)
				AdjustControlState(PipeLineState.NLP_NOT_LOADED);	
			else if (txtInstruction.getText() != "")
				AdjustControlState(PipeLineState.NEW_INSTRUCTION);
			else
				AdjustControlState(PipeLineState.NLP_LOADED);
		}
		catch (Exception pe) {
			lblMetaKBStatus.setText("Failed to Load");
			System.err.println("Meta-Knowledge Base File (" + NLRGParameterLib.NLRGPipeline_MetaKnowledgeBaseFile + ") failed to load");
			System.err.println(pe.toString());
	    	currentMetaKBState = MKB_NOT_LOADED;

			// CID - Should have different state for NLP and instruction
			if (currentPipeLineState == PipeLineState.NLP_NOT_LOADED)
				AdjustControlState(PipeLineState.NLP_NOT_LOADED);	
			else
				AdjustControlState(PipeLineState.NLP_LOADED);
    	}
				
	}

	

	static void AdjustControlState(PipeLineState lPipeLineState) {
		
		switch (lPipeLineState) {
			case NLP_NOT_LOADED:
    			btnNlParseText.setEnabled(false);
    			btnViewParseData.setEnabled(false);
    			btnGeneratePredicates.setEnabled(false);
    			btnViewPredicates.setEnabled(false);
    			btnGenerateContext.setEnabled(false);
    			btnViewContext.setEnabled(false);
    			btnGenerateRule.setEnabled(false);
    			btnAddRuleToKB.setEnabled(false);
    			break;

			case NLP_LOADED:
			case NEW_INSTRUCTION:
    			btnNlParseText.setEnabled(true);
    			btnViewParseData.setEnabled(false);
    			btnGeneratePredicates.setEnabled(true);
    			btnViewPredicates.setEnabled(false);
    			btnGenerateContext.setEnabled(true);
    			btnViewContext.setEnabled(false);
    			btnGenerateRule.setEnabled(true);
    			btnAddRuleToKB.setEnabled(false);
    			break;

			case NL_PARSED:
    			btnNlParseText.setEnabled(false);
    			btnViewParseData.setEnabled(true);
    			btnGeneratePredicates.setEnabled(true);
    			btnViewPredicates.setEnabled(false);
    			btnGenerateContext.setEnabled(true);
    			btnViewContext.setEnabled(false);
    			btnGenerateRule.setEnabled(true);
    			btnAddRuleToKB.setEnabled(false);
    			break;

			case PREDICATES_GENERATED:
    			btnNlParseText.setEnabled(false);
    			btnViewParseData.setEnabled(true);
    			btnGeneratePredicates.setEnabled(false);
    			btnViewPredicates.setEnabled(true);
    			btnGenerateContext.setEnabled(true);
    			btnViewContext.setEnabled(false);
    			btnGenerateRule.setEnabled(true);
    			btnAddRuleToKB.setEnabled(false);
    			break;

			case CONTEXT_GENERATED:
    			btnNlParseText.setEnabled(false);
    			btnViewParseData.setEnabled(true);
    			btnGeneratePredicates.setEnabled(false);
    			btnViewPredicates.setEnabled(true);
    			btnGenerateContext.setEnabled(true);
    			btnViewContext.setEnabled(true);
    			btnGenerateRule.setEnabled(true);
    			btnAddRuleToKB.setEnabled(false);
    			break;

			case RULE_GENERATED:
    			btnNlParseText.setEnabled(false);
    			btnViewParseData.setEnabled(true);
    			btnGeneratePredicates.setEnabled(false);
    			btnViewPredicates.setEnabled(true);
    			btnGenerateContext.setEnabled(false);
    			btnViewContext.setEnabled(true);
    			btnGenerateRule.setEnabled(true);
    			btnAddRuleToKB.setEnabled(true);
    			break;

			default:
    			btnNlParseText.setEnabled(false);
    			btnViewParseData.setEnabled(false);
    			btnGeneratePredicates.setEnabled(false);
    			btnViewPredicates.setEnabled(false);
    			btnGenerateContext.setEnabled(false);
    			btnViewContext.setEnabled(false);
    			btnGenerateRule.setEnabled(false);
    			btnAddRuleToKB.setEnabled(false);
    			break;

		}

		currentPipeLineState = lPipeLineState;
	}


	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		shlRuleGeneration.open();
		shlRuleGeneration.layout();
		while (!shlRuleGeneration.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


	/**
	 * Create contents of the window.
	 */
	protected void createContents() {

		shlRuleGeneration = new Shell();
		shlRuleGeneration.setSize(700, 800);
		shlRuleGeneration.setText("Rule Generation from Natural Language");
    	shlRuleGeneration.setLayout(new FillLayout(SWT.VERTICAL));

    	Composite cmpStatus = new Composite(shlRuleGeneration, SWT.NONE);
    	cmpStatus.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    	Group grpNLP = new Group(cmpStatus, SWT.NONE);
    	grpNLP.setText("NLP");
    	grpNLP.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	Label lblNLPInfo = new Label(grpNLP, SWT.NONE);
    	lblNLPInfo.setAlignment(SWT.CENTER);
		lblNLPInfo.setText("Stanford Core NLP");
   	
    	Label lblNLPStatus = new Label(grpNLP, SWT.NONE);
    	
    	Button btnLoadNLP = new Button(grpNLP, SWT.NONE);
    	btnLoadNLP.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			try {
    				lblNLPStatus.setText("NLP Loading...");
    				System.gc();
    				NLRGPipe.loadNLProcessor();
    		    	AdjustControlState(PipeLineState.NLP_LOADED);
    				lblNLPStatus.setText("NLP Loaded.");
    			}
    			catch (Exception | OutOfMemoryError nlpe) {
    				System.gc();
    				lblNLPStatus.setText("NLP Failed to Load.");
    				System.out.println("NLP failed to load!");
    				System.out.println(nlpe.getMessage());
    		    	AdjustControlState(PipeLineState.NLP_NOT_LOADED);
    	    	}
    		}
    	});
    	btnLoadNLP.setText("Load NLP Library");
    	
    	Group grpMetaKBase = new Group(cmpStatus, SWT.NONE);
    	grpMetaKBase.setText("Meta-Knowledge Base");
    	grpMetaKBase.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	Label lblMetaKBInfo = new Label(grpMetaKBase, SWT.NONE);
    	lblMetaKBInfo.setAlignment(SWT.CENTER);
    	lblMetaKBInfo.setText(NLRGParameterLib.NLRGPipeline_MetaKnowledgeBaseFile);
 	
    	lblMetaKBStatus = new Label(grpMetaKBase, SWT.NONE);
    	lblMetaKBStatus.setAlignment(SWT.CENTER);
    	
    	Button btnMetaKBaseLoad = new Button(grpMetaKBase, SWT.NONE);
    	btnMetaKBaseLoad.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    	    	LoadMetaKnowledgeBase();
    		}
    	});
    	btnMetaKBaseLoad.setText("Load Meta-Knowledge Base");
    	
    	btnMetaKBaseEdit = new Button(grpMetaKBase, SWT.NONE);
    	btnMetaKBaseEdit.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {

    	        try {
    	        	Program.launch(NLRGParameterLib.NLRGPipeline_MetaKnowledgeBaseFile);

    	        } catch (Exception ple) {
		        	MessageBox mt = new MessageBox(shlRuleGeneration);
		        	mt.setMessage(ple.toString());
		        	mt.open();          
    	        }
    		}
    	});
    	btnMetaKBaseEdit.setText("Edit Meta-Knowledge Base");
    	
    	Group grpKBase = new Group(cmpStatus, SWT.NONE);
    	grpKBase.setText("Knowledge Base");
    	grpKBase.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	Label lblKBInfo = new Label(grpKBase, SWT.NONE);
    	lblKBInfo.setAlignment(SWT.CENTER);
    	lblKBInfo.setText( NLRGParameterLib.NLRGPipeline_KnowledgeBaseFile);
    	
    	Label lblKBStatus = new Label(grpKBase, SWT.NONE);
    	lblKBStatus.setText("");
    	
    	Button btnKBaseEdit = new Button(grpKBase, SWT.NONE);
    	btnKBaseEdit.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    	        try {
    	        	Program.launch( NLRGParameterLib.NLRGPipeline_KnowledgeBaseFile);

    	        } catch (Exception ple) {
		        	MessageBox mt = new MessageBox(shlRuleGeneration);
		        	mt.setMessage(ple.toString());
		        	mt.open();          
    	        }
    		}
    	});
    	btnKBaseEdit.setText("Edit Knowledge Base");
    	
    	Group grpInstructionText = new Group(shlRuleGeneration, SWT.NONE);
    	grpInstructionText.setText("Natural Language Policy");
    	grpInstructionText.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    	txtInstruction = new Text(grpInstructionText, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
    	txtInstruction.addModifyListener(new ModifyListener() {
    		public void modifyText(ModifyEvent arg0) {
	    		if (currentPipeLineState.ordinal() >= PipeLineState.NLP_LOADED.ordinal())
    		    	AdjustControlState(PipeLineState.NEW_INSTRUCTION);
	    		else
	    			AdjustControlState(currentPipeLineState);
    		}
    	});
    	
    	Composite cmpInstructionTextControls = new Composite(shlRuleGeneration, SWT.NONE);
    	cmpInstructionTextControls.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    	Group grpParseControls = new Group(cmpInstructionTextControls, SWT.NONE);
    	grpParseControls.setText("Text NL Parsing");
    	grpParseControls.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	btnViewParseData = new Button(grpParseControls, SWT.NONE);
    	btnViewParseData.setEnabled(false);
    	btnViewParseData.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    	        try
    	        {
    	        	if (currentPipeLineState.ordinal() >= PipeLineState.NL_PARSED.ordinal()) {
    	        		Files.write(Paths.get( NLRGParameterApp.NLRGPipeline_ParseDataFile), NLRGPipe.getParseData().getBytes());
    	        		Program.launch( NLRGParameterApp.NLRGPipeline_ParseDataFile);
    	        	}
    	        	else {
    		        	MessageBox m = new MessageBox(shlRuleGeneration);
    		        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot show parse data! (MSG005-1)");
    		        	m.open();            	
    	        	}
    	        }
    	        catch(Exception e2) {
		        	MessageBox m = new MessageBox(shlRuleGeneration);
		        	m.setMessage("Error: Cannot show parse data! (MSG005-2)" + ls + e2.getMessage());
		        	m.open();            	
     	        }
    		}
    	});
    	btnViewParseData.setText("View Parse Data");
    	
    	btnNlParseText = new Button(grpParseControls, SWT.NONE);
    	btnNlParseText.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			PipeLineState prevPipeLineState = currentPipeLineState;
    	        try
    	        {
    				txtGeneratedRule.setText("");
    	        	String instructionText = txtInstruction.getText();
    	        	if (	currentPipeLineState.ordinal() >= PipeLineState.NEW_INSTRUCTION.ordinal() &&
    	        			instructionText != null && instructionText != "") {
    	        		NLRGPipe.processNL(instructionText);
    	        		AdjustControlState(PipeLineState.NL_PARSED);
    	        	}
    	        	else if (currentPipeLineState.ordinal() < PipeLineState.NEW_INSTRUCTION.ordinal()) {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlRuleGeneration);
    		        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot parse! (MSG006-1)");
    		        	m.open();            	   	        		
    	        	}
    	        	else {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlRuleGeneration);
    		        	m.setMessage("No instructions provided: Cannot parse! (MSG006-2)");
    		        	m.open();            	
    	        	}
    	        }
    	        catch(Exception e2) {
	        		AdjustControlState(prevPipeLineState);
		        	MessageBox m = new MessageBox(shlRuleGeneration);
		        	m.setMessage("Error: Cannot parse! (MSG006-3)" + ls + e2.getMessage());
		        	m.open();            	
     	        }
    		}
    	});
    	btnNlParseText.setEnabled(false);
    	btnNlParseText.setText("NL Parse Text");
    	
    	@SuppressWarnings("unused")
		Label lblGrpParseFiller = new Label(grpParseControls, SWT.NONE);
    	
    	Group grpPredicatesControls = new Group(cmpInstructionTextControls, SWT.NONE);
    	grpPredicatesControls.setText("Text Predicates");
    	grpPredicatesControls.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	btnViewPredicates = new Button(grpPredicatesControls, SWT.NONE);
    	btnViewPredicates.setEnabled(false);
    	btnViewPredicates.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
        	    try {
        	        if (currentPipeLineState.ordinal() >= PipeLineState.PREDICATES_GENERATED.ordinal()) {

    	        		Files.write(Paths.get(NLRGParameterApp.NLRGPipeline_PredicatesFile), NLRGPipe.getMetaPredicateTextData().getBytes());
        	        	Program.launch(NLRGParameterApp.NLRGPipeline_PredicatesFile);
        	        }
        	        else {
        		        MessageBox m = new MessageBox(shlRuleGeneration);
        		        m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot show predicates! (MSG007-1)");
        		        m.open();            	
        	        }
        	    }
        	    catch(Exception e2) {
    		        MessageBox m = new MessageBox(shlRuleGeneration);
    		        m.setMessage("Error: Cannot show predicates! (MSG007-2)" + ls + e2.getMessage());
    		        m.open();            	
         	    }
    		}
    	});
    	btnViewPredicates.setText("View Predicates");
    	
    	btnGeneratePredicates = new Button(grpPredicatesControls, SWT.NONE);
    	btnGeneratePredicates.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			PipeLineState prevPipeLineState = currentPipeLineState;

    			try {
    				txtGeneratedRule.setText("");
    	        	String instructionText = txtInstruction.getText();

    	        	if (	currentPipeLineState.ordinal() >= PipeLineState.NEW_INSTRUCTION.ordinal() &&
    	        			instructionText != null && instructionText != "") {

    	        		if (currentPipeLineState.ordinal() < PipeLineState.NL_PARSED.ordinal())
        	        		NLRGPipe.processNL(instructionText);

	        			NLRGPipe.generateMetaPredicates();
    	        		AdjustControlState(PipeLineState.PREDICATES_GENERATED);
    	        	}

    	        	else if (currentPipeLineState.ordinal() < PipeLineState.NEW_INSTRUCTION.ordinal()) {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlRuleGeneration);
    		        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot generate predicates! (MSG008-1)");
    		        	m.open();            	   	        		
    	        	}

    	        	else {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlRuleGeneration);
    		        	m.setMessage("No instructions provided: Cannot parse! (MSG008-2)");
    		        	m.open();            	
    	        	}
    	        	
    	        }
    	        catch(Exception e2) {
	        		AdjustControlState(prevPipeLineState);
		        	MessageBox m = new MessageBox(shlRuleGeneration);
		        	m.setMessage("Error: Cannot parse or generate predicates! (MSG008-3)" + ls + e2.getMessage());
		        	m.open();            	
     	        }
    		}
    	});
    	btnGeneratePredicates.setEnabled(false);
    	btnGeneratePredicates.setText("Generate Predicates");
    	
    	@SuppressWarnings("unused")
		Label lblGrpPredicatesFiller = new Label(grpPredicatesControls, SWT.NONE);
    	
    	Group grpGenerateControls = new Group(cmpInstructionTextControls, SWT.NONE);
    	grpGenerateControls.setText("Rule Generation");
    	grpGenerateControls.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	btnViewContext = new Button(grpGenerateControls, SWT.NONE);
    	btnViewContext.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
        	    try {
        	        if (currentPipeLineState.ordinal() >= PipeLineState.CONTEXT_GENERATED.ordinal()) {
        	        	Files.write(Paths.get(NLRGParameterApp.NLRGPipeline_ContextFile), NLRGPipe.getSentencesContextTextData().getBytes());
        	        	Program.launch(NLRGParameterApp.NLRGPipeline_ContextFile);
        	        }
        	        else {
        		        MessageBox m = new MessageBox(shlRuleGeneration);
        		        m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot show context! (MSG009-1)");
        		        m.open();            	
        	        }
        	    }
        	    catch(Exception e2) {
    		        MessageBox m = new MessageBox(shlRuleGeneration);
    		        m.setMessage("Error: Cannot show context! (MSG009-2)" + ls + e2.getMessage());
    		        m.open();            	
         	    }
    		}
    	});
    	btnViewContext.setEnabled(false);
    	btnViewContext.setText("View Context");
    	
    	btnGenerateContext = new Button(grpGenerateControls, SWT.NONE);
    	btnGenerateContext.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
    			PipeLineState prevPipeLineState = currentPipeLineState;

    			try {
    				txtGeneratedRule.setText("");
    	        	String instructionText = txtInstruction.getText();

    	        	if (	currentPipeLineState.ordinal() >= PipeLineState.NEW_INSTRUCTION.ordinal() &&
    	        			instructionText != null && instructionText != "") {

       	        		if (currentPipeLineState.ordinal() < PipeLineState.NL_PARSED.ordinal())
       	        			NLRGPipe.processNL(instructionText);

    	        		if (currentPipeLineState.ordinal() < PipeLineState.PREDICATES_GENERATED.ordinal())
	    	        		NLRGPipe.generateMetaPredicates();

    	        		NLRGPipe.buildSentencesContext();
    	        		AdjustControlState(PipeLineState.CONTEXT_GENERATED);
    	        	}

     	        	else if (instructionText == null || instructionText == "") {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlRuleGeneration);
    		        	m.setMessage("No instructions provided: Cannot parse! (MSG010-1)");
    		        	m.open();            	
    	        	}
    	        	
    	        	else {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlRuleGeneration);
    		        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot generate context! (MSG010-2)");
    		        	m.open();            	   	        		
    	        	}
    	        }
    	        catch(Exception e2) {
	        		AdjustControlState(prevPipeLineState);
		        	MessageBox m = new MessageBox(shlRuleGeneration);
		        	m.setMessage("Error: Cannot parse, generate predicates or context! (MSG010-3)" + ls + e2.getMessage());
		        	m.open();            	
     	        }
    		}
    	});
    	btnGenerateContext.setText("Generate Context");
    	btnGenerateContext.setEnabled(false);
    	
    	btnGenerateRule = new Button(grpGenerateControls, SWT.CENTER);
    	btnGenerateRule.setEnabled(false);
    	btnGenerateRule.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {

    			PipeLineState prevPipeLineState = currentPipeLineState;

    	        try {
 
    	        	txtGeneratedRule.setText("");
    	        	String instructionText = txtInstruction.getText();
    	        	if (	currentMetaKBState == MKB_LOADED &&
    	        			currentPipeLineState.ordinal() >= PipeLineState.NEW_INSTRUCTION.ordinal() &&
    	        			instructionText != null && instructionText != "") {

    	        		if (currentPipeLineState.ordinal() < PipeLineState.NL_PARSED.ordinal())
       	        			NLRGPipe.processNL(instructionText);
    	        		
    	        		if (currentPipeLineState.ordinal() < PipeLineState.PREDICATES_GENERATED.ordinal())
	    	        		NLRGPipe.generateMetaPredicates();

    	        		if (currentPipeLineState.ordinal() < PipeLineState.CONTEXT_GENERATED.ordinal())
        	        		NLRGPipe.buildSentencesContext();
    	        		
    	        		NLRGPipe.extractRules(null);	        		
    	        		txtGeneratedRule.setText(NLRGPipe.buildExtractedRules());
    	        		AdjustControlState(PipeLineState.RULE_GENERATED);
    	        	    
       	        	}

    	        	else if (instructionText == null || instructionText == "") {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlRuleGeneration);
    		        	m.setMessage("No instruction provided: Cannot parse! (MSG011-4)");
    		        	m.open();            	
    	        	}
    	        	
    	        	else {
		        		AdjustControlState(prevPipeLineState);
    		        	MessageBox m = new MessageBox(shlRuleGeneration);
    		        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot generate context! (MSG011-5)");
    		        	m.open();            	   	        		
    	        	}
    	        }
    	        catch(Exception e2) {
	        		AdjustControlState(prevPipeLineState);
		        	MessageBox m = new MessageBox(shlRuleGeneration);
		        	m.setMessage("Error: Cannot parse, generate predicates or context and rule! (MSG011-6)" + ls + e2.toString());
		        	m.open();            	
     	        }

		    }
    	});
    	btnGenerateRule.setText("Generate Rules");
    	
    	Group grpGeneratedRule = new Group(shlRuleGeneration, SWT.NONE);
    	grpGeneratedRule.setText("Generated Rules");
    	grpGeneratedRule.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    			    txtGeneratedRule = new Text(grpGeneratedRule, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
    	
    	Composite cmpGeneratedRuleControls = new Composite(shlRuleGeneration, SWT.NONE);
    	cmpGeneratedRuleControls.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	Composite cmpGRC1 = new Composite(cmpGeneratedRuleControls, SWT.NONE);
    	cmpGRC1.setLayout(new FillLayout(SWT.VERTICAL));
    	
    	@SuppressWarnings("unused")
		Label lblGRC1Filler1 = new Label(cmpGRC1, SWT.NONE);
    	
    	Composite cmpGRC2 = new Composite(cmpGeneratedRuleControls, SWT.NONE);
    	cmpGRC2.setLayout(new FillLayout(SWT.HORIZONTAL));
    	
    	@SuppressWarnings("unused")
		Label lblGRC2Filler1 = new Label(cmpGRC2, SWT.NONE);
    	
    	@SuppressWarnings("unused")
		Label lblGRC2Filler2 = new Label(cmpGRC2, SWT.NONE);
    	
    	btnAddRuleToKB = new Button(cmpGRC2, SWT.CENTER);
    	btnAddRuleToKB.addSelectionListener(new SelectionAdapter() {
    		@Override
    		public void widgetSelected(SelectionEvent e) {
     	        try {
        	        if (currentPipeLineState.ordinal() >= PipeLineState.RULE_GENERATED.ordinal()) {
        	        	Files.write(Paths.get( NLRGParameterLib.NLRGPipeline_KnowledgeBaseFile), txtGeneratedRule.getText().getBytes(), StandardOpenOption.APPEND);
			        	MessageBox m = new MessageBox(shlRuleGeneration);
			        	m.setMessage("Rules added to Knowledge Base!");
			        	m.open();
        	        }
        	        else {
			        	MessageBox m = new MessageBox(shlRuleGeneration);
			        	m.setMessage("Wrong pipeline state (" + currentPipeLineState + "): Cannot generate context! (MSG012-1)");
			        	m.open();
        	        }
				} catch (IOException e1) {
    		        MessageBox m = new MessageBox(shlRuleGeneration);
    		        m.setMessage("Error: Cannot write rules to Knowledge Base! (MSG012-2)" + ls + e1.getMessage());
    		        m.open();            	
				}
    		}
    	});
    	btnAddRuleToKB.setText("Add Rules to Knowledge Base");
    	
    	lblLblMessageArea = new Label(cmpGeneratedRuleControls, SWT.NONE);
    	lblLblMessageArea.setText("");

	}

}
