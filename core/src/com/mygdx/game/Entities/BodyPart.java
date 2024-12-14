package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;


public class BodyPart extends GameObject {

    public Texture texture;
    private int direction;
    public Color color;

    public BodyPart(float x, float y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return String.format("BodyPart x=%d,y=%d", x, y);
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
