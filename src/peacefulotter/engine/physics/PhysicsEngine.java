package peacefulotter.engine.physics;


import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.physics.colliders.PhysicsObject;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine
{
    List<PhysicsObject> physicsObjects;

    public PhysicsEngine()
    {
        physicsObjects = new ArrayList<>();
    }

    public void addPhysicObject( PhysicsObject physicsObject )
    {
        physicsObjects.add( physicsObject );
    }

    public void handleCollisions()
    {
        int numObjects = physicsObjects.size();

        for ( int i = 0; i < numObjects; i++ )
        {
            PhysicsObject sphereI = physicsObjects.get( i );
            for ( int j = i + 1; j < numObjects; j++ )
            {
                PhysicsObject sphereJ = physicsObjects.get( j );
                IntersectData intersectData = sphereI.getBoundingSphere()
                        .intersectBoundingSphere( sphereJ.getBoundingSphere() );
                if ( intersectData.getDoesIntersect() )
                {
                    sphereI.interactWith( sphereJ );
                    sphereI.setVelocity( sphereI.getVelocity().mul( -1 ) );
                    sphereJ.setVelocity( sphereJ.getVelocity().mul( -1 ) );
                }
            }
        }
    }

    public void simulate( GameObject root, float deltaTime )
    {
        root.simulateAll( deltaTime );
        handleCollisions();
    }
}
