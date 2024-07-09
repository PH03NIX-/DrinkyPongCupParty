package com.ph_3nix.drinkypongcupparty.gameplay;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Iterator;

public class Cup {

    private float width, height;

    /*private CircleShape shape;*/
    private FixtureDef fixtureDef;
    private BodyDef bodyDef;
    private Body cupBody;

    private Texture cupTexture;
    private Sprite cupSprite;
    private com.ph_3nix.drinkypongcupparty.gameplay.BodyEditorLoader cupLoader;
    private Vector2 position;
    private World world;
    private int depth;
    private Sound holeInOne;
    /*BitmapFont theFont;
    GlyphLayout textLayout;
    public FreeTypeFontGenerator generator;
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter;*/

    public Cup(World world, Texture texture, com.ph_3nix.drinkypongcupparty.gameplay.BodyEditorLoader loader, Vector2 position, float width, float height, int depth, Sound holeInOne/*, BitmapFont theFont, GlyphLayout textLayout, FreeTypeFontGenerator generator, FreeTypeFontGenerator.FreeTypeFontParameter parameter*/) {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.log("#info", "building cup with params:"+position.x+position.y);

        this.width = width;
        this.height = height;
        this.position = position;
        this.world = world;
        this.depth = depth;
        Gdx.app.log("#info", "segment 1");

        cupTexture = texture;
        cupTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        cupSprite = new Sprite(texture);
        cupSprite.setSize(width, height);
        cupLoader = loader;
        Gdx.app.log("#info", "segment 2");

        this.holeInOne = holeInOne;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; //DynamicBody;
        bodyDef.position.set(position);
        Gdx.app.log("#info", "segment 3");

        fixtureDef = new FixtureDef();
        //fixtureDef.shape = shape;
        fixtureDef.density = 0.7f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.6f;
        Gdx.app.log("#info", "segment 4");
//16 18
        cupBody = world.createBody(bodyDef);
        Gdx.app.log("#info", "segment 5");
        cupLoader.attachFixture(cupBody, "cup.png", fixtureDef, width);
        Gdx.app.log("#info", "segment 6");

        /*circleBody.createFixture(fixtureDef);
        //circleBody.applyForce(velocity.x, velocity.y, 0, 0, true);
        circleBody.applyForceToCenter(velocity.x, velocity.y, true);*/
    }

    public int getIsBalled(ArrayList<Pong> pongs) {
        Iterator<Pong> itr = pongs.iterator();
        while( itr.hasNext() ) {
            Pong pong = itr.next();
            if( pong.getY() > position.y + width*.25f && pong.getY() < position.y + width*.75f && pong.getX() > position.x + height*.25f && pong.getX() < position.x + height*.75f ) {
                pong.dispose();
                itr.remove();
                holeInOne.play(3f, 2, 0);
                if( depth > 1 ) {
                    depth = depth -1;
                    return 2;
                }
                else {
                    world.destroyBody(cupBody);
                    return 1;
                }
            }
        }
        return 0;
    }

    public void draw( Batch batch, BitmapFont theFontCups, GlyphLayout textLayout, FreeTypeFontGenerator generator, FreeTypeFontGenerator.FreeTypeFontParameter parameter, Camera camera, Camera parentCamera) {
        cupSprite.setPosition(cupBody.getPosition().x , cupBody.getPosition().y /*- width*/);
        cupSprite.draw(batch);


        if(depth > 1){
            batch.setProjectionMatrix(parentCamera.combined);
            String levelText = ""+depth;
            textLayout.setText(theFontCups, levelText);
            theFontCups.draw(batch, levelText, camera.project(new Vector3(cupBody.getPosition().x, 0, 0)).x + parentCamera.viewportWidth/34 - textLayout.width/2
                    ,camera.project(new Vector3(0, cupBody.getPosition().y, 0)).y + parentCamera.viewportWidth/34* 1.22449f );
            batch.setProjectionMatrix(camera.combined);
        }
    }

    public void dispose() {
        //cupTexture.dispose();
        world.destroyBody(cupBody);
        //cupSprite = null;
        /*cupLoader = null;
        bodyDef = null;
        fixtureDef = null;
        cupBody = null;
        cupLoader = null;*/
    }
}
