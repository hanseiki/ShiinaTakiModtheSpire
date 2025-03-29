package shiinatakimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import shiinatakimod.powers.BurokkuPower;

import static shiinatakimod.ShiinaTakiBasicMod.makeID;

public class BurokkuShiteYaruAction extends AbstractGameAction {

    public static final String ID = makeID("BurokkuShiteYaruAction");

    private static final UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = UIStrings.TEXT;

    private AbstractPlayer p;
    private AbstractMonster m;
    private  int i;

    public BurokkuShiteYaruAction(AbstractPlayer p, AbstractMonster m, int magicNumber) {
        this.p = p;
        this.m = m;
        this.i = magicNumber;
    }

    @Override
    public void update() {
        if (this.m != null){
            if( this.m.hasPower( makeID("Burokku"))){//怪兽被拉黑时
                BurokkuPower power = (BurokkuPower) m.getPower(BurokkuPower.POWER_ID);
                int amountToRemove = power.getMetallicizeAmount(); // 获取保存的金属化数值
                AbstractDungeon.actionManager.addToBottom(//移除拉黑
                        new RemoveSpecificPowerAction(
                                (AbstractCreature)this.m,
                                (AbstractCreature)this.p,
                                makeID("Burokku")
                        )
                );
                AbstractDungeon.actionManager.addToBottom(//移除金属化
                        new ApplyPowerAction(
                                (AbstractCreature)this.p,
                                (AbstractCreature)this.m,
                                (AbstractPower)new MetallicizePower(
                                        (AbstractCreature)this.p,
                                        -amountToRemove
                                ),
                                -amountToRemove
                        )
                );
                AbstractDungeon.effectList.add(//提示
                        new ThoughtBubble(
                                p.dialogX,
                                p.dialogY,
                                3.0F,
                                TEXT[0],
                                true
                        )
                );

            }
            else{//怪物没被拉黑的场合
                if(this.m.getIntentBaseDmg() >= 0){//怪物为攻击意图
                    AbstractDungeon.actionManager.addToBottom(//拉黑怪物
                            new ApplyPowerAction(
                                    (AbstractCreature)this.m,
                                    (AbstractCreature)this.p,
                                    (AbstractPower)new BurokkuPower(
                                            (AbstractCreature)this.m,
                                            this.i
                                    )
                            )
                    );

                    AbstractDungeon.actionManager.addToBottom(//获得金属化
                            new ApplyPowerAction(
                                    (AbstractCreature)this.p,
                                    (AbstractCreature)this.p,
                                    (AbstractPower)new MetallicizePower(
                                            (AbstractCreature)this.p,
                                            this.i
                                    ),
                                    this.i
                            )
                    );
                    AbstractDungeon.effectList.add(
                            new ThoughtBubble(
                                    p.dialogX,
                                    p.dialogY,
                                    3.0F,
                                    TEXT[1],
                                    true
                            )
                    );
                }else {//怪物不是攻击意图
                    AbstractDungeon.effectList.add(
                            new ThoughtBubble(
                                    p.dialogX,
                                    p.dialogY,
                                    3.0F,
                                    TEXT[2],
                                    true
                            )
                    );
                }

            };

        }
        this.isDone = true;
    }
}
