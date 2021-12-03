package Bomber.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;


public class OrealComponent extends Component {
    private static final double ANIM_TIME_CORAL = 0.5;
    private static final int SIZE_FLAME = 45;


    private final AnimatedTexture texture;
    private final AnimationChannel animWalkRight;
    private final AnimationChannel animWalkLeft;
    private final AnimationChannel animDie;

    private double lastX = 0;
    private double lastY = 0;

    public OrealComponent() {
        animDie = new AnimationChannel(FXGL.image("Oneal.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME_CORAL), 6, 6);
        animWalkRight = new AnimationChannel(FXGL.image("Oneal.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME_CORAL), 3, 5);
        animWalkLeft = new AnimationChannel(FXGL.image("Oneal.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(ANIM_TIME_CORAL), 0, 2);
        AnimationChannel animStop = new AnimationChannel(FXGL.image("Oneal.png"), 7, SIZE_FLAME, SIZE_FLAME,
                Duration.seconds(1), 1, 6);

        texture = new AnimatedTexture(animStop);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        double dx = entity.getX() - lastX;
        double dy = entity.getY() - lastY;

        lastX = entity.getX();
        lastY = entity.getY();

        if (dx == 0 && dy == 0) {
            // didn't move
            return;
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            // move was horizontal
            if (dx > 0) {
                texture.loopNoOverride(animWalkRight);
            } else {
                texture.loopNoOverride(animWalkLeft);
            }
        } else {
            // move was vertical
            if (dy > 0) {
                texture.loopNoOverride(animWalkLeft);
            } else {
                texture.loopNoOverride(animWalkRight);
            }
        }
    }

    public void OnealDie() {
        texture.loopNoOverride(animDie);
    }
}
