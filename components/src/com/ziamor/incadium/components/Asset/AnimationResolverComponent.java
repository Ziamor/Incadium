package com.ziamor.incadium.components.Asset;

import com.artemis.Component;


public class AnimationResolverComponent extends Component {
    public String path;
    public int numFrameWidth;
    public int numFrameHeight;
    public AnimationMetaData[] animationMetaData;

    public void set(String path, int numFrameWidth, int numFrameHeight, AnimationMetaData[] animationMetaData) {
        this.path = path;
        this.numFrameWidth = numFrameWidth;
        this.numFrameHeight = numFrameHeight;
        this.animationMetaData = animationMetaData;
    }
}
