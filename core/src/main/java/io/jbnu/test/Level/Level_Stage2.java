package io.jbnu.test.Level;

import io.jbnu.test.Manager.ObjectMgr;
import io.jbnu.test.Object.Background.Background_Club;
import io.jbnu.test.Object.GameCharacter;
import io.jbnu.test.Object.Goal;
import io.jbnu.test.Object.Pomp;

public class Level_Stage2 extends Level {
    public Level_Stage2() {
        ObjectMgr.GetInst().AddObject("Background", new Background_Club());
        ObjectMgr.GetInst().AddObject("Monster", new Pomp(32f*17f, 32f*2f, 32f*17f, 32f*20f, true));
        ObjectMgr.GetInst().AddObject("Monster", new Pomp(32f*30f, 32f*2f, 32f*30f, 32f*10f, true));
        ObjectMgr.GetInst().AddObject("Monster", new Pomp(32f*37f, 32f*14f, 32f*37f, 32f*13f, true));
        ObjectMgr.GetInst().AddObject("Player", new GameCharacter());
        ObjectMgr.GetInst().AddObject("Goal", new Goal(32f*12.5f, 32f*17f));
    }
}
