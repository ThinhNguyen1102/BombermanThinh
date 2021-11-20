package Bomber.Components;

import Bomber.BombermanType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class FlameComponent extends Component {
    private AnimatedTexture texture;
    private AnimationChannel animationFlame;


    public FlameComponent() {
        PhysicsWorld physics = getPhysicsWorld();

        physics.addCollisionHandler(new CollisionHandler(BombermanType.FIRE, BombermanType.WALL) {
            @Override
            protected void onCollisionBegin(Entity fire, Entity wall) {
                fire.removeFromWorld();
            }
        });

        physics.addCollisionHandler(new CollisionHandler(BombermanType.FIRE, BombermanType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity fire, Entity brick) {
                Entity bBreak = spawn("brick_break", new SpawnData(brick.getX(), brick.getY()));
                brick.removeFromWorld();
                getGameTimer().runOnceAfter(bBreak::removeFromWorld, Duration.seconds(1));
                inc("score", 10);
            }
        });

        animationFlame = new AnimationChannel(image("bomb_exploded_1.png"), 3, 64, 64,
                Duration.seconds(0.4), 0, 2);

        texture = new AnimatedTexture(animationFlame);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
