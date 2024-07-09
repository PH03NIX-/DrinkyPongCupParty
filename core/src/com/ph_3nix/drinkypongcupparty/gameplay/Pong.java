package com.ph_3nix.drinkypongcupparty.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Pong {

    private float radius;

    private CircleShape shape;
    private FixtureDef fixtureDef;
    private BodyDef bodyDef;
    private Body circleBody;
    private Sprite pongSprite;
    private World world;
    private boolean isLow;
    private boolean isDown;
    private float lastY;
    private float currentDensity;
    private float currentBounce;
    private Vector2 prevPosition;
    private Sound bounce;

    public Pong(World world, Texture texture, Vector2 position, Vector2 velocity, Sound bounce) {
        isLow = false;
        isDown = false;
        lastY = Gdx.input.getY();
        currentDensity = 0.00075f;
        currentBounce = 0;
        prevPosition = position;

        pongSprite = new Sprite(texture);
        //this.radius = .0381f;
        radius = 2*0.0115f;
        this.world = world;
        this.bounce = bounce;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        shape = new CircleShape();
        shape.setRadius(radius);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        //fixtureDef.density = 0.005f;
        fixtureDef.density = currentDensity;
        fixtureDef.friction = 0.01f;
        fixtureDef.restitution = 0.845f;

        circleBody = world.createBody(bodyDef);
        circleBody.createFixture(fixtureDef);
        //circleBody.applyForce(velocity.x, velocity.y, 0, 0, true);
        circleBody.applyForceToCenter(velocity.x, velocity.y, true);
        shape.dispose();
    }

    public void draw(Batch batch) {
        this.physicsFix();
        pongSprite.setPosition(circleBody.getPosition().x - radius, circleBody.getPosition().y - radius);
        pongSprite.setSize( radius*2, radius*2 );
        pongSprite.draw(batch);

        //batch.draw(texture, circleBody.getPosition().x - radius, circleBody.getPosition().y - radius,radius * 2, radius * 2); //
    }

    private void physicsFix() {
        Gdx.app.log("#info", "vars: "+circleBody.getPosition().y+","+lastY);
        if(currentBounce == 30) {
            //circleBody.destroyFixture(circleBody.getFixtureList().first());
            fixtureDef.restitution = 0f;
            //circleBody.createFixture((fixtureDef));
        }
        if(circleBody.getPosition().y < lastY ) {
            Gdx.app.log("#info", "attempten");
            if(isDown = false && lastY <= -0.8001f) {
                circleBody.destroyFixture(circleBody.getFixtureList().first());
                fixtureDef.density += 0.00001;
                circleBody.createFixture((fixtureDef));
                Gdx.app.log("#info", "density "+circleBody.getFixtureList().first().getDensity());
            }
            isDown = true;
        }
        else {
            if (isDown) {
                bounce.play(currentBounce < 30 ? 3f - (currentBounce*0.1f) : 0f, 2f - (currentBounce*0.01f), 0);
                currentBounce++;
            }
            isDown = false;
        }

        lastY = circleBody.getPosition().y;
    }


    public float getY() {
        return circleBody.getPosition().y;
    }

    public void setY(float y) {
        circleBody.getPosition().y = y;
    }

    public float getX() {
        return circleBody.getPosition().x;
    }

    public boolean hasMoved() {
        if( getX() == prevPosition.x && getY() == prevPosition.y ) {
            return false;
        }
        return true;
    }

    public void dispose() {
        world.destroyBody(this.circleBody);
        //shape.dispose();  --this causes crashes?
    }
}
