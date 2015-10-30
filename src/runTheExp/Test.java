package runTheExp;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import fr.lri.swingstates.canvas.CEllipse;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.PressOnShape;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.KeyPress;

public class Test {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		final Canvas canvas = new Canvas(800, 600);
		double x_middle = canvas.getPreferredSize().getWidth()/2;
		double y_middle = canvas.getPreferredSize().getHeight()/2;
		
		final CEllipse target = canvas.newEllipse(x_middle-20, y_middle-20, 40, 40);
		final CEllipse object = canvas.newEllipse(100, 100, 40, 40);
		object.setFillPaint(Color.RED);
		
		final CText text1 = canvas.newText(200, 200, "instruction1");
		final CText text2 = canvas.newText(200, 300, "instruction2");
		
		text1.addTag("instruction");
		text2.addTag("instruction");
		
		
		CStateMachine pressOnCircle = new CStateMachine() {
			State state = new State() {
				Transition pressOnShape = new PressOnShape() {
					public void action() {
						CShape shapePressed = getShape();
						if(shapePressed == target) {
							System.out.println("press on target");
							shapePressed.setFillPaint(Color.WHITE);
						} else {
							System.out.println("press on object");
						}
					}
				};
				
				Transition pressSpaceBar = new KeyPress(KeyEvent.VK_SPACE) {
					public void action() {
						System.out.println("Space bar");
						//target.setDrawable(false);
						canvas.getTag("instruction").setFillPaint(Color.RED);
					}
				};
				
			};
		};
		
		pressOnCircle.attachTo(canvas);		
		frame.getContentPane().add(canvas);
		frame.pack();
		frame.setVisible(true);
		canvas.requestFocus();
	}
	
}
