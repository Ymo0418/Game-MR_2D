package io.jbnu.test.Object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.jbnu.test.Collider;
import io.jbnu.test.Manager.ColliderMgr;
import io.jbnu.test.Manager.RenderMgr;

public class PlayerAttack extends GameObject{

    private Collider collider;
    private AnimSprite animSprite;
    boolean bSpriteFlip;
    boolean bUsed = false;

    Vector2 vPlayerPos;

    public PlayerAttack(Vector2 _vPosition, Vector2 _vDir) {
        super("Player", 0f, 0f, 1.5f, 1.5f);

        vPlayerPos = _vPosition;
        animSprite = new AnimSprite("Slash/Slash.atlas", AnimSprite.OriginType.ORIGIN_CC, 1);
        animSprite.AddAnimation(0.016f * 4f, "slash", false);
        bSpriteFlip = _vDir.x < 0f;

        collider = new Collider(Collider.ColliderType.COL_PLAYER_ATTACK
            , new Vector2(0f, 0f)
            , new Vector2(75f, 35f));
    }

    @Override
    public void Update(float _fTimeDelta) {
        vPosition.x = vPlayerPos.x;
        vPosition.y = vPlayerPos.y + 44f;

        if(!bUsed) {
            collider.Update(new Vector2(vPosition.x, vPosition.y), vScale);
            ColliderMgr.GetInst().RegisterCollider(collider);
        }
    }

    @Override
    public void LateUpdate(float _fTimeDelta) {

        if(!bUsed) {
            for(Collider.CollideInfo info : collider.colliderInfos.get(Collider.ColliderType.COL_MONSTER_BULLET)) {
                bUsed = true;
                break;
            }

            if(!bUsed) {
                for (Collider.CollideInfo info : collider.colliderInfos.get(Collider.ColliderType.COL_MONSTER)) {
                    bUsed = true;
                    break;
                }
            }
        }

        animSprite.Update(_fTimeDelta);
        animSprite.SyncSprite(vPosition, vScale, bSpriteFlip);
        if(animSprite.GetAnimRate() > 0.985f)
            bDead = true;

        RenderMgr.GetInst().RegisterRenderGroup(RenderMgr.RenderGroup.RG_NONBLEND, this);
        RenderMgr.GetInst().RegisterDebugRender(this);

    }

    @Override
    public void Render(SpriteBatch _batch) {
        animSprite.Draw(_batch);
    }

    @Override
    public void DebugRender(ShapeRenderer _renderer) {
        if(!bUsed) {
            collider.Update(vPosition, vScale); //LateUpdate에서 적용된 움직임 Update
            collider.DebugRender(_renderer);
        }
    }
}
