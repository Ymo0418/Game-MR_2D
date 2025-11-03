package io.jbnu.test.Object.Background;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.jbnu.test.Manager.ResourceMgr;

public class Background_Menu extends Background {
    BitmapFont font1 = new BitmapFont();
    BitmapFont font2 = new BitmapFont();
    BitmapFont font3 = new BitmapFont();

    public Background_Menu() {
        super("Stage0", 640f, -150f, 2f, 2f);
        vCamLimit.x = 0f;
        vCamLimit.y = 0f;
        font1.getData().setScale(6f);
        font2.getData().setScale(3f);
        font3.getData().setScale(4f);

        ResourceMgr.GetInst().StopSound("BGM_S0");
        ResourceMgr.GetInst().PlaySound("BGM_S0", 0.2f, true);
    }

    @Override
    public void Render(SpriteBatch _batch) {
        super.Render(_batch);
        font1.draw(_batch, "Game", 500f, 310f);
        font2.draw(_batch, "&", 740f, 250f);
        font3.draw(_batch, "MixedReality", 470f, 210f);
    }

    @Override
    protected void ReadyCollider() {}

    @Override
    protected void ReadyLines() {}
}
