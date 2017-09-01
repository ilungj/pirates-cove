package physics;

import java.awt.Color;
import java.util.ArrayList;

public class GameMenu {

	private EZText[] m = new EZText[3];
	private ArrayList<String> n = new ArrayList<String>(); 
	
	public GameMenu() {

	}

	public void start() {
		EZ.setBackgroundColor(Color.BLACK);
		n.add("PLAY");
		n.add("INSTRUCTION");
		n.add("SCORES");
		m[0] = EZ.addText(600, 200, n.get(0), Color.GRAY, 32);
		m[1] = EZ.addText(600, 300, n.get(1), Color.WHITE, 62);
		m[2] = EZ.addText(600, 400, n.get(2), Color.GRAY, 32);
	}

	public void draw() {

	}

	public void update() {
		m[0].setMsg(n.get(0));
		m[1].setMsg(n.get(1));
		m[2].setMsg(n.get(2));
		
		if(n.get(1) == "PLAY") {
			// if enter key is pressed while user is selecting PLAY, begin game
			if(EZInteraction.wasKeyPressed(10)) {
				GameState.state = GameState.State.Game;
				GameState.changeState = true;
			}
		}
	}

	public void input() {
		if (EZInteraction.wasMouseLeftButtonPressed()) {

		}

		if (EZInteraction.wasMouseLeftButtonReleased()) {

		}

		if (EZInteraction.isMouseLeftButtonDown()) {

		}
		
		// if down arrow key is pressed, scroll the menu items down
		if(EZInteraction.wasKeyPressed(40)) {
			
			String k = n.get(0);
			
			for(int i = 0; i < n.size() - 1; i++) {
				n.set(i, n.get(i + 1));
			}
			
			n.set(n.size() - 1, k);
			
		}
		
		// if up arrow key is pressed, scroll the menu items up
		if(EZInteraction.wasKeyPressed(38)) {
			
			String k = n.get(n.size() - 1);
			
			for(int i = n.size() - 1; i > 0; i--) {
				n.set(i, n.get(i - 1));
			}
			
			n.set(0, k);
		}
		
	}

	public void close() {
		EZ.removeAllEZElements();
	}
}
