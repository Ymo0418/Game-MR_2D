package io.jbnu.test.Manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.jbnu.test.Object.Background.Background;
import io.jbnu.test.Object.GameObject;

public class CameraMgr {
    private static CameraMgr    instance = new CameraMgr();
    private OrthographicCamera  mainCamera = new OrthographicCamera();
    private Viewport    viewport;
    private Vector3     vOriginalPos;
    private float       fShakeDuration = 0f;
    private float       fShakeCurDuration = 0f;
    private float       fShakeIntensity = 0f;

    private CameraMgr() {
        mainCamera.setToOrtho(false, 800, 600);
        mainCamera.position.x = 640f;
        mainCamera.position.y = 256f;

        viewport = new FitViewport(1280, 720, mainCamera);
        vOriginalPos = new Vector3(mainCamera.position);
    }

    public static CameraMgr GetInst() {
        return instance;
    }

    public void Update(float _fTimeDelta) {

        GameObject player = ObjectMgr.GetInst().GetObject("Player", 0);
        Background bg = (Background)(ObjectMgr.GetInst().GetObject("Background", 0));
        if(player != null && bg != null) {
            Vector2 playerPos = player.GetPosition();
            Vector3 newPos = mainCamera.position.lerp(new Vector3(playerPos.x, playerPos.y + 32*5f, 0f ), 0.017f);

            mainCamera.position.x = bg.CalCamLimit(newPos.x);
        }

        if(fShakeCurDuration > 0f) {
            float ratio = fShakeCurDuration / fShakeDuration;
            float curIntensity = ratio * fShakeIntensity;
            float offsetX = MathUtils.random(-1f, 1f) * curIntensity;
            float offsetY = MathUtils.random(-1f, 1f) * curIntensity;
            mainCamera.position.set(vOriginalPos.x + offsetX, vOriginalPos.y + offsetY, vOriginalPos.z);
            fShakeCurDuration -= _fTimeDelta;
        }

        viewport.update(1280, 720, false);
    }

    public void SetShake(float _fDuration, float _fIntensity) {
        fShakeDuration = _fDuration;
        fShakeCurDuration = _fDuration;
        fShakeIntensity = _fIntensity;

        mainCamera.position.set(vOriginalPos);
        vOriginalPos = new Vector3(mainCamera.position);
    }

    public void SetProjection(SpriteBatch _batch) {
        _batch.setProjectionMatrix(mainCamera.combined);
    }

    public void SetProjection(ShapeRenderer _renderer) {
        _renderer.setProjectionMatrix(mainCamera.combined);
    }

    public Vector3 Unproject(Vector3 _vPos) {
        return mainCamera.unproject(_vPos);
    }

    public void Dispose() {
        mainCamera.position.x = 640f;
        mainCamera.position.y = 256f;
    }
}
