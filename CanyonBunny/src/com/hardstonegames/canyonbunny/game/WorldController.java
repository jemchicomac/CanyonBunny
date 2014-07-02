package com.hardstonegames.canyonbunny.game;

import sun.font.CreatedFontTracker;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

public class WorldController {
	
	private static final String TAG = WorldController.class.getName(); // For debugging
	
	public Sprite[] testSprites;
	public int selectedSprite;
	
	public WorldController() {
		init();
	}
	
	private void init() {
		initTestObjects();
	}
	
	private void initTestObjects(){
		// Create new array for 5 sprites
		testSprites = new Sprite[5];
		// Create empty POT-sized Pixma with  8 bit RGBA pixel data
		int width = 32;
		int height = 32;
		Pixmap pixmap = createProceduralPixmap(width, height);
		// Create a new texture from pixmap data
		Texture texture = new Texture(pixmap);
		for(int i = 0; i < testSprites.length; i++) {
			Sprite spr = new Sprite(texture);
			// Define sprite size to be 1m x 1m in game world
			spr.setSize(1, 1);
			// Set origin to sprite center
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
			// Calculate random positions for sprite
			float radomX = MathUtils.random(-2.0f, 2.0f);
			float radomY = MathUtils.random(-2.0f, 2.0f);
			spr.setPosition(radomX, radomY);
			// Put the new sprite into array
			testSprites[i] = spr;
		}
		// Set first sprite as selected one
		selectedSprite = 0;
	}
	
	private Pixmap createProceduralPixmap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		// Fill square with red color at 50% opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		// Draw a yellow-colored X shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		// Draw a cyan-colored border around square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}
	
	public void update(float deltaTime){
		updateTestObjects(deltaTime);
	}
	
	private void updateTestObjects(float deltaTime) {
		// Get current rotation from selected sprite
		float rotation = testSprites[selectedSprite].getRotation();
		// Rotate sprite 90 degrees per second
		rotation += 90 * deltaTime;
		// Wrap aroun 360 degrees
		rotation %= 360;
		// Set the rotation value to selected sprite
		testSprites[selectedSprite].setRotation(rotation);
	}
}
