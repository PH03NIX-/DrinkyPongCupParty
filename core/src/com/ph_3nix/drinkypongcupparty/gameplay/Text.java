package com.ph_3nix.drinkypongcupparty.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Text {

    public BitmapFont theFont;
    public GlyphLayout textLayout;
    public FreeTypeFontGenerator generator;
    public FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    Text() {
        textLayout = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Hanken-Book.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        theFont = generator.generateFont(parameter);
    }
}


