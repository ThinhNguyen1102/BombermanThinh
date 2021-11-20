package Bomber;

import Bomber.Components.PlayerComponent;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getInput;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;

public class BombermanApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setHeight(704);
        gameSettings.setWidth(704);
        gameSettings.setTitle("Bomberman");
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BombermanFactory());
        FXGL.spawn("background");
        FXGL.setLevelFromMap("level1.tmx");

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(0, 0, 1280, 704);
        viewport.bindToEntity(getPlayer(), getAppWidth() / 2, getAppHeight() / 2);
        viewport.setLazy(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("bomb", 1);
        vars.put("flame", 1);
        vars.put("score", 0);
        vars.put("speed", 150);
    }

    @Override
    protected void initUI() {
        Label scoreLabel = new Label();
        scoreLabel.setTextFill(Color.BLACK);
        scoreLabel.setFont(Font.font(20.0));
        scoreLabel.textProperty().bind(FXGL.getip("score").asString("Score: %d"));
        FXGL.addUINode(scoreLabel, 20, 10);

        Label speedLabel = new Label();
        speedLabel.setTextFill(Color.BLACK);
        speedLabel.setFont(Font.font(20.0));
        speedLabel.textProperty().bind(FXGL.getip("speed").asString("Speed: %d"));
        FXGL.addUINode(speedLabel, 120, 10);

        Label flameLabel = new Label();
        flameLabel.setTextFill(Color.BLACK);
        flameLabel.setFont(Font.font(20.0));
        flameLabel.textProperty().bind(FXGL.getip("flame").asString("Flame: %d"));
        FXGL.addUINode(flameLabel, 320, 10);

        Label bombsLabel = new Label();
        bombsLabel.setTextFill(Color.BLACK);
        bombsLabel.setFont(Font.font(20.0));
        bombsLabel.textProperty().bind(FXGL.getip("bomb").asString("Bombs: %d"));
        FXGL.addUINode(bombsLabel, 420, 10);
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
