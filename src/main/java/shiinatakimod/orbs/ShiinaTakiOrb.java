package shiinatakimod.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import java.util.HashMap;
import java.util.Map;

public abstract class ShiinaTakiOrb extends AbstractOrb {

    public   String[] DESC;

    private   Map<String, Texture> orbTextures = new HashMap();

    protected int baseOnChannelAmount = 0;
    protected int onChannelAmount = 0;

    protected int baseOnRemoveAmount = 0;
    protected int onRemoveAmount = 0;

    protected int baseOnStartAmount = 0;
    protected int onStartAmount = 0;

    protected int baseOnEndAmount = 0;
    protected int onEndAmount = 0;


    public  ShiinaTakiOrb()
    {
        super();
    }
    public  ShiinaTakiOrb(String ID, String NAME,
                          String[] description, String imgPath,
                          int basePassiveAmount,
                          int baseEvokeAmount)
    {
        this(ID,NAME,description,imgPath,
                basePassiveAmount,basePassiveAmount,
                baseEvokeAmount,baseEvokeAmount,
                0,0,0,0);
    }
    public  ShiinaTakiOrb(String ID, String NAME,
                          String[] description, String imgPath,
                          int basePassiveAmount, int passiveAmount,
                          int baseEvokeAmount, int evokeAmount)
    {
        this(ID,NAME,description,imgPath,
                basePassiveAmount,passiveAmount,
                baseEvokeAmount,evokeAmount,
                0,0,0,0);
    }

    public ShiinaTakiOrb(
            String ID, String NAME,
            String[] description, String imgPath,
            int basePassiveAmount, int passiveAmount,
            int baseEvokeAmount, int evokeAmount,
            int baseOnChannelAmount, int baseOnRemoveAmount,
            int baseOnStartAmount, int baseOnEndAmount
            ) {
        this.ID = ID;
        this.name = NAME;
        this.DESC = description;

        this.basePassiveAmount = basePassiveAmount;
        this.passiveAmount = passiveAmount;

        this.baseEvokeAmount = baseEvokeAmount;
        this.evokeAmount = evokeAmount;

        this.baseOnStartAmount = baseOnStartAmount;
        this.onStartAmount = this.baseOnStartAmount;

        this.baseOnEndAmount = baseOnEndAmount;
        this.onEndAmount = this.baseOnEndAmount;

        this.baseOnChannelAmount = baseOnChannelAmount;
        this.onChannelAmount = this.baseOnChannelAmount;

        this.baseOnRemoveAmount = baseOnRemoveAmount;
        this.onRemoveAmount = this.baseOnRemoveAmount;


        if (imgPath != null) {
            this.img = (Texture)orbTextures.get(imgPath);
            if (this.img == null) {
                this.img = ImageMaster.loadImage(imgPath);
                orbTextures.put(imgPath, this.img);
            }
        }
        this.updateDescription();
    }

    public void onChannel() {

    }

    public void onRemove() {

    }


    // 更新 Orb 的描述文本（抽象方法，子类必须实现）
    @Override
    public void updateDescription() {
        if (DESC == null || DESC.length < 12) {
            this.description = "错误：Invalid description or length is too short: " + DESC.length; // 默认描述
            return;
        }

        this.description = DESC[0] + this.passiveAmount + DESC[1]
                + DESC[2]+ this.evokeAmount + DESC[3]
                + DESC[4]+ this.onStartAmount + DESC[5]
                + DESC[6]+ this.onEndAmount + DESC[7]
                + DESC[8]+ this.onChannelAmount + DESC[9]
                + DESC[10]+ this.onRemoveAmount + DESC[11];
    }

    // 当 Orb 被触发时调用（抽象方法，子类必须实现）
    @Override
    public void onEvoke() {

    }

    // 创建一个 Orb 的副本（抽象方法，子类必须实现）
    @Override
    public AbstractOrb makeCopy() {
        return null;
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

    @Override
    protected void renderText(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
    }

    // 播放 Orb 的通道音效（抽象方法，子类必须实现）
    @Override
    public void playChannelSFX() {

    }
}
