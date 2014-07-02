package com.hardstonegames.canyonbunny;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.hardstonegames.canyonbunny.game.WorldController;
import com.hardstonegames.canyonbunny.game.WorldRenderer;

public class CanyonBunnyMain implements ApplicationListener {
	
	private static final String TAG = CanyonBunnyMain.class.getName(); // For debugging
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused; // To manage android pausing
	
	@Override
	public void create() {		
		// Set LibGDX log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Initialize the controller and the renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		paused = false;
	}

	@Override
	public void render() { // NOTE: 1- UPDATE; 2- RENDER
		
		if(!paused) {
			// Update game world by the time that has passed since last rendered frame
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		
		// Sets the clear screen color to: Cornflower Blue (RGMA) (range: 0 >= x <=1)
			//Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xED/255.0f, 0xFF/255.0f); // Hex notation
		Gdx.gl.glClearColor(100/255.0f, 149/255.0f, 237/255.0f, 255/255.0f); // Dec notation
		// Clears the screen
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);	
	}

	@Override
	public void dispose() {
		worldRenderer.dispose();
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}
}
