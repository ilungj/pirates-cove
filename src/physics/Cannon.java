package physics;
import java.awt.Color;

public class Cannon
{
	public GameScene gameScene;
	public EZCircle cannonBall;
	public EZImage cannon;
	public Arrow powerArrow;
	public double angle = 0;
	public boolean shooting;
	
	public Cannon( GameScene gameScene )
	{
		this.gameScene = gameScene;
		cannon = EZ.addImage( "cannon.png", 300, 500 );
		
	}
	
	public void update()
	{
		cannon.rotateTo( angle );
	}
	
	public void input()
	{
		if (EZInteraction.wasMouseLeftButtonPressed() && cannon.isPointInElement(gameScene.mouse.x, gameScene.mouse.y))
		{
			shooting = true;
			cannonBall = EZ.addCircle( cannon.getXCenter() + 91, cannon.getYCenter() - 46, 30, 30, Color.WHITE, true );
			powerArrow = new Arrow( cannonBall.getXCenter(), cannon.getYCenter() , EZInteraction.getXMouse(), EZInteraction.getYMouse());
			powerArrow.line.setPoint1( cannonBall.getXCenter(), cannonBall.getYCenter() );
			powerArrow.line.setThickness( 3 );
		}
		
		if (EZInteraction.wasMouseLeftButtonReleased() && shooting )
		{
		   // Change in x/y per second
		   float xVector = (powerArrow.getX2() - powerArrow.getX1()) * 3;
		   float yVector = (powerArrow.getY2() - powerArrow.getY1()) * 3;
		   Body b;
		   
		   EZ.removeEZElement( gameScene.impulse.bodies.get(0).circle ); // removes previous ball
		   b = gameScene.impulse.add2( new Circle( 15.0f ), cannonBall.getXCenter(), cannonBall.getYCenter() );
		   b.setDensity( 5.0f ); //makes the cannonball heavy
		   
		   EZ.removeEZElement( cannonBall );
		   b.velocity.set(xVector, yVector + 0.01f );
		   
		   EZ.removeEZElement( powerArrow.line );

		   b = null; //removes previous ball

		   powerArrow = null;
		   
		   gameScene.camera.scrollOn = true;
		   gameScene.camera.cameraOn = true;
		   shooting = false;
		}
		
		if (EZInteraction.isMouseLeftButtonDown() && shooting )
		{
			gameScene.camera.scrollOn = false;
			gameScene.camera.cameraOn = false;
			int x1 = powerArrow.getX1();
			int y1 = powerArrow.getY1();
			int x2 = EZInteraction.getXMouse();
			int y2 = EZInteraction.getYMouse();
			int dx = Math.abs(x2 - x1);
			int dy = Math.abs(y2 - y1);

			angle = Math.toDegrees( Math.atan2(y1 - y2, x1 - x2));
			
			double newX = cannon.getXCenter() + (Math.cos(Math.toRadians(angle - 25)) * (x1 - cannon.getXCenter()) + Math.sin(Math.toRadians(angle - 25) * (y1 - cannon.getYCenter())));
			double newY = cannon.getYCenter() + (Math.sin(Math.toRadians(angle - 25)) * (x1 - cannon.getXCenter()) + Math.cos(Math.toRadians(angle - 25) * (y1 - cannon.getYCenter())));
			cannonBall.translateTo( newX, newY );
			
			powerArrow.line.setPoint1((int)newX, (int)newY);
		
			powerArrow.line.setPoint2( EZInteraction.getXMouse(), EZInteraction.getYMouse() );
			
			if ((x2 - x1) < 0)
			{
				powerArrow.setX2(x1 + dx);
			}
			else
			{
				powerArrow.setX2(x1 - dx);
			}

			if ((y2 - y1) < 0)
			{
				powerArrow.setY2(y1 + dy);
			}
			else
			{
				powerArrow.setY2(y1 - dy);
			}
		}
	}
}
