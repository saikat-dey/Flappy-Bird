package com.skat_sd.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameOver;
	ShapeRenderer shapeRenderer;
	Texture[] birds;
	int flapState=0;
	float birdY=0;
	float velocity=0;
	Circle birdCircle;
	BitmapFont scorefont,highScoreFont;
	int score=0;
	int scoringTube=0;

	int gameState=0;
	float gravity=2;

	Texture topTube,bottomTube;

	float gap=500;
	float maxTubeoffset;
	Random randomGenerator;

	float tubeVelocity=4;

	int numberOftubes=4;
	float []tubeX=new float[numberOftubes];
	float []tubeOffset=new float[numberOftubes];
	float distanceBetweenTubes=4;

	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;


	Texture message;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		shapeRenderer=new ShapeRenderer();
		birdCircle=new Circle();
		gameOver=new Texture("GameOver.png");

		birds=new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");


		topTube=new Texture("toptube.png");
		bottomTube=new Texture("bottomtube.png");

		maxTubeoffset=Gdx.graphics.getHeight()/2-gap/2-100;
		randomGenerator=new Random();

		distanceBetweenTubes = (Gdx.graphics.getWidth() * 5) / 8;

		topTubeRectangles=new Rectangle[numberOftubes];
		bottomTubeRectangles=new Rectangle[numberOftubes];

		scorefont=new BitmapFont();
		scorefont.setColor(Color.WHITE);
		scorefont.getData().setScale(15);

		highScoreFont=new BitmapFont();
		highScoreFont.setColor(Color.RED);
		highScoreFont.getData().setScale(5);

		message=new Texture("message.png");




		startGame();

	}

	public void startGame(){
		birdY=Gdx.graphics.getHeight()/2-(birds[0].getHeight())/2;

		for(int i=0;i<numberOftubes;i++){
			tubeOffset[i]=(randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()/1.5f-gap-200);
			tubeX[i] = (((Gdx.graphics.getWidth() / 2) - (topTube.getWidth() / 2)) + (Gdx.graphics.getWidth() *3/4)) + (i * distanceBetweenTubes);

			topTubeRectangles[i]=new Rectangle();
			bottomTubeRectangles[i]=new Rectangle();


//			batch.begin();
//			batch.draw(message,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
//			batch.end();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


//		if(Gdx.input.justTouched()) {
//			Gdx.app.log("Touched", "Yep");
//			gameState=1;
//		}


		if(gameState==1) {

			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("Score:",String.valueOf(score));

				if(scoringTube<numberOftubes-1){
					scoringTube++;
				}

				else {
					scoringTube=0;
				}
			}


			if(Gdx.input.justTouched()) {
				velocity=-30;

				//gameState=1;
			}

			for(int i=0;i<numberOftubes;i++) {

				if(tubeX[i]<-topTube.getWidth()){
					tubeX[i]+=numberOftubes*distanceBetweenTubes;
					tubeOffset[i]=(randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()/1.5f-gap-200);

				}else{
					tubeX[i] -= tubeVelocity;

				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				bottomTubeRectangles[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());


			}


			//if(birdY>0||velocity<0) {

			if(birdY>0 && birdY<=Gdx.graphics.getHeight()){
				velocity += gravity;
				birdY -= velocity;
			}else{
				gameState=2;

			}
		}else if (gameState==0){
			if(Gdx.input.justTouched()) {
				gameState=1;
			}
		}else if(gameState==2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			//batch.draw(birds[flapState],Gdx.graphics.getWidth()/2-birds[flapState].getWidth()/2,100-birds[flapState].getHeight()/2);



			if(Gdx.input.justTouched()) {
				gameState=1;
				startGame();
				score=0;
				scoringTube=0;
				velocity=0;

			}
		}


		if (flapState == 0)
			flapState = 1;
		else
			flapState = 0;



		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - (birds[flapState].getWidth()) / 2, birdY);

		scorefont.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()/2-75,Gdx.graphics.getHeight()-100);

		batch.end();

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for(int i=0;i<numberOftubes;i++) {
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());


			//collision
			if (Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){
				//Gdx.app.log("High score:",String.valueOf(score));
				gameState=2;

				Preferences pref;
				pref = Gdx.app.getPreferences("Scores");
				updateScore(score, pref);

				//Gdx.app.log("Collision","yes");
			}
		}

		//shapeRenderer.end();
	}

	public void updateScore(int score, Preferences pref) {

		int highScore=pref.getInteger("High score",0);
		if(highScore>=score){
			batch.begin();
			highScoreFont.draw(batch,"Highest Score:"+String.valueOf(highScore),90,200);
			batch.end();
		}else{
			pref.putInteger("High score",score);
			pref.flush();
		}


//		pref.putInteger("score:",10);
//		pref.flush();
//		int scoreNow=pref.getInteger("score:",10);
//		Gdx.app.log("Now high score:",String.valueOf(scoreNow));


	}

//	@Override
//	public void dispose () {
//		batch.dispose();
//		img.dispose();
//	}
}
