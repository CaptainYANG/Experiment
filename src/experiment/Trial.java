package experiment;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.KeyPress;

public class Trial {
	protected int block;
	protected int trial;
	protected String targetChange;
	protected int objectsCount;
	protected Experiment experiment;
	protected CEllipse target;
	protected int targetPosition;
	Canvas canvas;
	double canvasCenterX;
	double canvasCenterY;
	public CExtensionalTag allEllipse = new CExtensionalTag(){};
	public Trial(int block, int trial, String targetChange,
			int objectsCount, Experiment experiment) {
		super();
		this.block = block;
		this.trial = trial;
		this.targetChange = targetChange;
		this.objectsCount = objectsCount;
		this.experiment = experiment;
		canvas = experiment.getCanva();
		canvasCenterX = canvas.getWidth()/2;
		canvasCenterY = canvas.getHeight()/2;
	}
	public void displayInstructions() {
		String welcome = "Welcome to test "+trial+"! Thank you for taking this test.";
		CText text1 = canvas.newText(0, 50, welcome);
		CText text2 = canvas.newText(0, 100, "1. If you are ready, please press enter to enter the test.");
		CText text3 = canvas.newText(0, 150, "2. If you see the different circle, please press space as soon as possible, and then click on the circle on the same place.");
		text1.addTag(experiment.getInstructions());
		text2.addTag(experiment.getInstructions());
		text3.addTag(experiment.getInstructions());
		double textCenterX = experiment.getInstructions().getCenterX();
		double textCenterY = experiment.getInstructions().getCenterY();
		
		double canvasCenterX = canvas.getWidth()/2;
		double canvasCenterY = canvas.getHeight()/2; 
		double dx = canvasCenterX - textCenterX;
		double dy = canvasCenterY - textCenterY;		
		
		experiment.getInstructions().translateBy(dx, dy);
		canvas.setAntialiased(true);

	}
	
	public void hideInstructions() {
		canvas.removeShapes(experiment.getInstructions());
		if (targetChange.equals("VV1")){
			experimentColorEllipse();
		}else if(targetChange.equals("VV2")){
			experimentSizeEllipse();
		}else if(targetChange.equals("VV1VV2")){
			experimentCSEllipse();
		}else {
		}
	}

	private void experimentColorEllipse() {
		double n = Math.sqrt(objectsCount);
		int rand = (int) Math.round(Math.random()*(objectsCount-1));
		targetPosition = rand;
		for(int i = 0; i < n; i++){
			for (int j = 0; j< n; j++){
				CEllipse tmp = canvas.newEllipse(canvasCenterX-150+ j*70,canvasCenterY-150+i*70,30,30);
				tmp.setFillPaint(Color.gray).addTag(allEllipse);
				canvas.addShape(tmp);
// Only new Ellipse can add tag successfully
				if(rand == 0){
					target = tmp;
					target.setFillPaint(Color.YELLOW);
				}
				rand--;
			}
		}
	}
	private void experimentSizeEllipse() {
		double n = Math.sqrt(objectsCount);
		int rand = (int) Math.round(Math.random()*(objectsCount-1));
		targetPosition = rand;
		for(int i = 0; i < n; i++){
			for (int j = 0; j<n; j++){
				CEllipse tmp = canvas.newEllipse(canvasCenterX-150+ j*70,canvasCenterY-150+i*70,30,30);
				tmp.setFillPaint(Color.gray).addTag(allEllipse);
				canvas.addShape(tmp);
// Only new Ellipse can add tag successfully
				if(rand == 0){
					target = tmp;
					target.setWidth(50);  
					target.setHeight(50);
				}
				rand--;
			}
		}
	}
	private void experimentCSEllipse() {
		double n = Math.sqrt(objectsCount);
		int rand = (int) Math.round(Math.random()*(objectsCount-1));
		targetPosition = rand;
		for(int i = 0; i < n; i++){
			for (int j = 0; j<n; j++){
				CEllipse tmp = canvas.newEllipse(canvasCenterX-150+ j*70,canvasCenterY-150+i*70,30,30);
				tmp.setFillPaint(Color.gray).addTag(allEllipse);
// Only new Ellipse can add tag successfully
				if(rand == 0){
					target = tmp;
					target.setFillPaint(Color.YELLOW);
					target.setWidth(50);
					target.setHeight(50);
				}else{
					int randOther = (int) Math.round(Math.random()*3);
					if (randOther == 0){
						tmp.setFillPaint(Color.YELLOW);
					}else if(randOther == 1){
						tmp.setWidth(50);
						tmp.setHeight(50);
					}
				}
				canvas.addShape(tmp);
				rand--;
			}
		}
	}
	public void start() {
		canvas.removeShapes(allEllipse);
		double n = Math.sqrt(objectsCount);
		int rand = targetPosition;
		for(int i = 0; i < n; i++){
			for (int j = 0; j<n; j++){
				CEllipse tmp = canvas.newEllipse(canvasCenterX-150+ j*70,canvasCenterY-150+i*70,30,30);
				tmp.setFillPaint(Color.white).addTag(allEllipse);
				canvas.addShape(tmp);
// Only new Ellipse can add tag successfully
				if(rand == 0){
					target = tmp;
				}
				rand--;
			}
		}
	}
	public void stop() {
		canvas.removeShapes(allEllipse);
	}
	public CShape getTarget() {	
		return target;
	}
}