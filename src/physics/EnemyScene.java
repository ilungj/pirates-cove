package physics;

public class EnemyScene {

	public GameScene gameScene;
	public int a, b;
	
	public EnemyScene(GameScene gameScene) {
		this.gameScene = gameScene;
		a = b = (int) gameScene.totalTime;
	}
	
	public void update() {
		// shoots arrow every 3 seconds
		if((int) gameScene.totalTime == a + 3) {
			shootArrow();
			a = (int) gameScene.totalTime;
		}
		
		// shoots fast arrow every 7 seconds
		if((int) gameScene.totalTime == b + 7) {
			shootFastArrow();
			b = (int) gameScene.totalTime;
		}
		
	}
	
	private void shootArrow() {
		Body b;
		b = gameScene.impulse.add(new Polygon(30.0f, 5.0f), gameScene.player.ship.getXCenter() + 1000, 300);
		b.setType(new Enemy());
		b.setOrient(0);
		b.velocity.y = -400;
		b.velocity.x = -250;
	}
	
	private void shootFastArrow() {
		Body b;
		b = gameScene.impulse.add(new Polygon(30.0f, 5.0f), gameScene.player.ship.getXCenter() + 1000, 300);
		b.setType(new Enemy());
		b.setOrient(0);
		b.velocity.y = -100;
		b.velocity.x = -700;
	}
}
