package io.jbnu.test.Object.Background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.jbnu.test.Manager.RenderMgr;
import io.jbnu.test.Object.AnimSprite;
import io.jbnu.test.Object.GameObject;

public class Plant extends GameObject {
    AnimSprite animSprite;
    public Plant() {
        super("Stage0", 640f, -200.f, 1.5f, 1.5f);

        animSprite = new AnimSprite("Plant/Plant.atlas", AnimSprite.OriginType.ORIGIN_CB, 1);
        animSprite.AddAnimation(0.016f * 9f, "plant", true);
    }

    @Override
    public void LateUpdate(float _fTimeDelta) {
        animSprite.Update(_fTimeDelta);
        animSprite.SyncSprite(vPosition, vScale, false);

        RenderMgr.GetInst().RegisterRenderGroup(RenderMgr.RenderGroup.RG_NONBLEND, this);
        RenderMgr.GetInst().RegisterDebugRender(this);
    }

    @Override
    public void Render(SpriteBatch _batch) {
        animSprite.Draw(_batch);
    }
}
