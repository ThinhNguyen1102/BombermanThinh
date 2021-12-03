package Bomber.Components;

import Bomber.BombermanType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.onCollisionEnd;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static Bomber.Constants.GameConst.*;

public class BombComponent extends Component {
    private final ArrayList<Entity> listFire = new ArrayList<>();
    private Entity wallBomb;

    private final AnimatedTexture texture;

    public BombComponent() {
        onCollisionEnd(BombermanType.BOMB, BombermanType.PLAYER, (b, p) -> {
            if (entity != null) {
                wallBomb = spawn("wall_bomb", new SpawnData(entity.getX(), entity.getY()));
            }
        });
        AnimationChannel animation = new AnimationChannel(image("bomb_ani.png"), 3, SIZE_BLOCK, SIZE_BLOCK,
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
            listFire.add(spawn("fire", new SpawnData(entity.getX() + SIZE_BLOCK * i, entity.getY())));
            listFire.add(spawn("fire", new SpawnData(entity.getX() - SIZE_BLOCK * i, entity.getY())));
            listFire.add(spawn("fire", new SpawnData(entity.getX(), entity.getY() + SIZE_BLOCK * i)));
            listFire.add(spawn("fire", new SpawnData(entity.getX(), entity.getY() - SIZE_BLOCK * i)));
        }
        listFire.add(spawn("fire", new SpawnData(entity.getX(), entity.getY())));

        FXGL.getGameTimer().runOnceAfter(() -> {
            for (Entity value : listFire) {
                value.removeFromWorld();
            }
        }, Duration.seconds(0.4));
        if (wallBomb != null) wallBomb.removeFromWorld();
        entity.removeFromWorld();
    }
}
