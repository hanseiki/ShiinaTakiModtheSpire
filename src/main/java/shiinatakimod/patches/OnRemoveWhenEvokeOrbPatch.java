package shiinatakimod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import javassist.CtBehavior;
import shiinatakimod.orbs.ShiinaTakiOrb;


@SpirePatch(
        clz= AbstractPlayer.class,
        method="evokeOrb"
)
public class OnRemoveWhenEvokeOrbPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {}
    )
    public static void Insert(AbstractPlayer __instance){
        if(__instance.orbs.get(0) instanceof ShiinaTakiOrb){
            ((ShiinaTakiOrb)__instance.orbs.get(0)).onRemove();
        }
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethod) throws Exception {
            // 匹配新建EmptyOrbSlot的位置
            Matcher finalMatcher = new Matcher.NewExprMatcher(EmptyOrbSlot.class);

            // 使用LineFinder找到第一个符合的插入点
            int[] positions = LineFinder.findAllInOrder(ctMethod, finalMatcher);

            // 返回第一个匹配位置的前一个位置（插入在循环开始前）
            return new int[] { positions[0] - 1 };
        }

    }
}
/*
public void evokeOrb() {
    if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
        ((AbstractOrb)this.orbs.get(0)).onEvoke();

        if(__instance.orbs.get(0) instanceof ShiinaTakiOrb){
            ((ShiinaTakiOrb)__instance.orbs.get(0)).onRemove();
        }
        //在此处之前插入
        AbstractOrb orbSlot = new EmptyOrbSlot();

        for(int i = 1; i < this.orbs.size(); ++i) {
            Collections.swap(this.orbs, i, i - 1);
        }

        this.orbs.set(this.orbs.size() - 1, orbSlot);

        for(int i = 0; i < this.orbs.size(); ++i) {
            ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
        }
    }

}

 */