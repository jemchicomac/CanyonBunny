package com.hardstonegames.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.hardstonegames.canyonbunny.game.objects.AbstractGameObject;
import com.hardstonegames.canyonbunny.game.objects.Clouds;
import com.hardstonegames.canyonbunny.game.objects.Mountains;
import com.hardstonegames.canyonbunny.game.objects.Rock;
import com.hardstonegames.canyonbunny.game.objects.WaterOverlay;
import com.hardstonegames.canyonbunny.game.objects.BunnyHead;
import com.hardstonegames.canyonbunny.game.objects.Feather;
import com.hardstonegames.canyonbunny.game.objects.GoldCoin;

public class Level {
	
	public static final String TAG = Level.class.getName();
	
	public enum BLOCK_TYPE {
		EMPTY(0,0,0),
		ROCK(0, 255, 0),
		PLAYER_SPAWNPOINT(255, 255, 255),
		ITEM_FEATHER(255, 0, 255),
		ITEM_GOLD_COIN(255, 255, 0);
		
		private int color;
		
		private BLOCK_TYPE (int r, int g, int b) {
			color = r << 24 | g << 16 | b << 8 | 0xFF;
		}
		
		public boolean sameColor(int color) {
			return this.color == color;
		}
		
		public int getColor() {
			return color;
		}
	}
	
	// Objects
	public Array<Rock> rocks;
	public Array<GoldCoin> goldcoins;
	public Array<Feather> feathers;
	
	// Decoration
	public Clouds clouds;
	public Mountains mountains;
	public WaterOverlay waterOverlay;
	
	// Character
	public BunnyHead bunnyHead;
	
	public Level(String filename) {
		init(filename);
	}
	
	private void init(String filename) {
		//Player character
		bunnyHead = null;
		
		// Objects
		rocks = new Array<Rock>();
		goldcoins = new Array<GoldCoin>();
		feathers = new Array<Feather>();
		
		// Load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		// Scan pixels from topLeft to bottom-right
		int lastPixel = -1;
		for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
			for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
				AbstractGameObject obj = null;
				// Height grows from bottom to top
				float offsetHeight = 0;
				float baseHeight = pixmap.getHeight() - pixelY;
				// get color of the current pixel as 32-bits RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelX);
				
				// Find matching color value to identify block type at (x,y) point and create the
				// corresponding game object if there is a match
				
				// Empty space
				if(BLOCK_TYPE.EMPTY.sameColor(currentPixel)); // Do nothing
				
				// Rock
				else if(BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
					if(lastPixel != currentPixel) {
						obj = new Rock();
						float heightIncreaseFactor = 0.25f;
						offsetHeight = -2.5f;
						obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor * offsetHeight);
						rocks.add((Rock) obj);
					} else 
						rocks.get(rocks.size - 1).increaseLength(1);
				}
				
				// Player spawn point
				else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
					obj = new BunnyHead();
					offsetHeight = -3.0f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					bunnyHead = (BunnyHead) obj;
				}
				
				// Feather
				else if(BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)) {
					obj = new Feather();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					feathers.add((Feather) obj);
				}
				
				// Gold coin
				else if(BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)) {
					obj = new GoldCoin();
					offsetHeight = -1.5f;
					obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
					goldcoins.add((GoldCoin) obj);
				}
				
				// Unknow object/pixel color
				else {
					int r = 0xFF & (currentPixel >>> 24); // Red color channel
					int g = 0xFF & (currentPixel >>> 16); // Red color channel
					int b = 0xFF & (currentPixel >>> 8); // Red color channel
					int a = 0xFF & currentPixel; // Alpha channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY 
							+ ">: r>" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
		
		// Decoration
		clouds = new Clouds(pixmap.getWidth());
		clouds.position.set(0, 2);
		mountains = new Mountains(pixmap.getWidth());
		mountains.position.set(-1, -1);
		waterOverlay = new WaterOverlay(pixmap.getWidth());
		waterOverlay.position.set(0, -3.75f);
		
		// Free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "Level '" + filename + "' loaded");
	}
		
	public void render(SpriteBatch batch) { // Order sensitive!!
		// Draw Mountains
		mountains.render(batch);
		// Draw Rocks
		for(Rock rock: rocks)
			rock.render(batch);
		// Draw Gold Coins
		for (GoldCoin goldCoin: goldcoins)
			goldCoin.render(batch);
		// Draw Feathers
		for(Feather feather: feathers)
			feather.render(batch);
		// Draw Player Character
		bunnyHead.render(batch);
		// Draw Water Overlay
		waterOverlay.render(batch);
		// Draw clouds
		clouds.render(batch);
	}
	
	public void update(float deltaTime) {
		bunnyHead.update(deltaTime);
		for(Rock rock: rocks)
			rock.update(deltaTime);
		for(GoldCoin goldCoin: goldcoins)
			goldCoin.update(deltaTime);
		for(Feather feather: feathers)
			feather.update(deltaTime);
		clouds.update(deltaTime);
	}

}
