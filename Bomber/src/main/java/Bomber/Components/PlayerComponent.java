package Bomber.Components;

import Bomber.BombermanType;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
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

public class PlayerComponent extends Component {
    enum MoveDirection {
        DOWN, UP, LEFT, RIGHT, STOP, DIE
    }

    public enum AnimationSkin {
        NORMAL, FLAME_PASS
    }

    private int bombsPlaced = 0;

    private MoveDirection currMove = MoveDirection.STOP;
    private PhysicsComponent physics;
    private int speed = FXGL.geti("speed");

    private AnimatedTexture texture;
    private AnimationChannel animIdleDown, animIdleRight, animIdleUp, animIdleLeft;
    private AnimationChannel animWalkDown, animWalkRight, animWalkUp, animWalkLeft;
    private AnimationChannel animDie;

    public PlayerComponent() {
        PhysicsWorld physics = getPhysicsWorld();
        physics.addCollisionHandler(new CollisionHandler(BombermanType.PLAYER, BombermanType.SPEED_ITEM) {
            @Override
            protected void onCollisionBegin(Entity player, Entity speedItem) {
                speedItem.removeFromWorld();
                inc("score", 20);
                inc("speed", 150);
                speed = geti("speed");

                getGameTimer().runOnceAfter(() -> {
                    inc("speed", -150);
                    speed = geti("speed");
                }, Duration.seconds(8));
            }
        });
        physics.addCollisionHandler(new CollisionHandler(BombermanType.PLAYER, BombermanType.BOMB_ITEM) {
            @Override
            protected void onCollisionBegin(Entity player, Entity bombItem) {
                bombItem.removeFromWorld();
                inc("score", 20);
                inc("bomb", 1);
            }
        });
        physics.addCollisionHandler(new CollisionHandler(BombermanType.PLAYER, BombermanType.FLAME_ITEM) {
            @Override
            protected void onCollisionBegin(Entity player, Entity flameItem) {
                flameItem.removeFromWorld();
                inc("score", 20);
                inc("flame", 1);
            }
        });

        physics.addCollisionHandler(new CollisionHandler(BombermanType.PLAYER, BombermanType.FLAME_PASS_ITEM) {
            @Override
            protected void onCollisionBegin(Entity player, Entity flamePassItem) {
                flamePassItem.removeFromWorld();
                inc("score", 20);
                setAnimation(AnimationSkin.FLAME_PASS);
                getGameTimer().runOnceAfter(() -> {
                    setAnimation(AnimationSkin.NORMAL);
                }, Duration.seconds(8));
            }
        });

        setAnimation(AnimationSkin.NORMAL);

        texture = new AnimatedTexture(animIdleDown);
//        getEntity().setScaleUniform(0.9);
    }

    public void setAnimation(AnimationSkin animation) {
        if (animation == AnimationSkin.NORMAL) {
            animDie = new AnimationChannel(image("player_die.png"), 3, 60, 60,
                    Duration.seconds(1.8), 0, 2);

            animIdleDown = new AnimationChannel(image("player_down.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 0);
            animIdleRight = new AnimationChannel(image("player_right.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 0);
            animIdleUp = new AnimationChannel(image("player_up.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 0);
            animIdleLeft = new AnimationChannel(image("player_left.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 0);

            animWalkDown = new AnimationChannel(image("player_down.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 2);
            animWalkRight = new AnimationChannel(image("player_right.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 2);
            animWalkUp = new AnimationChannel(image("player_up.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 2);
            animWalkLeft = new AnimationChannel(image("player_left.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 2);
        } else {
            animDie = new AnimationChannel(image("player_die.png"), 3, 60, 60,
                    Duration.seconds(1.8), 0, 2);

            animIdleDown = new AnimationChannel(image("player_down_1.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 0);
            animIdleRight = new AnimationChannel(image("player_right_1.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 0);
            animIdleUp = new AnimationChannel(image("player_up_1.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 0);
            animIdleLeft = new AnimationChannel(image("player_left_1.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 0);

            animWalkDown = new AnimationChannel(image("player_down_1.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 2);
            animWalkRight = new AnimationChannel(image("player_right_1.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 2);
            animWalkUp = new AnimationChannel(image("player_up_1.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 2);
            animWalkLeft = new AnimationChannel(image("player_left_1.png"), 3, 60, 60,
                    Duration.seconds(0.5), 0, 2);
        }
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        getEntity().setScaleUniform(0.9);
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
        if(currMove != MoveDirection.DIE) {
            currMove = MoveDirection.UP;
            physics.setVelocityY(-speed);
        }
    }

    public void down() {
        if(currMove != MoveDirection.DIE) {
            currMove = MoveDirection.DOWN;
            physics.setVelocityY(speed);
        }
    }

    public void left() {
        if(currMove != MoveDirection.DIE) {
            currMove = MoveDirection.LEFT;
            physics.setVelocityX(-speed);
        }
    }

    public void right() {
        if(currMove != MoveDirection.DIE) {
            currMove = MoveDirection.RIGHT;
            physics.setVelocityX(speed);
        }
    }

    public void stop() {
        if(currMove != MoveDirection.DIE) {
            currMove = MoveDirection.STOP;
        }
    }

    public void die() {
        currMove = MoveDirection.DIE;
    }

    public void placeBomb() {
        if (bombsPlaced == geti("bomb")) {
            return;
        }
        bombsPlaced++;
        int bombLocationX = (int) (entity.getX() % 64 > 32
                ? entity.getX() + 64 - entity.getX() % 64 + 1
                : entity.getX() - entity.getX() % 64 + 1);
        int bombLocationY = (int) (entity.getY() % 64 > 32
                ? entity.getY() + 64 - entity.getY() % 64 + 1
                : entity.getY() - entity.getY() % 64 + 1);

        Entity bomb = spawn("bomb", new SpawnData(bombLocationX, bombLocationY));

        getGameTimer().runOnceAfter(() -> {
            bomb.getComponent(BombComponent.class).explode();
            bombsPlaced--;
        }, Duration.seconds(2.5));

    }
}