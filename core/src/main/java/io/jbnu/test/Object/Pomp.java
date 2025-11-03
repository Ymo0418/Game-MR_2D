package io.jbnu.test.Object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.jbnu.test.Collider;
import io.jbnu.test.Manager.ColliderMgr;
import io.jbnu.test.Manager.RenderMgr;

public class Pomp extends GameObject {
    private enum AnimIndex {
        ANIM_IDLE,
        ANIM_ATTACK,
        ANIM_HURT,
        ANIM_KNOCKDOWN,
        ANIM_RUN,
        ANIM_WALK,
        ANIM_END
    }
    public boolean bSpriteFlip = true;
    private final Collider hitBox;
    private final Collider attackBox;
    private final Collider scanCollider;

    private final AnimSprite animSprite;

    boolean bIdling = true;
    float fIdleTimer = 3f;
    final float fIdleTime = 3f;
    float fAttackTimer = 3f;
    final float fAttackTime = 3f;

    boolean bRunning = false;
    boolean bAttackTrigger = false;
    boolean bHit = false;

    float fMoveFrom;
    float fMoveRange;

    public Pomp(float _fStartX, float _fStartY, float _fMoveFrom, float _fMoveRange, boolean _bRunning) {
        super("Player", _fStartX, _fStartY, 2f, 2f);
        animSprite = new AnimSprite("Pomp/Pomp.atlas", AnimSprite.OriginType.ORIGIN_CB, AnimIndex.ANIM_END.ordinal());
        ReadyAnimation();

        if(_fMoveRange < 0f) {
            fMoveFrom = _fMoveFrom + _fMoveRange;
            fMoveRange = -_fMoveRange;
            bSpriteFlip = !bSpriteFlip;
        }
        else {
            fMoveFrom = _fMoveFrom;
            fMoveRange = _fMoveRange;
        }

        bRunning = _bRunning;

        hitBox = new Collider(Collider.ColliderType.COL_MONSTER
            , new Vector2(0f, 17f)
            , new Vector2(20f, 35f));

        attackBox = new Collider(Collider.ColliderType.COL_MONSTER_ATTACK
            , new Vector2(0f, 25f)
            , new Vector2(48f, 10f));

        scanCollider = new Collider(Collider.ColliderType.COL_DUMMY
            , new Vector2(0f, 25f)
            , new Vector2(65f, 10f));
    }

    @Override
    public void Update(float _fTimeDelta) {
        fAttackTimer -= _fTimeDelta;

        if(bAttackTrigger) {
            bAttackTrigger = false;
            fAttackTimer = fAttackTime;
            animSprite.ChangeAnimation(AnimIndex.ANIM_ATTACK.ordinal(), true, false);
        }
        else if (animSprite.IsAnimPlaying(AnimIndex.ANIM_ATTACK.ordinal())) {
            hitBox.Update(new Vector2(vMovement.x + vPosition.x, vMovement.y + vPosition.y), vScale);
            ColliderMgr.GetInst().RegisterCollider(hitBox);

            if (animSprite.GetAnimRate() > 0.6f) {
                if (bHit) {
                    bHit = false;
                    animSprite.ChangeAnimation(AnimIndex.ANIM_KNOCKDOWN.ordinal(), true, false);
                    fAttackTimer = fAttackTime * 2f;
                } else {
                    attackBox.Update(new Vector2(vMovement.x + vPosition.x, vMovement.y + vPosition.y), vScale);
                    ColliderMgr.GetInst().RegisterCollider(attackBox);
                }
            }
            else if (bHit) {
                bHit = false;
                animSprite.ChangeAnimation(AnimIndex.ANIM_HURT.ordinal(), true, false);
            }
        }
        else {
            hitBox.Update(new Vector2(vMovement.x + vPosition.x, vMovement.y + vPosition.y), vScale);
            ColliderMgr.GetInst().RegisterCollider(hitBox);

            if(animSprite.IsAnimPlaying(AnimIndex.ANIM_KNOCKDOWN.ordinal())) {
                if(bHit) {
                    bHit = false;
                    animSprite.ChangeAnimation(AnimIndex.ANIM_HURT.ordinal(), true, false);
                }
            }
            if (!animSprite.IsAnimPlaying(AnimIndex.ANIM_HURT.ordinal())) {
                if(bHit) {
                    bHit = false;
                    animSprite.ChangeAnimation(AnimIndex.ANIM_HURT.ordinal(), true, false);
                }
                else if(bIdling) {
                    fIdleTimer -= _fTimeDelta;
                    if(fIdleTimer <= 0f) {
                        bIdling = false;
                        bSpriteFlip = !bSpriteFlip;
                        animSprite.ChangeAnimation(bRunning ? AnimIndex.ANIM_RUN.ordinal() :  AnimIndex.ANIM_WALK.ordinal(), true, true);
                    }
                }
                else {
                    if(bSpriteFlip) {
                        if(vPosition.x <= fMoveFrom) {
                            vPosition.x = fMoveFrom;
                            bIdling = true;
                            fIdleTimer = fIdleTime;
                            animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE.ordinal(), true, true);
                        }
                        else {
                            animSprite.ChangeAnimation(bRunning ? AnimIndex.ANIM_RUN.ordinal() :  AnimIndex.ANIM_WALK.ordinal(), false, true);
                            vMovement.x = 85f * -_fTimeDelta * (bRunning ? 3.6f : 1f);
                        }
                    }
                    else {
                        if(fMoveFrom + fMoveRange <= vPosition.x) {
                            vPosition.x = fMoveFrom + fMoveRange;
                            bIdling = true;
                            fIdleTimer = fIdleTime;
                            animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE.ordinal(), true, true);
                        }
                        else {
                            animSprite.ChangeAnimation(bRunning ? AnimIndex.ANIM_RUN.ordinal() :  AnimIndex.ANIM_WALK.ordinal(), false, true);
                            vMovement.x = 85f * _fTimeDelta * (bRunning ? 3.6f : 1f);
                        }
                    }
                }

                scanCollider.Update(new Vector2(vMovement.x + vPosition.x, vMovement.y + vPosition.y), vScale);
                ColliderMgr.GetInst().RegisterCollider(scanCollider);
            }
        }
    }

    @Override
    public void LateUpdate(float _fTimeDelta) {
        for(Collider.CollideInfo info : hitBox.colliderInfos.get(Collider.ColliderType.COL_PLAYER_ATTACK)) {
            bHit = true;
            break;
        }

        if(fAttackTimer <= 0f) {
            for(Collider.CollideInfo info : scanCollider.colliderInfos.get(Collider.ColliderType.COL_PLAYER)) {
                bSpriteFlip = info.vNormal.x >= 0f;
                bAttackTrigger = true;
                break;
            }
        }

        vPosition.add(vMovement);
        vMovement.setZero();

        animSprite.Update(_fTimeDelta);
        animSprite.SyncSprite(vPosition, vScale, bSpriteFlip);

        RenderMgr.GetInst().RegisterRenderGroup(RenderMgr.RenderGroup.RG_NONBLEND, this);
        RenderMgr.GetInst().RegisterDebugRender(this);
    }

    @Override
    public void Render(SpriteBatch _batch) {
        animSprite.Draw(_batch);
    }

    @Override
    public void DebugRender(ShapeRenderer _renderer) {
        hitBox.Update(vPosition, vScale);
        scanCollider.Update(vPosition, vScale);
        hitBox.DebugRender(_renderer);
        scanCollider.DebugRender(_renderer);

        if(animSprite.IsAnimPlaying(AnimIndex.ANIM_ATTACK.ordinal())) {
            if(animSprite.GetAnimRate() > 0.6f) {
                attackBox.Update(vPosition, vScale);
                attackBox.DebugRender(_renderer);
            }
        }
    }

    private void ReadyAnimation() {
        animSprite.AddAnimation(0.016f * 7f, "idle", true);
        animSprite.AddAnimation(0.016f * 5f, "attack", false);
        animSprite.AddAnimation(0.016f * 6f, "hurt", false);
        animSprite.AddAnimation(0.016f * 6f, "knockdown", false);
        animSprite.AddAnimation(0.016f * 5f, "run", true);
        animSprite.AddAnimation(0.016f * 7f, "walk", true);

        animSprite.AddExitAnimation(AnimIndex.ANIM_ATTACK.ordinal(), AnimIndex.ANIM_IDLE.ordinal());
        animSprite.AddExitAnimation(AnimIndex.ANIM_KNOCKDOWN.ordinal(), AnimIndex.ANIM_IDLE.ordinal());
    }

}
