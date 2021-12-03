package Bomber.Components.ai;

import Bomber.BombermanType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;

@Required(AStarMoveComponent.class)
public class DelayedChasePlayerComponent extends Component {
    private AStarMoveComponent astar;
    private boolean isDelayed = false;

    public void withDelay() {
        isDelayed = true;
    }

    @Override
    public void onUpdate(double tpf) {
        if (!isDelayed) {
            move();
        }
        else {

            // if delayed, only move when reached destination
            if (astar.isAtDestination()) {
                move();
            }
        }
    }

    private void move() {
        var player = FXGL.getGameWorld().getSingleton(BombermanType.PLAYER);

        int x = player.call("getCellX");
        int y = player.call("getCellY");

        astar.moveToCell(x, y);
    }
}
