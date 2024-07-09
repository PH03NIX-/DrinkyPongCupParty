package com.ph_3nix.drinkypongcupparty.gameplay;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.ph_3nix.drinkypongcupparty.levels.LevelSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Gameplay extends LevelSelection {
    private static final float VIEWPORT_WIDTH = 10;
    private static final float SCALE = 15f;
    final int BACK_KEY = Input.Keys.BACK;

    float w, h;
    //Batch batch;
    Sprite pongSprite, tableSprite, cupSprite;
    Texture img;
    Texture /*pongTexture,*/ tableTexture, cupTexture;
    World world;
    Box2DDebugRenderer debugRenderer;
    Camera camera;
    CircleShape circle;
    Body circleBody, tableBody;
    Pong pong;
    CupLoader cupLoader;
    ArrayList<Cup> cups;
    ArrayList<Pong> activePongs;
    Vector2 unprojectedXY, unprojectedDelta;
    InputListener inputListener;
    PongShooter pongShooter;
    boolean isTouched, isForIncrementLevel, isOver;
    float lastDeltaX, lastDeltaY, currentDeltaTime;
    long levelNum/*, bottleCapCount*/;
    long currentBalls;
    Sound bounce, holeInOne, yay, applause, almost, ow;
    //Preferences bottleCaps;

    public Gameplay() {
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        isTouched = false;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.log("#info", "w:"+w);
        Gdx.app.log("#info", "h:"+h);
        Box2D.init();
        world = new World(new Vector2(0, -10), true);

        debugRenderer = new Box2DDebugRenderer(); //remove in prod

        camera = new OrthographicCamera(3.3496f,1.92024f); // 11 x 6.3ft   //2.74 / 9 ft is table width
        //new FitViewport(3,2.8f, camera);
        //camera.position.set(0, 0, 0);
        camera.update();

        /*createBall();*/
        createTable();
        //createSprites();
        tableTexture = new Texture(/*"pongtable.png"*/"tableDark3.png" );
        cupTexture = new Texture("cup.png");
        pongSprite = new Sprite(pongTexture);
        tableSprite = new Sprite(tableTexture);

        pongSprite.setPosition(0,0);
        pongSprite.setSize(50f, 50f );
        activePongs = new ArrayList<Pong>();

        //tableSprite.setSize(camera.viewportWidth, camera.viewportHeight/20);
        tableSprite.setSize(camera.viewportWidth/10, camera.viewportHeight/5);
        //tableSprite.setPosition(camera.viewportWidth/2*(-1) + 1.6096f*.625f, /*tableBody.getPosition().y-camera.viewportHeight/20/2*/camera.viewportHeight/2*(-1)-0.1215f);
        tableSprite.setPosition(-3.37372857f, 3.0035560096f);

        cupSprite = new Sprite(cupTexture);

        bounce = Gdx.audio.newSound(Gdx.files.internal("bounce.wav"));
        holeInOne = Gdx.audio.newSound(Gdx.files.internal("holeInOne.wav"));
        yay = Gdx.audio.newSound(Gdx.files.internal("yay.wav"));
        applause = Gdx.audio.newSound(Gdx.files.internal("applause.wav"));
        almost = Gdx.audio.newSound(Gdx.files.internal("almost.wav"));
        ow = Gdx.audio.newSound(Gdx.files.internal("ow.wav"));
        //BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("cup.json"));
        //cup = new Cup(world, cupTexture, loader, camera, new Vector2(/*camera.viewportWidth/2*(-1)*/0, 0), new Vector2(0/*0.00035f, 0.0007f*/, 0) );
        //cupLoader = new CupLoader(world, cupTexture, loader, camera, /*level*/ levelNum);
        //pong = new Pong(world, pongTexture, new Vector2(camera.viewportWidth/2*(-1), 0), new Vector2(0.00035f, 0.0007f) );
        unprojectedXY = new Vector2();
        unprojectedDelta = new Vector2();
    }

    public void createGameplay(/*Batch currentBatch,*/ long selectedLevel, Long bottleCapCount, boolean isForIncrementLevel/*, /*long bottleCapCount,*/ /*Preferences bottleCaps*/) {
        Gdx.app.log("#info", "creating the gameplay");
        //this.bottleCaps = bottleCaps;
        //this.bottleCapCount = bottleCapCount;
        //batch = currentBatch;
        currentBalls = super.ballsCount;
        this.isForIncrementLevel = isForIncrementLevel;
        yay.play(0.7f, 1, 0);
        this.bottleCapCount = bottleCapCount;
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("cup.json"));
        levelNum = selectedLevel;
        Gdx.input.setCatchBackKey(true);
        cupLoader = new CupLoader(world, cupTexture, loader, camera, /*level*/ selectedLevel, holeInOne);
        cups = cupLoader.getCups();
        Gdx.app.log("#info", "initial cupsize:"+cups.size());
        currentDeltaTime = 0;
        isOver = false;

        //Collections.reverse(cups);
    }

    public void drawGameplay(Batch batch, Camera parentCamera/*, BitmapFont theFont, GlyphLayout textLayout, FreeTypeFontGenerator generator, FreeTypeFontGenerator.FreeTypeFontParameter parameter*/) {
        //batch = thisBatch;
        //Matrix4 prevProj = batch.getProjectionMatrix();

        Gdx.gl.glClearColor(0, 0.6f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        unprojectedXY = convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
        unprojectedDelta = convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getDeltaX(), Gdx.input.getDeltaY(), 0)));

        world.step(1/60f, 6, 2);

        if(currentBalls > 0 && isTouched && ( !Gdx.input.isTouched() || convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0))).x > -1.1396569)) {
            activePongs.add(new Pong( world, pongTexture, convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0))), /*unprojectedDelta*/ new Vector2(/*0.00035f, 0.0007f*/ lastDeltaX*0.000001f/*0.00001f*/, lastDeltaY*(-0.000001f/*-0.00001f*/)), bounce ));
            currentBalls--;
            if(currentBalls < 3 ) {
                //Gdx.app.log("#info", "pitch:"+(1.2f+(0.1f*(2f-currentBalls))));
                almost.play( 0.6f, 1.2f+(0.1f*(2f-currentBalls)), 1);
            }
        }
        else if(currentBalls > 0 && isTouched && ( Gdx.input.getX() <= 0 && lastDeltaX < 0)) {
            activePongs.add(new Pong( world, pongTexture, convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0))), /*unprojectedDelta*/ new Vector2(/*0.00035f, 0.0007f*/ -1*lastDeltaX*0.000001f/*0.00001f*/, -1*lastDeltaY*(-0.0000015f/*-0.00001f*/)), bounce ));
            currentBalls--;
            if(currentBalls < 3 ) {
                almost.play( 0.6f, 1.2f+(0.1f*(2f-currentBalls)), 1);
            }
        }

        if(Gdx.input.isTouched() && convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0))).x <= -1.1396569) {
            isTouched = true;
            lastDeltaX = Gdx.input.getDeltaX();
            lastDeltaY = Gdx.input.getDeltaY();
        }
        else {
            isTouched = false;
        }
        if(Gdx.input.isTouched()) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
            Gdx.app.log("#info", "x:"+convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0))).x);
            Gdx.app.log("#info", "y:"+convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0))).y+"|"+camera.viewportHeight*(-1));
        }
        //if(Gdx.input.isTouched() /*&& unprojectedXY.x >= 1.6096 && unprojectedXY.x - unprojectedDelta.x < 1.6096*/) {
        //	activePongs.add(new Pong( world, pongTexture, convertV3ToV2(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0))), /*unprojectedDelta*/ new Vector2(/*0.00035f, 0.0007f*/ Gdx.input.getDeltaX()*0.00003f, Gdx.input.getDeltaY()*(-0.00003f)) ));
		/*	Gdx.app.setLogLevel(Application.LOG_DEBUG);
			Gdx.app.log("#info", "x:"+Gdx.input.getDeltaX()*0.000025f);
			Gdx.app.log("#info", "y:"+Gdx.input.getDeltaY()*(-0.000025f));
			Gdx.app.log("#info", "x:"+unprojectedDelta.x);
			Gdx.app.log("#info", "y:"+unprojectedDelta.y);
			Gdx.app.log("#info", "x:"+Gdx.input.getDeltaX());
			Gdx.app.log("#info", "y:"+Gdx.input.getDeltaY());
		}*/
        batch.setProjectionMatrix(camera.combined);
        //batch.begin();
			/*camera.translate((float)Gdx.input.getDeltaX()*(-1), (float)Gdx.input.getDeltaY(), 0);
		//((OrthographicCamera)this.stage.getCamera()).zoom += .01;
			camera.update();*/

        /*Iterator<Cup> cupItr = cups.iterator();
        while( cupItr.hasNext() ) {
            //Gdx.app.log("#info", "pongx:"+Gdx.input.getX());
            //Gdx.app.log("#info", "pongx:"+pong.getY());
            Cup cup = cupItr.next();
            if( cup.getIsBalled(activePongs) ) {
                Gdx.app.log("#info", "pre:"+cups.size());
                Gdx.app.log("#info", "was true!");
                cupItr.remove();
                Gdx.app.log("#info", "post:"+cups.size());
            }
            else {
                cup.draw(batch, theFontCups, textLayout, generator, parameter, camera, parentCamera);
            }
        }*/
        /*Texture testTexture;
        testTexture = new Texture("table.png");*/
        batch.draw(tableTexture, -1.1413453f, camera.viewportHeight/2*(-1),camera.viewportWidth, camera.viewportHeight/5);


        /*textLayout.setText(theFontCups, "testing"+bottleCapCount);
        //theFontCups.draw(batch, "testing"+bottleCapCount, -1.1413453f, camera.viewportHeight/4*(-1) );
        theFontCups.draw(batch, "testing"+bottleCapCount, camera.viewportHeight/4*(-1),camera.viewportHeight/4*(-1) );
*/

        /*

            textLayout.setText(theFontScores, ""+bottleCapCount);
            //batch.draw(bottleCapTexture, 151-displayWidth/34-10, displayHeight-45-displayWidth/34, displayWidth/34, displayWidth/34);
            batch.draw(bottleCapTexture, 2*textLayout.height - textLayout.height-10, displayHeight-textLayout.height-textLayout.height/2, textLayout.height, textLayout.height);
            theFontScores.draw(batch, ""+bottleCapCount, 2*textLayout.height, displayHeight-textLayout.height/2 );
            //batch.draw(pongTexture, 302+textLayout.width-displayWidth/34-10, displayHeight-45-displayWidth/34, displayWidth/34, displayWidth/34);
            batch.draw(pongTexture, 4*textLayout.height + textLayout.width-textLayout.height-10, displayHeight-textLayout.height-textLayout.height/2, textLayout.height, textLayout.height);
            theFontScores.draw(batch, ""+ballsCount, 4*textLayout.height+textLayout.width, displayHeight-textLayout.height/2 );
*/
        batch.setProjectionMatrix(parentCamera.combined);
        /*batch.draw(bottleCapTexture, camera.project(new Vector3(camera.viewportWidth/2*(-1), 0, 0)).x +151 - camera.project(new Vector3(camera.viewportWidth, 0, 0)).x/48-12//+ parentCamera.viewportWidth/34 - textLayout.width/2
                ,camera.project(new Vector3(0, camera.viewportHeight/2 , 0)).y - 45 - camera.project(new Vector3(camera.viewportWidth, 0, 0)).x/48
                , camera.project(new Vector3(camera.viewportWidth, 0, 0)).x/48
                , camera.project(new Vector3(camera.viewportWidth, 0, 0)).x/48 );*/
        String bottleCapText = ""+bottleCapCount;
        textLayout.setText(theFontScores, bottleCapText);
        batch.draw(bottleCapTexture, 3*textLayout.height - textLayout.height-10, displayHeight-textLayout.height-textLayout.height/2, textLayout.height, textLayout.height);

        theFontScores.draw(batch, bottleCapText, 3*textLayout.height, displayHeight-textLayout.height/2 );
        batch.draw(pongTexture, 6*textLayout.height + textLayout.width-textLayout.height-10, displayHeight-textLayout.height-textLayout.height/2, textLayout.height, textLayout.height);
        theFontScores.draw(batch, ""+currentBalls, 6*textLayout.height+textLayout.width, displayHeight-textLayout.height/2 );
        batch.setProjectionMatrix(camera.combined);

        Collections.reverse(cups);
        Iterator<Cup> cupItrBallcheck = cups.iterator();
        int ballcheckResult = 0;
        while( cupItrBallcheck.hasNext() ) {
            Cup cup = cupItrBallcheck.next();
            ballcheckResult = cup.getIsBalled(activePongs);
            Gdx.app.log("#info", "check result:"+ballcheckResult);
            if( ballcheckResult == 1 ) {
                cupItrBallcheck.remove();
                //bottleCaps.flush();
                /*bottleCaps.putLong("bottleCaps", bottleCapCount);
                bottleCaps.flush();*/
            }
            if( ballcheckResult == 1 || ballcheckResult == 2 ) {
                bottleCapCount = bottleCaps.getLong("bottleCaps", 0);
                Gdx.app.log("#info", "check pre count:"+bottleCapCount);
                bottleCapCount += 1;
                Gdx.app.log("#info", "check post count:"+bottleCapCount);
                super.writeBottleCapCount(bottleCapCount);
            }
        }

        Collections.reverse(cups);
        Iterator<Cup> cupItrRender = cups.iterator();
        while( cupItrRender.hasNext() ) {
            Cup cup = cupItrRender.next();
            cup.draw( batch, theFontCups, textLayout, generator, parameter, camera, parentCamera);
        }

        Iterator<Pong> itr = activePongs.iterator();
        while( itr.hasNext() ) {
            //Gdx.app.log("#info", "pongx:"+Gdx.input.getX());
            //Gdx.app.log("#info", "pongx:"+pong.getY());
            Pong pong = itr.next();
            if( pong.getY() > -50 ) {
                pong.draw(batch);
            }
            else {
                pong.dispose();
                itr.remove();
            }
        }
        //Gdx.app.log("#info", "pong count:"+activePongs.size());
			/*for( Cup cup: cups) {
				cup.draw(batch);
			}*/
        //Gdx.app.log("#info", "pong count:"+activePongs.size());
        //tableSprite.setPosition(camera.viewportWidth/2*(-1) + 1.6096f*.625f, /*tableBody.getPosition().y-camera.viewportHeight/20/2*/camera.viewportHeight/2*(-1)-0.1215f);

        //tableSprite.setSize(camera.viewportWidth, camera.viewportHeight/20);
        //tableSprite.setSize(camera.viewportWidth/10, camera.viewportHeight/5);
        //tableSprite.setPosition(camera.viewportWidth/2*(-1) + 1.6096f*.625f, /*tableBody.getPosition().y-camera.viewportHeight/20/2*/camera.viewportHeight/2*(-1)-0.1215f);
        //tableSprite.setPosition(-0.37372857f, 0.0035560096f);
        tableSprite.draw(batch);
        //batch.draw(tableSprite, camera.viewportWidth/2*(-1) + 1.6096f*.625f, /*tableBody.getPosition().y-camera.viewportHeight/20/2*/camera.viewportHeight/2*(-1)-0.1215f ,camera.viewportWidth, camera.viewportHeight/20 );
        //batch.draw(pongSprite, circleBody.getPosition().x - radius, circleBody.getPosition().y - radius,radius * 2, radius * 2);
        //debugRenderer.render(world, camera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());
        //batch.end();
        //tableSprite.setPosition(tableBody.getPosition().x - 25f, tableBody.getPosition().y - 25f);
        world.step(1/60f, 6, 2);
        //batch.setProjectionMatrix(prevProj);
        if( currentBalls == 0 ) {
            isOver = true;
            for( Pong pong : activePongs) {
                if ( pong.hasMoved() ) {
                    isOver = false;
                }
            }
            currentDeltaTime += Gdx.graphics.getDeltaTime();
            //Gdx.app.log("#info", "delta:"+currentDeltaTime);
        }
        if(isOver || cups.size() == 0 || Gdx.input.isKeyPressed(BACK_KEY) || currentDeltaTime > 6f || (currentBalls <= 0 && activePongs.size() == 0) ) {
            if(cups.size() == 0) {
                applause.play(2,1,0);
                if(isForIncrementLevel) {
                    super.writeMaxLevelIncrement();
                    //super.updateSprite();
                }
            }
            else {
                ow.play(0.7f, 1, 1);
            }
            this.disposeGameplay();
            batch.setProjectionMatrix(parentCamera.combined);
            isFirstTouched = true;
            isGameplay = false;
        }
    }
    private float accumulator = 0;
    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 1/60f) {
            world.step(1/60f, 6, 2);
            accumulator -= 1/60f;
        }
    }

    private Vector2 convertV3ToV2( Vector3 v3 ) {
        return new Vector2(v3.x, v3.y);
    }

    public void createTable() {
        // Create our body definition
        BodyDef tableBodyDef = new BodyDef();
        // Set its world position
        tableBodyDef.position.set(new Vector2(/*camera.viewportWidth/2*(-1) -*/ 1.6096f /*- (camera.viewportWidth - 0.01905f)*/,camera.viewportHeight/2*(-1) /*+ 0.01f*/ ));

        // Create a body from the definition and add it to the world
        tableBody = world.createBody(tableBodyDef);

        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(2.74f , 0.01905f );  // 2.74 / 9 ft is table length, 0.01905 / 3/4 inch thick
        // Create a fixture from our polygon shape and add it to our ground body
        tableBody.createFixture(groundBox, 0.0f);
        // Clean up after ourselves
        groundBox.dispose();
    }

    public long getLevel() {
        return levelNum;
    }

    public void setCurrentBalls(Long currentBalls) {
        this.currentBalls = currentBalls;
    }

    public void disposePongs() {
        for( Pong pong : activePongs) {
            pong.dispose();
        }
    }

    public void disposeGameplay () {
        //batch.dispose();
        /*img.dispose();
        world.dispose();
        pongTexture.dispose();
        cupTexture.dispose();
        tableTexture.dispose();*/
        /*for( Pong pong : activePongs){
            pong.dispose();
        }*/
        for( Cup cup : cups )
            cup.dispose();

        /*bounce.dispose();
        holeInOne.dispose();*/
        for(Pong pong : activePongs)
            pong.setY(-9999999);
    }

}
