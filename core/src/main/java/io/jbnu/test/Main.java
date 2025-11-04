package io.jbnu.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import io.jbnu.test.Level.Level;
import io.jbnu.test.Level.Level_Stage0;
import io.jbnu.test.Level.Level_Stage1;
import io.jbnu.test.Level.Level_Stage2;
import io.jbnu.test.Manager.CameraMgr;
import io.jbnu.test.Manager.InputMgr;
import io.jbnu.test.Manager.ObjectMgr;
import io.jbnu.test.Manager.RenderMgr;
import io.jbnu.test.Manager.ResourceMgr;
import io.jbnu.test.Object.GameCharacter;
import io.jbnu.test.Object.GameObject;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private int iStage = 0;
    private boolean bDebugRender = false;
    private boolean bGameRunning = true;
    private boolean bSlow = false;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    Sound effectSound;
    Level curLevel;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        LoadResources();
        NewLevel();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        logic();
        draw();
    }

    private void logic() {
        InputMgr.GetInst().Update();

        GameCharacter player = (GameCharacter)(ObjectMgr.GetInst().GetObject("Player", 0));
        if(player != null) {
            if (player.IsDead()) {
                curLevel.Dispose();
                ResourceMgr.GetInst().StopBgm();
                NewLevel();
            } else if (player.IsGoal()) {
                curLevel.Dispose();
                ResourceMgr.GetInst().StopBgm();
                iStage = ((iStage == 2) ? 0 : (iStage + 1));
                NewLevel();
            }
        }
        else if(iStage == 0 && InputMgr.GetInst().IsKeyInput(Keys.SPACE)) {
            iStage++;
            curLevel.Dispose();
            ResourceMgr.GetInst().StopBgm();
            NewLevel();
        }

        if(InputMgr.GetInst().IsKeyJustPress(Keys.F9))
            bDebugRender = !bDebugRender;

        if (InputMgr.GetInst().IsKeyJustPress(Keys.ESCAPE)) {
            bGameRunning = !bGameRunning;
            System.out.println(5);
        }

        float fTimeDelta = bGameRunning ? Gdx.graphics.getDeltaTime() : 0f;

        bSlow = InputMgr.GetInst().IsKeyPressing(Keys.SHIFT_LEFT);
        if(bSlow && iStage != 0)
            fTimeDelta /= 10f;

        if(bGameRunning) {
            curLevel.Update(fTimeDelta);
        }
        curLevel.LateUpdate(fTimeDelta);
    }

    private void draw(){
        batch.begin();
        CameraMgr.GetInst().SetProjection(batch);
        RenderMgr.GetInst().Render(batch);
        batch.end();

        if(bSlow || !bGameRunning) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Color color = bSlow ? new Color(0,0,0.2f,0.4f) : new Color(0,0,0,0.7f);
            shapeRenderer.setColor(color);
            shapeRenderer.rect(-500, -500, 5000, 5000);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        if(bDebugRender) {
            CameraMgr.GetInst().SetProjection(shapeRenderer);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.GREEN);
            RenderMgr.GetInst().DebugRender(shapeRenderer);
            shapeRenderer.end();
        }

        RenderMgr.GetInst().ClearRenderGroup();
    }

    @Override
    public void dispose() {
        curLevel.Dispose();
        ResourceMgr.GetInst().Dispose();
        batch.dispose();
    }

    private void NewLevel() {
        switch (iStage) {
            case 0:
                curLevel = new Level_Stage0();
                break;
            case 1:
                curLevel = new Level_Stage1();
                break;
            case 2:
                curLevel = new Level_Stage2();
                break;
        }
    }

    private void LoadResources() {
        effectSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        ResourceMgr.GetInst().LoadResource("Player", "0.png", MySprite.OriginType.ORIGIN_CB);
        ResourceMgr.GetInst().LoadResource("Pause", "pause.png", MySprite.OriginType.ORIGIN_LB);
        ResourceMgr.GetInst().LoadResource("Fence", "Map/Fence.png", MySprite.OriginType.ORIGIN_CC);
        ResourceMgr.GetInst().LoadResource("Stage0", "Map/Stage0.png", MySprite.OriginType.ORIGIN_CB);
        ResourceMgr.GetInst().LoadResource("Stage1", "Map/Stage1.png", MySprite.OriginType.ORIGIN_LB);
        ResourceMgr.GetInst().LoadResource("Stage2", "Map/Stage2.png", MySprite.OriginType.ORIGIN_LB);

        ResourceMgr.GetInst().LoadSoundResource("BGM_S0", "Sounds/song_rainonbrick.ogg");
        ResourceMgr.GetInst().LoadSoundResource("BGM_S1", "Sounds/song_katanazero.ogg");
        ResourceMgr.GetInst().LoadSoundResource("BGM_S2", "Sounds/song_edm.ogg");

        ResourceMgr.GetInst().LoadSoundResource("EnemyParry", "Sounds/sound_enemy_weapon_swing.wav");
        ResourceMgr.GetInst().LoadSoundResource("EnemyDeath1", "Sounds/sound_enemy_death_sword_01.wav");
        ResourceMgr.GetInst().LoadSoundResource("EnemyDeath2", "Sounds/sound_enemy_death_sword_02.wav");

        ResourceMgr.GetInst().LoadSoundResource("Slash1", "Sounds/slash_1.wav");
        ResourceMgr.GetInst().LoadSoundResource("Slash2", "Sounds/slash_2.wav");
        ResourceMgr.GetInst().LoadSoundResource("Slash3", "Sounds/slash_3.wav");
        //ResourceMgr.GetInst().LoadSoundResource("CassetPlay", "Sounds/player_cassetplay.ogg");
        //ResourceMgr.GetInst().LoadSoundResource("CassetRewind", "Sounds/player_cassetrewind.ogg");
    }
}
