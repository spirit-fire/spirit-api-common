package com.spirit.commons.thd.image;

/**
 * com.spirit.commons.thd.image class
 *
 * @author guoxiong
 * @date 2017/12/28 20:43
 */
public class TextModel {

    private float startX;
    private float startY;
    private int width;
    private int height = 0;
    private String data;

    public TextModel() {

    }

    public TextModel(float startX, float startY, int width, int height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
