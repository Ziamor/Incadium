package com.ziamor.incadium.components.Asset;

import com.artemis.Component;


public class TextureRegionResolverComponent extends Component {
    public String path;
    public int px, py, w, h;

    public void set(String path, int px, int py, int w, int h) {
        this.path = path;
        this.px = px;
        this.py = py;
        this.w = w;
        this.h = h;
    }
}
