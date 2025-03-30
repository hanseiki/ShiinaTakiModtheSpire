package shiinatakimod.patches;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import shiinatakimod.orbs.ShiinaTakiOrb;


@SpirePatch(
        clz=AbstractPlayer.class,
        method="channelOrb"
)
public class OnChannelWhenChannelOrbPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"orbToSet"}
    )
    public static void Insert(AbstractPlayer __instance, AbstractOrb orbToSet){
        if(orbToSet instanceof ShiinaTakiOrb){
            ((ShiinaTakiOrb) orbToSet).onChannel();
        }
    }
    private static class Locator extends SpireInsertLocator{
        @Override
        public int[] Locate(CtBehavior ctMethod) throws Exception {
            // 匹配在调用powers循环之前的代码位置
            Matcher powerLoopMatcher = new Matcher.MethodCallMatcher(
                    AbstractPower.class,
                    "onChannel"
            );

            // 使用LineFinder找到第一个符合的插入点
            int[] positions = LineFinder.findAllInOrder(ctMethod, powerLoopMatcher);

            // 返回第一个匹配位置的前一个位置（插入在循环开始前）
            return new int[] { positions[0] - 1 };
        }

    }
}
/*
//预想效果
public void channelOrb(AbstractOrb orbToSet) {
    if (this.maxOrbs <= 0) {
        AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, MSG[4], true));
    } else {
        if (this.maxOrbs > 0) {
            if (this.hasRelic("Dark Core") && !(orbToSet instanceof Dark)) {
                orbToSet = new Dark();
            }

            int index = -1;

            for(int i = 0; i < this.orbs.size(); ++i) {
                if (this.orbs.get(i) instanceof EmptyOrbSlot) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                orbToSet.cX = ((AbstractOrb)this.orbs.get(index)).cX;
                orbToSet.cY = ((AbstractOrb)this.orbs.get(index)).cY;
                this.orbs.set(index, orbToSet);
                ((AbstractOrb)this.orbs.get(index)).setSlot(index, this.maxOrbs);
                orbToSet.playChannelSFX();

                if(orbToSet instanceof ShiinaTakiOrb){
                    ((ShiinaTakiOrb) orbToSet).onChannel();
                }
                //在此处之前插入
                for(AbstractPower p : this.powers) {
                    p.onChannel(orbToSet);
                }

                AbstractDungeon.actionManager.orbsChanneledThisCombat.add(orbToSet);
                AbstractDungeon.actionManager.orbsChanneledThisTurn.add(orbToSet);
                int plasmaCount = 0;

                for(AbstractOrb o : AbstractDungeon.actionManager.orbsChanneledThisTurn) {
                    if (o instanceof Plasma) {
                        ++plasmaCount;
                    }
                }

                if (plasmaCount == 9) {
                    UnlockTracker.unlockAchievement("NEON");
                }

                orbToSet.applyFocus();
            } else {
                AbstractDungeon.actionManager.addToTop(new ChannelAction(orbToSet));
                AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
                AbstractDungeon.actionManager.addToTop(new AnimateOrbAction(1));
            }
        }

    }
}

 */

