package experiment;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.KeyPress;

public class Experiment {
	// input file (design): "experiment.csv"
	protected File designFile = null;
	// output file: logs
	protected PrintWriter pwLog = null;
	protected ArrayList<Trial> allTrials = new ArrayList<Trial>();
	protected int currentTrial = 0;
	protected CExtensionalTag instructions = new CExtensionalTag(){};
	protected CExtensionalTag allEllipse = new CExtensionalTag(){};
	protected long previousTime;
	protected long currentTime;
	
	public CExtensionalTag getInstructions() {
		return instructions;
	}

	public void setInstructions(CExtensionalTag instructions) {
		this.instructions = instructions;
	}
	public CExtensionalTag getAllEllipse(){
		return allEllipse;
	}
	public void setAllEllipse(CExtensionalTag allEllipse) {
		this.allEllipse = allEllipse;
	}

	protected Canvas canva = new Canvas(800,600);
	public Canvas getCanva() {
		return canva;
	}

	public void setCanva(Canvas canva) {
		this.canva = canva;
	}

	protected String participant;
	protected int block;
	protected int trial;
	
	public Experiment(String participant, int block, int trial, File designFile) {
		this.participant = participant;
		this.designFile = designFile;
		this.block = block;
		this.trial = trial;
		JFrame frame = new JFrame();
		frame.getContentPane().add(canva);
		frame.pack();
		frame.setVisible(true);
		canva.requestFocus();
		loadTrials(participant,block,trial);
//		CText chooseCurrent = canva.newText(0,150,"Please choose which trials you want to begin");
//		JTextField textField = new JTextField(15);
//		JButton buttonConfirm = new JButton("Confirm");
//		trial = Integer.parseInt(textField.getText());
//		System.out.println(trial);
//		ActionListener buttonConfirmListener = new ActionListener() {
//			public void actionPerformed(ActionEvent e){
//				trial = Integer.parseInt(textField.getText());
//			}
//		};
		initlog();
		nextTrial();
		CStateMachine interaction = new CStateMachine() {
//			State chooseTrial = new State(){
//				Transition pressButton = new PressOnShape(BUTTON1,">>instruction"){
//					public void action(){
//						CText chooseCurrent = canva.newText(0,150, "Please choose which trials you want to begin");
//						CText label = canva.newText(0, 0, "Confirm", new Font("verdana", Font.PLAIN, 12));
//						CRectangle confirm = new CRectangle(400,200,100,100);
//						label.setParent(confirm);
//						confirm.below(label);
//					}
//				};
//			};
			State instruction = new State() {
				Transition pressEnter = new KeyPress(KeyEvent.VK_ENTER,">>fullShapeShown") {
					public void action() {
						allTrials.get(currentTrial).hideInstructions();
						previousTime = System.currentTimeMillis();
						
					}
				};	
			};
			State fullShapeShown = new State(){
				Transition pressSpace = new KeyPress(KeyEvent.VK_SPACE,">>holderShown"){
					public void action(){
						allTrials.get(currentTrial).start();
						currentTime = System.currentTimeMillis();
					}
				};
			};
			State holderShown = new State(){
				Transition clickOnShape = new PressOnShape(BUTTON1,">>instruction"){
					public void action(){
						Boolean hit = true;
						if (getShape() == allTrials.get(currentTrial).getTarget()){
							hit = true;
						}else{
							System.out.println("You click a wrong shape");
							hit = false;
						}
						long duration = currentTime - previousTime;
						String eachTrial =allTrials.get(currentTrial).block+","+allTrials.get(currentTrial).trial+","+allTrials.get(currentTrial).targetChange+","+
								allTrials.get(currentTrial).objectsCount+","+duration+","+hit;
								pwLog.println(eachTrial);
								pwLog.flush();
						trialCompleted();
					}
				};
			};
		};
		interaction.attachTo(canva);
	}
	public void loadTrials(String participant, int block, int trial) {
		allTrials.clear();
		try {
			BufferedReader br = new BufferedReader(new FileReader(designFile));
			String line = br.readLine();
			line = br.readLine();
			while(line != null) {
				String[] parts = line.split(";");
				String p = parts[0];
				int b = Integer.parseInt(parts[2]);
				int t = Integer.parseInt(parts[3]);
				String targetChange = parts[4];
				int objectCount = Integer.parseInt(parts[5]);
				if(p.compareTo(participant) == 0) {
					if(b > block || (b == block && t >= trial)) {
						Trial thisTrial = new Trial(block, t, targetChange, objectCount, this);
						allTrials.add(thisTrial);
					}
				}
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void trialCompleted() {
		Trial trial = allTrials.get(currentTrial);
		trial.stop();
		currentTrial++;
		nextTrial();
	}
	public void log(Trial trial){
			String eachTrial = trial.block+","+trial.trial+","+trial.targetChange+","+
			trial.objectsCount+",";
			pwLog.print(eachTrial);
			pwLog.flush();
	}
	
	public void initlog() { 
		String logFileName = "test"; //String
		logFileName = "log_S"+participant+"_"+".csv";
		File logFile = new File(logFileName); 
		try { 
			pwLog = new PrintWriter(logFile); 
			String header = "Block\t" +"Trial\t"
					+"TargetChange\t" +"ObjectsCount\t" +"Duration\t" +"Hit";
			pwLog.println(header); 
			pwLog.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		}
	}
	public void stop() {
		canva.newText(canva.getCenterX(), canva.getCenterY(), "Thank you");
	}
	public void nextTrial() {
		if(currentTrial >= allTrials.size()) {
			stop();
		}
		Trial trial = allTrials.get(currentTrial);
		trial.displayInstructions();
	}

	public static void main(String[] args) {
		File file = new File("experiment.csv");
		Experiment experiment = new Experiment("0",1,0,file);
	}
}