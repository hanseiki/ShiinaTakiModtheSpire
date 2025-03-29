package shiinatakimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import static shiinatakimod.ShiinaTakiBasicMod.makeID;

public class KizuiteteWatashiMoRiyoShitetaAction extends AbstractGameAction {

    public static final String ID = makeID("KizuiteteWatashiMoRiyoShitetaAction");

    private static final UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = UIStrings.TEXT;

    private AbstractPlayer p;
    private AbstractMonster m;
    private  int i;
    private boolean norenderintent = false ;

    public KizuiteteWatashiMoRiyoShitetaAction(AbstractPlayer p, AbstractMonster m, int magicNumber) {
        this.p = p;
        this.m = m;
        this.i = magicNumber;
    }

    @Override
    public void update() {

        if(norenderintent){
            AbstractDungeon.effectList.add(
                    new ThoughtBubble(
                            p.dialogX,
                            p.dialogY,
                            3.0F,
                            TEXT[0],
                            true
                    )
            );
        }else{
            if(this.m.getIntentBaseDmg() >= 0){
                AbstractDungeon.effectList.add(
                        new ThoughtBubble(
                                p.dialogX,
                                p.dialogY,
                                3.0F,
                                TEXT[1],
                                true
                        )
                );
                addToBot(new DrawPileToHandAction(1, AbstractCard.CardType.ATTACK));
            }else{
                addToBot(new DrawPileToHandAction(this.i, AbstractCard.CardType.SKILL));
                addToBot(new GainEnergyAction(this.i));
                addToBot(new ApplyPowerAction(this.p,this.p,new EntanglePower(this.p),1));
            }
        }
        this.isDone = true;
    }
}
