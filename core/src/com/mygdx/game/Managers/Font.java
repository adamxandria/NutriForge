package com.mygdx.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public enum Font {

    MANAGER;

    final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    // set font
    private final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/munro-small.ttf"));
    private final GlyphLayout layout = new GlyphLayout();

    public BitmapFont set(int size) {

        return set(size, Color.WHITE);
    }
    public BitmapFont set(int size, Color color) {
        // set parameters for the font
        fontParameter.size = size;
        fontParameter.color = color;
        return fontGenerator.generateFont(fontParameter);
    }

     //Displays left-justify a message in assigned coordinates.
    public void left(SpriteBatch sb, BitmapFont font, String message, float width, float height) {
        sb.begin();
        layout.reset();
        layout.setText(font, message);
        font.draw(sb, layout, width, height);
        sb.end();
        layout.reset();
    }


     //Displays right-justify a message in assigned coordinates.
    public void right(SpriteBatch sb, BitmapFont font, String message, float width, float height) {
        sb.begin();
        layout.reset();
        layout.setText(font, message);
        font.draw(sb, layout, width - layout.width, height);
        sb.end();
        layout.reset();
    }


     //Displays centered a message in assigned coordinates.
    public void centered(SpriteBatch sb, BitmapFont font, String message, float width, float height) {
        sb.begin();
        layout.reset();
        layout.setText(font, message);
        font.draw(sb, layout, width - layout.width / 2, height);
        sb.end();
        layout.reset();
    }
}
