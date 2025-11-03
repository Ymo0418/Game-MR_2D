package io.jbnu.test.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.jbnu.test.MySprite;

public class ResourceMgr {
    private static ResourceMgr instance = null;
    private HashMap<String, Texture> Textures = new HashMap<>();
    private HashMap<String, MySprite> Sprites = new HashMap<>();
    private HashMap<String, Sound> Sounds = new HashMap<>();

    private ResourceMgr() {
    }

    public void CheckResourceContain(String _strTag) {
        if(Textures.containsKey(_strTag))
            throw new IllegalArgumentException("Already Loaded Texture " + _strTag);
        if(Sprites.containsKey(_strTag))
            throw new IllegalArgumentException("Already Contain SpriteTag " + _strTag);
    }

    public void LoadResource(String _strTag, String _strPath, MySprite.OriginType _eOriginType) {
        CheckResourceContain(_strTag);
        Texture tex = new Texture(_strPath);
        Textures.put(_strTag, tex);
        Sprites.put(_strTag, new MySprite(tex, _eOriginType));
    }

    public void LoadSoundResource(String _strTag, String _strPath) {
        CheckResourceContain(_strTag);
        Sounds.put(_strTag, Gdx.audio.newSound(Gdx.files.internal(_strPath)));
    }

    public void PlaySound(String _strTag, float _fVolume, boolean bLoop) {
        if(Sounds.containsKey(_strTag)) {
            if(bLoop)
                Sounds.get(_strTag).loop(_fVolume);
            else
                Sounds.get(_strTag).play(_fVolume);
        }
    }
    public void StopSound(String _strTag) {
        if(Sounds.containsKey(_strTag))
            Sounds.get(_strTag).stop();
    }
    public void PauseSound(String _strTag) {
        if(Sounds.containsKey(_strTag))
            Sounds.get(_strTag).pause();
    }
    public void ResumeSound(String _strTag) {
        if(Sounds.containsKey(_strTag))
            Sounds.get(_strTag).resume();
    }

    public void VolumeAllSound() {

    }

    public MySprite GetSprite(String _strTag) {
        if(Sprites.containsKey(_strTag))
            return new MySprite(Sprites.get(_strTag));
        else {
            throw new IllegalArgumentException("No Contain Tag " + _strTag);
        }
    }

    public static ResourceMgr GetInst() {
        if (instance == null)
            instance =  new ResourceMgr();

        return instance;
    }

    public void Dispose() {
        Sprites.clear();

        Iterator<HashMap.Entry<String, Texture>> iterator = Textures.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Texture> entry = iterator.next();
            entry.getValue().dispose();
            iterator.remove();
        }
    }
}
