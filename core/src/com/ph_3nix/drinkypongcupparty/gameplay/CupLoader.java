package com.ph_3nix.drinkypongcupparty.gameplay;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CupLoader  {
    ArrayList<Cup> cups;
    float WIDTH = 0.193f;//0.092f;//.23f;
    //float perc =
    float HEIGHT = WIDTH * 1.22449f; // png width x height ratio
    float y;
    float x;
    long level;
    Sound holeInOne;
    /*BitmapFont theFont;
    GlyphLayout textLayout;
    public FreeTypeFontGenerator generator;
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter;*/

    public CupLoader(World world, Texture texture, com.ph_3nix.drinkypongcupparty.gameplay.BodyEditorLoader loader, Camera camera, long level, Sound holeInOne) {
        /*textLayout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Hanken-Book.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        theFont = generator.generateFont(parameter);*/

        cups = new ArrayList();
        y = camera.viewportHeight/2*(-1);
        x = camera.viewportWidth/2*(-1);
        this.level = level;
        this.holeInOne = holeInOne;

        int[][] usedPositions = new int[70][2];
        //Arrays.fill(usedPositions, 0);

        for( int i=0; i<level+2; i++) {
            // will add level/position logic here 20->22 21->
            int position = (((i+1*773%(i+1)*((int)level*4))%(i+2)+1)%68)+1;
            //int position = (((i+1*773%(i+1)*((int)level*7%(int)level*123))%(i+2)+1)%68)+1;
            usedPositions[position-1][0] = usedPositions[position-1][0] + 1;
            usedPositions[position-1][1] = position;
            //cups.add(new Cup( world, texture, loader, getPosition(position, camera), WIDTH, HEIGHT, usedPositions[position-1]/*, theFont, textLayout, generator, parameter*/));
        }
        Gdx.app.log("#info", "the array:"+usedPositions.toString());
        for( int i=0; i<70; i++) {
            if( usedPositions[i][0] > 0 ) {
                cups.add(new Cup( world, texture, loader, getPosition(usedPositions[i][1], camera), WIDTH, HEIGHT, usedPositions[i][0], holeInOne/*, theFont, textLayout, generator, parameter*/));
            }
        }


    }
//20(22)
    //y:-0.9458961
    //13(15)
    private Vector2 getPosition(int position, Camera camera) {
        if( position <= 10 ) {
            y = camera.viewportHeight/2*(-1) + 0.01905f + HEIGHT*0;
            x = camera.viewportWidth/2*(-1) + WIDTH*7 + position%10*WIDTH;
        }
        else if( position <= 20 ) {
            y = camera.viewportHeight/2*(-1) + 0.01905f + HEIGHT*1;
            x = camera.viewportWidth/2*(-1) + WIDTH*7 + position%10*WIDTH + WIDTH/2;
        }
        else if( position <= 30 ) {
            y = camera.viewportHeight/2*(-1) + 0.01905f + HEIGHT*2;
            x = camera.viewportWidth/2*(-1) + WIDTH*7 + position%10*WIDTH;
        }
        else if( position <= 40 ) {
            y = camera.viewportHeight/2*(-1) + 0.01905f + HEIGHT*3;
            x = camera.viewportWidth/2*(-1) + WIDTH*7 + position%10*WIDTH + WIDTH/2;
        }
        else if( position <= 50 ) {
            y = camera.viewportHeight/2*(-1) + 0.01905f + HEIGHT*4;
            x = camera.viewportWidth/2*(-1) + WIDTH*7 + position%10*WIDTH;
        }
        else if( position <= 60 ) {
            y = camera.viewportHeight/2*(-1) + 0.01905f + HEIGHT*5;
            x = camera.viewportWidth/2*(-1) + WIDTH*7 + position%10*WIDTH + WIDTH/2;
        }
        else if( position <= 70) {
            y = camera.viewportHeight/2*(-1) + 0.01905f + HEIGHT*6;
            x = camera.viewportWidth/2*(-1) + WIDTH*7 + position%10*WIDTH;
        }

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.log("#info", "cup:"+position+",x:"+x);
        Gdx.app.log("#info", "cup:"+position+",y:"+y);
        return new Vector2(x, y);
        //return new Vector2(camera.viewportWidth/2*(-1) + WIDTH*7,camera.viewportHeight/2*(-1)/*-0.1215f*/);
    }


    public ArrayList<Cup> getCups() {
        return cups;
    }


}
