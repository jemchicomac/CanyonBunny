package com.hardstonegames.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.hardstonegames.canyonbunny.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * This class manages all the assets that the game will have.
 * This class have some inner classes who describes what resources
 * (images, audio,...) form each object of the game world
 */
public class Assets implements Disposable, AssetErrorListener {
	
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	public AssetBunny bunny;
	public AssetRock rock;
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetLevelDecoration levelDecorator;
	public AssetFonts fonts;
	
	// Singleton in eager mode
	private Assets() {}
	
	public class AssetBunny {
		public final AtlasRegion head;
		
		public AssetBunny(TextureAtlas atlas) {
			head = atlas.findRegion("bunny_head");
		}
	}
	
	public class AssetRock {
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		
		public AssetRock(TextureAtlas atlas) {
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}
	
	public class AssetGoldCoin {
		public final AtlasRegion goldCoin;
		
		public AssetGoldCoin(TextureAtlas atlas) {
			goldCoin = atlas.findRegion("item_gold_coin"); 
		}
	}
	
	public class AssetFeather {
		public final AtlasRegion feather;
		
		public AssetFeather(TextureAtlas atlas) {
			feather = atlas.findRegion("item_feather");
		}
	}
	
	public class AssetLevelDecoration {
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;
		
		public AssetLevelDecoration(TextureAtlas atlas) {
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
		}
	}
	
	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts() {
			// Create these fonts using Libgdx's 15px bitamp font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.ftn"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.ftn"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.ftn"), true);
			// Set font sizes
			defaultSmall.setScale(0.75f);
			defaultNormal.setScale(1.0f);
			defaultBig.setScale(2.0f);
			// Enable linear testure filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}
	
	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		// Set assets manager error handler
		assetManager.setErrorListener(this);
		// Load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		// Start loading assets and wait until finish
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for(String a: assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		// Enable texture filtering for pixel smoothing
		for(Texture t : atlas.getTextures())
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// Create game resource objects
		bunny = new AssetBunny(atlas);
		rock = new AssetRock(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		feather = new AssetFeather(atlas);
		levelDecorator = new AssetLevelDecoration(atlas);
		fonts = new AssetFonts();
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultBig.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultSmall.dispose();
	}
	
	@Override
	public void error(String fileName, Class type, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + fileName + "'", (Exception)throwable);
	}
}
