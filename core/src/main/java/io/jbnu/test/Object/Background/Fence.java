package io.jbnu.test.Object.Background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import io.jbnu.test.Manager.RenderMgr;
import io.jbnu.test.Object.AnimSprite;
import io.jbnu.test.Object.GameCharacter;
import io.jbnu.test.Object.GameObject;

public class Fence extends GameObject {
    AnimSprite animSprite;
    public Fence(String _spriteTag, float _fStartX, float _fStartY, float _fScaleX, float _fScaleY) {
        super(_spriteTag, _fStartX, _fStartY, _fScaleX, _fScaleY);

        animSprite = new AnimSprite("Plant/Plant.atlas", AnimSprite.OriginType.ORIGIN_CB, 1);
        animSprite.AddAnimation(0.016f * 6f, "plant", true);
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
        super.Render(_batch);
        animSprite.Draw(_batch);
    }
}
