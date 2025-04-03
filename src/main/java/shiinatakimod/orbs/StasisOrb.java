package shiinatakimod.orbs;
/*
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import shiinatakimod.ShiinaTakiBasicMod;


public class StasisOrb extends ShiinaTakiOrb {
    public static final String[] DESC;

    private static final OrbStrings orbString;

    public static final String ID = ShiinaTakiBasicMod.makeID("StasisOrb");

    public AbstractCard stasisCard;

    private AbstractGameEffect stasisStartEffect;

    public boolean cardExhausted = false;

    public StasisOrb(AbstractCard card) {
        this(card, (CardGroup)null);
    }

    public StasisOrb(AbstractCard card, CardGroup source) {
        this(card, source, false);
    }

    public StasisOrb(AbstractCard card, CardGroup source, boolean selfStasis) {
        this.stasisCard = card;
        if (this.stasisCard instanceof AbstractGuardianCard)
            ((AbstractGuardianCard)this.stasisCard).belongedOrb = this;
        GuardianMod.logger.info("New Stasis Orb made");
        this.stasisCard.beginGlowing();
        this.name = orbString.NAME + this.stasisCard.name;
        this.channelAnimTimer = 0.5F;
        if (card.isCostModifiedForTurn) {
            this.basePassiveAmount = this.passiveAmount = card.costForTurn + 1;
        } else {
            this.basePassiveAmount = this.passiveAmount = card.cost + 1;
        }
        if (card.freeToPlay() && !selfStasis)
            this.basePassiveAmount = this.passiveAmount = 1;
        if (this.basePassiveAmount < 1)
            this.basePassiveAmount = this.passiveAmount = 1;
        this.baseEvokeAmount = this.basePassiveAmount;
        this.evokeAmount = this.passiveAmount;
        card.targetAngle = 0.0F;
        if (AbstractDungeon.player != null &&
                AbstractDungeon.player.hasRelic("Guardian:StasisUpgradeRelic"))
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new UpgradeSpecificCardAction(card));
        this.stasisCard.tags.add(GuardianMod.STASISGLOW);
        updateDescription();
        initialize(source, selfStasis);
        if (this.stasisCard instanceof InStasisCard)
            ((InStasisCard)this.stasisCard).whenEnteredStasis(this);
    }

    public void applyFocus() {}

    public void updateDescription() {
        applyFocus();
        if (this.stasisCard.hasTag(GuardianMod.VOLATILE)) {
            if (this.passiveAmount > 1) {
                this.description = this.stasisCard.name + DESC[4] + this.passiveAmount + DESC[5];
            } else {
                this.description = this.stasisCard.name + DESC[3];
            }
        } else if (this.passiveAmount > 1) {
            this.description = this.stasisCard.name + DESC[1] + this.passiveAmount + DESC[2];
        } else {
            this.description = this.stasisCard.name + DESC[0];
        }
    }

    public void onStartOfTurn() {
        super.onStartOfTurn();
        if (GuardianMod.stasisDelay) {
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new WaitAction(1.4F));
            GuardianMod.stasisDelay = false;
        }
        if (this.stasisCard instanceof InStasisCard) {
            this.stasisCard.applyPowers();
            ((InStasisCard)this.stasisCard).onStartOfTurn(this);
        }
        if (this.passiveAmount > 0) {
            this.passiveAmount--;
            this.evokeAmount--;
            updateDescription();
        }
        if (this.passiveAmount <= 0)
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new StasisEvokeIfRoomInHandAction(this));
    }

    public void onEvoke() {
        if (this.cardExhausted)
            return;
        if (this.stasisCard instanceof InStasisCard)
            ((InStasisCard)this.stasisCard).onEvoke(this);
        if (this.stasisCard.hasTag(GuardianMod.VOLATILE)) {
            AbstractDungeon.player.limbo.addToTop(this.stasisCard);
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ExhaustSpecificCardAction(this.stasisCard, AbstractDungeon.player.limbo));
        } else {
            if (this.passiveAmount <= 0) {
                if (this.stasisCard.cost > 0) {
                    this.stasisCard.freeToPlayOnce = true;
                } else {
                    this.stasisCard.tags.remove(GuardianMod.STASISGLOW);
                }
            } else {
                this.stasisCard.tags.remove(GuardianMod.STASISGLOW);
            }
            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new ReturnStasisCardToHandAction(this.stasisCard));
            this.stasisCard.superFlash(Color.GOLDENROD);
        }
    }

    public void triggerEvokeAnimation() {}

    public void updateAnimation() {
        super.updateAnimation();
    }

    private void initialize(CardGroup source, boolean selfStasis) {
        if (source != null) {
            source.removeCard(this.stasisCard);
            switch (source.type) {
                case HAND:
                    this.stasisStartEffect = (AbstractGameEffect)new AddCardToStasisEffect(this.stasisCard, this, this.stasisCard.current_x, this.stasisCard.current_y, !selfStasis);
                    break;
                case DRAW_PILE:
                    this.stasisStartEffect = (AbstractGameEffect)new AddCardToStasisEffect(this.stasisCard, this, AbstractDungeon.overlayMenu.combatDeckPanel.current_x + 100.0F * Settings.scale, AbstractDungeon.overlayMenu.combatDeckPanel.current_y + 100.0F * Settings.scale, !selfStasis);
                    break;
                case DISCARD_PILE:
                    this.stasisStartEffect = (AbstractGameEffect)new AddCardToStasisEffect(this.stasisCard, this, AbstractDungeon.overlayMenu.discardPilePanel.current_x - 100.0F * Settings.scale, AbstractDungeon.overlayMenu.discardPilePanel.current_y + 100.0F * Settings.scale, !selfStasis);
                    break;
                case EXHAUST_PILE:
                    this.stasisCard.unfadeOut();
                    this.stasisStartEffect = (AbstractGameEffect)new AddCardToStasisEffect(this.stasisCard, this, AbstractDungeon.overlayMenu.discardPilePanel.current_x - 100.0F * Settings.scale, AbstractDungeon.overlayMenu.exhaustPanel.current_y + 100.0F * Settings.scale, !selfStasis);
                    break;
                default:
                    this.stasisStartEffect = (AbstractGameEffect)new AddCardToStasisEffect(this.stasisCard, this, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, !selfStasis);
                    break;
            }
        } else {
            this.stasisStartEffect = (AbstractGameEffect)new AddCardToStasisEffect(this.stasisCard, this, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, !selfStasis);
        }
        AbstractDungeon.effectsQueue.add(this.stasisStartEffect);
        this.stasisCard.retain = false;
    }

    public void update() {
        super.update();
        if (this.stasisStartEffect == null || this.stasisStartEffect.isDone) {
            this.stasisCard.target_x = this.tX;
            this.stasisCard.target_y = this.tY;
            this.stasisCard.applyPowers();
            if (this.hb.hovered) {
                this.stasisCard.targetDrawScale = 1.0F;
            } else {
                this.stasisCard.targetDrawScale = GuardianMod.stasisCardRenderScale.floatValue();
            }
        }
        this.stasisCard.update();
    }

    public void render(SpriteBatch sb) {
        if (!this.hb.hovered && (this.stasisStartEffect == null || this.stasisStartEffect.isDone))
            renderActual(sb);
    }

    public void renderActual(SpriteBatch sb) {
        this.stasisCard.render(sb);
        if (!this.hb.hovered)
            renderText(sb);
        this.hb.render(sb);
    }

    public void renderPreview(SpriteBatch sb) {
        if (this.hb.hovered && (this.stasisStartEffect == null || this.stasisStartEffect.isDone))
            renderActual(sb);
    }

    public void playChannelSFX() {}

    public AbstractOrb makeCopy() {
        StasisOrb so = new StasisOrb(this.stasisCard);
        so.passiveAmount = so.basePassiveAmount = this.passiveAmount;
        so.evokeAmount = so.baseEvokeAmount = this.evokeAmount;
        return so;
    }

    static {
        orbString = CardCrawlGame.languagePack.getOrbString(ID);
        DESC = orbString.DESCRIPTION;
    }
}

 */


