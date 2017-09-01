package physics;

import java.awt.Color;

public class Player {
	
	public GameScene gameScene;
	public GameOver gameOver;
	public EZImage ship;
	public EZRectangle mainBody;
	public int heartCount = 3;
	public static float score;
	private float n = 1;
	
	public Player(GameScene gameScene) {
		this.gameScene = gameScene;
		ship = EZ.addImage("ship.png", 200, 400);
		ship.rotateTo(-10);
		mainBody = EZ.addRectangle(200, 440, 330, 250, Color.WHITE, false);
		mainBody.hide();
	}
	
	public void update() {
		// SHIP ROTATION
		if (((int) gameScene.totalTime) % 2 == 0)
			n = -0.05f;
		else
			n = 0.05f;
		ship.rotateBy(n);
		//

		// PLAYER TO ENEMY COLLISION TEST
		// player loses a life upon collision with an enemy
		for(Body b : gameScene.impulse.bodies) {
			if(b.type instanceof Enemy) {
				if(mainBody.isPointInElement(b.position.x, b.position.y) && !b.collided) {
					heartCount--;
					b.collided = true;
				}
			}
		}
		//
		
		// GAMEOVER CHECK
		if(heartCount <= 0) {
			score = gameScene.totalTime;
			GameState.state = GameState.State.Over;
			gameScene.bgm.stop();
			GameState.changeState = true;
		}
	}
}
