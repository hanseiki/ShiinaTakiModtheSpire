package shiinatakimod.powers;



import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static shiinatakimod.ShiinaTakiBasicMod.makeID;

public class DrawNextTurnPower extends BasePower {
    public static final String POWER_ID = makeID("DrawNextTurn");

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.DEBUFF;
    private static final boolean TURN_BASED = true;

    private static int drawIdOffset = 0 ;

    public DrawNextTurnPower(AbstractCreature owner, int drawAmount){
        this(owner,drawAmount,1);
    }

    public DrawNextTurnPower(AbstractCreature owner, int drawAmount, int countDown ) {
        super(POWER_ID, TYPE, TURN_BASED, owner, drawAmount);
        this.amount2 =countDown;
        this.ID = POWER_ID+ this.amount2+"_"+ drawIdOffset;
        this.canGoNegative = true;
    }

    @Override
    public void onInitialApplication() {//初始化时

        AbstractDungeon.player.gameHandSize += this.amount;
    }

    @Override
    public void onRemove() {//移除时，恢复抽牌
        AbstractDungeon.player.gameHandSize -= this.amount;
    }

    @Override
    public void atStartOfTurn(){
        if(amount2 != 0){
            AbstractDungeon.player.gameHandSize -= this.amount;
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
       this.flash();
       if(this.amount2 ==0){
           this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
       }else {
           AbstractDungeon.player.gameHandSize += this.amount;
       }

    }

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

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.drawIdOffset ++;
        this.amount2 --;
    }

    @Override
    public void updateDescription() {
        if (this.amount < 0) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
            this.type = AbstractPower.PowerType.DEBUFF;
            loadRegion("draw2");
        } else {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
            this.type = AbstractPower.PowerType.BUFF;
            loadRegion("draw");
        }
    }
}
    /*
    在AbstractPlayer中记载
      public void applyStartOfTurnOrbs() {
    if (!this.orbs.isEmpty()) {
      for (AbstractOrb o : this.orbs)
        o.onStartOfTurn();
      if (hasRelic("Cables") && !(this.orbs.get(0) instanceof EmptyOrbSlot))
        ((AbstractOrb)this.orbs.get(0)).onStartOfTurn();
    }
  }
    在AbstractRoom中记载
     public void update() {
         AbstractDungeon.player.applyStartOfTurnPowers();
         AbstractDungeon.player.applyStartOfTurnOrbs();
     }

     游戏中，NormalStressOrb先获得-的DrawNextTurn；
     然后抽牌，此时的抽牌似乎不受到这回合开始获得的-的DrawNextTurn影响；
     这之后，也只会失去上回合的-的DrawNextTurn；
     */