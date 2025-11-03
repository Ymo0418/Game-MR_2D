package io.jbnu.test.Level;

import io.jbnu.test.Manager.ObjectMgr;
import io.jbnu.test.Object.Background.Background_Sewer;
import io.jbnu.test.Object.GameCharacter;
import io.jbnu.test.Object.Goal;
import io.jbnu.test.Object.Grunt;
import io.jbnu.test.Object.Pomp;

public class Level_Stage1 extends Level {
    public Level_Stage1() {
        ObjectMgr.GetInst().AddObject("Background", new Background_Sewer());
        ObjectMgr.GetInst().AddObject("Monster", new Pomp(32f*37f, 32f*25f, 32f*37f, 32f*10f, false));
        ObjectMgr.GetInst().AddObject("Monster", new Grunt(32f*14f, 32f*16f, 32f*14f, 32f*14f));
        ObjectMgr.GetInst().AddObject("Monster", new Grunt(32f*28f, 32f*16f, 32f*28f, -32f*14f));
        ObjectMgr.GetInst().AddObject("Player", new GameCharacter());
        ObjectMgr.GetInst().AddObject("Goal", new Goal(32f*62f, 32f*28f));
    }
}
