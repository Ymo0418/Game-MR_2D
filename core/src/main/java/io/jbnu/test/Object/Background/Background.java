package io.jbnu.test.Object.Background;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import io.jbnu.test.Collider;
import io.jbnu.test.Line;
import io.jbnu.test.Manager.ColliderMgr;
import io.jbnu.test.Manager.RenderMgr;
import io.jbnu.test.Object.GameObject;

public abstract class Background extends GameObject {
    protected final ArrayList<Collider> colliders;
    protected List<Line> lines = new ArrayList<>();
    protected Vector2 vCamLimit = new Vector2();

    public Background(String _spriteTag, float _fStartX, float _fStartY, float _fScaleX, float _fScaleY) {
        super(_spriteTag, _fStartX, _fStartY, _fScaleX, _fScaleY);

        colliders = new ArrayList<>();
        ReadyCollider();
        ReadyLines();
        ReadyLineNeighbor();
    }

    @Override
    public void Update(float _fTimeDelta) {
        for(Collider col : colliders) {
            ColliderMgr.GetInst().RegisterCollider(col);
        }
    }

    @Override
    public void LateUpdate(float _fTimeDelta) {
        RenderMgr.GetInst().RegisterRenderGroup(RenderMgr.RenderGroup.RG_BG, this);
        RenderMgr.GetInst().RegisterDebugRender(this);
    }

    @Override
    public void DebugRender(ShapeRenderer _renderer) {
        for(Collider col : colliders)
            col.DebugRender(_renderer);

        for(Line line : lines)
            line.DebugRender(_renderer);
    }

    abstract protected void ReadyCollider();

    abstract protected void ReadyLines();

    final private void ReadyLineNeighbor() {
        for(int i = 0; i < lines.size() - 1; ++i) {
            for(int k = i + 1; k < lines.size(); ++k) {
                lines.get(i).TrySetNeighbor(lines.get(k));
            }
        }
    }

    public float GetHeight(float _fX, float _fY, int[] _idx) {
        int iBestLineIdx = -1;
        float fBestLineHeight = Float.NEGATIVE_INFINITY;

        for(int i = 0; i < lines.size(); ++i) {
            if(lines.get(i).WithinLine(_fX)) {
                float fHeight = lines.get(i).GetHeight(_fX);

                if(fHeight <= _fY && fBestLineHeight <= fHeight) {
                    iBestLineIdx = i;
                    fBestLineHeight = fHeight;
                }
            }
        }

        if(iBestLineIdx == -1)
            return 0f;
        else {
            _idx[0] = iBestLineIdx;
            return fBestLineHeight;
        }
    }

    public float GetFloorHeight(float _fX, float _fY, int[] _idx) {
        Line targetLine = lines.get(_idx[0]);

        while (targetLine != null && !targetLine.WithinLine(_fX)) {
            targetLine = targetLine.IsOnLeft(_fX) ? targetLine.neighborL : targetLine.neighborR;
        }

        if(targetLine == null) {
            return GetHeight(_fX, _fY, _idx);
        }
        else {
            _idx[0] = targetLine.idx;
            return targetLine.GetHeight(_fX);
        }
    }

    public float CalCamLimit(float _fCamX) {
        return Math.max(vCamLimit.x, Math.min(vCamLimit.y, _fCamX));
    }
}
