package io.jbnu.test.Level;

import io.jbnu.test.Manager.ObjectMgr;
import io.jbnu.test.Object.Background.Background_Menu;
import io.jbnu.test.Object.Background.Background_Sewer;
import io.jbnu.test.Object.Background.Fence;
import io.jbnu.test.Object.Background.Plant;
import io.jbnu.test.Object.GameCharacter;
import io.jbnu.test.Object.Pomp;

public class Level_Stage0 extends Level {
    public Level_Stage0() {
        ObjectMgr.GetInst().AddObject("Background", new Background_Menu());
        ObjectMgr.GetInst().AddObject("Background", new Fence("Fence", 640f, 560f, 2f, 2f));
        ObjectMgr.GetInst().AddObject("Background", new Plant());
    }
}
