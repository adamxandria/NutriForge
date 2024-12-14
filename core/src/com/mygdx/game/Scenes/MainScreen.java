package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Entities.*;
import com.mygdx.game.NutriForge;
import com.mygdx.game.Entities.CollidableObstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainScreen extends ScreenAdapter {

    public static final float CELL_WIDTH = 32.0f;
    public static final float CELL_HEIGHT = 32.0f;
    private static final int MAX_FOOD = 3;
    private static final float MINIMUM_WALK_TIME = 0.076f;
    private static final float WORLD_WIDTH = 640.0f;
    private static final float WORLD_HEIGHT = 480.0f;
    private static final float INITIAL_WALK_TIME = 0.4f;
    private static final float SPECIAL_TIME_HOP = 1.0f;
    private final NutriForge game;
    private Batch batch;
    private ShapeRenderer shapeRenderer;
    private long gameOverTimer;
    private float timeNeededToMove;
    private float specialTime;
    private float time;
    private float timeSpentUntilNextMove;
    private BitmapFont gameOverFont;
    private BitmapFont scoreFont;
    private Sound gameOverSound;
    private Sound eatFoodSound;
    private Stage stage;
    private Texture thead;
    private Texture theadHurt;
    private Texture tchest;
    private Texture tbody;
    private Texture tfeet;
    private Texture playUpTexture;
    private Texture playDownTexture;
    private Player player;
    private Food specialFood;
    private Food unhealthyFood;
    private List<GameObject> hitObjects;
    private Stack<Food> foods;
    private Stack<Food> deadFoods;
    private Texture specialFoodTexture;
    private Texture bgTexture;
    private Texture foodTexture1;
    private Texture unhealthyFoodTexture;
    private int score;
    private int winScore;
    private int numCellsX;
    private int numCellsY;
    private BodyPart newBodyPart;
    private boolean pauseChanged;
    private boolean executedGameOver;
    private boolean executedGameWin;
    private GlyphLayout glyphLayout;
    private OrthographicCamera camera;
    private Viewport viewport;
    private long lastTimeOfEating;
    private boolean shapeDebug = false;
    private boolean changedDirection = false;
    private SpecialHandler specialHandler;
    private Music bgAudio;
    private STATE state = STATE.PLAYING;
    private CollidableObstacle collidableSnail;
    private CollidableObstacle collidableSnail2;
    private Texture collidableSnailT;
    private Texture collidableSnail2T;
    private Sound minusScoreSound;

    public MainScreen(NutriForge game) {
        this.game = game;
    }

    @Override
    public void show() {

        AssetManager assetManager = game.getAssetManager();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        bgAudio = assetManager.get("mandioca-loop.ogg", Music.class);
        bgAudio.setLooping(true);
        bgAudio.setVolume(0.04f);

        {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("dk-prince-frog.otf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 28;
            scoreFont = generator.generateFont(parameter);
            scoreFont.setColor(Color.WHITE);
            generator.dispose();
        }

        {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arcade-classic.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 56;
            gameOverFont = generator.generateFont(parameter);
            gameOverFont.setColor(Color.DARK_GRAY);
            generator.dispose();
        }

        specialHandler = new SpecialHandler(viewport);

        bgTexture = assetManager.get("bg-grass-and-holes.png", Texture.class);
        foodTexture1 = assetManager.get("food1.png", Texture.class);
        specialFoodTexture = assetManager.get("special-food1.png", Texture.class);
        unhealthyFoodTexture = assetManager.get("donuts.png", Texture.class);
        collidableSnailT = new Texture(Gdx.files.internal("snail-burger.png"));
        collidableSnail2T = new Texture(Gdx.files.internal("snail-ice-cream.png"));
        playDownTexture = assetManager.get("startbutton.png", Texture.class);
        playUpTexture = assetManager.get("startbutton.png", Texture.class);

        numCellsX = (int) MathUtils.floor(viewport.getWorldWidth() / CELL_WIDTH);
        numCellsY = (int) MathUtils.floor(viewport.getWorldHeight() / CELL_HEIGHT);

        Gdx.app.log("MainScreen", String.format("WORLD_WIDTH %.2f and WORLD_HEIGHT %.2f", viewport.getWorldWidth(), viewport.getWorldHeight()));
        Gdx.app.log("MainScreen", String.format("ZOOM %.2f ", camera.zoom));

        hitObjects = new ArrayList<>();

        specialFood = new Food();
        hitObjects.add(specialFood);
        unhealthyFood = new Food();
        hitObjects.add(unhealthyFood);

        foods = new Stack<>();
        deadFoods = new Stack<>();
        for (int i = 0; i < MAX_FOOD; i++) {
            Food food = new Food();
            deadFoods.add(food);
            hitObjects.add(food);
        }

        collidableSnail = new CollidableObstacle();
        collidableSnail2 = new CollidableObstacle();
        thead = assetManager.get("head.png");
        theadHurt = assetManager.get("head-hurt.png");
        tchest = assetManager.get("chest.png");
        tfeet = assetManager.get("feet.png");
        tbody = assetManager.get("body.png");

        glyphLayout = new GlyphLayout();
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        player = new Player();

        for (int i = 0; i < player.bodyParts.size(); i++) {
            hitObjects.add(player.bodyParts.get(i));
        }

        gameOverSound = assetManager.get("game-over.ogg");
        eatFoodSound = assetManager.get("plin.ogg");
        minusScoreSound = Gdx.audio.newSound(Gdx.files.internal("minus-point.wav"));

        restartGame();
    }

    private void restartGame() {
        state = STATE.PLAYING;
        bgAudio.play();
        specialHandler.stop();
        gameOverTimer = 0;
        score = 0;
        time = 0;
        lastTimeOfEating = System.currentTimeMillis();
        executedGameOver = false;
        executedGameWin = false;
        pauseChanged = false;
        specialTime = 0.0f;
        timeSpentUntilNextMove = 0.0f;
        timeNeededToMove = INITIAL_WALK_TIME;

        float midX = MathUtils.floor(numCellsX / 2) * CELL_WIDTH;
        float midY = MathUtils.floor(numCellsY / 2) * CELL_HEIGHT;

        player.setup(midX, midY, thead, theadHurt, tchest, tfeet);

        specialFood.set(-100, -100, null);
        unhealthyFood.set(-100, -100, null);

        Gdx.app.log("show", String.format(" initial position (x,y) (%.2f,%.2f)", midX, midY));
        Gdx.app.log("show", String.format(" head position (x,y) (%.2f,%.2f)", player.bodyParts.get(0).getX(), player.bodyParts.get(0).getY()));

        for (int i = 0, leni = foods.size(); i < leni; leni--) {
            deadFoods.push(foods.remove(i));
        }

        for (int i = 0; i < MAX_FOOD; i++) {
            addFood();
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (state == STATE.GAME_OVER) {
            gameOver();
            boolean enter = Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
            if (enter && System.currentTimeMillis() - gameOverTimer > 7000) {
                restartGame();
            }
        } else if (state == STATE.GAME_WON) {
            gameWin();

            boolean enter = Gdx.input.isKeyJustPressed(Input.Keys.ENTER);

            if (enter && System.currentTimeMillis() - gameOverTimer > 7000) {
                restartGame();
            }
        } else if (state == STATE.PAUSED) {
            stage.act(delta);
            stage.draw();

            boolean space = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

            if (space) {
                resume();
                state = STATE.PLAYING;
            }
        } else if (state == STATE.PLAYING) {

            boolean space = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

            if (space) {

                state = STATE.PAUSED;
                pause();
            } else {

                queryInput();

                timeSpentUntilNextMove += delta;
                time += delta;

                if (timeSpentUntilNextMove >= timeNeededToMove) {
                    timeSpentUntilNextMove -= timeNeededToMove;

                    move();
                    checkCollisions();
                    checkWinCondition();
                    player.update(delta);
                    changedDirection = false;
                }

                addSpecialFood(delta);
                addUnhealthyFood(delta);

                if (specialHandler.isRunning()) {
                    specialHandler.update(delta);
                }
            }
        }
        clearScreen();
        draw();
    }

    private void gameOver() {

        if (executedGameOver) {
            return;
        }
        executedGameOver = true;
        bgAudio.stop();
        gameOverTimer = System.currentTimeMillis();
        gameOverSound.play();
    }

    private void gameWin() {

        if (executedGameWin) {
            return;
        }

        executedGameWin = true;
        bgAudio.stop();

        gameOverTimer = System.currentTimeMillis();
        gameOverSound.play();
    }


    private void queryInput() {

        boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean up = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean enter = Gdx.input.isKeyPressed(Input.Keys.ENTER);

        BodyPart head = player.head();

        if (!changedDirection) {
            if (left && (head.getDirection() == Player.UP || head.getDirection() == Player.DOWN)) {
                head.setDirection(Player.LEFT);
                changedDirection = true;
            } else if (right && (head.getDirection() == Player.UP || head.getDirection() == Player.DOWN)) {
                head.setDirection(Player.RIGHT);
                changedDirection = true;
            } else if (up && (head.getDirection() == Player.LEFT || head.getDirection() == Player.RIGHT)) {
                head.setDirection(Player.UP);
                changedDirection = true;
            } else if (down && (head.getDirection() == Player.LEFT || head.getDirection() == Player.RIGHT)) {
                head.setDirection(Player.DOWN);
                changedDirection = true;
            }
        }
    }

    private void move() {

        BodyPart head = player.bodyParts.get(0);
        BodyPart chest = player.bodyParts.get(1);
        BodyPart feet = player.bodyParts.get(player.bodyParts.size() - 1);

        BodyPart lastBody = player.bodyParts.get(player.bodyParts.size() - 2); // may be the chest
        boolean lastBodyIsTheChest = lastBody.getX() == chest.getX() && lastBody.getY() == chest.getY();

        boolean justAdded = newBodyPart != null;

        int lastHeadDirection = head.getDirection();
        float lastHeadY = head.getY();
        float lastHeadX = head.getX();

        int chestDirection = chest.getDirection();
        float chestX = chest.getX();
        float chestY = chest.getY();

        int lastBodyDirection = lastBody.getDirection();
        float lastBodyX = lastBody.getX();
        float lastBodyY = lastBody.getY();

        if (head.getDirection() == player.UP) {

            head.setY(head.getY() + CELL_HEIGHT);

            if (head.getY() >= viewport.getWorldHeight() || head.getY() < 0) {
                player.collidedHurt();
                head.setY(head.getY() - CELL_HEIGHT);

                state = STATE.GAME_OVER;
                return;
            }
        } else if (head.getDirection() == player.RIGHT) {

            head.setX(head.getX() + CELL_WIDTH);

            if (head.getX() >= viewport.getWorldWidth() || head.getX() < 0) {
                player.collidedHurt();
                head.setX(head.getX() - CELL_WIDTH);

                state = STATE.GAME_OVER;
                return;
            }
        } else if (head.getDirection() == player.DOWN) {

            head.setY(head.getY() - CELL_HEIGHT);

            if (head.getY() < 0 || head.getY() >= viewport.getWorldHeight()) {
                player.collidedHurt();
                head.setY(head.getY() + CELL_HEIGHT);

                state = STATE.GAME_OVER;
                return;
            }
        } else if (head.getDirection() == player.LEFT) {

            head.setX(head.getX()-CELL_WIDTH);

            if (head.getX() < 0 || head.getX() >= viewport.getWorldWidth()) {
                player.collidedHurt();
                head.setX(head.getX() + CELL_WIDTH);
                state = STATE.GAME_OVER;
                return;
            }
        }

        Gdx.app.log("move", String.format("velocity %.3f direction %d. x=%.2f y=%.2f", timeNeededToMove, head.getDirection(), head.getX(), head.getY()));

        chest.setX(lastHeadX);
        chest.setY(lastHeadY);
        chest.setDirection(lastHeadDirection);

        if (justAdded) {

            newBodyPart.setX(chestX);
            newBodyPart.setY(chestY);
            newBodyPart.setDirection(chestDirection);

            player.bodyParts.add(2, newBodyPart);

            if (player.bodyParts.size() > 4) {
                player.bodyParts.set(player.bodyParts.size() - 2, lastBody);
            }

            newBodyPart = null;
        } else {

            if (!lastBodyIsTheChest) {

                lastBody.setX(chestX);
                lastBody.setY(chestY);
                lastBody.setDirection(chestDirection);

                player.bodyParts.remove(player.bodyParts.size() - 2);
                player.bodyParts.add(2, lastBody);
            }

            feet.setX(lastBodyX);
            feet.setY(lastBodyY);
            feet.setDirection(lastBodyDirection);
        }
    }

    private void checkCollisions() {

        boolean collidedToFood = false;
        boolean collidedToSpecial = false;
        boolean collidedToUnhealthy = false;
        Integer collidedToSnail = 0;
        Integer collidedToSnail2 = 0;
        BodyPart head = player.head();

        if (specialFood.getX()>= 0) {
            if (specialFood.getX()== head.getX() && specialFood.getY() == head.getY()) {
                collidedToSpecial = true;
            }
        }

        if (unhealthyFood.getX() >= 0) {
            if (unhealthyFood.getX() == head.getX() && unhealthyFood.getY() == head.getY()) {
                collidedToUnhealthy = true;
            }
        }

        ArrayList<Float> tempX = new ArrayList<Float>();
        ArrayList<Float> tempY = new ArrayList<Float>();
        ArrayList<Float> tempX2 = new ArrayList<Float>();
        ArrayList<Float> tempY2 = new ArrayList<Float>();
        tempX.add(collidableSnail.getX()); tempX2.add(collidableSnail2.getX());
        tempY.add(collidableSnail.getY()); tempY2.add(collidableSnail2.getY());
        for (int i = 1; i < 41; i++) {
            tempX.add(collidableSnail.getX() + i); tempX2.add(collidableSnail2.getX() + i);
            tempX.add(collidableSnail.getX() - i); tempX2.add(collidableSnail2.getX()- i);
            tempY.add(collidableSnail.getY() + i); tempY2.add(collidableSnail2.getY() + i);
            tempY.add(collidableSnail.getY() - i); tempY2.add(collidableSnail2.getY() + i);
        }
//        Check for collision within +-40 pixels of x & y
        for (int i = 0; i < tempX.size(); i++) {
            if (tempX.get(i) == head.getX()) {
                collidedToSnail += 1;
                break;
            }
        }
        for (int i = 0; i < tempY.size(); i++) {
            if (tempY.get(i) == head.getY()) {
                collidedToSnail += 1;
                break;
            }
        }
        tempX.clear();
        tempY.clear();

        if (collidedToSnail == 2) {
            collidedToSnail = 0;
            collidedToSnail();
        }

        for (int i = 0; i < tempX2.size(); i++) {
            if (tempX2.get(i) == head.getX()) {
                collidedToSnail2 += 1;
                break;
            }
        }
        for (int i = 0; i < tempY2.size(); i++) {
            if (tempY2.get(i) == head.getY()) {
                collidedToSnail2 += 1;
                break;
            }
        }
        tempX2.clear();
        tempY2.clear();

        if (collidedToSnail2 == 2) {
            collidedToSnail2 = 0;
            collidedToSnail();
        }

        if (collidedToSpecial) {
            collidedToSpecial();
        }
        else if (collidedToUnhealthy) {
            collidedToUnhealthy();
        }else {

            for (int i = 0, leni = foods.size(); !collidedToFood && i < leni; i++) {

                Food food = foods.get(i);

                if (food.getX() == head.getX() && food.getY() == head.getY()) {
                    collidedToFood = true;
                    foods.remove(i);
                    deadFoods.push(food);
                    leni--;
                    i--;
                }
            }
        }

        if (!collidedToFood) {

            if (!specialHandler.isRunning()) {

                l1:
                for (int i = 1, leni = player.bodyParts.size(); i < leni; i++) {

                    BodyPart bodyPart = player.bodyParts.get(i);

                    if (bodyPart.getX() == head.getX() && bodyPart.getY() == head.getY()) {
                        state = STATE.GAME_OVER;
                        player.collidedHurt();
                        break l1;
                    }
                }
            }
        }

        if (state == STATE.PLAYING && collidedToFood) {

            eatFoodSound.play();
            addBodyPart();
            addFood();

            computeScoreFromEatingFood();

            lastTimeOfEating = System.currentTimeMillis();

            if (timeNeededToMove < 0.20f) {
                timeNeededToMove -= 0.005f;
            } else {
                timeNeededToMove -= 0.01f;
            }

            if (timeNeededToMove < MINIMUM_WALK_TIME) {
                timeNeededToMove = MINIMUM_WALK_TIME;
            }
        }
    }

    private void checkWinCondition() {
        winScore = 100;
        if (score >= winScore) {
            state = STATE.GAME_WON;
        }
    }

    private void collidedToUnhealthy() {
        minusScoreSound.play();

        Gdx.app.log("render", String.format("unhealthy - 10"));

        score -= 10;
        unhealthyFood.setX(-100);
        if (score <= 0) {
            state = STATE.GAME_OVER;
        }
//        specialHandler.start();
    }

    private void collidedToSpecial() {

        eatFoodSound.play();

        specialFood.setX(-100);
        specialHandler.start();
    }

    private void collidedToSnail() {
        minusScoreSound.play();
        score -= 5;
        if (score <= 0) {
            state = STATE.GAME_OVER;
            player.collidedHurt();
        }
    }

    private void timercount() {
        if ((int) Math.floor(time) >=60) {
            state = STATE.GAME_OVER;
        }
    }

    private void computeScoreFromEatingFood() {

        float deltaEating = System.currentTimeMillis() - lastTimeOfEating;

        score += 10 + Math.floor(10.0f * Math.max(0, (5f - (deltaEating / 1000)) / 5f));
    }

    private void addBodyPart() {

        BodyPart feet = player.bodyParts.get(player.bodyParts.size() - 1);

        newBodyPart = new BodyPart(feet.getX(), feet.getY());
        newBodyPart.color = Color.RED;
        newBodyPart.texture = tbody;

        hitObjects.add(newBodyPart);

        // Increase the player's scale by 0.05
        player.scaleX += 0.05f;
        player.scaleY += 0.05f;
    }

    private float[] getEmptyCell() {

        float x = 0;
        float y = 0;

        boolean collision = false;

        do {

            collision = false;
            x = (int) (MathUtils.floor(MathUtils.random() * (float) numCellsX) * CELL_WIDTH);
            y = (int) (MathUtils.floor(MathUtils.random() * (float) numCellsY) * CELL_HEIGHT);

            for (int i = 0, leni = hitObjects.size(); i < leni && collision == false; i++) {

                GameObject gameObject = hitObjects.get(i);

                if (gameObject.getX() == x && gameObject.getY() == y) {
                    collision = true;
                }
            }
        } while (collision);

        return new float[]{x, y};
    }

    private void addSpecialFood(float delta) {
        specialTime += delta;

        if (specialTime > SPECIAL_TIME_HOP) {
            specialTime -= SPECIAL_TIME_HOP;

            float f = MathUtils.random();
            Gdx.app.log("render", String.format("specialFood.x %.2f MathUtils.random(): %.2f", specialFood.getX(), f));

            if (specialFood.getX() < 0 && f < 0.03f) {
                addSpecialFood0();
            }
        }
    }

    private void addUnhealthyFood(float delta) {
        if (unhealthyFood.getX() < 0) {
            addUnhealthyFood0();
        }
    }

    private void addUnhealthyFood0() {

        float[] emptyCell = getEmptyCell();

        float x = emptyCell[0];
        float y = emptyCell[1];

        unhealthyFood.set(x, y, unhealthyFoodTexture);
        hitObjects.add(unhealthyFood);

//        Gdx.app.log("addSpecialFood", String.format("x=%.2f, y=%.2f", x, y));
    }

    private void addSpecialFood0() {

        float[] emptyCell = getEmptyCell();

        float x = emptyCell[0];
        float y = emptyCell[1];

        specialFood.set(x, y, specialFoodTexture);
        hitObjects.add(specialFood);

        Gdx.app.log("addSpecialFood", String.format("x=%.2f, y=%.2f", x, y));
    }

    private void addFood() {

        if (foods.size() >= MAX_FOOD) {
            return;
        }

        Food food = deadFoods.pop();

        float[] emptyCell = getEmptyCell();

        float x = emptyCell[0];
        float y = emptyCell[1];

        food.set(x, y, foodTexture1);
        foods.add(food);

        hitObjects.add(food);

        Gdx.app.log("addFood", String.format("x=%.2f, y=%.2f", x, y));
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0.4f, 1, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        if (shapeDebug) {

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (BodyPart part : player.bodyParts) {

                if (part.color != null) {
                    shapeRenderer.setColor(part.color);
                }

                shapeRenderer.rect(part.getX(), part.getY(), CELL_WIDTH, CELL_HEIGHT);
            }
            shapeRenderer.end();
        }

        batch.begin();

        batch.draw(bgTexture, 0, 0);

        for (Food food : foods) {
            batch.draw(food.texture, food.getX(), food.getY());
        }

        if (specialFood.getX() >= 0) {
            batch.draw(specialFood.texture, specialFood.getX(), specialFood.getY());
        }

        if (unhealthyFood.getX() >= 0) {
            batch.draw(unhealthyFood.texture, unhealthyFood.getX(), unhealthyFood.getY());
        }

        // condition to set positions of snails for the first time
        if (collidableSnail.getUsed() != true && collidableSnail2.getUsed() != true) {
            if (state != STATE.GAME_OVER && state != STATE.PAUSED) {
                collidableSnail.set(50, 50, collidableSnailT, 1);
                collidableSnail2.set(350, 300, collidableSnail2T, 3);
                collidableSnail.setUsed();
                collidableSnail2.setUsed();
            }
        }
        if (collidableSnail.getUsed() == true && collidableSnail2.getUsed() == true){
            if (state != STATE.GAME_OVER && state != STATE.PAUSED) {
                collidableSnail.movement(1);
                collidableSnail2.movement(2);
                if (collidableSnail.getStopped() == true && collidableSnail2.getStopped() == true) {
                    collidableSnail.setStopped();
                    collidableSnail2.setStopped();
                }
            }
            else if (state == STATE.GAME_OVER || state == STATE.PAUSED) {
                if (collidableSnail.getStopped() != true && collidableSnail2.getStopped() != true) {
                    collidableSnail.setStopped();
                    collidableSnail2.setStopped();
                }
            }
        }
        batch.draw(collidableSnail.getTexture(), collidableSnail.getX(), collidableSnail.getY());
        batch.draw(collidableSnail2.getTexture(), collidableSnail2.getX(), collidableSnail2.getY());

        for (int i = 1; i < player.bodyParts.size(); i++) {
            drawBodyPart(player.bodyParts.get(i));
        }

        drawBodyPart(player.head());

        if (state == STATE.GAME_OVER) {

            String s = "Game Over";

            glyphLayout.reset();
            glyphLayout.setText(gameOverFont, s);

            Gdx.app.log("render ", s + " at " + ((viewport.getWorldHeight() / 2) + (glyphLayout.height / 2)));
            gameOverFont.draw(batch, s, (viewport.getWorldWidth() / 2) - (glyphLayout.width / 2), (viewport.getWorldHeight() / 2) + (glyphLayout.height / 2));
        }

        if (state == STATE.PAUSED) {

            String s = "Paused";

            glyphLayout.reset();
            glyphLayout.setText(gameOverFont, s);

            Gdx.app.log("render ", s + " at " + ((viewport.getWorldHeight() / 2) + (glyphLayout.height / 2)));
            gameOverFont.draw(batch, s, (viewport.getWorldWidth() / 2) - (glyphLayout.width / 2), (viewport.getWorldHeight() / 2) + (glyphLayout.height / 2));
        }

        if (state == STATE.GAME_WON) {

            String s = "You Win";

            glyphLayout.reset();
            glyphLayout.setText(gameOverFont, s);

            Gdx.app.log("render ", s + " at " + ((viewport.getWorldHeight() / 2) + (glyphLayout.height / 2)));
            gameOverFont.draw(batch, s, (viewport.getWorldWidth() / 2) - (glyphLayout.width / 2), (viewport.getWorldHeight() / 2) + (glyphLayout.height / 2));
        }

        drawScore();
        drawTime();
        timercount();

        if (specialHandler.isRunning()) {
            specialHandler.render(batch);
        }

        batch.end();
    }

    private void drawScore() {

        String s = "SCORE: " + score;

        glyphLayout.reset();
        glyphLayout.setText(scoreFont, s);

        scoreFont.draw(batch, s, scoreFont.getScaleX(), viewport.getWorldHeight() - glyphLayout.height);
    }

    private void drawTime() {

        String s = "TIME: " + (int) Math.floor(time);

        glyphLayout.reset();
        glyphLayout.setText(scoreFont, s);

        scoreFont.draw(batch, s, viewport.getWorldWidth() - glyphLayout.width - scoreFont.getScaleY(), viewport.getWorldHeight() - glyphLayout.height);
    }

    private void drawBodyPart(BodyPart part) {

        float rotation = part.getDirection() * 90;
        int srcWidth = part.texture.getWidth();
        int srcHeight = part.texture.getHeight();
        int srcX = 0;
        int srcY = 0;
        boolean flipX = false;
        boolean flipY = false;

        batch.draw(part.texture, part.getX(), part.getY(), part.texture.getWidth() / 2.0f, part.texture.getHeight() / 2.0f,
                part.texture.getWidth(), part.texture.getHeight(), player.scaleX, player.scaleY, rotation,
                srcX, srcY, srcWidth, srcHeight, flipX, flipY);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

        bgAudio.pause();
    }

    @Override
    public void resume() {

        bgAudio.play();
    }

    @Override
    public void hide() {

        bgAudio.pause();
    }

    @Override
    public void dispose() {

        bgAudio.dispose();
        bgTexture.dispose();
        foodTexture1.dispose();
        specialFoodTexture.dispose();
        unhealthyFoodTexture.dispose();
        collidableSnailT.dispose();
        collidableSnail2T.dispose();
        playDownTexture.dispose();
        playUpTexture.dispose();
        stage.dispose();

        gameOverFont.dispose();
        scoreFont.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }

    private enum STATE {
        PLAYING, PAUSED, GAME_OVER, GAME_WON
    }
}
