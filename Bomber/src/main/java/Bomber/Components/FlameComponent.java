package Bomber.Components;

import Bomber.BombermanType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static Bomber.Constants.GameConst.*;

public class FlameComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animationFlame;


    public FlameComponent() {
        PhysicsWorld physics = getPhysicsWorld();

        onCollisionBegin(BombermanType.FIRE, BombermanType.WALL, (f, w) -> {
            f.removeFromWorld();
        });

        onCollisionBegin(BombermanType.FIRE, BombermanType.BRICK, (f, b) -> {
            Entity bBreak = spawn("brick_break", new SpawnData(b.getX(), b.getY()));
            b.removeFromWorld();
            getGameTimer().runOnceAfter(bBreak::removeFromWorld, Duration.seconds(1));
            inc("score", SCORE_BRICK);
        });

        onCollisionBegin(BombermanType.FIRE, BombermanType.GRASS, (f, g) -> {
            Entity gBreak = spawn("grass_break", new SpawnData(g.getX(), g.getY()));
            g.removeFromWorld();
            getGameTimer().runOnceAfter(gBreak::removeFromWorld, Duration.seconds(1));
            inc("score", SCORE_BRICK);
        });

        onCollisionBegin(BombermanType.FIRE, BombermanType.CORAL, (f, c) -> {
            c.removeFromWorld();
            inc("score", SCORE_BRICK);
        });

        onCollisionBegin(BombermanType.FIRE, BombermanType.BALLOOM_E, (f, b) -> {
            b.getComponent(BalloomComponent.class).BalloomDie();
            getGameTimer().runOnceAfter(b::removeFromWorld, Duration.seconds(1));
        });

        animationFlame = new AnimationChannel(image("bomb_exploded_1.png"), 3, SIZE_BLOCK, SIZE_BLOCK,
                Duration.seconds(0.4), 0, 2);

        texture = new AnimatedTexture(animationFlame);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
