package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Texture;

public class CollidableObstacle extends GameObject {
    private Integer xSpeed;
    private boolean used;
    private boolean stopped;
    Texture texture;

    public CollidableObstacle() {
        set(0, 0, null, 0);
    }
    public void set(float x, float y, Texture texture, Integer xSpeed) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.xSpeed = xSpeed;
    }

    public void movement(Integer snail) {
        switch(snail) {
            case 1:
                if (this.x > 200) {
                    this.xSpeed = -1;
                } else if (this.x < 50) {
                    this.xSpeed = 1;
                }
                break;
            case 2:
                if (this.x > 550) {
                    this.xSpeed = -3;
                } else if (this.x < 350) {
                    this.xSpeed = 3;
                }
                break;
        }
        this.x += this.xSpeed;
    }

    public void setUsed() {
        this.used = !this.used;
    }
    public boolean getUsed() {
        return this.used;
    }
    public void setStopped() {
        this.stopped = !this.stopped;
    }
    public boolean getStopped() {
        return this.stopped;
    }
    public Texture getTexture() {
        return texture;
    }
    @Override
    public String toString() {
        return String.format("CollidableObstacle x=%d,y=%d", x, y);
    }

}
