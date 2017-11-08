package com.ziamor.incadium.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ziamor.incadium.GamePlayScreen;
import com.ziamor.incadium.Incadium;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Incadium";
		config.width = 768;
		config.height = 512;
		new LwjglApplication(new Incadium(), config);
	}
}
