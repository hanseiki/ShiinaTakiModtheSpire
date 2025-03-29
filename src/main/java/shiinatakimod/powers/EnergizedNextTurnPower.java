package shiinatakimod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static shiinatakimod.ShiinaTakiBasicMod.makeID;

public class EnergizedNextTurnPower
        extends BasePower {
    public static final String POWER_ID = makeID("EnergizedNextTurn");

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    private static int energyIdOffset = 0 ;

    public EnergizedNextTurnPower(AbstractCreature owner, int drawAmount){
        this(owner, drawAmount, 1);
    }

    public EnergizedNextTurnPower(AbstractCreature owner, int energizedAmount, int countDown) {
        super(POWER_ID, TYPE, TURN_BASED, owner, energizedAmount);
        this.amount2 =countDown;
        this.ID = POWER_ID+ this.amount2+"_"+ energyIdOffset;
        this.canGoNegative = true;
    }

    @Override
    public void onEnergyRecharge() {
        flash();
        if(this.amount2 == 0){
            if(this.amount > 0){
                AbstractDungeon.player.gainEnergy(this.amount);
            }else {
                AbstractDungeon.player.loseEnergy(-this.amount);
            }
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }

    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0)
            addToTop((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        if (this.amount >= 999)
            this.amount = 999;
        if (this.amount <= -999)
            this.amount = -999;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.energyIdOffset++;
        this.amount2 --;
    }

    @Override
    public void updateDescription() {
        if (this.amount < 0) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
            this.type = AbstractPower.PowerType.DEBUFF;
            loadRegion("energized_green");
        } else {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
            this.type = AbstractPower.PowerType.BUFF;
            loadRegion("energized_blue");
        }
    }
}
