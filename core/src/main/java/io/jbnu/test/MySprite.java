package io.jbnu.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MySprite {
    public static enum OriginType {
        ORIGIN_CB,
        ORIGIN_CC,
        ORIGIN_LT,
        ORIGIN_LB,
        ORIGIN_RB,
        ORIGIN_RT,
        ORIGIN_END
    }

    protected Sprite sprite;
    protected Vector2 vPivot = new Vector2(0f, 0f);

    public MySprite(MySprite rhs) {
        this.sprite = new Sprite(rhs.sprite);
        this.vPivot = new Vector2(rhs.vPivot);
    }
    public MySprite(Texture tex) {
        sprite = new Sprite(tex);
    }
    public MySprite(Texture tex, OriginType eOriginType) {
        this(tex);
        SetOrigin(eOriginType);
    }

    public void SyncSprite(Vector2 pos, Vector2 scale) {
        sprite.setScale(scale.x, scale.y);

        Vector2 vWorldSize = new Vector2(sprite.getWidth() * scale.x, sprite.getHeight() * scale.y);

        sprite.setPosition(pos.x - sprite.getWidth() * vPivot.x, pos.y - sprite.getHeight() * vPivot.y);
    }

    public void Draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void SetFlip(boolean isFlip) {
        sprite.setFlip(isFlip, false);
    }

    protected void SetOrigin(OriginType eOriginType) {
        switch (eOriginType) {
            case ORIGIN_CB:
                vPivot = new Vector2(0.5f, 0.f);
                break;
            case ORIGIN_LB:
                vPivot = new Vector2(0.f, 0.f);
                break;
            case ORIGIN_CC:
                vPivot = new Vector2(0.5f, 0.5f);
                break;
            case ORIGIN_LT:
                vPivot = new Vector2(0.f, 1.f);
                break;
            case ORIGIN_RB:
                vPivot = new Vector2(1.f, 0.f);
                break;
            case ORIGIN_RT:
                vPivot = new Vector2(1.f, 1.f);
                break;
        }

        sprite.setOrigin(sprite.getWidth() * vPivot.x, sprite.getHeight() * vPivot.y);
    }
}
