package io.jbnu.test.Level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.jbnu.test.Collider;
import io.jbnu.test.Manager.CameraMgr;
import io.jbnu.test.Manager.ColliderMgr;
import io.jbnu.test.Manager.ObjectMgr;
import io.jbnu.test.Manager.RenderMgr;
import io.jbnu.test.Object.Background.Background;
import io.jbnu.test.Object.Background.Background_Club;
import io.jbnu.test.Object.Background.Background_Sewer;
import io.jbnu.test.Object.GameCharacter;
import io.jbnu.test.Object.GameObject;

public class Level {
    public Level() {
    }

    public void Update(float _fTimeDelta) {
        ObjectMgr.GetInst().Update(_fTimeDelta);
        CameraMgr.GetInst().Update(_fTimeDelta);
    }

    public void LateUpdate(float _fTimeDelta) {
        ColliderMgr.GetInst().CheckCollides(Collider.ColliderType.COL_PLAYER, Collider.ColliderType.COL_OBSTACLE);
        ColliderMgr.GetInst().CheckCollides(Collider.ColliderType.COL_PLAYER, Collider.ColliderType.COL_WALL);
        ColliderMgr.GetInst().CheckCollides(Collider.ColliderType.COL_PLAYER, Collider.ColliderType.COL_DUMMY);
        ColliderMgr.GetInst().CheckCollides(Collider.ColliderType.COL_PLAYER, Collider.ColliderType.COL_GOAL);
        ColliderMgr.GetInst().CheckCollides(Collider.ColliderType.COL_PLAYER, Collider.ColliderType.COL_MONSTER_ATTACK);
        ColliderMgr.GetInst().CheckCollides(Collider.ColliderType.COL_PLAYER, Collider.ColliderType.COL_MONSTER_BULLET);
        ColliderMgr.GetInst().CheckCollides(Collider.ColliderType.COL_MONSTER, Collider.ColliderType.COL_PLAYER_ATTACK);

        ObjectMgr.GetInst().LateUpdate(_fTimeDelta);
        ColliderMgr.GetInst().ClearColliders();
    }

    public void Dispose() {
        RenderMgr.GetInst().Dispose();
        ColliderMgr.GetInst().Dispose();
        CameraMgr.GetInst().Dispose();
        ObjectMgr.GetInst().Dispose();
    }
}
