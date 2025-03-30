package shiinatakimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import shiinatakimod.orbs.TakiOrbsManager;

public class ConvertOrbAction extends AbstractGameAction {
    private Class<? extends AbstractOrb> targetOrbClass; // 记录目标 Orb 类型
    private Class<? extends AbstractOrb> sourceOrbClass;//记录源Orb类型
    int passiveAmount;
    int evokeAmount;

    public ConvertOrbAction(Class<? extends AbstractOrb> sourceOrbClass , Class<? extends AbstractOrb> targetOrbClass) {
        this.sourceOrbClass = sourceOrbClass;
        this.targetOrbClass = targetOrbClass;
        this.passiveAmount = -1;
        this.evokeAmount = -1;
    }

    public ConvertOrbAction(Class<? extends AbstractOrb> sourceOrbClass , Class<? extends AbstractOrb> targetOrbClass ,int passiveAmount ,int evokeAmount) {
        this.sourceOrbClass = sourceOrbClass;
        this.targetOrbClass = targetOrbClass;
        this.passiveAmount = passiveAmount;
        this.evokeAmount = evokeAmount;
    }


    @Override
    public void update() {
        // 调用 TakiOrbsManager 时不再传入预制实例
        TakiOrbsManager.convertNextOrb(sourceOrbClass,targetOrbClass,passiveAmount,evokeAmount);

        this.isDone = true;
    }
}
