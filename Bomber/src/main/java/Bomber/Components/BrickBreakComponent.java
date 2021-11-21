package Bomber.Components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static Bomber.Constants.GameConst.*;

public class BrickBreakComponent extends Component {
    AnimatedTexture texture;
    AnimationChannel animation;

    public BrickBreakComponent() {
        animation = new AnimationChannel(image("brick_break.png"), 3, SIZE, SIZE,
                Duration.seconds(1), 0, 2);
        texture = new AnimatedTexture(animation);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
