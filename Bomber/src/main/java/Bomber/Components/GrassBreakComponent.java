package Bomber.Components;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.image;
import static Bomber.Constants.GameConst.*;

public class GrassBreakComponent extends Component {
    AnimatedTexture texture;
    AnimationChannel animation;

    public GrassBreakComponent() {
        animation = new AnimationChannel(image("grass_break_2.png"), 3, SIZE, SIZE,
                Duration.seconds(1), 0, 2);
        texture = new AnimatedTexture(animation);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }
}
