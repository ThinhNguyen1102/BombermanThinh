package Bomber.Components.ai;

import Bomber.BombermanType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;

import java.util.List;

@Required(AStarMoveComponent.class)
public class DelayedChasePlayerComponent extends Component {
    private AStarMoveComponent astar;
    private static boolean isDelayed = false;

    public static void setDelayed(boolean delayed) {
        isDelayed = delayed;
    }

    @Override
    public void onUpdate(double tpf) {
        BoundingBoxComponent bbox = entity.getBoundingBoxComponent();
        List<Entity> list = FXGL.getGameWorld().getEntitiesInRange(bbox.range(60, 60));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isType(BombermanType.PLAYER)) {
                isDelayed = false;
                break;
            }

            if (i == list.size() - 1) {
                isDelayed = true;
            }
        }

        if (!isDelayed) {
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
