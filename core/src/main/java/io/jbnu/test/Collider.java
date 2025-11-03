package io.jbnu.test;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Collider {
    public static class CollideInfo {
        public Vector2 vNormal = new Vector2();
        public float fOverlap = 1f;
        public CollideInfo() {}
        public CollideInfo(float fOverlap) { this.fOverlap = fOverlap; }
    }
    public enum ColliderType {
        COL_PLAYER,
        COL_OBSTACLE,
        COL_PLAYER_ATTACK,
        COL_MONSTER,
        COL_MONSTER_ATTACK,
        COL_MONSTER_BULLET,
        COL_WALL,
        COL_GOAL,
        COL_DUMMY,
    }

    public ColliderType eColType;
    private Rectangle bounds;
    private Vector2 vOffset;
    private Vector2 vExtend;    //지름
    public Map<ColliderType, List<CollideInfo>> colliderInfos;

    protected Collider(ColliderType _eColType) {
        bounds = new Rectangle();
        eColType = _eColType;

        colliderInfos = new EnumMap<>(ColliderType.class);
        for(ColliderType type : ColliderType.values())
            colliderInfos.put(type, new ArrayList<>());
    }
    public Collider(ColliderType _eColType, Vector2 _vOffset, Vector2 _vExtend) {
        this(_eColType);
        vOffset = _vOffset;
        vExtend = _vExtend;
    }
    public Collider(ColliderType _eColType, float _fL, float _fT, float _fR, float _fB) {
        this(_eColType);
        vOffset = new Vector2((_fR + _fL) / 2f, (_fT + _fB) / 2f);
        vExtend = new Vector2((_fR - _fL), (_fT - _fB));
        SetBound(vOffset, vExtend);
    }

    public void Update(Vector2 vParentPos, Vector2 vParentScale)
    {
        for(ColliderType type : ColliderType.values())
            colliderInfos.get(type).clear();

        Vector2 vWorldCenter = new Vector2(vParentPos);
        vWorldCenter.add(vOffset.x * vParentScale.x, vOffset.y * vParentScale.y);

        Vector2 vWorldSize = new Vector2(vExtend.x * vParentScale.x, vExtend.y * vParentScale.y);

        bounds.setSize(vWorldSize.x, vWorldSize.y);
        bounds.setCenter(vWorldCenter);
    }

    public void SetBound(Vector2 _vCenter, Vector2 _vSize) {
        bounds.setSize(_vSize.x, _vSize.y);
        bounds.setCenter(_vCenter);
    }

    public void DebugRender(ShapeRenderer renderer) {
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public static void CheckCollision(Collider a, Collider b)
    {
        Rectangle ra = a.bounds;
        Rectangle rb = b.bounds;

        if(ra.x + ra.width < rb.x || ra.x > rb.x + rb.width)        { return; }
        else if(ra.y + ra.height < rb.y || ra.y > rb.y + rb.height) { return; }

        Vector2 vCenterA = new Vector2(); ra.getCenter(vCenterA);
        Vector2 vCenterB = new Vector2(); rb.getCenter(vCenterB);
        Vector2 vDistance = new Vector2(vCenterB.x - vCenterA.x, vCenterB.y - vCenterA.y); //A to B

        float fOverlapX = (ra.width + rb.width) / 2 - Math.abs(vDistance.x);
        float fOverlapY = (ra.height + rb.height) / 2 - Math.abs(vDistance.y);

        CollideInfo infoA = new CollideInfo();
        CollideInfo infoB = new CollideInfo();

        CollideInfo best = null;
        float minOverlap = Float.MAX_VALUE;

        if(fOverlapX < fOverlapY) {
            infoA.fOverlap = fOverlapX;
            infoB.fOverlap = fOverlapX;

            if(vDistance.x < 0) {
                infoA.vNormal = new Vector2(1, 0);
                infoB.vNormal = new Vector2(-1, 0);
            }
            else {
                infoA.vNormal = new Vector2(-1, 0);
                infoB.vNormal = new Vector2(1, 0);
            }
        }
        else {
            infoA.fOverlap = fOverlapY;
            infoB.fOverlap = fOverlapY;
            if(vDistance.y < 0) {
                infoA.vNormal = new Vector2(0, 1);
                infoB.vNormal = new Vector2(0, -1);
            }
            else {
                infoA.vNormal = new Vector2(0, -1);
                infoB.vNormal = new Vector2(0, 1);
            }
        }

        a.colliderInfos.get(b.eColType).add(infoA);
        b.colliderInfos.get(a.eColType).add(infoB);
    }
}
