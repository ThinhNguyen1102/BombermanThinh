package Bomber.Components.enemy;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;


import static Bomber.Constants.GameConst.*;


public abstract class NormalEnemy extends Component {
    protected double dx = ENEMY_SPEED;
    protected double dy = 0;
    private double lastX = 0;
    private double lastY = 0;

    protected enum TurnDirection {
        BLOCK_LEFT, BLOCK_RIGHT, BLOCK_UP, BLOCK_DOWN
    }


    protected final AnimatedTexture texture;
    protected static final double ANIM_TIME = 0.5;
    protected static final int SIZE_FLAME = 45;

    protected AnimationChannel animWalkRight;
    protected AnimationChannel animWalkLeft;
    protected AnimationChannel animDie;
    protected AnimationChannel animStop;

    public NormalEnemy() {
        setAnimationMove();

        texture = new AnimatedTexture(animWalkRight);
        texture.loop();
    }

    protected abstract void setAnimationMove();

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        entity.setScaleUniform(0.9);
        entity.translateX(dx * tpf);
        entity.translateY(dy * tpf);

        setAnimationStage();
    }

    protected void setAnimationStage() {
        double dx = entity.getX() - lastX;
        double dy = entity.getY() - lastY;

        lastX = entity.getX();
        lastY = entity.getY();

        if (dx == 0 && dy == 0) {
            return;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                texture.loopNoOverride(animWalkRight);
            } else {
                texture.loopNoOverride(animWalkLeft);
            }
        } else {
            if (dy > 0) {
                texture.loopNoOverride(animWalkLeft);
            } else {
                texture.loopNoOverride(animWalkRight);
            }
        }
    }

    protected void setTurnEnemy(TurnDirection direct) {
        switch (direct) {
            case BLOCK_LEFT:
                entity.translateX(3);
                dx = 0.0;
                dy = getRandom();
                break;
            case BLOCK_RIGHT:
                entity.translateX(-3);
                dx = 0.0;
                dy = getRandom();
                break;
            case BLOCK_UP:
                entity.translateY(3);
                dy = 0.0;
                dx = getRandom();
                break;
            case BLOCK_DOWN:
                entity.translateY(-3);
                dy = 0.0;
                dx = getRandom();
                break;
        }
    }

    protected double getRandom() {
        return Math.random() > 0.5 ? ENEMY_SPEED : -ENEMY_SPEED;
    }

    protected void turn() {
        if (dx < 0.0) {
            setTurnEnemy(TurnDirection.BLOCK_LEFT);
        } else if (dx > 0.0) {
            setTurnEnemy(TurnDirection.BLOCK_RIGHT);
        } else if (dy < 0.0) {
            setTurnEnemy(TurnDirection.BLOCK_UP);
        } else if (dy > 0.0) {
            setTurnEnemy(TurnDirection.BLOCK_DOWN);
        }
    }

    public void enemyDie() {
        dx = 0;
        dy = 0;
        texture.loopNoOverride(animDie);
    }

    public void enemyStop() {
        dx = 0;
        dy = 0;
        texture.loopNoOverride(animStop);
    }
}

