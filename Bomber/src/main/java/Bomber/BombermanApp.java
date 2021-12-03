package Bomber;


import Bomber.Components.PlayerComponent;
import Bomber.menu.BombermanGameMenu;
import Bomber.menu.BombermanMenu;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.physics.PhysicsWorld;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;
import static Bomber.Constants.GameConst.*;

public class BombermanApp extends GameApplication {
    public static boolean sound_enabled = true;
    private boolean requestNewGame = false;
    private AStarGrid grid;

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setHeight(VIEW_HEIGHT);
        gameSettings.setWidth(VIEW_WIDTH);
        gameSettings.setTitle(GAME_TITLE);
        gameSettings.setVersion(GAME_VERSION);
        gameSettings.setIntroEnabled(false);
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setGameMenuEnabled(true);
        gameSettings.setPreserveResizeRatio(true);
        gameSettings.setManualResizeEnabled(true);
        gameSettings.setDeveloperMenuEnabled(true);
        gameSettings.setFontUI("Quinquefive-Ea6d4.ttf");
        gameSettings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newMainMenu() {
                return new BombermanMenu();
            }

            @Override
            public FXGLMenu newGameMenu() {
                return new BombermanGameMenu();
            }
        });
    }


    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BombermanFactory());
        nextLevel();
        FXGL.spawn("background");

        run(() -> inc("time", -1), Duration.seconds(1));
        getWorldProperties().<Integer>addListener("time", (old, now) -> {
            if (now == 0) {
                onPlayerKilled();
            }
        });

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("bomb", 1);
        vars.put("flame", 1);
        vars.put("score", 0);
        vars.put("speed", SPEED);
        vars.put("time", TIME_PER_LEVEL);
        vars.put("level", START_LEVEL);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalSoundVolume(sound_enabled ? 0.1 : 0.0);
        getSettings().setGlobalMusicVolume(sound_enabled ? 0.1 : 0.0);
        loopBGM("title_screen.mp3");
    }

    private double timeChangeWallE = 0;

    @Override
    protected void onUpdate(double tpf) {

        if (geti("time") == 0) {
            getDialogService().showMessageBox("Game Over!!!", getGameController()::startNewGame);
        }

        if (requestNewGame) {
            requestNewGame = false;
            getPlayer().getComponent(PlayerComponent.class).die();
            getPlayer().getComponent(PlayerComponent.class).setExploreCancel(true);
            getGameTimer().runOnceAfter(() -> {
                getGameScene().getViewport().fade(() -> {
                    set("bomb", 1);
                    setLevel();
                });
            }, Duration.seconds(0.5));
        }
    }

    @Override
    protected void initUI() {
//        HBox box = new HBox();
//        box.setAlignment(Pos.CENTER);
//        box.getChildren().addAll(score, speed, flame, bomb, levelTime);
//        box.getStylesheets().add(getClass().getResource("/assets/ui/fonts/Style.css").toExternalForm());
//        FXGL.addUINode(box, 0, 0);

        FXGL.addUINode(setTextUI("score", "Score: %d"), 20, 30);

        FXGL.addUINode(setTextUI("speed", "Speed: %d"), 140, 30);

        FXGL.addUINode(setTextUI("flame", "Flame: %d"), 280, 30);

        FXGL.addUINode(setTextUI("bomb", "Bombs: %d"), 390, 30);

        FXGL.addUINode(setTextUI("time", "Time: %d"), 500, 30);
    }

    private Label setTextUI(String valGame, String content) {
        Label label = new Label();
        label.setTextFill(Color.BLACK);
        label.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 20));
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.LIGHTGREEN);
        label.setEffect(shadow);
        // valGame's datatype is int
        label.textProperty().bind(FXGL.getip(valGame).asString(content));
        return label;
    }

    private static Entity getPlayer() {
        return FXGL.getGameWorld().getSingleton(BombermanType.PLAYER);
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Move Up") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).up();
            }

            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Move Down") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).down();
            }

            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).left();
            }

            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                getPlayer().getComponent(PlayerComponent.class).right();
            }

            @Override
            protected void onActionEnd() {
                getPlayer().getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Place Bomb") {
            @Override
            protected void onActionBegin() {
                getPlayer().getComponent(PlayerComponent.class).placeBomb();
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physics = getPhysicsWorld();
        physics.setGravity(0, 0);

        onCollisionBegin(BombermanType.PLAYER, BombermanType.PORTAL, (p, po) -> {
            getGameTimer().runOnceAfter(() -> {
                getGameScene().getViewport().fade(this::nextLevel);
            }, Duration.seconds(1));
        });

//        onCollisionBegin(BombermanType.PLAYER, BombermanType.FIRE, (p, f) -> {
//            onPlayerKilled();
//        });

//        onCollisionBegin(BombermanType.PLAYER, BombermanType.BALLOOM_E, (p, b) -> {
//            onPlayerKilled();
//        });

//        onCollisionBegin(BombermanType.PLAYER, BombermanType.ONEAL_E, (p, b) -> {
//            onPlayerKilled();
//        });
    }

    private void gameOver() {
        getDialogService().showMessageBox("Demo Over. Press OK to exit", getGameController()::exit);
    }

    private void onPlayerKilled() {
        requestNewGame = true;
    }

    private void nextLevel() {
        if (FXGL.geti("level") == MAX_LEVEL) {
            showMessage("You win !!!");
            return;
        }

        inc("level", +1);
        setLevel();
    }

    private void setLevel() {
        FXGL.setLevelFromMap("bbm_level" + FXGL.geti("level") + ".tmx");
        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(0, 0, GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);
        viewport.bindToEntity(getPlayer(), getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);
        set("time", TIME_PER_LEVEL);

        grid = AStarGrid.fromWorld(getGameWorld(), 31, 15,
                SIZE_BLOCK, SIZE_BLOCK, (type) -> {
                    if (type == BombermanType.BRICK
                            || type == BombermanType.WALL
                            || type == BombermanType.GRASS
                            || type == BombermanType.CORAL) {
                        return CellState.NOT_WALKABLE;
                    } else {
                        return CellState.WALKABLE;
                    }
                });

        set("grid", grid);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
