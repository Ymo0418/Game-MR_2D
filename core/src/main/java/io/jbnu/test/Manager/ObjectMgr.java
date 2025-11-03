package io.jbnu.test.Manager;

import com.badlogic.gdx.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.jbnu.test.Object.GameObject;

public class ObjectMgr {
    private static ObjectMgr instance  = new ObjectMgr();
    private Map<String, ArrayList<GameObject>> layers = new HashMap<>();

    private static class Reserve {
        String str;
        GameObject obj;
        Reserve(String _str, GameObject _obj) {
            str =_str; obj = _obj;
        }
    }
    private ArrayList<Reserve> reserves = new ArrayList<>();

    private ObjectMgr() {
    }

    private ArrayList<GameObject> FindLayer(String _strLayerTag) {
        if(layers.containsKey(_strLayerTag)) {
            return layers.get(_strLayerTag);
        }

        return null;
    }

    public void Update(float _fTimeDelta) {
        for(Reserve reserve : reserves) {
            AddObject(reserve.str, reserve.obj);
        }
        reserves.clear();

        for(ArrayList<GameObject> layer : layers.values()) {
            for(Iterator<GameObject> it = layer.iterator(); it.hasNext();) {
                GameObject obj = it.next();
                if(obj.IsDead())
                    it.remove();
                else
                    obj.Update(_fTimeDelta);
            }
        }
    }

    public void LateUpdate(float _fTimeDelta) {
        for(ArrayList<GameObject> layer : layers.values()) {
            for(GameObject obj : layer) {
                obj.LateUpdate(_fTimeDelta);
            }
        }
    }

    public void AddObject(String _strLayerTag, GameObject _obj) {
        ArrayList<GameObject> targetLayer = FindLayer(_strLayerTag);

        if(targetLayer == null) {
            layers.put(_strLayerTag, new ArrayList<>());
            layers.get(_strLayerTag).add(_obj);
        }
        else {
            targetLayer.add(_obj);
        }
    }

    public void AddObjectRuntime(String _strLayerTag, GameObject _obj) {
        reserves.add(new Reserve(_strLayerTag, _obj));
    }

    public GameObject GetObject(String _strLayerTag, int _idx) {
        ArrayList<GameObject> targetLayer = FindLayer(_strLayerTag);

        if(targetLayer == null)
            return null;

        if(targetLayer.size() <= _idx)
            return null;

        return targetLayer.get(_idx);
    }

    public static ObjectMgr GetInst() {
        return instance;
    }

    public void Dispose() {
        for(ArrayList<GameObject> layer : layers.values()) {
            layer.clear();
        }
        reserves.clear();
    }
}
