package Bomber.Components;

import Bomber.BombermanType;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static Bomber.Constants.GameConst.*;

public class PlayerComponent extends Component {
    private static final double ANIM_TIME_PLAYER = 0.5;
    private static final int SIZE_FRAMES = 45;
    private int bombsPlaced = 0;
    private boolean exploreCancel = false;

    public enum StatusDirection {
        UP, RIGHT, DOWN, LEFT, STOP,DIE
    }

    public enum AnimationSkin {
        NORMAL, FLAME_PASS
    }

    private StatusDirection currMove = StatusDirection.STOP;
    private PhysicsComponent physics;
    private int speed = FXGL.geti("speed");

    private final AnimatedTexture texture;
    private AnimationChannel animIdleDown, animIdleRight, animIdleUp, animIdleLeft;
    private AnimationChannel animWalkDown, animWalkRight, animWalkUp, animWalkLeft;
    private AnimationChannel animDie;

    public PlayerComponent() {
        PhysicsWorld physics = getPhysicsWorld();
        physics.setGravity(0, 0);

        onCollisionBegin(BombermanType.PLAYER, BombermanType.SPEED_ITEM, (p, speed_i) -> {
            speed_i.removeFromWorld();
            inc("score", SCORE_ITEM);
            inc("speed", SPEED / 3);
            speed = geti("speed");

            getGameTimer().runOnceAfter(() -> {
                inc("speed", -SPEED / 3);
                speed = geti("speed");
            }, Duration.seconds(8));
        });

        onCollisionBegin(BombermanType.PLAYER, BombermanType.BOMB_ITEM, (p, bombs_t) -> {
            bombs_t.removeFromWorld();
            inc("score", SCORE_ITEM);
            inc("bomb", 1);
        });

        onCollisionBegin(BombermanType.PLAYER, BombermanType.FLAME_ITEM, (p, flame_i) -> {
            flame_i.removeFromWorld();
            inc("score", SCORE_ITEM);
            inc("flame", 1);
        });

        onCollisionBegin(BombermanType.PLAYER, BombermanType.FLAME_PASS_ITEM, (p, flame_pass_i) -> {
            flame_pass_i.removeFromWorld();
            inc("score", SCORE_ITEM);
            setAnimation(AnimationSkin.FLAME_PASS);
            getGameTimer().runOnceAfter(() -> {
                setAnimation(AnimationSkin.NORMAL);
            }, Duration.seconds(8));
        });

        setAnimation(AnimationSkin.NORMAL);

        texture = new AnimatedTexture(animIdleDown);
    }

    public void setAnimation(AnimationSkin animation) {
        if (animation == AnimationSkin.NORMAL) {
            animDie = new AnimationChannel(image("player_die.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(1.5), 0, 2);

            animIdleDown = new AnimationChannel(image("player_down.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 0);
            animIdleRight = new AnimationChannel(image("player_right.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 0);
            animIdleUp = new AnimationChannel(image("player_up.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 0);
            animIdleLeft = new AnimationChannel(image("player_left.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 0);

            animWalkDown = new AnimationChannel(image("player_down.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 2);
            animWalkRight = new AnimationChannel(image("player_right.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 2);
            animWalkUp = new AnimationChannel(image("player_up.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 2);
            animWalkLeft = new AnimationChannel(image("player_left.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 2);
        } else {
            animDie = new AnimationChannel(image("player_die.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(1.5), 0, 2);

            animIdleDown = new AnimationChannel(image("player_down_1.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 0);
            animIdleRight = new AnimationChannel(image("player_right_1.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 0);
            animIdleUp = new AnimationChannel(image("player_up_1.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 0);
            animIdleLeft = new AnimationChannel(image("player_left_1.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 0);

            animWalkDown = new AnimationChannel(image("player_down_1.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 2);
            animWalkRight = new AnimationChannel(image("player_right_1.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 2);
            animWalkUp = new AnimationChannel(image("player_up_1.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 2);
            animWalkLeft = new AnimationChannel(image("player_left_1.png"), 3, SIZE_FRAMES, SIZE_FRAMES,
                    Duration.seconds(ANIM_TIME_PLAYER), 0, 2);
        }
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        getEntity().setScaleUniform(0.95);
        if (physics.getVelocityX() != 0) {
            physics.setVelocityX((int) physics.getVelocityX() * 0.9);
            if (FXGLMath.abs(physics.getVelocityX()) < 1) {
                physics.setVelocityX(0);
            }
        }

        if (physics.getVelocityY() != 0) {
            physics.setVelocityY((int) physics.getVelocityY() * 0.9);
            if (FXGLMath.abs(physics.getVelocityY()) < 1) {
                physics.setVelocityY(0);
            }
        }

        switch (currMove) {
            case UP:
                texture.loopNoOverride(animWalkUp);
                break;
            case RIGHT:
                texture.loopNoOverride(animWalkRight);
                break;
            case DOWN:
                texture.loopNoOverride(animWalkDown);
                break;
            case LEFT:
                texture.loopNoOverride(animWalkLeft);
                break;
            case STOP:
                if (texture.getAnimationChannel() == animWalkDown) {
                    texture.loopNoOverride(animIdleDown);
                } else if (texture.getAnimationChannel() == animWalkUp) {
                    texture.loopNoOverride(animIdleUp);
                } else if (texture.getAnimationChannel() == animWalkLeft) {
                    texture.loopNoOverride(animIdleLeft);
                } else if (texture.getAnimationChannel() == animWalkRight) {
                    texture.loopNoOverride(animIdleRight);
                }
                break;
            case DIE:
                texture.loopNoOverride(animDie);
                break;
        }
    }

    public void up() {
        if(currMove != StatusDirection.DIE) {
            currMove = StatusDirection.UP;
            physics.setVelocityY(-speed);
        }
    }

    public void down() {
        if(currMove != StatusDirection.DIE) {
            currMove = StatusDirection.DOWN;
            physics.setVelocityY(speed);
        }
    }

    public void left() {
        if(currMove != StatusDirection.DIE) {
            currMove = StatusDirection.LEFT;
            physics.setVelocityX(-speed);
        }
    }

    public void right() {
        if(currMove != StatusDirection.DIE) {
            currMove = StatusDirection.RIGHT;
            physics.setVelocityX(speed);
        }
    }

    public void stop() {
        if(currMove != StatusDirection.DIE) {
            currMove = StatusDirection.STOP;
        }
    }

    public void die() {
        currMove = StatusDirection.DIE;
    }

    public void placeBomb() {
        if (bombsPlaced == geti("bomb")) {
            return;
        }
        bombsPlaced++;
        // fix
        int bombX = (int) (entity.getX() % SIZE_BLOCK > SIZE_BLOCK / 2
                ? entity.getX() + SIZE_BLOCK - entity.getX() % SIZE_BLOCK + 1
                : entity.getX() - entity.getX() % SIZE_BLOCK + 1);
        int bombY = (int) (entity.getY() % SIZE_BLOCK > SIZE_BLOCK / 2
                ? entity.getY() + SIZE_BLOCK - entity.getY() % SIZE_BLOCK + 1
                : entity.getY() - entity.getY() % SIZE_BLOCK + 1);

        Entity bomb = spawn("bomb", new SpawnData(bombX, bombY));

        if (currMove != StatusDirection.DIE) {
            getGameTimer().runOnceAfter(() -> {
                if (!exploreCancel) {
                    bomb.getComponent(BombComponent.class).explode();
                } else {
                    bomb.removeFromWorld();
                }
                bombsPlaced--;
            }, Duration.seconds(2.5));
        }
    }

    public boolean isExploreCancel() {
        return exploreCancel;
    }

    public void setExploreCancel(boolean exploreCancel) {
        this.exploreCancel = exploreCancel;
    }
}
