package io.jbnu.test.Manager;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import io.jbnu.test.Collider;

public class ColliderMgr {
    private static final ColliderMgr instance = new ColliderMgr();
    Map<Collider.ColliderType, ArrayList<Collider>> colliders;

    private ColliderMgr() {
        colliders = new EnumMap<>(Collider.ColliderType.class);
        for(Collider.ColliderType type : Collider.ColliderType.values())
            colliders.put(type, new ArrayList<>());
    }

    public void CheckCollides(Collider.ColliderType type1, Collider.ColliderType type2) {
        for(Collider col1 : colliders.get(type1)) {
            for(Collider col2 : colliders.get(type2)) {
                if(col1 != col2) {
                    Collider.CheckCollision(col1, col2);
                }
            }
        }
    }

    public void RegisterCollider(Collider col) {
        colliders.get(col.eColType).add(col);
    }

    public void ClearColliders() {
        for(Collider.ColliderType type : Collider.ColliderType.values())
            colliders.get(type).clear();
    }

    public static ColliderMgr GetInst() {
        return instance;
    }

    public void Dispose() {
        ClearColliders();
    }
}
