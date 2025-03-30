package shiinatakimod.cards.uncommon;


import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import shiinatakimod.cards.BaseCard;
import shiinatakimod.cards.basic.Strike;
import shiinatakimod.characters.ShiinaTakiCharacter;
import shiinatakimod.powers.DEXConvertOrbPower;
import shiinatakimod.powers.STRConvertOrbPower;
import shiinatakimod.util.CardStats;

public class DEXUnderStress
        extends BaseCard {
    public static final String ID = makeID(DEXUnderStress.class.getSimpleName());
    private static final CardStats info = new CardStats(
            ShiinaTakiCharacter.Meta.TAKI_CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.POWER, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DEX = 1;
    private static final int UPG_DEX = 0;

    public DEXUnderStress() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setMagic(DEX, UPG_DEX); //Sets the card's damage and how much it changes when upgraded.
        setInnate(false,true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        /*
        addToBot(
                new ApplyPowerAction(p,p,new DexterityPower(p,magicNumber),magicNumber)
        );
         */
        addToBot(
                new ApplyPowerAction(p,p,new DEXConvertOrbPower(p,magicNumber),magicNumber)
        );

    }
}