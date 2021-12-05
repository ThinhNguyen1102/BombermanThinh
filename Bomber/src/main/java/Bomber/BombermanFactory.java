package Bomber;

import Bomber.Components.*;
import Bomber.Components.enemy.BalloomComponent;
import Bomber.Components.enemy.DoriaComponent;
import Bomber.Components.enemy.OnealComponent;
import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyDef;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.AnimatedTexture;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static Bomber.Constants.GameConst.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class BombermanFactory implements EntityFactory {
    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(new Rectangle(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, Color.LIGHTGRAY))
                .zIndex(-100)
                .with(new IrremovableComponent())
                .build();
    }

    @Spawns("player")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef().friction(0).density(0.1f));
        BodyDef bd = new BodyDef();
        bd.setFixedRotation(true);
        bd.setType(BodyType.DYNAMIC);
        physics.setBodyDef(bd);

        return FXGL.entityBuilder(data)
                .type(BombermanType.PLAYER)
                .bbox(new HitBox(BoundingShape.circle(24)))
                .atAnchored(new Point2D(24, 24), new Point2D(24, 24))
                .with(physics)
                .with(new PlayerComponent())
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(48, 48, 100))
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .zIndex(5)
                .build();
    }

    @Spawns("balloom_e")
    public Entity newBalloom(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.BALLOOM_E)
                .bbox(new HitBox(BoundingShape.circle(22)))
                .atAnchored(new Point2D(24, 24), new Point2D(24, 24))
                .with(new BalloomComponent())
                .with(new CollidableComponent(true))
                .zIndex(2)
                .build();
    }

    @Spawns("oneal_e")
    public Entity newOneal(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.ONEAL_E)
                .bbox(new HitBox(BoundingShape.circle(22)))
                .with(new CollidableComponent(true))
                .atAnchored(new Point2D(24, 24), new Point2D(24, 24))
                .with(new CellMoveComponent(48, 48, 100))
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new OnealComponent())
                .zIndex(2)
                .build();
    }

    @Spawns("doria_e")
    public Entity newDoria(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.DORIA_E)
                .bbox(new HitBox(BoundingShape.circle(22)))
                .with(new CollidableComponent(true))
                .atAnchored(new Point2D(24, 24), new Point2D(24, 24))
                .with(new CellMoveComponent(48, 48, 100))
                .with(new AStarMoveComponent(new LazyValue<>(() -> geto("grid"))))
                .with(new DoriaComponent())
                .zIndex(2)
                .build();
    }

    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.WALL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),
                        data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("wall_bomb")
    public Entity newWallBomb(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.WALL_BOMB)
                .bbox(new HitBox(new Point2D(0, 0), BoundingShape.box(SIZE_BLOCK, SIZE_BLOCK)))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("brick")
    public Entity newBrick(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.BRICK)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),
                        data.<Integer>get("height"))))
                .view("brick.png")
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("grass")
    public Entity newGrass(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.GRASS)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),
                        data.<Integer>get("height"))))
                .view("grass.png")
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("grass_break")
    public Entity newGrassBreak(SpawnData data) {
        AnimatedTexture view = texture("grass_break_2.png").toAnimatedTexture(3, Duration.seconds(1));
        return FXGL.entityBuilder(data)
                .type(BombermanType.GRASS_BREAK)
                .viewWithBBox(new Rectangle(SIZE_BLOCK / 2 - 3, SIZE_BLOCK / 2 - 3, Color.TRANSPARENT))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .view(view.loop())
                .zIndex(1)
                .build();
    }

    @Spawns("coral")
    public Entity newCoral(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.CORAL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"),
                        data.<Integer>get("height"))))
                .view("coral.png")
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("coral_break")
    public Entity newCoralBreak(SpawnData data) {
        AnimatedTexture view = texture("coral_break_2.png").toAnimatedTexture(3, Duration.seconds(1));
        return FXGL.entityBuilder(data)
                .type(BombermanType.CORAL_BREAK)
                .viewWithBBox(new Rectangle(SIZE_BLOCK - 3, SIZE_BLOCK - 3, Color.TRANSPARENT))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .view(view.loop())
                .zIndex(1)
                .build();
    }

    @Spawns("brick_break")
    public Entity newBrickBreak(SpawnData data) {
        AnimatedTexture view = texture("brick_break.png").toAnimatedTexture(3, Duration.seconds(1));
        return FXGL.entityBuilder(data)
                .type(BombermanType.BRICK_BREAK)
                .view(view.loop())
                .viewWithBBox(new Rectangle(SIZE_BLOCK / 2 - 3, SIZE_BLOCK / 2 - 3, Color.TRANSPARENT))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .zIndex(1)
                .build();
    }

    @Spawns("bomb")
    public Entity newBomb(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.BOMB)
                .with(new BombComponent())
                .bbox(new HitBox(new Point2D(2, 2), BoundingShape.circle(22)))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("fire")
    public Entity newFire(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.FIRE)
                .with(new FlameComponent())
                // cần đổi
                .viewWithBBox(new Rectangle(SIZE_BLOCK / 2 - 3, SIZE_BLOCK / 2 - 3, Color.TRANSPARENT))
                .atAnchored(new Point2D(0, 0), new Point2D(data.getX(), data.getY()))
                .with(new CollidableComponent(true))
                .zIndex(1)
                .build();
    }

    @Spawns("speedItem")
    public Entity newItem3(SpawnData data) {
        return entityBuilder(data)
                .type(BombermanType.SPEED_ITEM)
                .view("powerup_speed.png")
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("bombsItem")
    public Entity newBombItem(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.BOMB_ITEM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .view("powerup_bombs.png")
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("flameItem")
    public Entity newFlameItem(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.FLAME_ITEM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .view("powerup_flames.png")
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("flamePassItem")
    public Entity newFlamePassItem(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.FLAME_PASS_ITEM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .view("powerup_flamepass.png")
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("portal")
    public Entity newPortal(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BombermanType.PORTAL)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .view("portal.png")
                .with(new PhysicsComponent())
                .with(new CollidableComponent(true))
                .build();
    }
}
