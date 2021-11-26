package Bomber.Components;

import Bomber.BombermanType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.CollisionHandler;
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

//        physics.addCollisionHandler(new CollisionHandler(BombermanType.FIRE, BombermanType.WALL) {
//            @Override
//            protected void onCollisionBegin(Entity fire, Entity wall) {
//                fire.removeFromWorld();
//            }
//        });

        onCollisionBegin(BombermanType.FIRE, BombermanType.BRICK, (f, b) -> {
            Entity bBreak = spawn("brick_break", new SpawnData(b.getX(), b.getY()));
            b.removeFromWorld();
            getGameTimer().runOnceAfter(bBreak::removeFromWorld, Duration.seconds(1));
            inc("score", SCORE_BRICK);
        });

//        physics.addCollisionHandler(new CollisionHandler(BombermanType.FIRE, BombermanType.BRICK) {
//            @Override
//            protected void onCollisionBegin(Entity fire, Entity brick) {
//                Entity bBreak = spawn("brick_break", new SpawnData(brick.getX(), brick.getY()));
//                brick.removeFromWorld();
//                getGameTimer().runOnceAfter(bBreak::removeFromWorld, Duration.seconds(1));
//                inc("score", SCORE_BRICK);
//            }
//        });

        onCollisionBegin(BombermanType.FIRE, BombermanType.GRASS, (f, g) -> {
            Entity gBreak = spawn("grass_break", new SpawnData(g.getX(), g.getY()));
            g.removeFromWorld();
            getGameTimer().runOnceAfter(gBreak::removeFromWorld, Duration.seconds(1));
            inc("score", SCORE_BRICK);
        });

//        physics.addCollisionHandler(new CollisionHandler(BombermanType.FIRE, BombermanType.GRASS) {
//            @Override
//            protected void onCollisionBegin(Entity fire, Entity grass) {
//                Entity gBreak = spawn("grass_break", new SpawnData(grass.getX(), grass.getY()));
//                grass.removeFromWorld();
//                getGameTimer().runOnceAfter(gBreak::removeFromWorld, Duration.seconds(1));
//                inc("score", SCORE_BRICK);
//            }
//        });

        onCollisionBegin(BombermanType.FIRE, BombermanType.CORAL, (f, c) -> {
            c.removeFromWorld();
            inc("score", SCORE_BRICK);
        });

//        physics.addCollisionHandler(new CollisionHandler(BombermanType.FIRE, BombermanType.CORAL) {
//            @Override
//            protected void onCollisionBegin(Entity fire, Entity coral) {
//                coral.removeFromWorld();
//                inc("score", SCORE_BRICK);
//            }
//        });

        animationFlame = new AnimationChannel(image("bomb_exploded_1.png"), 3, SIZE, SIZE,
                Duration.seconds(0.4), 0, 2);

        texture = new AnimatedTexture(animationFlame);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
