package physics;

import java.awt.Color;
import java.util.Iterator;

public class GameScene {

	public ImpulseScene impulse;
	public EnemyScene enemyScene;
	public Player player;
	public Cannon cannon;
	public Vec2 mouse;
	public Camera camera;
	public UI ui;
	public EZSound bgm;

	public long previousTime;
	public long currentTime;
	public float totalTime;
	public float dt;

	public GameScene() {

	}

	public void start() {
		bgm = EZ.addSound("bgm.wav");
		bgm.loop();
		EZ.setBackgroundColor(Color.ORANGE);
		impulse = new ImpulseScene(ImpulseMath.DT, 10);
		enemyScene = new EnemyScene(this);
		mouse = new Vec2(600, 300); // initialize it where it doesnt affect
									// scrolling
		player = new Player(this);
		cannon = new Cannon(this);
		camera = new Camera(this);
		ui = new UI(this);

		currentTime = System.currentTimeMillis();
		previousTime = currentTime;

		Body b;
		

		b = impulse.add(new Circle(1.0f), -100, -100); // placeholder for
														// cannonball
		// IMPORTANT!! cannonball must be drawn first so that it takes the place
		// of index 0

		// ground
		b = impulse.add(new Polygon(1000.0f, 20.0f), 1000, 590);
		b.setStatic();
		b.setOrient(0);

		// roof
		b = impulse.add(new Polygon(140.0f, 10.0f), 1500, 370);
		b.setOrient(0);

		b = impulse.add(new Polygon(90.0f, 10.0f), 1420, 290);
		b.setOrient((float) Math.toRadians(140.0f));

		b = impulse.add(new Polygon(90.0f, 10.0f), 1570, 290);
		b.setOrient((float) Math.toRadians(40.0f));

		// left block
		b = impulse.add(new Polygon(30.0f, 30.0f), 1400, 410);
		b.setOrient(0);

		b = impulse.add(new Polygon(30.0f, 30.0f), 1400, 470);
		;
		b.setOrient(0);
		b = impulse.add(new Polygon(30.0f, 30.0f), 1400, 530);
		b.setOrient(0);

		// right block
		b = impulse.add(new Polygon(30.0f, 30.0f), 1600, 410);
		b.setOrient(0);
		b = impulse.add(new Polygon(30.0f, 30.0f), 1600, 470);
		b.setOrient(0);
		b = impulse.add(new Polygon(30.0f, 30.0f), 1600, 530);
		b.setOrient(0);
	}

	public void draw() {
		for (Body b : impulse.bodies) {
			if (b.drawn == false) {
				if (b.shape instanceof Circle) {
					Circle c = (Circle) b.shape;

					b.circle = EZ.addCircle((int) b.position.x, (int) b.position.y, (int) c.radius * 2,
							(int) c.radius * 2, Color.WHITE, true);
					b.drawn = true;
				} else if (b.shape instanceof Polygon) {
					Polygon p = (Polygon) b.shape;

					for (int i = 0; i < p.vertexCount; i++) {
						Vec2 v = new Vec2(p.vertices[i]);
						b.shape.u.muli(v);
						v.addi(b.position);
					}

					b.rectangle = EZ.addRectangle((int) b.position.x, (int) b.position.y,
							(int) p.vertices[0].distance(p.vertices[1]), (int) p.vertices[1].distance(p.vertices[2]),
							Color.BLACK, true);
					b.rectangle.rotateTo(Math.toDegrees(b.orient));
					if (b.type instanceof Enemy) {
						b.image = EZ.addImage("arrow.png", (int) b.position.x, (int) b.position.y);
						b.image.rotateTo(Math.toDegrees(b.orient));
					}
					// b.image = EZ.addImage("box.jpg", (int) b.position.x,
					// (int) b.position.y);
					b.drawn = true;
				}
			}
		}

		for (Manifold m : impulse.contacts) {
			for (int i = 0; i < m.contactCount; i++) {
				Vec2 v = m.contacts[i];
				Vec2 n = m.normal;

			}
		}
	}

	public void update() {
		currentTime = System.currentTimeMillis();
		dt = (currentTime - previousTime) / 1000.0f;
		totalTime += dt;
		
		/* 5-SEC IF COLLISION THEN DISAPPEAR TEST*/
		// After 5 seconds of collision, the object will disappear by removing
		// it from
		// the arraylist 'impulse.bodies' BUT!!!! Remember to use the Iterator
		// removing method
		// to avoid concurrent modification exception error
		for (Iterator<Body> b = impulse.bodies.iterator(); b.hasNext();) {
			Body c = b.next();
			if (c.type instanceof Enemy) {
				if (c.collided && c.collidedTime == 0) {
					c.collidedTime = (int) totalTime;
				}
				if (c.collidedTime != 0 && (int) (totalTime) >= c.collidedTime + 5) {
					b.remove();
					EZ.removeEZElement(c.rectangle);
					EZ.removeEZElement(c.image);
					c = null;
				}
			}
		}

		//

		// ARROW DRAG-TORQUE IMITATION TEST
		for (Body b : impulse.bodies) {
			if (b.type instanceof Enemy) {
				Enemy e = (Enemy) b.type;
				e.dp.x = b.velocity.x * dt;
				e.dp.y = (float) (0.5 * ImpulseMath.GRAVITY.y * dt + b.velocity.y) * dt;
				if (!b.collided) {
					b.angularVelocity = 0;
					b.orient = (float) Math.atan2(e.dp.y, e.dp.x);
				}
			}
		}

		player.update();
		ui.update();
		cannon.update();
		camera.update(impulse.bodies.get(0)); // parameter: center of camera
		mouse.x = EZInteraction.getXMouse();
		mouse.y = EZInteraction.getYMouse();
		impulse.step();

		for (Body b : impulse.bodies) {

			if (b.shape instanceof Circle) {
				b.circle.translateTo(b.position.x, b.position.y);
				// b.circle.rotateBy( b.angularVelocity );

				if (b.angularVelocity > 0.0f) // IMPORTANT!! Without it ball
												// keeps on rolling down
					b.angularVelocity -= 0.1f;
				if (b.angularVelocity < 0.0f)
					b.angularVelocity += 0.1f;
				if (Math.abs(b.angularVelocity) <= 3)
					b.angularVelocity = 0;

			} else if (b.shape instanceof Polygon) {
				if (b.type instanceof Enemy) {
					b.image.translateTo(b.position.x, b.position.y);
					b.image.rotateTo(Math.toDegrees(b.orient));
				}
				b.rectangle.translateTo(b.position.x, b.position.y);
				b.rectangle.rotateTo(Math.toDegrees(b.orient));

			}
		}

		enemyScene.update();

		previousTime = currentTime;
	}

	public void input() {
		cannon.input();

		if (EZInteraction.wasKeyPressed('a')) {
			float r = ImpulseMath.random(10.0f, 30.0f);
			Body b = impulse.add(new Circle(r), EZInteraction.getXMouse(), EZInteraction.getYMouse());
			b.velocity.set(0, 0.02f);
		}
		if (EZInteraction.wasKeyPressed('s')) {
			// float hw = ImpulseMath.random( 10.0f, 30.0f );
			// float hh = ImpulseMath.random( 10.0f, 30.0f );

			Body b = impulse.add(new Polygon(30.0f, 30.0f), EZInteraction.getXMouse(), EZInteraction.getYMouse());
			b.setOrient(0.0f);
			b.velocity.set(0, 0.02f);
		}

	}
	
	public void close() {
		EZ.removeAllEZElements();
		//impulse.bodies.clear();
	}

}
