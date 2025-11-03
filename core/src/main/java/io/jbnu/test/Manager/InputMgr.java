package io.jbnu.test.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class InputMgr {
    private static InputMgr instance  = new InputMgr();
    private final boolean[] preInputs = new boolean[256];
    private final boolean[] curInputs = new boolean[256];

    private InputMgr() {
    }

    public void Update() {
        System.arraycopy(curInputs, 0, preInputs, 0, curInputs.length);

        for(int i = 0; i < curInputs.length; ++i)
            curInputs[i] = Gdx.input.isKeyPressed(i);
    }

    public boolean IsKeyInput(int _iKey)    { return curInputs[_iKey]; }
    public boolean IsKeyUp(int _iKey)       { return preInputs[_iKey] && !curInputs[_iKey]; }
    public boolean IsKeyJustPress(int _iKey){ return !preInputs[_iKey] && curInputs[_iKey]; }
    public boolean IsKeyPressing(int _iKey) { return preInputs[_iKey] && curInputs[_iKey]; }

    public Vector3 GetMouseWorldPos() {
        Vector3 vMouseWorldPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
        vMouseWorldPos = CameraMgr.GetInst().Unproject(vMouseWorldPos);
        return vMouseWorldPos;
    }

    public static InputMgr GetInst() {
        return instance;
    }
}
