package com.ziamor.incadium.utils;

import com.badlogic.gdx.graphics.Color;

import java.util.Map;
import java.util.TreeMap;


public class Gradient {
    TreeMap<Float, Color> points;

    public Gradient(Color start, Color end) {
        points = new TreeMap<Float, Color>();

        points.put(0f, start);
        points.put(1f, end);
    }

    public void addPoint(Color color, float point) {
        if (color == null)
            return;
        if (point <= 0 && point >= 1)
            return;
        points.put(point, color);
    }

    public Color getColor(float point) {
        Map.Entry<Float, Color> lastEntry = null;
        for (Map.Entry<Float, Color> entry : points.entrySet()) {
            if (lastEntry == null) {
                lastEntry = entry;
                continue;
            }

            if (point == lastEntry.getKey())
                return lastEntry.getValue();
            else if (point == entry.getKey())
                return entry.getValue();
            else if (point > lastEntry.getKey() && point < entry.getKey()) {
                float a = lastEntry.getKey();
                float b = entry.getKey();
                float percent = (point - a) * (1 / (b - a));
                Color c = lastEntry.getValue().cpy();
                c.lerp(entry.getValue(), percent);
                return c;
            }
            lastEntry = entry;
        }
        return null;
    }
}
