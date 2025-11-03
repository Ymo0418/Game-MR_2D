package io.jbnu.test.Object.Background;

import com.badlogic.gdx.math.Vector2;

import io.jbnu.test.Collider;
import io.jbnu.test.Line;

public class Background_Club extends Background {
    public Background_Club() {
        super("Stage2", -32f, 0f, 2f, 2f);

        vCamLimit.x = 32*20f;
        vCamLimit.y = 32*60f;
    }

    @Override
    protected void ReadyCollider() {
        Collider col;

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            32*-1, 32 * 8, 32 * 0, 32 * 3);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            32*-1, 32 * 9, 32 * 10, 32 * 8);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            32 * 8, 32 * 25, 32 * 9, 32 * 14);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            32 * 78, 32 * 25, 32 * 79, 32 * 14);
        colliders.add(col);
    }

    @Override
    protected void ReadyLines() {
        int idx = 0;

        lines.add(new Line(new Vector2(32 * 0, 32 * 3),
        /*0*/              new Vector2(32 * 9, 32 * 3), idx++));

        lines.add(new Line(new Vector2(32 * 9, 32 * 3),
        /*1*/              new Vector2(32 * 10, 32 * 2), idx++));

        lines.add(new Line(new Vector2(32 * 10, 32 * 2),
        /*2*/              new Vector2(32 * 62, 32 * 2), idx++));

        lines.add(new Line(new Vector2(32 * 62, 32 * 2),
        /*3*/              new Vector2(32 * 74, 32 * 14), idx++));

        lines.add(new Line(new Vector2(32 * 74, 32 * 14),
        /*4*/              new Vector2(32 * 78, 32 * 14), idx++));

        lines.add(new Line(new Vector2(32 * 9, 32 * 14),
        /*5*/              new Vector2(32 * 74-0.1F, 32 * 14), idx++));
    }
}
