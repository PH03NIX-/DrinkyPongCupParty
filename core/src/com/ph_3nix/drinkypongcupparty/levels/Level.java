package com.ph_3nix.drinkypongcupparty.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Level {
    private double y, sideLength, screenWidth, xOffset;
    private float x;
    private long level;
    private boolean isSelectable;
    private Texture levelPanel, levelSelectedPanel, locked;
    private Sprite panelSprite, lockedSprite;
    Preferences highscores;
    public BitmapFont theFont;
    public GlyphLayout textLayout;
    public FreeTypeFontGenerator generator;
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter;


    Level( long thisLevel, boolean isThisSelectable, double thisCurrentSelection, double thisScreenWidth, double thisScreenHeight, Texture thisPanel, Texture thisPanelSelected, GlyphLayout textLayout, /*FreeTypeFontGenerator generator, FreeTypeFontGenerator.FreeTypeFontParameter parameter,*/ BitmapFont theFont/*, Texture locked*/ ) {
        this.level = thisLevel;
        this.isSelectable = isThisSelectable;
        this.screenWidth = thisScreenWidth;
        this.sideLength = screenWidth / 5;
        this.y = this.sideLength*0.1;
        this.levelPanel = thisPanel;
        this.levelSelectedPanel = thisPanelSelected;
        this.locked = locked;
        this.xOffset = 0;
        //setThisX( thisCurrentSelection );

        this.theFont = theFont;
        this.textLayout = textLayout;
        this.generator = generator;
        this.parameter = parameter;

        this.panelSprite = new Sprite(this.levelPanel);
        this.panelSprite.setSize((float) this.sideLength, (float) this.sideLength);
        this.panelSprite.setPosition( (float) this.x, (float) this.y );
        this.panelSprite.setOrigin( (float) this.sideLength / 2, (float) this.sideLength / 2);

        /*this.lockedSprite = new Sprite(this.levelPanel);
        this.lockedSprite.setSize((float) this.sideLength, (float) this.sideLength);
        this.lockedSprite.setPosition( (float) this.x, (float) this.y );
        this.lockedSprite.setOrigin( (float) this.sideLength / 2, (float) this.sideLength / 2);
    */}

    public void drawLevel( Batch batch, double currentSelection, float offset ) {

        this.x = ( (float)this.level*(float)sideLength ) - ( (float)currentSelection*(float)sideLength ) + offset + (float)sideLength*1.5f;
        this.panelSprite.setPosition( this.x, (float) this.y );
        this.panelSprite.draw( batch );

        /*this.lockedSprite.setPosition( this.x, (float) this.y );
        this.lockedSprite.draw( batch );*/

        String levelText = "STAGE";
        textLayout.setText(theFont, levelText);
        theFont.draw(batch, levelText, this.x + (float)this.sideLength/2 - textLayout.width/2, (float)this.y + (float)this.sideLength*.8f );

        String levelNumber = ""+this.level;
        textLayout.setText(theFont, levelNumber);
        theFont.draw(batch, ""+this.level, this.x + (float)this.sideLength/2 - textLayout.width/2, (float)this.y + (float)this.sideLength/2 );

        //setThisX( currentSelection );
        //this.x = offset;
    }

    public void updateTexture( Texture newTexture ) {
        panelSprite.setTexture(newTexture);
    }

    public double getSideLength() {
        return sideLength;
    }

    public void setXOffset( double newOffset ) {
        this.xOffset += newOffset;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public long getLevel() {
        return this.level;
    }
}
/*
im.addListener((new DragListener() {
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        float dx = x - im.getWidth() * 0.5 f;
        float dy = y - im.getHeight() * 0.5 f;
        im.setPosition(im.getX() + dx, im.getY() + dy);
    }
}));
*/
/*
for(Brick b : map.getList()){

    final Image im = new Image(b.ar);

    stage.addActor(im);
    im.setPosition(b.posX, b.posY);
    im.setOrigin(b.posX, b.posY);

    im.addListener((new DragListener() {
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            float dx = x - im.getWidth() * 0.5 f;
            float dy = y - im.getHeight() * 0.5 f;
            im.setPosition(im.getX() + dx, im.getY() + dy);
        }
    }));
}*/
