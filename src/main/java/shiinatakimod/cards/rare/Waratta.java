package shiinatakimod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import shiinatakimod.cards.BaseCard;
import shiinatakimod.characters.ShiinaTakiCharacter;
import shiinatakimod.orbs.FutsuStressOrb;
import shiinatakimod.util.CardStats;

//说话了
//しゃべった
public class Waratta
        extends BaseCard {
    public static final String ID = makeID(Waratta.class.getSimpleName());
    private static final CardStats info = new CardStats(
            ShiinaTakiCharacter.Meta.TAKI_CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int MAGIC = 1;

    public Waratta() {
        super(ID, info); //Pass the required information to the BaseCard constructor.

        setMagic(MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {//每2个球打4
        int j = 0;
        for (int i = 0; i < p.orbs.size(); i=i+1) {
            if ((p.orbs.get(i) instanceof com.megacrit.cardcrawl.orbs.EmptyOrbSlot)){
                j++;
            }
        }
        for(int i = 3 ; i <= j; i = i + 3){
            addToBot(
                    new ApplyPowerAction(p, p, new EnergizedPower(p,1),1 )
            );
        }

        int k = p.maxOrbs;
        if(j-k ==0){
            addToBot(new ChannelAction(new FutsuStressOrb()));
        }
        if(10*j-9*k >= 0){
            addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p,this.magicNumber),this.magicNumber));
        }
        if(10*j-8*k >= 0){
            addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p,this.magicNumber),this.magicNumber));
        }
        if(10*j-7*k >= 0 && this.upgraded){
            addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p,this.magicNumber),this.magicNumber));
        }



    }
}
