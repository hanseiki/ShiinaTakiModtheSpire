package shiinatakimod.orbs;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.lang.reflect.Constructor;
import java.util.Collections;

import static com.megacrit.cardcrawl.characters.AbstractPlayer.MSG;
import static shiinatakimod.ShiinaTakiBasicMod.makeID;

public class TakiOrbsManager {
    public static final String ID = makeID("TakiOrbsManager");

    private static final UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = UIStrings.TEXT;

    public void triggerEvokeAnimation(int slot) {
        if (AbstractDungeon.player.maxOrbs > 0) {
            ((AbstractOrb)AbstractDungeon.player.orbs.get(slot)).triggerEvokeAnimation();
        }
    }

    public void evokeOrb() {
        if (!AbstractDungeon.player.orbs.isEmpty() && !(AbstractDungeon.player.orbs.get(0) instanceof EmptyOrbSlot)) {
            ((AbstractOrb)AbstractDungeon.player.orbs.get(0)).onEvoke();
            AbstractOrb orbSlot = new EmptyOrbSlot();

            for(int i = 1; i < AbstractDungeon.player.orbs.size(); ++i) {
                Collections.swap(AbstractDungeon.player.orbs, i, i - 1);
            }

            AbstractDungeon.player.orbs.set(AbstractDungeon.player.orbs.size() - 1, orbSlot);

            for(int i = 0; i < AbstractDungeon.player.orbs.size(); ++i) {
                ((AbstractOrb)AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
            }
        }

    }

    public void evokeNewestOrb() {
        if (!AbstractDungeon.player.orbs.isEmpty() && !(AbstractDungeon.player.orbs.get(AbstractDungeon.player.orbs.size() - 1) instanceof EmptyOrbSlot)) {
            ((AbstractOrb)AbstractDungeon.player.orbs.get(AbstractDungeon.player.orbs.size() - 1)).onEvoke();
        }

    }

    public void evokeWithoutLosingOrb() {
        if (!AbstractDungeon.player.orbs.isEmpty() && !(AbstractDungeon.player.orbs.get(0) instanceof EmptyOrbSlot)) {
            ((AbstractOrb)AbstractDungeon.player.orbs.get(0)).onEvoke();
        }

    }

    public void removeNextOrb() {
        if (!AbstractDungeon.player.orbs.isEmpty() && !(AbstractDungeon.player.orbs.get(0) instanceof EmptyOrbSlot)) {
            AbstractOrb orbSlot = new EmptyOrbSlot(((AbstractOrb)AbstractDungeon.player.orbs.get(0)).cX, ((AbstractOrb)AbstractDungeon.player.orbs.get(0)).cY);

            for(int i = 1; i < AbstractDungeon.player.orbs.size(); ++i) {
                Collections.swap(AbstractDungeon.player.orbs, i, i - 1);
            }

            AbstractDungeon.player.orbs.set(AbstractDungeon.player.orbs.size() - 1, orbSlot);

            for(int i = 0; i < AbstractDungeon.player.orbs.size(); ++i) {
                ((AbstractOrb)AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
            }
        }

    }

    public boolean hasEmptyOrb() {
        if (AbstractDungeon.player.orbs.isEmpty()) {
            return false;
        } else {
            for(AbstractOrb o : AbstractDungeon.player.orbs) {
                if (o instanceof EmptyOrbSlot) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean hasOrb() {
        if (AbstractDungeon.player.orbs.isEmpty()) {
            return false;
        } else {
            return !(AbstractDungeon.player.orbs.get(0) instanceof EmptyOrbSlot);
        }
    }

    public int filledOrbCount() {
        int orbCount = 0;

        for(AbstractOrb o : AbstractDungeon.player.orbs) {
            if (!(o instanceof EmptyOrbSlot)) {
                ++orbCount;
            }
        }

        return orbCount;
    }

    public static void channelOrb(AbstractOrb orbToSet) {
        if (AbstractDungeon.player.maxOrbs <= 0) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, MSG[4], true));
        } else {
            if (AbstractDungeon.player.maxOrbs > 0) {
                if (AbstractDungeon.player.hasRelic("Dark Core") && !(orbToSet instanceof Dark)) {
                    orbToSet = new Dark();
                }

                int index = -1;

                for(int i = 0; i < AbstractDungeon.player.orbs.size(); ++i) {
                    if (AbstractDungeon.player.orbs.get(i) instanceof EmptyOrbSlot) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    orbToSet.cX = ((AbstractOrb)AbstractDungeon.player.orbs.get(index)).cX;
                    orbToSet.cY = ((AbstractOrb)AbstractDungeon.player.orbs.get(index)).cY;
                    AbstractDungeon.player.orbs.set(index, orbToSet);
                    ((AbstractOrb)AbstractDungeon.player.orbs.get(index)).setSlot(index, AbstractDungeon.player.maxOrbs);
                    orbToSet.playChannelSFX();

                    for(AbstractPower p : AbstractDungeon.player.powers) {
                        p.onChannel(orbToSet);
                    }

                    //如果时ShiinaTakiOrb就调用onChannel
                    if(orbToSet instanceof ShiinaTakiOrb){
                        ((ShiinaTakiOrb) orbToSet).onChannel();
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

    public void increaseMaxOrbSlots(int amount, boolean playSfx) {
        if (AbstractDungeon.player.maxOrbs == 10) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, MSG[3], true));
        } else {
            if (playSfx) {
                CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1F);
            }

            AbstractDungeon.player.maxOrbs += amount;

            for(int i = 0; i < amount; ++i) {
                AbstractDungeon.player.orbs.add(new EmptyOrbSlot());
            }

            for(int i = 0; i < AbstractDungeon.player.orbs.size(); ++i) {
                ((AbstractOrb)AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
            }

        }
    }

    public void decreaseMaxOrbSlots(int amount) {
        if (AbstractDungeon.player.maxOrbs > 0) {
            AbstractDungeon.player.maxOrbs -= amount;
            if (AbstractDungeon.player.maxOrbs < 0) {
                AbstractDungeon.player.maxOrbs = 0;
            }

            if (!AbstractDungeon.player.orbs.isEmpty()) {
                AbstractDungeon.player.orbs.remove(AbstractDungeon.player.orbs.size() - 1);
            }

            for(int i = 0; i < AbstractDungeon.player.orbs.size(); ++i) {
                ((AbstractOrb)AbstractDungeon.player.orbs.get(i)).setSlot(i, AbstractDungeon.player.maxOrbs);
            }

        }
    }

    public void applyStartOfTurnOrbs() {
        if (!AbstractDungeon.player.orbs.isEmpty()) {
            for(AbstractOrb o : AbstractDungeon.player.orbs) {
                o.onStartOfTurn();
            }

            if (AbstractDungeon.player.hasRelic("Cables") && !(AbstractDungeon.player.orbs.get(0) instanceof EmptyOrbSlot)) {
                ((AbstractOrb)AbstractDungeon.player.orbs.get(0)).onStartOfTurn();
            }
        }

    }



    public static void convertNextOrb(
            Class<? extends AbstractOrb> sourceOrbType, // 要替换的原始 Orb 类型
            Class<? extends AbstractOrb> targetOrbType,// 目标 Orb 类型
            int passiveAmount,
            int evokeAmount
    ) {
        if (AbstractDungeon.player.maxOrbs <= 0) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, MSG[4], true));

        } else {
            if (AbstractDungeon.player.maxOrbs > 0) {
                int index = -1;

                for(int i = 0; i < AbstractDungeon.player.orbs.size(); ++i) {
                    if (sourceOrbType.isInstance(AbstractDungeon.player.orbs.get(i))) {//AbstractDungeon.player.orbs.get(i) instanceof NormalStressOrb
                        if(passiveAmount == -1 && evokeAmount==-1){
                            index = i;
                            break;
                        }
                        if(((ShiinaTakiOrb) AbstractDungeon.player.orbs.get(i)).getPassiveAmount() == passiveAmount
                                &&((ShiinaTakiOrb) AbstractDungeon.player.orbs.get(i)).getEvokeAmount() == evokeAmount
                        ){
                            index = i;
                            break;
                        }

                    }
                }

                if (index != -1) {
                    AbstractOrb oldOrb = AbstractDungeon.player.orbs.get(index);
                    AbstractOrb newOrb;
                    if(passiveAmount == -1
                            && evokeAmount == -1
                    ){
                        newOrb = createOrbWithInheritance(targetOrbType, oldOrb);
                    }else {
                        newOrb = createOrbWithoutInheritance(targetOrbType,passiveAmount,evokeAmount);
                    }

                    // 设置坐标等逻辑
                    newOrb.cX = oldOrb.cX;//orbToSet.cX = ((AbstractOrb)AbstractDungeon.player.orbs.get(index)).cX;
                    newOrb.cY = oldOrb.cY;//orbToSet.cY = ((AbstractOrb)AbstractDungeon.player.orbs.get(index)).cY;

                    // 触发旧 Orb 的 onRemove
                    if (oldOrb instanceof ShiinaTakiOrb) {
                        ((ShiinaTakiOrb) oldOrb).onRemove();
                    }
                    // 替换 Orb
                    AbstractDungeon.player.orbs.set(index, newOrb);//AbstractDungeon.player.orbs.set(index, orbToSet);
                    newOrb.setSlot(index, AbstractDungeon.player.maxOrbs);//((AbstractOrb)AbstractDungeon.player.orbs.get(index)).setSlot(index, AbstractDungeon.player.maxOrbs);
                    newOrb.playChannelSFX();
                    // 触发新 Orb 的 onChannel
                    if(newOrb instanceof ShiinaTakiOrb){
                        ((ShiinaTakiOrb) newOrb).onChannel();
                    }

                    for(AbstractPower p : AbstractDungeon.player.powers) {
                        p.onChannel(newOrb);
                    }

                    AbstractDungeon.actionManager.orbsChanneledThisCombat.add(newOrb);
                    AbstractDungeon.actionManager.orbsChanneledThisTurn.add(newOrb);

                    //newOrb.applyFocus();
                } else {
                    /*
                    AbstractDungeon.actionManager.addToTop(new ChannelAction(orbToSet));
                    AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
                    AbstractDungeon.actionManager.addToTop(new AnimateOrbAction(1));
                     */
                    AbstractDungeon.effectList.add(
                            new ThoughtBubble(
                                    AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY,
                                    3.0F, TEXT[0], true
                            )
                    );
                }
            }

        }
    }

    private static AbstractOrb createOrbWithInheritance(
            Class<? extends AbstractOrb> targetOrbType,
            AbstractOrb oldOrb
    ) {
        try {
            // 如果旧 Orb 是 ShiinaTakiOrb 子类，且目标类有对应构造函数
            if (oldOrb instanceof ShiinaTakiOrb
                    && ShiinaTakiOrb.class.isAssignableFrom(targetOrbType)){
                ShiinaTakiOrb oldTaki = (ShiinaTakiOrb) oldOrb;
                // 尝试通过反射调用目标类的构造函数（需提前定义）
                Constructor<?> constructor = targetOrbType.getConstructor(int.class, int.class);
                return (AbstractOrb) constructor.newInstance(
                        oldTaki.passiveAmount,
                        oldTaki.evokeAmount
                );
            } else {
                // 默认无参构造
                return targetOrbType.newInstance();
            }
        } catch (Exception e) {
            // 异常处理（例如目标类无合适构造函数）
            e.printStackTrace();
            return new EmptyOrbSlot();
        }
    }

    private static AbstractOrb createOrbWithoutInheritance(
            Class<? extends AbstractOrb> targetOrbType,
            int passiveAmount,
            int evokeAmount
    ) {
        try {
            // 如果旧 Orb 是 ShiinaTakiOrb 子类，且目标类有对应构造函数
            if (ShiinaTakiOrb.class.isAssignableFrom(targetOrbType)){
                // 尝试通过反射调用目标类的构造函数（需提前定义）
                Constructor<?> constructor = targetOrbType.getConstructor(int.class, int.class);
                return (AbstractOrb) constructor.newInstance(
                        passiveAmount,
                        evokeAmount
                );
            } else {
                // 默认无参构造
                return targetOrbType.newInstance();
            }
        } catch (Exception e) {
            // 异常处理（例如目标类无合适构造函数）
            e.printStackTrace();
            return new EmptyOrbSlot();
        }
    }


}
