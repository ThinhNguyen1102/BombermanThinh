package Bomber.Components;

import Bomber.BombermanType;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;


import static Bomber.Constants.GameConst.*;
import static com.almasb.fxgl.dsl.FXGL.*;


public class BalloomComponent extends Component {
    private static final double ANIM_TIME_BALLOOM = 0.5;
    private static final int SIZE_B = 45;
    private double speed = 100.0;

    private double dx = speed;
    private double dy = 0;


    private enum MoveDirection {
        RIGHT, LEFT, DIE, STOP
    }

    private MoveDirection currMove = MoveDirection.RIGHT;

    private AnimatedTexture texture;
    private AnimationChannel animWalkRight, animWalkLeft, animDie, animStop;

    public BalloomComponent() {
        onCollisionBegin(BombermanType.BALLOOM_E, BombermanType.WALL, (b, w) -> {
            b.getComponent(BalloomComponent.class).turn();
        });

        onCollisionBegin(BombermanType.BALLOOM_E, BombermanType.BRICK, (b, br) -> {
            b.getComponent(BalloomComponent.class).turn();
        });

        animDie = new AnimationChannel(image("balloom.png"), 7, SIZE_B, SIZE_B,
                Duration.seconds(ANIM_TIME_BALLOOM), 0, 0);
        animWalkRight = new AnimationChannel(image("balloom.png"), 7, SIZE_B, SIZE_B,
                Duration.seconds(ANIM_TIME_BALLOOM), 4, 6);
        animWalkLeft = new AnimationChannel(image("balloom.png"), 7, SIZE_B, SIZE_B,
                Duration.seconds(ANIM_TIME_BALLOOM), 1, 3);
        animStop = new AnimationChannel(image("balloom.png"), 7, SIZE_B, SIZE_B,
                Duration.seconds(1), 1, 6);

        texture = new AnimatedTexture(animWalkRight);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        entity.setScaleUniform(0.9);
        entity.translateX(dx * tpf);
        entity.translateY(dy * tpf);

        switch (currMove) {
            case RIGHT:
                texture.loopNoOverride(animWalkRight);
                break;
            case LEFT:
                texture.loopNoOverride(animWalkLeft);
                break;
            case STOP:
                texture.loopNoOverride(animStop);
                break;
            case DIE:
                texture.loopNoOverride(animDie);
                break;
        }

    }

    public void turn() {
        if (dx < 0.0) {
            entity.translateX(2);
            dx = 0.0;
            dy = getRandomSpeed();
            if (dy > 0.0) {
                currMove = MoveDirection.RIGHT;
            } else {
                currMove = MoveDirection.LEFT;
            }
        } else if (dx > 0.0) {
            entity.translateX(-2);
            dx = 0.0;
            dy = getRandomSpeed();
            if (dy < 0.0) {
                currMove = MoveDirection.RIGHT;
            } else {
                currMove = MoveDirection.LEFT;
            }
        } else if (dy < 0.0) {
            entity.translateY(2);
            dy = 0.0;
            dx = getRandomSpeed();
            if (dx > 0.0) {
                currMove = MoveDirection.RIGHT;
            } else {
                currMove = MoveDirection.LEFT;
            }
        } else if (dy > 0.0) {
            entity.translateY(-2);
            dy = 0.0;
            dx = getRandomSpeed();
            if (dx > 0.0) {
                currMove = MoveDirection.RIGHT;
            } else {
                currMove = MoveDirection.LEFT;
            }
        }
    }

    public void BalloomDie() {
        dx = 0;
        dy = 0;
        currMove = MoveDirection.DIE;
    }

    public void BalloomStop() {
        dx = 0;
        dy = 0;
        currMove = MoveDirection.STOP;
    }

    private double getRandomSpeed() {
        return Math.random() > 0.5 ? BALLOOM_SPEED : -BALLOOM_SPEED;
    }
}
