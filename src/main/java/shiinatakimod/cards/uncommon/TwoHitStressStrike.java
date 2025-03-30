package shiinatakimod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import shiinatakimod.cards.BaseCard;
import shiinatakimod.characters.ShiinaTakiCharacter;
import shiinatakimod.orbs.FutsuStressOrb;
import shiinatakimod.util.CardStats;

public class TwoHitStressStrike
        extends BaseCard {
    public static final String ID = makeID(TwoHitStressStrike.class.getSimpleName());
    private static final CardStats info = new CardStats(
            ShiinaTakiCharacter.Meta.TAKI_CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 12;
    private static final int UPG_DAMAGE = 2;

    public TwoHitStressStrike() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.

        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new DamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                DamageInfo.DamageType.NORMAL
                        ),
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL
                )
        );
        addToBot(
                new DamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                DamageInfo.DamageType.NORMAL
                        ),
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL
                )
        );
        for(int i=0; i<this.cost ; i++){
            addToBot(
                    new ChannelAction(
                            new FutsuStressOrb()
                    )
            );
        }
    }
}
