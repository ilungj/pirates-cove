package physics;

import java.awt.Color;

public class GameOver {

	private EZText over;
	private EZText score;
	
	public GameOver() {
		
	}
	
	public void start() {
		EZ.setBackgroundColor(Color.BLACK);
		over = EZ.addText(600, 300, "GAMEOVER", Color.WHITE, 72);
		score = EZ.addText(600, 400, "Score: "+Player.score, Color.WHITE, 60);
	}
	
	public void update() {
		
	}
	
	public void input() {
		
	}
	
	public void close() {
		
	}
}
