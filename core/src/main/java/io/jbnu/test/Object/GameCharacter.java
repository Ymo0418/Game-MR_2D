package io.jbnu.test.Object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.jbnu.test.Collider;
import io.jbnu.test.Manager.ColliderMgr;
import io.jbnu.test.Manager.InputMgr;
import io.jbnu.test.Manager.ObjectMgr;
import io.jbnu.test.Manager.RenderMgr;
import io.jbnu.test.Manager.ResourceMgr;
import io.jbnu.test.Object.Background.Background;

public class GameCharacter extends GameObject {
    private enum AnimIndex {
        ANIM_PLAYSONG,
        ANIM_ATTACK,
        ANIM_DANCE,
        ANIM_DOORBREAK,
        ANIM_FALL,
        ANIM_FLIP,
        ANIM_IDLE,
        ANIM_IDLE_TO_RUN,
        ANIM_JUMP,
        ANIM_ROLL,
        ANIM_RUN_TO_IDLE,
        ANIM_RUN,
        ANIM_WALLSLIDE,
        ANIM_END
    }

    protected int[] iCurLineIdx = { 0 };
    protected float fSpeed = 2.8f;
    public Vector2 vVelocity;
    public boolean bFalling = false;
    private boolean bJumping = false;
    public boolean bSpriteFlip = false;

    private final Collider collider;
    private final AnimSprite animSprite;
    boolean bGoal = false;
    boolean bRunning = false;   //true(IdleToRun, Run), false(RunToIdle, Idle)
    boolean bTouchWall = false;
    boolean bAttacking = false;
    float fAttackTimer = 0f;

    final float JUMP_ASCEND_MULTI = 13.98f;
    final float JUMP_DESCEND_MULTI = 17.98f;
    final float JUMP_FORCE = 4.8f;

    float fHorizontalAxis = 0f;
    final float START_ACCELERATE = 3f;
    final float STOP_ACCELERATE = -3.75f; //3f * -1.25f;

    public GameCharacter() {
        super("Player", 96f, 96f, 2f, 2f);
        vVelocity = new Vector2(0, 0);
        collider = new Collider(Collider.ColliderType.COL_PLAYER
            , new Vector2(0f, 17f)
            , new Vector2(20f, 35f));
        animSprite = new AnimSprite("Player/Player.atlas", AnimSprite.OriginType.ORIGIN_CB, AnimIndex.ANIM_END.ordinal());
        ReadyAnimation();
        animSprite.ChangeAnimation(AnimIndex.ANIM_PLAYSONG.ordinal(), true, false);
    }

    private void StateUpdate(float _fTimeDelta) {
        HorizontalInput(_fTimeDelta);

        if(bAttacking) {
            fAttackTimer += _fTimeDelta;
            vVelocity.x *= 139 * _fTimeDelta;

            Background background = (Background)(ObjectMgr.GetInst().GetObject("Background", 0));
            float fHeight;

            if(vVelocity.y > 0f) {
                vVelocity.y += -JUMP_ASCEND_MULTI * _fTimeDelta;
                fHeight = background.GetHeight(vPosition.x, vPosition.y, iCurLineIdx);
            }
            else {
                vVelocity.y += -JUMP_DESCEND_MULTI * _fTimeDelta;
                fHeight = background.GetFloorHeight(vPosition.x, vPosition.y, iCurLineIdx);
            }

            if(vPosition.y + vVelocity.y <= fHeight) {
                bJumping = false;
                bFalling = false;
                vPosition.y = fHeight;
                vVelocity.y = 0f;

                if(fAttackTimer > 0.2f) {
                    bAttacking = false;
                    fAttackTimer = 0f;
                    if (bRunning) animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE_TO_RUN.ordinal(), true, false);
                    else          animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE.ordinal(), true, true);
                }
            }
            else if(fAttackTimer > 0.5f) {
                bAttacking = false;
                fAttackTimer = 0f;
            }

        }
        else if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 vDir = InputMgr.GetInst().GetMouseWorldPos();

            animSprite.ChangeAnimation(AnimIndex.ANIM_ATTACK.ordinal(), true, false);

            vDir.sub(vPosition.x, vPosition.y, 0f);
            vDir.x *= 1.25f;
            vDir.nor().scl(!(bJumping || bFalling) ? 4.5f : 3.5f);

            bSpriteFlip = vDir.x <= 0f;
            vVelocity.x = vDir.x;
            vVelocity.y = Math.max(vDir.y, 0f);

            ObjectMgr.GetInst().AddObjectRuntime("Effect", new PlayerAttack(vPosition, new Vector2(vDir.x, vDir.y)));
            ResourceMgr.GetInst().PlaySound("Slash"+(MathUtils.random(2)+1), 0.2f, false);

            bAttacking = true;
            bJumping = true;
            bFalling = true;
        }
        else {
            if(bJumping) {
                if(bTouchWall) {
                    if(animSprite.IsAnimPlaying(AnimIndex.ANIM_WALLSLIDE.ordinal())) {
                        if(InputMgr.GetInst().IsKeyJustPress(Input.Keys.SPACE)) {
                            bJumping = true;
                            bTouchWall = false;
                            bFalling = false;
                            vVelocity.y = JUMP_FORCE * 0.85f;
                            vVelocity.x = bSpriteFlip ? 5.3f : -5.3f;
                            bSpriteFlip = !bSpriteFlip;
                            animSprite.ChangeAnimation(AnimIndex.ANIM_FLIP.ordinal(), true, false);
                        }
                        else {
                            if(bFalling) {
                                vVelocity.y += -JUMP_DESCEND_MULTI * _fTimeDelta / 15f;
                                Background background = (Background)(ObjectMgr.GetInst().GetObject("Background", 0));
                                float fHeight = background.GetHeight(vPosition.x, vPosition.y, iCurLineIdx);
                                if(vPosition.y + vVelocity.y <= fHeight) {
                                    bJumping = false;
                                    bFalling = false;
                                    bSpriteFlip = !bSpriteFlip;
                                    vVelocity.y = fHeight - vPosition.y;
                                    animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE.ordinal(), true, true);
                                }
                            }
                            else {
                                vVelocity.y += -JUMP_ASCEND_MULTI * _fTimeDelta;
                                if(vVelocity.y < 0f) {
                                    bFalling = true;
                                }
                            }
                        }
                    }
                    else {
                        animSprite.ChangeAnimation(AnimIndex.ANIM_WALLSLIDE.ordinal(), false, true);
                    }
                }
                else {
                    if(bFalling) {
                        vVelocity.y += -JUMP_DESCEND_MULTI * _fTimeDelta;

                        if(animSprite.IsAnimPlaying(AnimIndex.ANIM_WALLSLIDE.ordinal())) {
                            animSprite.ChangeAnimation(AnimIndex.ANIM_FALL.ordinal(), true, true);
                            bSpriteFlip = !bSpriteFlip;
                        }

                        Background background = (Background)(ObjectMgr.GetInst().GetObject("Background", 0));
                        float fHeight = background.GetHeight(vPosition.x, vPosition.y, iCurLineIdx);
                        if(vPosition.y + vVelocity.y <= fHeight) {
                            bJumping = false;
                            bFalling = false;
                            vVelocity.y = fHeight - vPosition.y;

                            if(bRunning) animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE_TO_RUN.ordinal(), true, false);
                            else         animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE.ordinal(), true, true);
                        }
                    }
                    else {
                        vVelocity.y += -JUMP_ASCEND_MULTI * _fTimeDelta;
                        if(vVelocity.y < 0f) {
                            bFalling = true;
                            animSprite.ChangeAnimation(AnimIndex.ANIM_FALL.ordinal(), true,true);
                        }

                        Background background = (Background)(ObjectMgr.GetInst().GetObject("Background", 0));
                        float fHeight = background.GetFloorHeight(vPosition.x, vPosition.y, iCurLineIdx);
                        if(vPosition.y + vVelocity.y <= fHeight) {
                            bJumping = false;
                            bFalling = false;
                            vVelocity.y = fHeight - vPosition.y;

                            if(bRunning) animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE_TO_RUN.ordinal(), true, false);
                            else         animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE.ordinal(), true, true);
                        }
                    }
                }
            }
            else {
                if(!bFalling) {
                    Background background = (Background)(ObjectMgr.GetInst().GetObject("Background", 0));
                    float fHeight = background.GetFloorHeight(vPosition.x, vPosition.y, iCurLineIdx);

                    vVelocity.y = fHeight - vPosition.y;

                    if(InputMgr.GetInst().IsKeyJustPress(Input.Keys.SPACE)) {
                        bJumping = true;
                        vVelocity.y = JUMP_FORCE;
                        animSprite.ChangeAnimation(AnimIndex.ANIM_JUMP.ordinal(), true, false);
                    }
                }
            }

            if(bSpriteFlip) {
                float fInputMove = -fSpeed * Interpolation.pow4.apply(fHorizontalAxis);
                vVelocity.x = Math.min(vVelocity.x + _fTimeDelta * 13f, 0f);
                vVelocity.x = Math.min(fInputMove, vVelocity.x);
            }
            else {
                float fInputMove = fSpeed * Interpolation.pow4.apply(fHorizontalAxis);
                vVelocity.x = Math.max(vVelocity.x - _fTimeDelta * 13f, 0f);
                vVelocity.x = Math.max(fInputMove, vVelocity.x);
            }
        }
    }

    private void HorizontalInput(float _fTimeDelta) {
        float fAxisDelta = 0f;

        int iKeyForward;
        int iKeyBackward;
        if(bSpriteFlip) {
            iKeyForward = Input.Keys.A;
            iKeyBackward = Input.Keys.D;
        }
        else {
            iKeyForward = Input.Keys.D;
            iKeyBackward = Input.Keys.A;
        }
        boolean bInputForward = InputMgr.GetInst().IsKeyInput(iKeyForward);
        boolean bInputBackward = InputMgr.GetInst().IsKeyInput(iKeyBackward);

        if(bInputForward == bInputBackward) {
            fAxisDelta = _fTimeDelta * STOP_ACCELERATE;
            if(bRunning) {
                bRunning = false;
                if(!(bFalling || bJumping)) {
                    animSprite.ChangeAnimation(AnimIndex.ANIM_RUN_TO_IDLE.ordinal(), true, false);
                }
            }
        }
        else {
            if(bInputForward) {
                fAxisDelta = _fTimeDelta * START_ACCELERATE;
                if(!bRunning) {
                    bRunning = true;
                    if(!(bFalling||bJumping)) {
                        animSprite.ChangeAnimation(AnimIndex.ANIM_IDLE_TO_RUN.ordinal(), true, false);
                    }
                }
            }
            else {
                fHorizontalAxis = 0f;
                bSpriteFlip = !bSpriteFlip;
                bRunning = false;
            }
        }

        fHorizontalAxis += fAxisDelta;
        fHorizontalAxis = Math.max(0f, Math.min(fHorizontalAxis, 1f));
    }

    @Override
    public void Update(float _fTimeDelta) {
        if(animSprite.IsAnimPlaying(AnimIndex.ANIM_PLAYSONG.ordinal())) {
            if(animSprite.GetAnimRate() > 0.9f && ResourceMgr.GetInst().IsNoBgm()) {
                ResourceMgr.GetInst().PlayBgm("BGM_S1");
            }
        }
        else {
            StateUpdate(_fTimeDelta);
        }

        vMovement.x = vVelocity.x * _fTimeDelta * 128f;
        vMovement.y = vVelocity.y * _fTimeDelta * 128f;

        collider.Update(new Vector2(vMovement.x + vPosition.x, vMovement.y + vPosition.y), vScale);
        ColliderMgr.GetInst().RegisterCollider(collider);
    }

    @Override
    public void LateUpdate(float _fTimeDelta) {
        bTouchWall = (_fTimeDelta == 0f && bTouchWall);
        for(Collider.CollideInfo info : collider.colliderInfos.get(Collider.ColliderType.COL_WALL)) {
            if((bSpriteFlip ? (info.vNormal.x > 0f) : (info.vNormal.x < 0f))) {
                bTouchWall = true;
            }
            vMovement.add(info.vNormal.x * info.fOverlap,
                info.vNormal.y * info.fOverlap);
        }
        for(Collider.CollideInfo info : collider.colliderInfos.get(Collider.ColliderType.COL_GOAL)) {
            bGoal = true;
        }
        for(Collider.CollideInfo info : collider.colliderInfos.get(Collider.ColliderType.COL_MONSTER_BULLET)) {
            bDead = true;
        }
        for(Collider.CollideInfo info : collider.colliderInfos.get(Collider.ColliderType.COL_MONSTER_ATTACK)) {
            if (!bAttacking) {
                bDead = true;
                break;
            }
        }
        for(Collider.CollideInfo info : collider.colliderInfos.get(Collider.ColliderType.COL_OBSTACLE)) {
            vMovement.add(info.vNormal.x * info.fOverlap,
                info.vNormal.y * info.fOverlap);
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
        collider.Update(vPosition, vScale); //LateUpdate에서 적용된 움직임 Update
        collider.DebugRender(_renderer);
    }

    private void ReadyAnimation() {
        float FRAME_DURATION = 0.016f * 6f;
        animSprite.AddAnimation(0.016f * 6f, "playsong", false);
        animSprite.AddAnimation(0.016f * 4f, "attack", true);
        animSprite.AddAnimation(0.016f * 6f, "dance", true);
        animSprite.AddAnimation(FRAME_DURATION, "doorbreak", true);
        animSprite.AddAnimation(FRAME_DURATION, "fall", true);
        animSprite.AddAnimation(0.016f * 3f, "flip", true);
        animSprite.AddAnimation(0.016f * 6f, "idle", true);
        animSprite.AddAnimation(0.016f * 4f, "itor", false);
        animSprite.AddAnimation(FRAME_DURATION, "jump", true);
        animSprite.AddAnimation(FRAME_DURATION, "roll", true);
        animSprite.AddAnimation(0.016f * 6f, "rtoi", false);
        animSprite.AddAnimation(0.016f * 4f, "run", true);
        animSprite.AddAnimation(FRAME_DURATION, "wallslide", false);

        animSprite.AddExitAnimation(AnimIndex.ANIM_PLAYSONG.ordinal(), AnimIndex.ANIM_IDLE.ordinal());
        animSprite.AddExitAnimation(AnimIndex.ANIM_RUN_TO_IDLE.ordinal(), AnimIndex.ANIM_IDLE.ordinal());
        animSprite.AddExitAnimation(AnimIndex.ANIM_IDLE_TO_RUN.ordinal(), AnimIndex.ANIM_RUN.ordinal());
        animSprite.AddExitAnimation(AnimIndex.ANIM_ATTACK.ordinal(), AnimIndex.ANIM_FALL.ordinal());
    }

    public boolean IsGoal() { return bGoal; }
}
