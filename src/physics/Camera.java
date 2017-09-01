package physics;

public class Camera {
	public GameScene gameScene;
	public Vec2 center;
	public float boundRight;
	public boolean cameraOn = true;
	public boolean scrollOn = true;
	private float dx;
	private float dy;

	public Camera(GameScene gameScene) {
		this.gameScene = gameScene;
		center = new Vec2(0, 0);
		boundRight = center.x + 100;
		dx = dy = 0; // origin
	}

	public void update(Body b) {
		// FUNCTION FOR SCROLLING
		if (gameScene.camera.scrollOn) {
			if (gameScene.mouse.x >= 0 && gameScene.mouse.y <= 1200 && gameScene.mouse.y <= 600
					&& gameScene.mouse.y >= 0) { // checks if mouse is within
													// the window screen
				if (gameScene.mouse.x <= 30) {
					//gameScene.ship.translateTo(gameScene.ship.xCenter + 2.0f, gameScene.ship.yCenter);
					dx += 5.0f; // deviation from origin
					gameScene.player.ship.translateBy(5, 0);
					gameScene.player.mainBody.translateBy(5, 0);
					gameScene.cannon.cannon.translateBy(5, 0);
					for (Body c : gameScene.impulse.bodies) {
						c.position.set(c.position.x + 5, c.position.y);
					}
				}

				if (gameScene.mouse.x >= EZ.getWindowWidth() - 30) {
					dx += -5.0f; // deviation from origin
					gameScene.player.ship.translateBy(-5, 0);
					gameScene.player.mainBody.translateBy(-5, 0);
					gameScene.cannon.cannon.translateBy(-5, 0);
					for (Body c : gameScene.impulse.bodies) {
						c.position.set(c.position.x - 5, c.position.y);
					}
				}
			}
		}

		if (b.position.y >= 600 || b.position.y <= 0) { // if ball goes below or
														// above the screen,
														// suppose that it
														// collided so that the
														// camera knows not to
														// follow it
			b.collided = true;
		}

		if (b != null && !b.collided) // if cannonball is empty, has collided,
										// or goes above or below the screen,
										// then the camera will stop following
										// it
		{
			center.x = b.position.x;
			boundRight = center.x + 100;
		}

		else
			boundRight = 0;

		// FUNCTION FOR CAMERA
		if (cameraOn) {
			if (boundRight >= 1200) {
				dx += -(boundRight - 1200); // deviation from origin
				for (Body c : gameScene.impulse.bodies) {
					if (c.shape instanceof Circle) {
						c.position.x -= boundRight - 1200;
						c.circle.translateBy(boundRight - 1200, 0);
					}

					if (c.shape instanceof Polygon) {
						c.position.x -= boundRight - 1200;
						c.rectangle.translateBy(boundRight - 1200, 0);
					}
				}
				gameScene.player.mainBody.translateBy(-(boundRight - 1200), 0);
				gameScene.cannon.cannon.translateBy(-(boundRight - 1200), 0);
				gameScene.player.ship.translateBy(-(boundRight - 1200), 0);
			}
		}
		
		// FUNCTION FOR RETURN FOCUS
		if(EZInteraction.wasKeyPressed('b')) {
			cameraOn = false;
			for (Body c : gameScene.impulse.bodies) {
				if (c.shape instanceof Circle) {
					c.position.x -= dx;
					c.circle.translateBy(-dx, 0);
				}

				if (c.shape instanceof Polygon) {
					c.position.x -= dx;
					c.rectangle.translateBy(-dx, 0);
				}
			}
			gameScene.cannon.cannon.translateBy(-dx, 0);
			gameScene.player.mainBody.translateBy(-dx, 0);
			gameScene.player.ship.translateBy(-dx, 0);
			dx = 0;
		}
	}
}
