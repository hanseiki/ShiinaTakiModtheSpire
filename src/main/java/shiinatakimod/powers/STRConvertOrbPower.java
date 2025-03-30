package shiinatakimod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shiinatakimod.actions.ConvertOrbAction;
import shiinatakimod.orbs.FutsuStressOrb;
import shiinatakimod.orbs.STRStressOrb;

import static shiinatakimod.ShiinaTakiBasicMod.makeID;

public class STRConvertOrbPower
        extends BasePower {
    public static final String POWER_ID = makeID("STRConvertOrb");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public STRConvertOrbPower(AbstractPlayer owner, int magicNumber) {
        super(POWER_ID, TYPE, TURN_BASED,owner, magicNumber);
    }


    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if(card.type == AbstractCard.CardType.ATTACK){
            // 不再直接创建 STRStressOrb，而是触发转换逻辑
            for (int i = 0; i < this.amount; ++i) {
                addToBot(new ConvertOrbAction(FutsuStressOrb.class, STRStressOrb.class)); // 传递目标 Orb 类型
            }
        }
    }


    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] ;
    }
}