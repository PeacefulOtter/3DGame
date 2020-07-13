package peacefulotter.engine.physics;


import peacefulotter.engine.components.GameObject;
import peacefulotter.engine.components.PhysicsObject;
import peacefulotter.engine.physics.colliders.*;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine
{
    private List<PhysicsObject> physicsObjects;

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
            PhysicsObject objectI = physicsObjects.get( i );
            for ( int j = i + 1; j < numObjects; j++ )
            {
                PhysicsObject objectJ = physicsObjects.get( j );
                IntersectData intersectData = Intersections.intersect( objectI.getCollider(), objectJ.getCollider() );
                if ( intersectData.getDoesIntersect() )
                    objectI.interactWith( objectJ, intersectData );
            }
        }
    }

    public void simulate( GameObject root, float deltaTime )
    {
        root.simulateAll( deltaTime );
        handleCollisions();
    }
}
