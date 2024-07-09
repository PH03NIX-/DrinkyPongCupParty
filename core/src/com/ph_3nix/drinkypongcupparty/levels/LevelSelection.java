package com.ph_3nix.drinkypongcupparty.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.ph_3nix.drinkypongcupparty.AdsController;
import com.ph_3nix.drinkypongcupparty.gameplay.Gameplay;
import com.ph_3nix.drinkypongcupparty.levels.Level;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by david on 2/22/2021.
 */

public class LevelSelection {
    private float displayWidth, deltaX;
    protected float displayHeight;
    protected ArrayList<Level> baseLevels;
    private Texture levelPanel, levelSelectedPanel, locked;
    protected Texture pongTexture, bottleCapTexture;
    public static boolean isGameplay, isGameplayPrev;

    public static BitmapFont theFont;
    protected static BitmapFont theFontCups;
    protected static BitmapFont theFontScores;
    public static GlyphLayout textLayout;
    public static FreeTypeFontGenerator generator;
    public static FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    public Gameplay gameplay;
    public static boolean isTouched, isFirstTouched;
    private float deltaAccumulator;
    protected Preferences bottleCaps, balls, maxLevelPreferences;
    protected long bottleCapCount, ballsCount;
    final int BACK_KEY = Input.Keys.BACK;
    private AdsController adsController;
    //public Preferences maxLevelPreferences;
    long maxLevel/*, maxLevelPrev*/;
    TextButton moreBallsButton, watchAdButton, buyCapsButton;
    BitmapFont arialFont;
    Sound chaching, click;

    public LevelSelection() {
        displayWidth = Gdx.graphics.getWidth();
        displayHeight = Gdx.graphics.getHeight();
        levelSelectedPanel = new Texture("levelselectedbackground.png");
        levelPanel = new Texture("levelbackground22.png");
        locked = new Texture("locked.png");
        pongTexture = new Texture("pong.png");
        bottleCapTexture = new Texture("bottleCaps.png"); ///placeholder
        bottleCaps = Gdx.app.getPreferences("bottleCaps");
        bottleCapCount = bottleCaps.getLong("bottleCaps", 0);
        balls = Gdx.app.getPreferences("balls");
        ballsCount = balls.getLong("balls", 10);
        maxLevelPreferences = Gdx.app.getPreferences("maxLevel");
        maxLevel = maxLevelPreferences.getLong("maxLevel", 1);
        isGameplay = false;
        isGameplayPrev = false;
        isTouched = true;
        isFirstTouched = true;
        deltaAccumulator = 0f;
        arialFont = new BitmapFont(Gdx.files.internal("arial.fnt"));

        baseLevels = new ArrayList<Level>();
        //maxLevelPrev = maxLevel;
    }

    public void createLevelSelection(Gameplay currentGameplay, AdsController adsController ) {
        this.adsController = adsController;
        moreBallsButton = createButton("MORE BALLS!"
                , Gdx.graphics.getWidth()/3 //- Gdx.graphics.getWidth()/3
                , Gdx.graphics.getHeight()/6
                , 0 //Gdx.graphics.getWidth()/24
                , Gdx.graphics.getHeight() / 3 * 2
                , 2f) ;
        watchAdButton = createButton("FREE CAPS!"
                , Gdx.graphics.getWidth()/3 //- Gdx.graphics.getWidth()/3
                , Gdx.graphics.getHeight()/6
                , Gdx.graphics.getWidth()/3
                , Gdx.graphics.getHeight() / 3 * 2
                , 2f) ;
        buyCapsButton = createButton("BUY CAPS!"
                , Gdx.graphics.getWidth()/3 //- Gdx.graphics.getWidth()/3
                , Gdx.graphics.getHeight()/6
                , Gdx.graphics.getWidth()/3 *2
                , Gdx.graphics.getHeight() / 3 * 2
                , 2f) ;


        textLayout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Hanken-Book.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)displayWidth/22; //100;//(int)displayHeight/10;
        parameter.color= new Color(0,0, 0, 1);
        theFont = generator.generateFont(parameter);

        parameter.size = (int)(displayWidth/34/1.8); //100;//(int)displayHeight/10
        parameter.color= new Color(255,255, 255, 1);
        theFontCups = generator.generateFont(parameter);

        parameter.size = (int)(displayWidth/34); //100;//(int)displayHeight/10
        parameter.color= new Color(0, 0, 0, 1);
        theFontScores = generator.generateFont(parameter);

        gameplay = currentGameplay;
        chaching = Gdx.audio.newSound(Gdx.files.internal("cha-ching3.wav"));
        click = Gdx.audio.newSound(Gdx.files.internal("bounce.wav"));
        //baseLevels = new ArrayList<Level>();
        deltaX = 0f;
        appendLevels( maxLevel-30, maxLevel+30, maxLevel, baseLevels, false);
    }

    public TextButton createButton(String text, float width, float height, float x, float y, float scale) {
        Skin skin;
        TextButton.TextButtonStyle buttonStyle;
        TextButton myButton;
        Touchable touchable;

        skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("button.atlas"));
        skin.addRegions(atlas);

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = arialFont;
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.up = new NinePatchDrawable(skin.getPatch("button-up"));
        buttonStyle.down = new NinePatchDrawable(skin.getPatch("button-down"));

        myButton = new TextButton(text, buttonStyle);
        //myButton.setColor(0, 0.5f, 0.5f, 1);   todo - changes other sprite textures color need to fix
        myButton.getLabel().setFontScale(scale);
        myButton.setSize( width, height );
        myButton.setPosition( x, y );

        skin.dispose();

        return myButton;
    }

    public boolean isClicked( TextButton button, float x, float y ) {
        float sb =  Gdx.graphics.getHeight() - button.getY();
        float sl =  button.getX();
        float sr =  sl + button.getWidth();
        float st =  sb - button.getHeight();
        Gdx.app.log("#jt", st+"&"+Gdx.input.getY()+"&"+sb);
        Gdx.app.log("#jt", sl+"&"+Gdx.input.getX()+"&"+sr);
        //float x = Gdx.input.getX();
        //float y = Gdx.input.getY();
        return ( sl < x && sr > x && st < y && sb > y );
    }

    private void appendLevels( long start, long end, long currentLevel, ArrayList<Level> levelArray, boolean isTrim) {
        for( long i = start; i <= end; i++ ) {
            if( i > 0 ) {
                levelArray.add(new Level(i, (i <= currentLevel), currentLevel, displayWidth, displayHeight, (i <= maxLevel) ? levelPanel : locked, levelSelectedPanel, textLayout, /*generator, parameter,*/ theFont/*, locked*/));
                if( isTrim ){
                    levelArray.remove(0);
                }
            }
        }
    }

    private void prependLevels( long start, long end, long currentLevel, ArrayList<Level> levelArray, boolean isTrim) {
        for( long i = start; i >= end; i-- ) {
            if( i > 0 ) {
                levelArray.add(0, new Level(i, (i <= currentLevel), currentLevel, displayWidth, displayHeight, (i <= maxLevel) ? levelPanel : locked, levelSelectedPanel, textLayout, /*generator, parameter,*/ theFont/*, locked*/));
                if( isTrim ){
                    levelArray.remove(levelArray.size()-1);
                }
            }
        }
    }

    public void writeBottleCapCount( long newCount ) {
        bottleCaps.flush();
        bottleCaps.putLong("bottleCaps", newCount );
        bottleCaps.flush();
        bottleCapCount = newCount;
        Gdx.app.log("#info", "write bottle count:"+newCount);
        Gdx.app.log("#info", "write bottle count:"+bottleCapCount);
    }

    public void writeMaxLevelIncrement() {
        maxLevel++; // = maxLevelPreferences.getLong("maxLevel") + 1;
        maxLevelPreferences.flush();
        maxLevelPreferences.putLong("maxLevel", maxLevel );
        maxLevelPreferences.flush();
        Gdx.app.log("#info", "max level:"+maxLevel);
        //maxLevelPrev = maxLevel - 1;
        //updateSprite();
    }

    public void writeBalls(Long additionalBalls) {
        ballsCount += additionalBalls;
        balls.flush();
        balls.putLong("balls", ballsCount);
        balls.flush();
    }

    private Long getRequiredBottleCaps() {
        Long requiredAmount = new Long(30);
        Gdx.app.log("#info", "r1:"+requiredAmount);
        Gdx.app.log("#info", "rr:"+ballsCount/5);
        for(Long i = new Long(2); i < ballsCount/5; i++) {
            requiredAmount += requiredAmount/2;
            Gdx.app.log("#info", "r2:"+i+":"+requiredAmount);
        }
        Gdx.app.log("#info", "r3:"+requiredAmount);
        return requiredAmount;
    }



    /*public void updateSprite() {
        Gdx.app.log("#info", "ATTEMPTING:"+baseLevels.size());
        for (Level currentLevel : baseLevels) {
            Gdx.app.log("#info", "current level:"+currentLevel.getLevel());
            if( currentLevel.getLevel() == maxLevel ) {
                currentLevel.updateTexture(levelPanel);
            }
        }
    }*/

    public void drawLevelSelections( Batch batch, double currentSelection, Camera camera ) {
        /*if(Gdx.input.isKeyPressed(BACK_KEY)) {
        //if(Gdx.input.isKeyJustPressed(BACK_KEY)) {
            Gdx.app.log("#info", "caught back key");
            gameplay.disposeGameplay();
            isFirstTouched = true;
            isGameplay = false;
            batch.setProjectionMatrix(camera.combined);

            //this.createLevelSelection(gameplay.getLevel(), gameplay);
        }*/

        textLayout.setText(theFontScores, "observing:"+bottleCapCount);
        if( isGameplay != isGameplayPrev) {
            bottleCapCount = bottleCaps.getLong("bottleCaps", 0);
            if(!isGameplay) {
                maxLevel = maxLevelPreferences.getLong("maxLevel");
                if(maxLevel == 0)
                    maxLevel = 1;

                baseLevels.get((int) maxLevel - 1).updateTexture(levelPanel);
            }
            /*if(maxLevelPrev != maxLevel) {
                baseLevels.get((int)maxLevel-1).updateTexture(levelPanel);
                maxLevelPrev = maxLevel;
            }*/
        }
        isGameplayPrev = isGameplay;

        if(isGameplay) {
            //else {
            //prevMatrix = batch.getProjectionMatrix();
            gameplay.drawGameplay(batch, camera/*, theFont, textLayout, generator, parameter*/);
            //batch.setProjectionMatrix(prevMatrix);
            //}
        }
        else {
            moreBallsButton.draw(batch, 1);
            watchAdButton.draw(batch, 1);
            buyCapsButton.draw(batch, 1);
            /* ad logic */
            if( Gdx.input.justTouched() ) {
                Gdx.app.log("#jt", "just touched");
                if (isClicked(moreBallsButton, Gdx.input.getX(), Gdx.input.getY())) {
                    click.play(2f, 0.05f, 0);
                    Gdx.app.log("#jt", "more balls");
                    adsController.getMoreBalls(getRequiredBottleCaps(), bottleCaps.getLong("bottleCaps", 0));
                }
                else if (isClicked(watchAdButton, Gdx.input.getX(), Gdx.input.getY())) {
                    click.play(2f, 0.05f, 0);
                    if (adsController != null)
                        adsController.showRewardedVideo();
                }
                else if (isClicked(buyCapsButton, Gdx.input.getX(), Gdx.input.getY())) {
                    click.play(2f, 0.05f, 0);
                    adsController.getAvailableProducts();
                    Gdx.app.log("#clicked", "trying to buy");
                }
            }


            if( adsController.getReward() > 0 ) {
                writeBottleCapCount( this.bottleCapCount + (long)adsController.getReward());
                chaching.play(0.13f, 1, 0);
                adsController.setReward(0);
            }
            if( adsController.checkMoreBalls() > 0 ) {
                writeBottleCapCount(bottleCapCount - getRequiredBottleCaps());
                chaching.play(0.13f, 1, 0);
                writeBalls(new Long(adsController.checkMoreBalls()));
                adsController.setMoreBalls(0);
            }
            if( adsController.getBuyingCapsStage() > 0 ) {
                if(adsController.getBuyingCapsStage() == 2 ) {
                    float deltaTime = 0;
                    //for(int i = 0; i<10; i++){
                        for(float deltaT=0; deltaT < 20f; deltaT += Gdx.graphics.getDeltaTime()){
                            deltaTime += Gdx.graphics.getDeltaTime();
                            if(deltaTime > 5f) {
                                chaching.play(0.13f, 1, 0);
                                deltaTime = 0f;
                            }
                        }
                    //}
                    writeBottleCapCount(this.bottleCapCount + adsController.getBuyingCaps());
                    adsController.setBuyingCaps(0);
                    adsController.setBuyingCapsStage(0);
                }
            }
            //if( isGameplay != isGameplayPrev) {
                //Gdx.app.log("#info", "max level prev:"+maxLevelPrev+"|max level:"+maxLevel);
            /*Gdx.app.log("#info", "max level prev:"+maxLevelPrev+"|max level:"+maxLevel);
                if(maxLevelPrev != maxLevel) {
                    baseLevels.get((int)maxLevel-1).updateTexture(levelPanel);
                    maxLevelPrev = maxLevel;
                }*/
            //}

            textLayout.setText(theFontScores, "" + bottleCapCount);
            batch.draw(bottleCapTexture, 3 * textLayout.height - textLayout.height - 10, displayHeight - textLayout.height - textLayout.height / 2, textLayout.height, textLayout.height);
            theFontScores.draw(batch, "" + bottleCapCount, 3 * textLayout.height, displayHeight - textLayout.height / 2);

            batch.draw(pongTexture, 6 * textLayout.height + textLayout.width - textLayout.height - 10, displayHeight - textLayout.height - textLayout.height / 2, textLayout.height, textLayout.height);
            theFontScores.draw(batch, "" + ballsCount, 6 * textLayout.height + textLayout.width, displayHeight - textLayout.height / 2);

            if (baseLevels.get(baseLevels.size() - 1).getX() < this.displayWidth * 2) {
                appendLevels(baseLevels.get(baseLevels.size() - 1).getLevel() + 1, baseLevels.get(baseLevels.size() - 1).getLevel() + 15, (long) currentSelection, baseLevels, true);
            } else if (baseLevels.get(0).getX() > this.displayWidth * (-2)) {
                prependLevels(baseLevels.get(0).getLevel() - 1, baseLevels.get(0).getLevel() - 15, (long) currentSelection, baseLevels, true);
            }

            if (true /*Gdx.input.getDeltaX() < 0 || deltaX / displayWidth / 5 *(-100) > -15*/) {
                deltaX += Gdx.input.getDeltaX() * Gdx.graphics.getDeltaTime() * (displayWidth / 5 / 7);
            }

            for (Level currentLevel : baseLevels) {
                //if(Gdx.input.justTouched() ) if(adsController != null) adsController.showRewardedVideo();
                //if( currentLevel.getLevel() <= maxLevel ) {
                    if (!isGameplay && isTouched && !Gdx.input.isTouched() && !isFirstTouched && deltaAccumulator < (currentLevel.getSideLength() / 20) && (Gdx.input.getX() > currentLevel.getX() && Gdx.input.getX() < currentLevel.getX() + currentLevel.getSideLength() || Gdx.input.getY() > currentLevel.getY() && Gdx.input.getY() < currentLevel.getY() + currentLevel.getSideLength()) && Gdx.input.getY() > displayHeight / 2 && currentLevel.getLevel() <= maxLevel) {
                        Actions.touchable(Touchable.disabled);
                        //gameplay.disposePongs();
                        isGameplay = true;
                        if(currentLevel.getLevel() == 1)
                            adsController.instructions();
                        gameplay.createGameplay(currentLevel.getLevel(), bottleCapCount, (maxLevel == currentLevel.getLevel()));
                        gameplay.setCurrentBalls(ballsCount);
                    } else {
                        currentLevel.drawLevel(batch, currentSelection, deltaX);
                    }
                //}
            }

            if (isTouched && !Gdx.input.isTouched()) {
                deltaAccumulator = 0;
                Gdx.app.log("#info", "before vars: " + isTouched + "," + Gdx.input.isTouched() + "," + deltaAccumulator);
            } else if (isTouched && Gdx.input.isTouched()) {
                deltaAccumulator += Math.abs(Gdx.input.getDeltaX()) + Math.abs(Gdx.input.getDeltaX());
                Gdx.app.log("#info", "after vars: " + isTouched + "," + Gdx.input.isTouched() + "," + deltaAccumulator);
            }
            isTouched = Gdx.input.isTouched();
            if (!Gdx.input.isTouched()) {
                isFirstTouched = false;
            }
        }
    }
    public void dispose () {
        arialFont.dispose();
        levelPanel.dispose();
        levelSelectedPanel.dispose();
        locked.dispose();
        pongTexture.dispose();
        bottleCapTexture.dispose();
        chaching.dispose();
        click.dispose();
    }

}
