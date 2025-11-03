package io.jbnu.test.Object.Background;

import com.badlogic.gdx.math.Vector2;

import io.jbnu.test.Collider;
import io.jbnu.test.Line;

public class Background_Sewer extends Background {
    public Background_Sewer() {
        super("Stage1", 0f, 0f, 1f, 1f);

        vCamLimit.x = 32*20f - 1;
        vCamLimit.y = 32*39f;
    }

    @Override
    protected void ReadyCollider() {
        Collider col;

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            32*-1, 32*9, 32*0, 32*3);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            0, 32*10, 32*12 - 1, 32*9);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_WALL,
            32*11, 32*23, 32*12, 32*10);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_WALL,
            32*16, 32*16, 32*17, 32*3);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            32*11, 32*24, 32*24, 32*23);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            32*24, 32*24, 32*25, 32*21);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            32*25, 32*22, 32*26-1, 32*21);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_WALL,
            32*25, 32*32, 32*26, 32*22);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_OBSTACLE,
            32*25, 32*33, 32*59, 32*32);
        colliders.add(col);

        col = new Collider(Collider.ColliderType.COL_WALL,
            32*31, 32*25, 32*32, 32*16);
        colliders.add(col);
    }

    @Override
    protected void ReadyLines() {
        int idx = 0;
        lines.add(new Line(new Vector2(0, 32*3),
        /*0*/              new Vector2(32*16, 32*3), idx++));

        lines.add(new Line(new Vector2(32*12, 32*16),
        /*1*/              new Vector2(32*31, 32*16), idx++));

        lines.add(new Line(new Vector2(32*26, 32*25),
        /*2*/              new Vector2(32*70, 32*25), idx++));
    }
}
