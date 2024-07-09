package com.ph_3nix.drinkypongcupparty;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.ph_3nix.drinkypongcupparty.buttons.Menu;
import com.ph_3nix.drinkypongcupparty.gameplay.Gameplay;
import com.ph_3nix.drinkypongcupparty.gameplay.Scoreboard;
import com.ph_3nix.drinkypongcupparty.levels.LevelSelection;


import java.util.ArrayList;

public class DrinkyPongCupParty extends ApplicationAdapter {
	private final AdsController adsController;
	public DrinkyPongCupParty( AdsController adsController) {
		this.adsController = adsController;
	}

	SpriteBatch batch;
	Texture menuBackground;
	OrthographicCamera camera;

	ArrayList<Menu> mainMenu;
	Scoreboard scoreboard;
	LevelSelection levelSelection;
	Gameplay gameplay;

	int volume, assetCount;
	float squaredY, lastDeltaX, lastDeltaY;
	boolean squaredDown;
	long levelNum;
	boolean isGameplay;
	TextButton myButton;
	BitmapFont arialFont, backgroundFont, backgroundFont2;
	GlyphLayout backgroundLayout;
	FreeTypeFontGenerator generator;
	FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	GlyphLayout backgroundLayout2;
	FreeTypeFontGenerator generator2;
	FreeTypeFontGenerator.FreeTypeFontParameter parameter2;
	float aliveTime;
	Sound whoosh,yay,holeInOne,applause;
	int whoshCount = 0;


	private void loadAssets() {
		menuBackground = new Texture("menu-color.png");
		/*selected = new Texture("easy.png");
		unselected = new Texture("hard.png");
		audio = new Texture("audio.png");
		audio_no = new Texture("audio_no.png");*/

		mainMenu = new ArrayList<Menu>();
		scoreboard = new Scoreboard();
		scoreboard.newScoreboard(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 12 * 6 /*- (Gdx.graphics.getHeight()/6/6)*//*, gamemode+gametype*/);

		mainMenu.add(new Menu(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2 /3, 1, 0.0833333f, 0.0833333f, "play", scoreboard.getFontGenerator(), true, false, 1));

	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		batch = new SpriteBatch();
		arialFont = new BitmapFont(Gdx.files.internal("arial.fnt"));

		backgroundLayout = new GlyphLayout();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("Hanken-Book.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int)Gdx.graphics.getHeight()/4;
		parameter.color= new Color(42,0, 84, 1);
		backgroundFont = generator.generateFont(parameter);

		backgroundLayout2 = new GlyphLayout();
		generator2 = new FreeTypeFontGenerator(Gdx.files.internal("Hanken-Book.ttf"));
		parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter2.size = (int)Gdx.graphics.getHeight()/4;
		parameter2.color= new Color(255,255, 255, 1);
		backgroundFont2 = generator.generateFont(parameter2);
		whoosh = Gdx.audio.newSound(Gdx.files.internal("whoosh.wav"));
		holeInOne = Gdx.audio.newSound(Gdx.files.internal("holeInOne.wav"));
		yay = Gdx.audio.newSound(Gdx.files.internal("yay.wav"));
		applause = Gdx.audio.newSound(Gdx.files.internal("applause.wav"));

		levelSelection = new LevelSelection();
		gameplay = new Gameplay();
		aliveTime = 0;

		/*memoryTexture = new Texture("memory_text.png");
		squaredTexture = new Texture("squared_text.png");*/
		myButton = createButton("PLAY"
								, Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/6
								, Gdx.graphics.getHeight()/6
								, Gdx.graphics.getWidth()/12
								, (Gdx.graphics.getHeight() / 3 * 2) - (Gdx.graphics.getHeight()/6*1.5f));
		levelNum = 0;
	}

	public TextButton createButton(String text, float width, float height, float x, float y) {
		Skin skin;
		TextButton.TextButtonStyle buttonStyle;
		TextButton myButton;
		BitmapFont arialFont;
		Touchable touchable;

		arialFont = new BitmapFont(Gdx.files.internal("arial.fnt"));

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
		myButton.getLabel().setFontScale(3.0f);
		myButton.setSize( width, height );
		myButton.setPosition( x, y );

		skin.dispose();

		return myButton;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		if( assetCount < 2 ) {
			//batch.draw(memoryTexture, 0, Gdx.graphics.getHeight()*  0.75f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*0.1666666666f);
			//batch.draw(squaredTexture, 0, squaredY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*0.1666666666f);
			if(assetCount == 1)
				loadAssets();
			assetCount++;
		}
		else {
			//if(Gdx.input.justTouched() ) if(adsController != null) adsController.showRewardedVideo();
			if( levelNum == 0 ) {
				gameMenuLogic();
			}
			else {
				levelSelection.drawLevelSelections( batch, levelNum, camera);
			}
		}
		camera.update();

		batch.end();
	}

	/*************************************************************************************************/
	/*************************		main game-loop logic functions		******************************/
	public void gotoLevelSelect() {
		levelNum = 1; //placeholder update to saved max
		holeInOne.play(6);
		levelSelection.createLevelSelection( gameplay, adsController );
		yay.dispose();
		applause.dispose();
		holeInOne.dispose();
	}

	public void gameMenuLogic() {
		aliveTime += Gdx.graphics.getDeltaTime();
		float theX = Gdx.input.getX();
		float theY = Gdx.graphics.getHeight() - Gdx.input.getY();

		batch.draw(menuBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*0.5f);

		if(aliveTime > 0.5f) {
			if(whoshCount == 0) {
				whoosh.play(1);
				whoshCount++;
			}
			backgroundLayout.setText(backgroundFont, "DRINKY");
			backgroundFont.draw(batch, "DRINKY", theX / 2 /*- backgroundLayout.width/2*/, theY);


			if(aliveTime > 1) {
				if(whoshCount == 1) {
					whoosh.play(1);
					whoshCount++;
				}
				backgroundLayout.setText(backgroundFont, "PONG");
				backgroundFont.draw(batch, "PONG", theX / 2 /*- backgroundLayout.width/2*/, theY - theY / 4);

				if(aliveTime > 1.5f) {
					if(whoshCount == 2) {
						whoosh.play(1);
						whoshCount++;
					}
					backgroundLayout2.setText(backgroundFont2, "CUP");
					backgroundFont2.draw(batch, "CUP", theX / 2 /*- backgroundLayout.width/2*/, theY - theY / 4 * 2);

					if(aliveTime > 2) {
						if(whoshCount == 3) {
							whoosh.play(1);
							whoshCount++;
						}
						backgroundLayout2.setText(backgroundFont2, "PARTY");
						backgroundFont2.draw(batch, "PARTY", theX / 2 /*- backgroundLayout.width/2*/, theY - theY / 4 * 3);
					}
				}
			}
		}

		//textLayout.setText(theFontScores, "" + bottleCapCount);
		//theFontScores.draw(batch, "" + bottleCapCount, 3 * textLayout.height, displayHeight - textLayout.height / 2);

		//backgroundFont.draw(batch, "DRINKY", theX/2, theY/2);
		if(aliveTime > 2.5f) {
			if(whoshCount == 4) {
				whoosh.play(1);
				holeInOne.play(1);
				yay.play(1);
				applause.play(1);
				whoshCount++;
			}
			myButton.draw(batch, 1);
		}

		if (Gdx.input.justTouched()) {
			if(theX > myButton.getX() && theX < myButton.getX()+myButton.getWidth() ) {
				gotoLevelSelect();
			}
		}

	}



	
	@Override
	public void dispose () {
		batch.dispose();
		scoreboard.dispose();
		for (Menu menu : mainMenu) {
			menu.dispose();
		}
		menuBackground.dispose();
		arialFont.dispose();
		backgroundFont.dispose();
		backgroundFont2.dispose();
		levelSelection.dispose();
	}
}
