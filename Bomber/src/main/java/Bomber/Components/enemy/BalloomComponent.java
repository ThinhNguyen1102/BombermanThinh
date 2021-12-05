package Bomber.Components.enemy;

import Bomber.BombermanType;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;


import static com.almasb.fxgl.dsl.FXGL.*;


public class BalloomComponent extends NormalEnemy {

    public BalloomComponent() {
        super();
        onCollisionBegin(BombermanType.BALLOOM_E, BombermanType.WALL, (b, w) -> {
            b.getComponent(BalloomComponent.class).turn();
        });

        onCollisionBegin(BombermanType.BALLOOM_E, BombermanType.BRICK, (b, br) -> {
            b.getComponent(BalloomComponent.class).turn();
        });
    }

    @Override
    protected void setAnimationMove() {
        animDie = new AnimationChannel(image("balloom.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 0, 0);
        animWalkRight = new AnimationChannel(image("balloom.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 4, 6);
        animWalkLeft = new AnimationChannel(image("balloom.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 1, 3);
        animStop = new AnimationChannel(image("balloom.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 1, 6);
    }

}
