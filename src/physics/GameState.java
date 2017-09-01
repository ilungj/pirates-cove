package physics;

public abstract class GameState {

	public GameState() {

	}

	public enum State {
		Game, Menu, Over
	}

	public static State state = State.Menu;
	public static boolean changeState = false;
}
