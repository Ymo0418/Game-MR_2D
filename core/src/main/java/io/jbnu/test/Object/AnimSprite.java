package io.jbnu.test.Object;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AnimSprite {
    final float FRAME_DURATION = 0.096f;
    public static enum  OriginType {
        ORIGIN_CB,
        ORIGIN_CC,
        ORIGIN_LT,
        ORIGIN_LB,
        ORIGIN_RB,
        ORIGIN_RT,
        ORIGIN_END
    }

    protected Vector2   vPosition = new Vector2(0f, 0f);
    protected Vector2   vSize = new Vector2(0f, 0f);
    protected Vector2   vPivot = new Vector2(0f, 0f);
    protected boolean   bLoopAnim = true;
    protected int       iAnimIndex = 0;
    protected float     fStateTime = 0f;

    private final int   NO_EXIT_ANIM = -1;
    protected int[]     exitAnims;

    protected TextureAtlas                   textureAtlas;
    protected TextureRegion                  curFrame;
    protected List<Animation<TextureRegion>> animations =  new ArrayList<>();

    public AnimSprite(String _strAtlasPath, OriginType _eOriginType, int _iAnimNum) {
        textureAtlas = new TextureAtlas(_strAtlasPath);
        SetOrigin(_eOriginType);

        exitAnims = new int[_iAnimNum];
        Arrays.fill(exitAnims, NO_EXIT_ANIM);
    }

    public void AddAnimation(float _fDuration, String _strAnimName, boolean bLoop) {
        Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(_strAnimName);

        if(regions.size <= 0)
            throw new IllegalArgumentException("No Anim Regions " + _strAnimName);
        if(animations.contains(regions))
            throw new IllegalArgumentException("Already Contains Anim Regions " + _strAnimName);

        animations.add(new Animation<>(_fDuration, regions, (bLoop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL)));
    }

    public void AddExitAnimation(int _fromAnimIdx, int _toAnimIdx) {
        if(_fromAnimIdx < 0 || _fromAnimIdx > exitAnims.length ||
            _toAnimIdx < 0 || _toAnimIdx > exitAnims.length)
            throw new IllegalArgumentException("Out of Range of Anim Index " + _fromAnimIdx + " to " + _toAnimIdx);

        exitAnims[_fromAnimIdx] = _toAnimIdx;
    }

    public void Update(float _fTimeDelta) {
        fStateTime += _fTimeDelta;

        float fAnimRate = fStateTime / animations.get(iAnimIndex).getAnimationDuration();
        if(fAnimRate >= 1f && exitAnims[iAnimIndex] != NO_EXIT_ANIM) {
            ChangeAnimation(exitAnims[iAnimIndex], true, true);
        }
    }

    public float GetAnimRate() {
        return fStateTime / animations.get(iAnimIndex).getAnimationDuration();
    }

    public boolean IsAnimPlaying(int _iAnimIdx) { return iAnimIndex == _iAnimIdx; }

    public void ChangeAnimation(int _iAnimIdx, boolean _bResetStateTime, boolean _bLoopAnim) {
        if(_iAnimIdx < 0 || _iAnimIdx >= animations.size())
            throw new IllegalArgumentException("Unable Anim Index " + _iAnimIdx);

        iAnimIndex = _iAnimIdx;
        if(_bResetStateTime)
            fStateTime = 0f;

        bLoopAnim = _bLoopAnim;
    }

    public void SyncSprite(Vector2 _vPos, Vector2 _vScale, boolean _bFlipX) {
        curFrame = animations.get(iAnimIndex).getKeyFrame(fStateTime, bLoopAnim);

        if(_bFlipX) {
            vSize.x = -curFrame.getRegionWidth() * _vScale.x;
            vSize.y = curFrame.getRegionHeight() * _vScale.y;
        }
        else {
            vSize.x = curFrame.getRegionWidth() * _vScale.x;
            vSize.y = curFrame.getRegionHeight() * _vScale.y;
        }

        vPosition.x = _vPos.x - vSize.x * vPivot.x;
        vPosition.y = _vPos.y - vSize.y * vPivot.y;
    }

    public void Draw(SpriteBatch _batch) {
        _batch.draw(curFrame, vPosition.x, vPosition.y, vSize.x, vSize.y);
    }

    private void SetOrigin(OriginType _eOriginType) {
        switch (_eOriginType) {
            case ORIGIN_CC:
                vPivot.x = 0.5f;
                vPivot.y = 0.5f;
                break;
            case ORIGIN_CB:
                vPivot.x = 0.5f;
                vPivot.y = 0.f;
                break;
            default:
                break;
        }
    }
}
