package evalu1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Experiment {
	// input file (design): "experiment.csv"
	protected File designFile = null;
	// output file: logs
	protected PrintWriter pwLog = null;
	protected ArrayList<Trial> allTrials = new ArrayList<Trial>();
	protected int currentTrial = 0;

	protected String participant;
	protected int block;
	protected int trial;
	
	public Experiment(String participant, int block, int trial, File designFile) {
		this.participant = participant;
		this.designFile = designFile;
		this.block = block;
		this.trial = trial;
		loadTrials();
		//initLog();
		nextTrial();
	}

	public void loadTrials() {
		allTrials.clear();
		// read the design file and keep only the trials to run
		try {
			BufferedReader br = new BufferedReader(new FileReader(designFile));
			String line = br.readLine();
			line = br.readLine();
			while(line != null) {
				String[] parts = line.split(";");
				int block = Integer.parseInt(parts[2]); 
				int trial = Integer.parseInt(parts[3]); 
				int targetChange = Integer.parseInt(parts[4]);
				int objectCount =  Integer.parseInt(parts[5]);
				//Trial trial = new Trial(block, trial, )
				// ...
				// allTrials.add(new Trial(...));
				line = br.readLine();
				System.out.println("line: "+line);
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
		log(trial);
		currentTrial++;
		nextTrial();
	}
	public void log(Trial trial) {
		
	}
	public void stop() {
		// display a "thank you" message
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