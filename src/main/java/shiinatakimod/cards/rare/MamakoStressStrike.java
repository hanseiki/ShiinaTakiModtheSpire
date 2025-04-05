package shiinatakimod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import shiinatakimod.cards.BaseCard;
import shiinatakimod.characters.ShiinaTakiCharacter;
import shiinatakimod.orbs.FutsuStressOrb;
import shiinatakimod.util.CardStats;

public class MamakoStressStrike
        extends BaseCard {
    public static final String ID = makeID(MamakoStressStrike.class.getSimpleName());
    private static final CardStats info = new CardStats(
            ShiinaTakiCharacter.Meta.TAKI_CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            3 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 10;
    private static final int MAGIC = 2;

    public MamakoStressStrike() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new DamageAllEnemiesAction(
                        p,
                        damage,
                        DamageInfo.DamageType.NORMAL,
                        AbstractGameAction.AttackEffect.NONE
                )
        );
        addToBot(
                new DamageAllEnemiesAction(
                        p,
                        damage,
                        DamageInfo.DamageType.NORMAL,
                        AbstractGameAction.AttackEffect.NONE
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


    private int countOrbs() {
        int count = 0;
        for(AbstractOrb o : AbstractDungeon.player.orbs){
            if(isStress(o)){
                ++count;
            }
        }
        return count;
    }

    private boolean isStress(AbstractOrb o) {
        return !(o instanceof EmptyOrbSlot);
    }

    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += this.magicNumber * countOrbs();
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }
    public void applyPowers() {
        int realBaseDamage = this.baseDamage;
        this.baseDamage += this.magicNumber * countOrbs();
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

}
