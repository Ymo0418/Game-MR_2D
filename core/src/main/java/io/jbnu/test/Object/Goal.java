package io.jbnu.test.Object;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.jbnu.test.Collider;
import io.jbnu.test.Manager.ColliderMgr;
import io.jbnu.test.Manager.RenderMgr;

public class Goal extends GameObject {
    private Collider collider;

    public Goal(float _fStartX, float _fStartY) {
        super("Player", _fStartX, _fStartY, 1f, 1f);
        collider = new Collider(Collider.ColliderType.COL_GOAL
            , new Vector2(0f, 0f)
            , new Vector2(64f, 128f));

        collider.Update(new Vector2(vPosition.x, vPosition.y), vScale);
    }

    @Override
    public void Update(float _fTimeDelta) {
        ColliderMgr.GetInst().RegisterCollider(collider);
    }

    @Override
    public void LateUpdate(float _fTimeDelta) {
        RenderMgr.GetInst().RegisterDebugRender(this);
    }

    @Override
    public void DebugRender(ShapeRenderer _renderer) {
        collider.DebugRender(_renderer);
    }
}
