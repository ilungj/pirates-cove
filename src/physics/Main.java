package physics;

import java.awt.Color;

public class Main implements Game {
	public GameScene gameScene;
	public GameMenu gameMenu;
	public GameOver gameOver;

	public static void main(String[] args) throws InterruptedException {
		EZ.initialize(1200, 600);
		EZ.setBackgroundColor(Color.BLACK);
		Game game = new Main();
		game.start();

		while (true) {
			game.draw();
			game.update();
			game.input();
			EZ.refreshScreen();
		}
	}

	@Override
	public void start() {
		gameScene = new GameScene();
		gameMenu = new GameMenu();
		gameOver = new GameOver();

		if (GameState.state == GameState.State.Game) {
			gameScene.start();
		}

		if (GameState.state == GameState.State.Menu) {
			gameMenu.start();
		}

		if (GameState.state == GameState.State.Over) {
			gameOver.start();
		}

		GameState.changeState = false;

	}

	@Override
	public void draw() {
		if (GameState.state == GameState.State.Game) {
			gameScene.draw();
		}

		if (GameState.state == GameState.State.Menu) {
			gameMenu.draw();
		}
	}

	@Override
	public void update() {
		if (GameState.changeState) {
			close();
			start();
		}
		if (GameState.state == GameState.State.Game) {
			gameScene.update();
		}

		if (GameState.state == GameState.State.Menu) {
			gameMenu.update();
		}

		if (GameState.state == GameState.State.Over) {
			gameOver.update();
		}
	}

	@Override
	public void input() {
		if (GameState.changeState) {
			close();
			start();
		}
		
		if (EZInteraction.wasKeyPressed('q') && GameState.state != GameState.State.Menu) {
			close();
			GameState.state = GameState.State.Menu;
			start();
			// test = EZ.addRectangle(0, 0, 600, 300, Color.WHITE, true );

		}

		if (EZInteraction.wasKeyPressed('w') && GameState.state == GameState.State.Menu) {
			// EZ.removeEZElement(test);
			close();
			GameState.state = GameState.State.Game;
			start();

		}

		if (GameState.state == GameState.State.Menu) {
			gameMenu.input();
		}

		if (GameState.state == GameState.State.Game) {
			gameScene.input();
		}

		if (GameState.state == GameState.State.Over) {
			gameOver.input();
		}
	}

	@Override
	public void close() {

		gameScene.close();
		gameScene = null;
		gameMenu.close();
		gameMenu = null;

	}

}
