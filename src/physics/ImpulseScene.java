package physics;
import java.util.ArrayList;
import java.util.List;

public class ImpulseScene
{

	public float dt;
	public int iterations;
	public List<Body> bodies = new ArrayList<Body>();
	public ArrayList<Body> bodies2 = new ArrayList<Body>();
	public ArrayList<Manifold> contacts = new ArrayList<Manifold>();

	public ImpulseScene( float dt, int iterations )
	{
		this.dt = dt;
		this.iterations = iterations;
	}

	public void step()
	{
		// Generate new collision info
		contacts.clear();
		
		for (int i = 0; i < bodies.size(); ++i)
		{
			Body A = bodies.get( i );

			for (int j = i + 1; j < bodies.size(); ++j)
			{
				Body B = bodies.get( j );

				if (A.invMass == 0 && B.invMass == 0)
				{
					continue;
				}

				Manifold m = new Manifold( A, B );
				m.solve();

				if (m.contactCount > 0)
				{
					contacts.add( m );
					
					//
					A.collided = true;
					B.collided = true;
					//
					
					if ( A == bodies.get(0) && A.shape instanceof Circle ) //purpose: turn off camera on cannonball after it collides with another object
					{
						bodies.get(0).collided = true;

					}	
				}
			}
		}
		
		// Integrate forces
		for (int i = 0; i < bodies.size(); i++ )
		{
				integrateForces( bodies.get( i ), dt );
		}
		
		//******************************
		for (int i = 0; i < bodies2.size(); i++ )
		{
				integrateForces( bodies2.get( i ), dt );
		}
		//*****************************************

		// Initialize collision
		for (int i = 0; i < contacts.size(); ++i)
		{
			contacts.get( i ).initialize();
		}

		// Solve collisions
		for(int j = 0; j < iterations; ++j)
		{
			for (int i = 0; i < contacts.size(); ++i)
			{
				contacts.get( i ).applyImpulse();
			}
		}

		// Integrate velocities
		for (int i = 0; i < bodies.size(); ++i)
		{
			integrateVelocity( bodies.get( i ), dt );
		}
		
		//******************************
		for (int i = 0; i < bodies2.size(); ++i)
		{
			integrateVelocity( bodies2.get( i ), dt );
		}
		//*****************************

		// Correct positions
		for (int i = 0; i < contacts.size(); ++i)
		{
			contacts.get( i ).positionalCorrection();
		}

		// Clear all forces
		for (int i = 0; i < bodies.size(); ++i)
		{
			Body b = bodies.get( i );
			b.force.set( 0, 0 );
			b.torque = 0;
		}
		//*************************************
		for (int i = 0; i < bodies2.size(); ++i)
		{
			Body b = bodies.get( i );
			b.force.set( 0, 0 );
			b.torque = 0;
		}
		//*****************************************
	}

	public Body add( Shape shape, int x, int y )
	{
		Body b = new Body( shape, x, y );
		bodies.add( b );
		return b;
	}
	
	//********************
	public Body add2( Shape shape, int x, int y )
	{
		Body b = new Body( shape, x, y );
		bodies.set( 0, b );
		return b;
	}
	//*********************
	
	public Body add( Body body )
	{
		Body b = body;
		return b;
	}

	public void clear()
	{
		contacts.clear();
		bodies.clear();
	}

	// Acceleration
	// F = mA
	// => A = F * 1/m

	// Explicit Euler
	// x += v * dt
	// v += (1/m * F) * dt

	// Semi-Implicit (Symplectic) Euler
	// v += (1/m * F) * dt
	// x += v * dt

	// see http://www.niksula.hut.fi/~hkankaan/Homepages/gravity.html

	public void integrateForces( Body b, float dt )
	{
//		if(b->im == 0.0f)
//			return;
//		b->velocity += (b->force * b->im + gravity) * (dt / 2.0f);
//		b->angularVelocity += b->torque * b->iI * (dt / 2.0f);

		if (b.invMass == 0.0f)
		{
			return;
		}

		float dts = dt * 0.5f;

		if ( Math.abs( b.velocity.x ) < 0.01f && Math.abs( b.velocity.y ) < 0.01f ) // IMPORTANT without it structures will crumble by itself
		{
			b.velocity.set( 0, 0 );
			return;
		}
		else
			b.velocity.addsi( b.force, b.invMass * dts );
		
		b.velocity.addsi( ImpulseMath.GRAVITY, dts );
		
		if ( Math.abs( b.angularVelocity ) < 0.001f ) // IMPORTANT without it structures will crumble by itself.
		{
			b.angularVelocity = 0;
			return;
		}
		else
			b.angularVelocity += b.torque * b.invInertia * dts;
		
	}
	
	public void integrateVelocity( Body b, float dt )
	{
//		if(b->im == 0.0f)
//			return;
//		b->position += b->velocity * dt;
//		b->orient += b->angularVelocity * dt;
//		b->SetOrient( b->orient );
//		IntegrateForces( b, dt );

		if (b.invMass == 0.0f)
		{
			return;
		}

		b.position.addsi( b.velocity, dt );
		b.orient += b.angularVelocity * dt;
		b.setOrient( b.orient );

		integrateForces( b, dt );
	}

}
