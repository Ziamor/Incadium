package com.ziamor.incadium.components.NonComponents;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;


public class HealthBarUI extends Widget {
    private HealthBarUIStyle style;
    private Gradient gradient;
    private Color barColor;
    private float max, value;

    // Reference https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/scenes/scene2d/ui/ProgressBar.java
    public HealthBarUI(Skin skin, Gradient gradient, float max) {
        this.gradient = gradient;
        this.max = max;
        this.value = 0;
        this.barColor = gradient.getColor(0);

        setStyle(skin.get("default", HealthBarUIStyle.class));
        setSize(getPrefWidth(), getPrefHeight());
    }

    public void setStyle(HealthBarUIStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        invalidateHierarchy();
    }

    @Override
    public float getPrefWidth() {
        return 140;
    }

    @Override
    public float getPrefHeight() {
        final Drawable bg = style.background;
        return bg.getMinHeight();
    }

    public void setValue(float newValue) {
        if (newValue < 0)
            this.value = 0;
        else if (newValue > max)
            this.value = max;
        else
            this.value = newValue;
        barColor = gradient.getColor(value / max);// Recalculate bar color;
    }

    public float getValue() {
        return value;
    }

    public void setMax(float newMax) {
        if (newMax < 0)
            max = 0;
        else
            max = newMax;
        barColor = gradient.getColor(value / max); // Recalculate bar color;
    }

    public float getMax() {
        return this.max;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        final Drawable bg = style.background;

        float x = getX();
        float y = getY();

        float width = getWidth();
        float height = getHeight();

        batch.setColor(getColor());
        bg.draw(batch, x, y, width, height);
        batch.setColor(barColor);
        bg.draw(batch, x, y, width * value / max, height);
    }

    static public class HealthBarUIStyle {
        public Drawable background;

        public HealthBarUIStyle() {
        }

        public HealthBarUIStyle(Drawable background) {
            this.background = background;
        }

        public HealthBarUIStyle(HealthBarUIStyle style) {
            this.background = style.background;
        }
    }
}
