package Bomber.Components.enemy;


import Bomber.BombermanType;
import Bomber.Components.ai.DelayedChasePlayerComponent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;


import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;


public class OnealComponent extends NormalEnemy {
    private boolean moveAi = false;
    private boolean die = false;
    public OnealComponent() {
        super();
        onCollisionBegin(BombermanType.ONEAL_E, BombermanType.WALL, (o, w) -> {
            o.getComponent(OnealComponent.class).turn();
        });

        onCollisionBegin(BombermanType.ONEAL_E, BombermanType.BRICK, (o, br) -> {
            o.getComponent(OnealComponent.class).turn();
        });

        onCollisionBegin(BombermanType.ONEAL_E, BombermanType.GRASS, (o, gr) -> {
            o.getComponent(OnealComponent.class).turn();
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (!die) {
            if (!moveAi) {
                DelayedChasePlayerComponent.setDelayed(true);
                entity.setScaleUniform(0.9);
                entity.translateX(dx * tpf);
                entity.translateY(dy * tpf);
            } else {
                DelayedChasePlayerComponent.setDelayed(false);
            }
        }
        setAnimationStage();
        detectPlayer();
    }

    private void detectPlayer() {
        BoundingBoxComponent bbox = entity.getBoundingBoxComponent();
        List<Entity> list = FXGL.getGameWorld().getEntitiesInRange(bbox.range(60, 60));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isType(BombermanType.PLAYER)) {
//                DelayedChasePlayerComponent.setDelayed(false);
                if (!die) moveAi = true;
                break;
            }

            if (i == list.size() - 1) {
                if (!die) moveAi = false;
//                DelayedChasePlayerComponent.setDelayed(true);
            }
        }
    }

    @Override
    protected void setAnimationMove() {
        animDie = new AnimationChannel(FXGL.image("Oneal.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 6, 6);
        animWalkRight = new AnimationChannel(FXGL.image("Oneal.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 3, 5);
        animWalkLeft = new AnimationChannel(FXGL.image("Oneal.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 0, 2);
        animStop = new AnimationChannel(FXGL.image("Oneal.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 1, 6);
    }

    @Override
    public void enemyDie() {
        dx = 0;
        dy = 0;
        DelayedChasePlayerComponent.setDelayed(true);
        die = true;
        texture.loopNoOverride(animDie);
    }
}

