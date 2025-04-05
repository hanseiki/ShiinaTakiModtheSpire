package shiinatakimod.relics;

import basemod.abstracts.CustomRelic;  // BaseMod 提供的自定义遗物基类
import basemod.helpers.RelicType;      // 遗物类型枚举
import shiinatakimod.util.GeneralUtils; // 自定义工具类
import shiinatakimod.util.TextureLoader; // 自定义纹理加载工具
import com.megacrit.cardcrawl.cards.AbstractCard; // 卡牌相关类
import com.megacrit.cardcrawl.core.CardCrawlGame; // 游戏核心类
import com.megacrit.cardcrawl.helpers.ImageMaster; // 游戏图像管理类
import com.megacrit.cardcrawl.localization.RelicStrings; // 遗物本地化字符串

import static shiinatakimod.ShiinaTakiBasicMod.relicPath;// 静态导入自定义mod中的路径工具
// 定义抽象的基础遗物类，继承自 CustomRelic
public abstract class BaseRelic extends CustomRelic {
    public AbstractCard.CardColor pool = null;    // 遗物所属的卡牌颜色池，null表示共享遗物
    public RelicType relicType = RelicType.SHARED;    // 遗物类型，默认为共享类型
    protected String imageName;    // 图像文件名

    //for character specific relics
    // 构造函数1：用于角色特定遗物
    // 参数：id-遗物ID, imageName-图像文件名, pool-卡牌颜色池, tier-遗物等级, sfx-落地音效
    public BaseRelic(String id, String imageName, AbstractCard.CardColor pool, RelicTier tier, LandingSound sfx) {
        this(id, imageName, tier, sfx);

        setPool(pool);
    }
    // 构造函数2：简化版构造函数，自动处理图像文件名
    // 参数：id-遗物ID, tier-遗物等级, sfx-落地音效
    public BaseRelic(String id, RelicTier tier, LandingSound sfx) {
        this(id, GeneralUtils.removePrefix(id), tier, sfx);
    }

    //To use a basegame relic image, just pass in the imagename used by a basegame relic instead of the ID.
    //eg. "calendar.png"
    // 构造函数3：主构造函数
    // 参数：id-遗物ID, imageName-图像文件名, tier-遗物等级, sfx-落地音效
    public BaseRelic(String id, String imageName, RelicTier tier, LandingSound sfx) {
        super(testStrings(id), notPng(imageName) ? "" : imageName, tier, sfx); // 调用父类构造函数，传入测试过的ID字符串和图像文件名(如果不是.png则传空字符串)

        this.imageName = imageName;
        if (notPng(imageName)) {
            loadTexture();
        }
    }

    protected void loadTexture() {
        this.img = TextureLoader.getTextureNull(relicPath(imageName + ".png"), true);// 尝试从指定路径加载纹理
        if (img != null) {// 如果主纹理加载成功，尝试加载轮廓纹理
            outlineImg = TextureLoader.getTextureNull(relicPath(imageName + "Outline.png"), true);
            if (outlineImg == null)// 如果轮廓纹理加载失败，使用主纹理作为轮廓
                outlineImg = img;
        }
        else {// 如果纹理加载失败，使用默认的"Derp Rock"图像作为回退
            ImageMaster.loadRelicImg("Derp Rock", "derpRock.png");
            this.img = ImageMaster.getRelicImg("Derp Rock");
            this.outlineImg = ImageMaster.getRelicOutlineImg("Derp Rock");
        }
    }

    // 加载大图的方法(用于遗物详情查看)
    @Override
    public void loadLargeImg() {
        if (notPng(imageName)) {// 如果图像名不是.png且大图未加载，则加载大图
            if (largeImg == null) {
                this.largeImg = ImageMaster.loadImage(relicPath("large/" + imageName + ".png"));
            }
        }
        else {// 否则调用父类方法
            super.loadLargeImg();
        }
    }

    // 设置遗物池的方法
    private void setPool(AbstractCard.CardColor pool) {
        switch (pool) { //Basegame pools are handled differently
            case RED:
                relicType = RelicType.RED;
                break;
            case GREEN:
                relicType = RelicType.GREEN;
                break;
            case BLUE:
                relicType = RelicType.BLUE;
                break;
            case PURPLE:
                relicType = RelicType.PURPLE;
                break;
            default:
                this.pool = pool;
                break;
        }
    }

    /**
     * Checks whether relic has localization set up correctly and gives a more accurate error message if it does not
     * @param ID the relic's ID
     * @return the relic's ID, to allow use in super constructor invocation
     */

    /**
     * 检查遗物本地化字符串是否正确设置，如果没有则提供更准确的错误信息
     * @param ID 遗物的ID
     * @return 遗物的ID，允许在super构造函数调用中使用
     */
    private static String testStrings(String ID) {
        RelicStrings text = CardCrawlGame.languagePack.getRelicStrings(ID);// 获取遗物的本地化字符串
        if (text == null) {// 如果本地化字符串不存在，抛出运行时异常并提示如何修复
            throw new RuntimeException("The \"" + ID + "\" relic does not have associated text. Make sure " +
                    "there's no issue with the RelicStrings.json file, and that the ID in the json file matches the " +
                    "relic's ID. It should look like \"${modID}:" + GeneralUtils.removePrefix(ID) + "\".");
        }
        return ID;
    }

    // 辅助方法：检查字符串是否不以.png结尾
    private static boolean notPng(String name) {
        return !name.endsWith(".png");
    }
}