package shiinatakimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import shiinatakimod.orbs.NormalStressOrb;
import shiinatakimod.orbs.STRStressOrb;
import shiinatakimod.orbs.TakiOrbsManager;

public class ConvertOrbAction extends AbstractGameAction {
    private Class<? extends AbstractOrb> targetOrbClass; // 记录目标 Orb 类型
    private boolean autoEvoke;

    public ConvertOrbAction(Class<? extends AbstractOrb> targetOrbClass) {
        this.targetOrbClass = targetOrbClass;
    }


    @Override
    public void update() {
        // 调用 TakiOrbsManager 时不再传入预制实例
        TakiOrbsManager.convertNextOrb(targetOrbClass);

        this.isDone = true;
    }
}
