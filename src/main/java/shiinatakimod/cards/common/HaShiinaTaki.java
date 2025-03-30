package shiinatakimod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shiinatakimod.cards.BaseCard;
import shiinatakimod.cards.basic.Strike;
import shiinatakimod.characters.ShiinaTakiCharacter;
import shiinatakimod.util.CardStats;

public class HaShiinaTaki extends BaseCard {
    public static final String ID = makeID(HaShiinaTaki.class.getSimpleName());
    private static final CardStats info = new CardStats(
            ShiinaTakiCharacter.Meta.TAKI_CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.COMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF_AND_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 1;
    private static final int UPG_DAMAGE = 1;
    private static final int BLOCK = 1;
    private static final int UPG_BLOCK = 1;
    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    public HaShiinaTaki() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        this.misc = 1;
        setMagic(MAGIC,UPG_MAGIC);
        setBlock(this.misc);
        setExhaust(true);

    }



    public void applyPowers() {
        this.baseBlock = this.misc;
        super.applyPowers();
        initializeDescription();
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new IncreaseMiscAction(this.uuid, this.misc, this.magicNumber));
        addToBot(
                new GainBlockAction(
                        p,
                        block)
        );
        addToBot(
                new DamageAction(
                        m,
                        new DamageInfo(
                                p,
                                block,
                                DamageInfo.DamageType.NORMAL
                        ),
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL
                )
        );
    }
}
