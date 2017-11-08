package com.ziamor.incadium.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.ziamor.incadium.systems.Util.MapSystem;

public class DijkstraMap {
    public static int defaultValue = 99999;

    public static int[][] getDijkstraMap(MapSystem map, int x_target, int y_target) {
        if (map == null)
            return null;

        int map_width = map.getMapWidth();
        int map_height = map.getMapHeight();

        if (map_width <= 0 || map_width <= 0)
            return null;

        int[][] weights = new int[map_width][map_height];

        for (int x = 0; x < weights.length; x++)
            for (int y = 0; y < weights[x].length; y++)
                weights[x][y] = DijkstraMap.defaultValue;

        weights[x_target][y_target] = 0;

        boolean[][] blockers = new boolean[map_width][map_height];
        for (int x = 0; x < blockers.length; x++)
            for (int y = 0; y < blockers[x].length; y++)
                if (map.isBlocking(x, y))
                    blockers[x][y] = true;

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int x = 0; x < weights.length; x++)
                for (int y = 0; y < weights[x].length; y++) {
                    if (blockers[x][y])
                        continue;

                    int lowest = weights[x][y];

                    if (x - 1 >= 0 && !blockers[x - 1][y] && weights[x - 1][y] < lowest)
                        lowest = weights[x - 1][y];

                    if (x + 1 < map_width && !blockers[x + 1][y] && weights[x + 1][y] < lowest)
                        lowest = weights[x + 1][y];

                    if (y - 1 >= 0 && !blockers[x][y - 1] && weights[x][y - 1] < lowest)
                        lowest = weights[x][y - 1];

                    if (y + 1 < map_height && !blockers[x][y + 1] && weights[x][y + 1] < lowest)
                        lowest = weights[x][y + 1];

                    if (weights[x][y] - lowest >= 2) {
                        weights[x][y] = lowest + 1;
                        changed = true;
                    }
                }
        }
        return weights;
    }

    public static void renderDijkstraMap(int[][] weights, ShapeRenderer batch) {

        float max_weight = 0;
        for (int x = 0; x < weights.length; x++)
            for (int y = 0; y < weights[x].length; y++) {
                if (weights[x][y] == defaultValue)
                    continue;
                if (weights[x][y] > max_weight)
                    max_weight = weights[x][y];
            }

        Gradient g = new Gradient(new Color(0.3f, 0, 0.8f, 1f), new Color(1f, 0.6f, 0, 0.75f));
        g.addPoint(new Color(1f, 0.2f, 0.2f, 0.75f), 0.75f);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < weights.length; x++)
            for (int y = 0; y < weights[x].length; y++) {
                float weight = weights[x][y];
                if (weight == defaultValue)
                    continue;
                float prog =(weight - max_weight) * -1 / max_weight;
                batch.setColor(g.getColor(prog));
                batch.rect(x, y, 1, 1);
            }
        batch.end();
    }

    public static Vector2 gradientDecent(int[][] weights, Vector2 pos) {
        Vector2 nextPos = new Vector2();

        int lowest = DijkstraMap.defaultValue;

        int x = (int) pos.x;
        int y = (int) pos.y;

        if (x - 1 >= 0 && weights[x - 1][y] != DijkstraMap.defaultValue && weights[x - 1][y] < lowest) {
            lowest = weights[x - 1][y];
            nextPos.set(x - 1, y);
        }
        if (x + 1 < weights.length && weights[x + 1][y] != DijkstraMap.defaultValue && weights[x + 1][y] < lowest) {
            lowest = weights[x + 1][y];
            nextPos.set(x + 1, y);
        }
        if (y - 1 >= 0 && weights[x][y - 1] != DijkstraMap.defaultValue && weights[x][y - 1] < lowest) {
            lowest = weights[x][y - 1];
            nextPos.set(x, y - 1);
        }
        if (y + 1 < weights[x].length && weights[x][y + 1] != DijkstraMap.defaultValue && weights[x][y + 1] < lowest)
            nextPos.set(x, y + 1);

        return nextPos;
    }
}
