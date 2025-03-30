package shiinatakimod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shiinatakimod.cards.BaseCard;
import shiinatakimod.characters.ShiinaTakiCharacter;
import shiinatakimod.util.CardStats;

public class TakiFingers extends BaseCard {
    public static final String ID = makeID(TakiFingers.class.getSimpleName());
    private static final CardStats info = new CardStats(
            ShiinaTakiCharacter.Meta.TAKI_CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 2;
    private static final int MAGIC = 2;

    public TakiFingers() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setMagic(MAGIC); //Sets the card's damage and how much it changes when upgraded.
        setDamage(DAMAGE);
    }

    @Override
    public void applyPowers() {
        int tmpStrength = 0;
        int tmpVigor = 0;
        int exponent = 0;

        AbstractPower strength = AbstractDungeon.player.getPower("Strength");
        AbstractPower vigor = AbstractDungeon.player.getPower("Vigor");

        if (strength != null){
            tmpStrength = strength.amount;
            strength.amount = 0;//在super.applyPowers();中视为0
        }
        if (vigor != null){
            tmpVigor = vigor.amount;
            vigor.amount = 0;//在super.applyPowers();中视为0
        }

        if(this.upgraded){
            exponent = (int)(( 1 + tmpStrength ) / 2 +( 3 + tmpVigor) / 4);
        }else{
            exponent = (int)(( tmpStrength ) / 2 +( tmpVigor) / 4);
        }

        if(exponent>20){//指数最大为20，防止溢出
            exponent =20;
        }
        this.baseDamage = this.baseMagicNumber * (int) Math.pow(2,exponent);
        super.applyPowers();

        if (strength != null){
            strength.amount = tmpStrength;//恢复正常数值
        }
        if (vigor != null){
            vigor.amount = tmpVigor;//恢复正常数值
        }

        initializeDescription();
    }


    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int tmpStrength = 0;
        int tmpVigor = 0;
        int exponent = 0;

        AbstractPower strength = AbstractDungeon.player.getPower("Strength");
        AbstractPower vigor = AbstractDungeon.player.getPower("Vigor");

        if (strength != null){
            tmpStrength = strength.amount;
            strength.amount = 0;
        }
        if (vigor != null){
            tmpVigor = vigor.amount;
            vigor.amount = 0;//在super.applyPowers();中视为0
        }

        if(this.upgraded){
            exponent = (int)(( 1 + tmpStrength ) / 2 +( 3 + tmpVigor) / 4);
        }else{
            exponent = (int)(( tmpStrength ) / 2 +( tmpVigor) / 4);
        }
        
        if(exponent>20){
            exponent =20;
        }
        this.baseDamage = this.baseMagicNumber * (int) Math.pow(2,exponent);
        super.calculateCardDamage(mo);
        if (strength != null){
            strength.amount = tmpStrength;
        }
        if (vigor != null){
            vigor.amount = tmpVigor;//恢复正常数值
        }

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.applyPowers();
        addToBot(
                new DamageAction(
                        m,
                        new DamageInfo(
                                p,
                                this.damage,
                                DamageInfo.DamageType.NORMAL
                        ),
                        AbstractGameAction.AttackEffect.SLASH_VERTICAL
                )
        );

    }
}
