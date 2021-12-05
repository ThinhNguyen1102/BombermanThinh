package Bomber.Components.ai;

import Bomber.BombermanType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;


@Required(AStarMoveComponent.class)
public class DelayedChasePlayerComponent extends Component {
    private AStarMoveComponent astar;
    private static boolean delayed = true;

    public static void setDelayed(boolean stage) {
        delayed = stage;
    }

    public static boolean isDelayed() {
        return delayed;
    }

    @Override
    public void onUpdate(double tpf) {
        if (!delayed) {
            move();
        }
    }

    private void move() {
        var player = FXGL.getGameWorld().getSingleton(BombermanType.PLAYER);

        int x = player.call("getCellX");
        int y = player.call("getCellY");

        astar.moveToCell(x, y);
    }
}
