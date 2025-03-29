package shiinatakimod.characters;

import basemod.BaseMod;
import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.Defend_Blue;
import com.megacrit.cardcrawl.cards.green.Neutralize;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.BurningBlood;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import shiinatakimod.orbs.ShiinaTakiOrb;

import java.util.ArrayList;
import java.util.Collections;

import static shiinatakimod.ShiinaTakiBasicMod.*;

public class ShiinaTakiCharacter extends CustomPlayer {
    //Stats
    public static final int ENERGY_PER_TURN = 3;
    public static final int MAX_HP = 70;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 9;

    //Strings
    private static final String ID = makeID("ShiinaTakiCharacter");// 角色的唯一标识符，应与 CharacterStrings.json 文件中的 ID 一致 //This should match whatever you have in the CharacterStrings.json file
    private static String[] getNames() { return CardCrawlGame.languagePack.getCharacterString(ID).NAMES; }//获取角色名称
    private static String[] getText() { return CardCrawlGame.languagePack.getCharacterString(ID).TEXT; }// 获取角色文本

    //This static class is necessary to avoid certain quirks of Java classloading when registering the character.
    // 这个静态类用于避免在注册角色时 Java 类加载的某些问题。
    public static class Meta {
        //These are used to identify your character, as well as your character's card color.
        //Library color is basically the same as card color, but you need both because that's how the game was made.
        // 这些枚举用于标识你的角色，以及角色的卡牌颜色。
        // Library color 基本上与卡牌颜色相同，但由于游戏的设计，你需要同时定义两者。
        @SpireEnum
        public static PlayerClass TAKI_CHARACTER;// 角色类枚举
        @SpireEnum(name = "TAKI_Magenta_COLOR") // 这两个必须匹配。将其更改为你角色的唯一值。// These two MUST match. Change it to something unique for your character.
        public static AbstractCard.CardColor TAKI_CARD_COLOR;// 卡牌颜色
        @SpireEnum(name = "TAKI_Magenta_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType TAKI_LIBRARY_COLOR;// 卡牌库颜色

        //Character select images
        // 角色选择界面的图片
        private static final String CHAR_SELECT_BUTTON = characterPath("select/button.png");
        private static final String CHAR_SELECT_PORTRAIT = characterPath("select/portrait.png");

        //Character card images
        // 角色卡牌背景图片
        private static final String BG_ATTACK = characterPath("cardback/bg_attack.png");
        private static final String BG_ATTACK_P = characterPath("cardback/bg_attack_p.png");
        private static final String BG_SKILL = characterPath("cardback/bg_skill.png");
        private static final String BG_SKILL_P = characterPath("cardback/bg_skill_p.png");
        private static final String BG_POWER = characterPath("cardback/bg_power.png");
        private static final String BG_POWER_P = characterPath("cardback/bg_power_p.png");
        private static final String ENERGY_ORB = characterPath("cardback/energy_orb.png");
        private static final String ENERGY_ORB_P = characterPath("cardback/energy_orb_p.png");
        private static final String SMALL_ORB = characterPath("cardback/small_orb.png");

        //This is used to color *some* images, but NOT the actual cards. For that, edit the images in the cardback folder!
        // 用于着色部分图片的颜色，但不是实际的卡牌颜色。要更改卡牌颜色，请编辑 cardback 文件夹中的图片！
        private static final Color cardColor = new Color(128f/255f, 128f/255f, 128f/255f, 1f);

        //Methods that will be used in the main mod file
        //这些方法将在主 mod 文件中用于执行卡片颜色和字符类与 BaseMod 的实际注册。它们不需要修改。
        public static void registerColor() {
            BaseMod.addColor(TAKI_CARD_COLOR, cardColor,
                    BG_ATTACK, BG_SKILL, BG_POWER, ENERGY_ORB,
                    BG_ATTACK_P, BG_SKILL_P, BG_POWER_P, ENERGY_ORB_P,
                    SMALL_ORB);
        }

        public static void registerCharacter() {
            BaseMod.addCharacter(new ShiinaTakiCharacter(), CHAR_SELECT_BUTTON, CHAR_SELECT_PORTRAIT);
        }
    }


    //In-game images
    // 游戏内图片
    private static final String SHOULDER_1 = characterPath("shoulder.png"); //Shoulder 1 and 2 are used at rest sites.
    private static final String SHOULDER_2 = characterPath("shoulder2.png");
    private static final String CORPSE = characterPath("corpse.png"); //Corpse is when you die.

    //Textures used for the energy orb
    // 能量球的纹理
    private static final String[] orbTextures = {
            characterPath("energyorb/layer1.png"), //When you have energy
            characterPath("energyorb/layer2.png"),
            characterPath("energyorb/layer3.png"),
            characterPath("energyorb/layer4.png"),
            characterPath("energyorb/layer5.png"),
            characterPath("energyorb/cover.png"), //"container"
            characterPath("energyorb/layer1d.png"), //When you don't have energy
            characterPath("energyorb/layer2d.png"),
            characterPath("energyorb/layer3d.png"),
            characterPath("energyorb/layer4d.png"),
            characterPath("energyorb/layer5d.png")
    };

    //Speeds at which each layer of the energy orb texture rotates. Negative is backwards.
    // 能量球每一层纹理的旋转速度。负值表示反向旋转。
    private static final float[] layerSpeeds = new float[] {
            -20.0F,
            20.0F,
            -40.0F,
            40.0F,
            360.0F
    };


    //Actual character class code below this point
    // 实际角色类的代码从这里开始
    public ShiinaTakiCharacter() {
        super(getNames()[0], Meta.TAKI_CHARACTER,
                new CustomEnergyOrb(orbTextures, characterPath("energyorb/vfx.png"), layerSpeeds),// 能量球 //Energy Orb
                new SpriterAnimation(characterPath("animation/default.scml"))); // 角色动画 //Animation

        initializeClass(null,
                SHOULDER_2,
                SHOULDER_1,
                CORPSE,
                getLoadout(),
                20.0F, -20.0F, 200.0F, 250.0F, // 角色的碰撞框。x y 位置，然后是宽度和高度。 //Character hitbox. x y position, then width and height.
                new EnergyManager(ENERGY_PER_TURN));

        //Location for text bubbles. You can adjust it as necessary later. For most characters, these values are fine.
        // 对话框的位置。你可以根据需要调整这些值。对于大多数角色，这些值是合适的。
        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        //List of IDs of cards for your starting deck.
        //If you want multiple of the same card, you have to add it multiple times.
        // 初始卡组的卡牌 ID 列表。
        // 如果你想要多张相同的卡牌，需要多次添加。
        for(int x = 0; x<3; x++) {
            retVal.add(makeID("Strike"));
        }
        retVal.add(makeID("INTUnderStress"));
        retVal.add(makeID("MamakoStressStrike"));
        for(int x = 0; x<3; x++) {
            retVal.add(makeID("Defend"));
        }
        retVal.add(makeID("DTM"));
        retVal.add(makeID("KizuiteteWatashiMoRiyoShiteta"));
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        //IDs of starting relics. You can have multiple, but one is recommended.
        // 初始遗物的 ID。你可以有多个遗物，但建议只使用一个。
        retVal.add(BurningBlood.ID);

        return retVal;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        //This card is used for the Gremlin card matching game.
        //It should be a non-strike non-defend starter card, but it doesn't have to be.
        return new Strike_Red();
    }

    /*- Below this is methods that you should *probably* adjust, but don't have to. -*/

    @Override
    public int getAscensionMaxHPLoss() {
        return 4; //Max hp reduction at ascension 14+
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        //These attack effects will be used when you attack the heart.
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    private final Color cardRenderColor = Color.LIGHT_GRAY.cpy(); //Used for some vfx on moving cards (sometimes) (maybe)
    private final Color cardTrailColor = Color.LIGHT_GRAY.cpy(); //Used for card trail vfx during gameplay.
    private final Color slashAttackColor = Color.LIGHT_GRAY.cpy(); //Used for a screen tint effect when you attack the heart.
    @Override
    public Color getCardRenderColor() {
        return cardRenderColor;
    }

    @Override
    public Color getCardTrailColor() {
        return cardTrailColor;
    }

    @Override
    public Color getSlashAttackColor() {
        return slashAttackColor;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        //Font used to display your current energy.
        //energyNumFontRed, Blue, Green, and Purple are used by the basegame characters.
        //It is possible to make your own, but not convenient.
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        //This occurs when you click the character's button in the character select screen.
        //See SoundMaster for a full list of existing sound effects, or look at BaseMod's wiki for adding custom audio.
        CardCrawlGame.sound.playA("ATTACK_DAGGER_2", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        //Similar to doCharSelectScreenSelectEffect, but used for the Custom mode screen. No shaking.
        return "ATTACK_DAGGER_2";
    }

    //Don't adjust these four directly, adjust the contents of the CharacterStrings.json file.
    @Override
    public String getLocalizedCharacterName() {
        return getNames()[0];
    }
    @Override
    public String getTitle(PlayerClass playerClass) {
        return getNames()[1];
    }
    @Override
    public String getSpireHeartText() {
        return getText()[1];
    }
    @Override
    public String getVampireText() {
        return getText()[2]; //Generally, the only difference in this text is how the vampires refer to the player.
    }

    /*- You shouldn't need to edit any of the following methods. -*/

    //This is used to display the character's information on the character selection screen.
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getNames()[0], getText()[0],
                MAX_HP, MAX_HP,  ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Meta.TAKI_CARD_COLOR;
    }

    @Override
    public AbstractPlayer newInstance() {
        //Makes a new instance of your character class.
        return new ShiinaTakiCharacter();
    }

    public void evokeOrb() {
        // 检查球体列表不为空且第一个槽位不是空球体槽
        if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
            ((AbstractOrb)this.orbs.get(0)).onEvoke();  // 触发第一个球体的激发效果
            // 触发takiorb的onRemove()
            if(this.orbs.get(0) instanceof ShiinaTakiOrb){
                ((ShiinaTakiOrb)this.orbs.get(0)).onRemove();
            }

            AbstractOrb orbSlot = new EmptyOrbSlot();  // 创建一个新的空球体槽

            // 将所有球体向前移动一个位置
            for(int i = 1; i < this.orbs.size(); ++i) {
                Collections.swap(this.orbs, i, i - 1);
            }

            this.orbs.set(this.orbs.size() - 1, orbSlot);  // 将最后一个位置设为空球体槽

            // 更新所有球体的槽位信息
            for(int i = 0; i < this.orbs.size(); ++i) {
                ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
            }
        }
    }
    @Override
    public void removeNextOrb() {
        // 检查球体列表不为空且第一个槽位不是空球体槽
        if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
            // 触发takiorb的onRemove()
            if(this.orbs.get(0) instanceof ShiinaTakiOrb){
                ((ShiinaTakiOrb)this.orbs.get(0)).onRemove();
            }
            // 创建一个新的空球体槽，位置与第一个球体相同
            AbstractOrb orbSlot = new EmptyOrbSlot(((AbstractOrb)this.orbs.get(0)).cX, ((AbstractOrb)this.orbs.get(0)).cY);

            // 将所有球体向前移动一个位置
            for(int i = 1; i < this.orbs.size(); ++i) {
                Collections.swap(this.orbs, i, i - 1);
            }

            this.orbs.set(this.orbs.size() - 1, orbSlot);  // 将最后一个位置设为空球体槽

            // 更新所有球体的槽位信息
            for(int i = 0; i < this.orbs.size(); ++i) {
                ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
            }
        }
    }

    @Override
    public void channelOrb(AbstractOrb orbToSet) {
        if (this.maxOrbs <= 0) {  // 如果没有球体槽位
            // 显示提示气泡
            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, MSG[4], true));
        } else {
            if (this.maxOrbs > 0) {
                // 如果有"黑暗核心"遗物且要生成的球体不是黑暗球体，强制生成黑暗球体
                if (this.hasRelic("Dark Core") && !(orbToSet instanceof Dark)) {
                    orbToSet = new Dark();
                }

                int index = -1;  // 初始化空槽位索引

                // 查找第一个空槽位
                for(int i = 0; i < this.orbs.size(); ++i) {
                    if (this.orbs.get(i) instanceof EmptyOrbSlot) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {  // 如果找到空槽位
                    // 设置新球体的位置
                    orbToSet.cX = ((AbstractOrb)this.orbs.get(index)).cX;
                    orbToSet.cY = ((AbstractOrb)this.orbs.get(index)).cY;
                    this.orbs.set(index, orbToSet);  // 放置新球体
                    ((AbstractOrb)this.orbs.get(index)).setSlot(index, this.maxOrbs);  // 更新槽位信息
                    orbToSet.playChannelSFX();  // 播放生成音效

                    // 触发所有能力(被动效果)的onChannel方法
                    for(AbstractPower p : this.powers) {
                        p.onChannel(orbToSet);
                    }
                    if(orbToSet instanceof ShiinaTakiOrb){
                        ((ShiinaTakiOrb) orbToSet).onChannel();
                    }

                    // 记录本次战斗和本回合生成的球体
                    AbstractDungeon.actionManager.orbsChanneledThisCombat.add(orbToSet);
                    AbstractDungeon.actionManager.orbsChanneledThisTurn.add(orbToSet);
                    int plasmaCount = 0;

                    // 统计本回合生成的等离子球数量
                    for(AbstractOrb o : AbstractDungeon.actionManager.orbsChanneledThisTurn) {
                        if (o instanceof Plasma) {
                            ++plasmaCount;
                        }
                    }

                    // 如果本回合生成了9个等离子球，解锁成就
                    if (plasmaCount == 9) {
                        UnlockTracker.unlockAchievement("NEON");
                    }

                    orbToSet.applyFocus();  // 应用焦点效果
                } else {  // 如果没有空槽位
                    // 添加一系列动作：动画、激发球体、生成新球体
                    AbstractDungeon.actionManager.addToTop(new ChannelAction(orbToSet));
                    AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
                    AbstractDungeon.actionManager.addToTop(new AnimateOrbAction(1));
                }
            }
        }
    }

    @Override
    public void increaseMaxOrbSlots(int amount, boolean playSfx) {
        if (this.maxOrbs == 20) {  // 如果已经达到最大槽位数(10)，此处修改为20
            // 显示提示气泡
            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, MSG[3], true));
        } else {
            if (playSfx) {  // 如果需要播放音效
                CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1F);
            }

            this.maxOrbs += amount;  // 增加最大槽位数

            // 添加新的空槽位
            for(int i = 0; i < amount; ++i) {
                this.orbs.add(new EmptyOrbSlot());
            }

            // 更新所有球体的槽位信息
            for(int i = 0; i < this.orbs.size(); ++i) {
                ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
            }
        }
    }


}
