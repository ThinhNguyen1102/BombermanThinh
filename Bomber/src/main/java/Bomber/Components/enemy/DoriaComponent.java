package Bomber.Components.enemy;

import Bomber.BombermanType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;

public class DoriaComponent extends OnealComponent {
    private double timeChangeMove = 0.0;
    public DoriaComponent() {
        super();
        onCollisionBegin(BombermanType.DORIA_E, BombermanType.WALL, (d, w) -> {
            d.getComponent(DoriaComponent.class).turn();
        });
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        timeChangeMove += tpf;
        if (!moveWithAi && timeChangeMove > 3.0) {
            timeChangeMove = 0;
            if (Math.random() > 0.5) {
                if (dy == 0) {
                    dx = 0;
                    dy = getRandom();
                } else {
                    dy = 0;
                    dx = getRandom();
                }
            }
        }
    }


    @Override
    protected void setAnimationMove() {
        animDie = new AnimationChannel(FXGL.image("doria.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 6, 6);
        animWalkRight = new AnimationChannel(FXGL.image("doria.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 3, 5);
        animWalkLeft = new AnimationChannel(FXGL.image("doria.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME), 0, 2);
        animStop = new AnimationChannel(FXGL.image("doria.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 1, 6);
    }
}
