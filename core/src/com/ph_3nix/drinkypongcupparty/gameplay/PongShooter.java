package com.ph_3nix.drinkypongcupparty.gameplay;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class PongShooter extends Actor {
    Body tableBody;

    PongShooter(Camera camera, World world) {
        BodyDef tableBodyDef = new BodyDef();
        // Set its world position
        tableBodyDef.position.set(new Vector2(camera.viewportWidth/2*(-1) /*- (camera.viewportWidth - 0.01905f)*/,camera.viewportHeight/2*(-1) /*+ 0.01f*/ ));

        // Create a body from the definition and add it to the world
        tableBody = world.createBody(tableBodyDef);

        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(camera.viewportWidth , camera.viewportHeight );  // 2.74 / 9 ft is table length, 0.01905 / 3/4 inch thick
        // Create a fixture from our polygon shape and add it to our ground body
        tableBody.createFixture(groundBox, 0.0f);
        // Clean up after ourselves
        groundBox.dispose();
        setTouchable(Touchable.enabled);
        addListener(new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                System.out.println("touchdown original");
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                System.out.println("touchup original");
            }
        });
    }
}
