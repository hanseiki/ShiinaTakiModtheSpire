package shiinatakimod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static shiinatakimod.ShiinaTakiBasicMod.makeID;

public class BurokkuPower
        extends BasePower {
    public static final String POWER_ID = makeID("Burokku");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.DEBUFF;
    private static final boolean TURN_BASED = false;

    private int metallicizeAmount; // 保存上一次赋予的金属化数值

    public BurokkuPower(AbstractCreature owner,int metallicizeAmount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1);
        this.metallicizeAmount = metallicizeAmount;
    }

    public int getMetallicizeAmount() {
        return metallicizeAmount;
    }


    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
    }
}
