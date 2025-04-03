package shiinatakimod.powers;



import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import static shiinatakimod.ShiinaTakiBasicMod.makeID;

public class ChannelOrbNextTurnPower extends BasePower {
    public static final String POWER_ID = makeID("ChannelNextTurn");

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    private static int drawIdOffset = 0 ;
    private AbstractOrb orbType;

    public ChannelOrbNextTurnPower(AbstractCreature owner, int drawAmount){
        this(owner,drawAmount,1, new EmptyOrbSlot());
    }

    public ChannelOrbNextTurnPower(AbstractCreature owner, int orbsAmount, int countDown, AbstractOrb newOrbType) {
        super(POWER_ID, TYPE, TURN_BASED, owner, orbsAmount);
        this.amount2 =countDown;
        this.ID = POWER_ID+ this.amount2+"_";//+ newOrbType.name;
        this.canGoNegative = false;
        this.orbType = newOrbType;
        this.type = PowerType.DEBUFF;
    }



    @Override
    public void atStartOfTurnPostDraw() {
       this.flash();
       if(this.amount2 ==0){
           for(int i=0;i<this.amount;i++){
               this.addToBot(new ChannelAction(this.orbType));
           }
           this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
       }

    }
/*
    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        AbstractDungeon.player.gameHandSize += stackAmount;
        this.amount += stackAmount;
        if (this.amount == 0)
            addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        if (this.amount >= 999)
            this.amount = 999;
        if (this.amount <= -999)
            this.amount = -999;
    }

 */

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.amount2 --;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] ;//+ this.orbType.name + DESCRIPTIONS[2];
    }
}
