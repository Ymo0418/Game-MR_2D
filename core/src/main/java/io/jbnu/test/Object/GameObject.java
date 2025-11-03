package io.jbnu.test.Object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.jbnu.test.Manager.ResourceMgr;
import io.jbnu.test.MySprite;

public abstract class GameObject {
    protected Vector2 vPosition;
    protected Vector2 vMovement;
    protected Vector2 vScale;
    protected MySprite sprite;
    protected boolean bDead = false;

    public GameObject(String _spriteTag, float _fStartX, float _fStartY) {
        this(_spriteTag, _fStartX, _fStartY, 1f, 1f);
    }

    public GameObject(String _spriteTag, float _fStartX, float _fStartY, float _fScaleX, float _fScaleY) {
        vPosition = new Vector2(_fStartX, _fStartY);
        vMovement = new Vector2(0f, 0f);
        vScale = new Vector2(_fScaleX, _fScaleY);
        sprite = ResourceMgr.GetInst().GetSprite(_spriteTag);
        sprite.SyncSprite(vPosition, vScale);
    }

    public void Update(float _fTimeDelta) {
    }

    public void LateUpdate(float _fTimeDelta) {
    }

    public void Render(SpriteBatch _batch) {
        sprite.SyncSprite(vPosition, vScale);
        sprite.Draw(_batch);
    }

    public void DebugRender(ShapeRenderer _renderer) {
    }

    public boolean IsDead() { return bDead; }

    public Vector2 GetPosition() { return vPosition; }
}
