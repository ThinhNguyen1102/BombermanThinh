package Bomber.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;

public class BombComponent extends Component {
    private ArrayList<Entity> listFire = new ArrayList<>();

    private AnimatedTexture texture;
    private AnimationChannel animation;

    public BombComponent() {
        animation = new AnimationChannel(image("bomb_ani.png"), 3, 64, 64,
                Duration.seconds(0.5), 0, 2);
        texture = new AnimatedTexture(animation);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    public void explode() {
        for (int i = 1; i <= 1; i++) {
            listFire.add(spawn("fire", new SpawnData(entity.getX() + 64 * i, entity.getY())));
            listFire.add(spawn("fire", new SpawnData(entity.getX() - 64 * i, entity.getY())));
            listFire.add(spawn("fire", new SpawnData(entity.getX(), entity.getY() + 64 * i)));
            listFire.add(spawn("fire", new SpawnData(entity.getX(), entity.getY() - 64 * i)));
        }
        listFire.add(spawn("fire", new SpawnData(entity.getX(), entity.getY())));

        FXGL.getGameTimer().runOnceAfter(() -> {
            for (int i = 0; i < listFire.size(); i++) {
                listFire.get(i).removeFromWorld();
            }
        }, Duration.seconds(0.4));
        entity.removeFromWorld();
    }
}
