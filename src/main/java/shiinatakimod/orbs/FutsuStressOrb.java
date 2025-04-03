package shiinatakimod.orbs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import shiinatakimod.ShiinaTakiBasicMod;
import shiinatakimod.powers.DrawNextTurnPower;
import shiinatakimod.powers.EnergizedNextTurnPower;

public class FutsuStressOrb extends ShiinaTakiOrb{

    public static final String ID = ShiinaTakiBasicMod.makeID("FutsuStressOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ID);
    private static final String NAME = orbString.NAME;
    private static final String[] DESC = orbString.DESCRIPTION;
    private static final String IMG_PATH ="";
    private final AbstractPlayer p = AbstractDungeon.player;

    public FutsuStressOrb(){
        super(ID, NAME, DESC,IMG_PATH,
                0,0);

    }

    public FutsuStressOrb(int passiveAmount, int evokeAmount){
        super(ID, NAME, DESC,IMG_PATH,
                0,passiveAmount,0,evokeAmount);

    }


    public void onStartOfTurn() {
        this.passiveAmount ++;//回合开始时计数器+1
        if(this.passiveAmount > 2){//计数器为0时
            this.onCount();//触发计数器的特效
        };
    }

    public void onCount(){//计数器触发时，弃1张牌，下回合减evokeAmount抽牌
       /*
       AbstractDungeon.actionManager.addToBottom(
                new DiscardAction(
                        p,
                        p,
                        1,
                        false
                )
        );

        */
        if(this.evokeAmount>0){
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                            p,
                            p,
                            new DrawNextTurnPower(p,-this.evokeAmount),
                            -this.evokeAmount
                    )
            );
        }
        this.evokeAmount ++;//evoke升级
        this.passiveAmount = this.basePassiveAmount;//计数器归零

    }

    public void onEvoke(){//激发时，减少1能量，下回合减evokeAmount能量
        /*
        AbstractDungeon.actionManager.addToBottom(
                new LoseEnergyAction(1)
        );

         */

        if(this.evokeAmount>0){
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                            p,
                            p,
                            new EnergizedNextTurnPower(p,-this.evokeAmount),
                            -this.evokeAmount
                    )
            );
        }


    }

    @Override
    public void updateDescription() {
        if (DESC == null || DESC.length < 4) {
            this.description = "错误：Invalid description or length is too short: " + DESC.length; // 默认描述
            return;
        }

        this.description = DESC[0] + this.passiveAmount + DESC[1] +this.evokeAmount + DESC[2]
                 + DESC[3] + this.evokeAmount +  DESC[4] ;

    }

    // 创建一个 Orb 的副本（抽象方法，子类必须实现）
    @Override
    public AbstractOrb makeCopy() {
        return new FutsuStressOrb();
    }

    // 渲染 Orb（抽象方法，子类必须实现）
    @Override
    public void render(SpriteBatch spriteBatch) {
        if (this.img != null) {
            spriteBatch.setColor(this.c);
            spriteBatch.draw(this.img, this.cX - (float)this.img.getWidth() / 2.0F + this.bobEffect.y / 4.0F, this.cY - (float)this.img.getHeight() / 2.0F + this.bobEffect.y / 4.0F, (float)this.img.getWidth() / 2.0F, (float)this.img.getHeight() / 2.0F, (float)this.img.getWidth(), (float)this.img.getHeight(), this.scale, this.scale, 0.0F, 0, 0, this.img.getWidth(), this.img.getHeight(), false, false);
        }

        this.renderText(spriteBatch);
        this.hb.render(spriteBatch);

    }

    // 播放 Orb 的通道音效（抽象方法，子类必须实现）
    @Override
    public void playChannelSFX() {

    }

}
