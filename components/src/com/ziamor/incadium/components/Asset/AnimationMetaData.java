package com.ziamor.incadium.components.Asset;

public class AnimationMetaData {
    public String name;
    public int[] frames;
    public float speed;
    public void set(String name, int[] frames, float speed){
        this.name = name;
        this.frames = frames;
        this.speed = speed;
    }
}
