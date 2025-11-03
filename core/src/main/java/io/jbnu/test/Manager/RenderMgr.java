package io.jbnu.test.Manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import io.jbnu.test.Object.GameObject;

public class RenderMgr {
    public static enum RenderGroup {
        RG_BG,
        RG_NONBLEND,
        RG_BLEND,
        RG_UI,
        RG_END
    }
    private static RenderMgr        instance = new RenderMgr();
    private Array<List<GameObject>> renderObjects;
    private List<GameObject>        debugObjects;

    private RenderMgr() {
        renderObjects = new Array<>(RenderGroup.RG_END.ordinal());

        for(int i = 0; i < RenderGroup.RG_END.ordinal(); ++i)
            renderObjects.add(new ArrayList<>());

        debugObjects = new ArrayList<>();
    }

    public void Render(SpriteBatch _batch) {
        for(GameObject obj : renderObjects.get(RenderGroup.RG_BG.ordinal()))
            obj.Render(_batch);

        for(GameObject obj : renderObjects.get(RenderGroup.RG_NONBLEND.ordinal()))
            obj.Render(_batch);

        for(GameObject obj : renderObjects.get(RenderGroup.RG_BLEND.ordinal()))
            obj.Render(_batch);

        for(GameObject obj : renderObjects.get(RenderGroup.RG_UI.ordinal()))
            obj.Render(_batch);
    }

    public void DebugRender(ShapeRenderer _renderer) {
        for(GameObject obj : debugObjects)
            obj.DebugRender(_renderer);
    }

    public void ClearRenderGroup() {
        for(List<GameObject> objList : renderObjects)
            objList.clear();

        debugObjects.clear();
    }

    public void RegisterRenderGroup(RenderGroup _eRenderGroup, GameObject _obj) {
        renderObjects.get(_eRenderGroup.ordinal()).add(_obj);
    }

    public void RegisterDebugRender(GameObject _obj) {
        debugObjects.add(_obj);
    }

    public static RenderMgr GetInst() {
        return instance;
    }

    public void Dispose() {
        ClearRenderGroup();
    }
}
