package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Texture;


public class Food extends GameObject {

    public Texture texture;


    public Food() {
        set(0, 0, null);
    }

    public void set(float x, float y, Texture texture) {
        this.setX(x);
        this.y = y;
        this.texture = texture;
    }

    @Override
    public String toString() {
        return String.format("Food x=%d,y=%d", this.getX(), y);
    }
}
