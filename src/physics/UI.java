package physics;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI {

	public GameScene gameScene;
	public boolean on = false;
	private ArrayList<EZImage> heart;
	EZRectangle i1;
	EZText t1;
	EZRectangle window;
	EZText time;

	public UI(GameScene gameScene) {
		this.gameScene = gameScene;
		heart = new ArrayList<EZImage>();
		for(int i = 0; i < gameScene.player.heartCount; i++) {
			EZImage e = EZ.addImage("heart.png", 50 + (i * 80), 50);
			heart.add(e);
		}
		i1 = EZ.addRectangle(1150, 50, 50, 50, Color.WHITE, true);
		t1 = EZ.addText(1150, 50, "icon1", Color.BLACK, 10);

		time = EZ.addText(600, 300, " ", Color.BLACK, 72);
	}

	public void update() {
		time.setMsg((String.format("%4.2f", gameScene.totalTime)));

		// DELETE ONE HEART PICTURE EACH TIME PLAYER HEARTCOUNT DECREASES
		if(heart.size() > gameScene.player.heartCount && heart.size() > 0) {
			EZ.removeEZElement(heart.get(heart.size() - 1));
			heart.remove(heart.get(heart.size() - 1));
		}
			
		if (!on) {
			if (i1.isPointInElement(EZInteraction.getXMouse(), EZInteraction.getYMouse())
					&& EZInteraction.wasMouseLeftButtonPressed()) {
				window = EZ.addRectangle(600, 300, 800, 400, Color.WHITE, true);
				on = true;
			}
		}

		else if (on) {
			if (i1.isPointInElement(EZInteraction.getXMouse(), EZInteraction.getYMouse())
					&& EZInteraction.wasMouseLeftButtonPressed()) {
				EZ.removeEZElement(window);
				on = false;
			}
		}

	}
}
