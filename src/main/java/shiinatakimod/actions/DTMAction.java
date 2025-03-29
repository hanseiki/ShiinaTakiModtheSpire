package shiinatakimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static shiinatakimod.ShiinaTakiBasicMod.makeID;

public class DTMAction extends AbstractGameAction {

    public static final String ID = makeID("DTMAction");

    private static final UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final String[] TEXT = UIStrings.TEXT;

    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;

    public DTMAction(int numberOfCards, boolean optional) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
    }

    public DTMAction(int numberOfCards) {
        this(numberOfCards, false);
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.drawPile.isEmpty() || this.numberOfCards <= 0) {//抽牌堆为空或DTM抽牌0
                this.isDone = true;
                return;
            }
            if (this.player.drawPile.size() <= this.numberOfCards && !this.optional) {//玩抽牌堆剩余数量小于DTM抽牌数量且不可选择
                ArrayList<AbstractCard> cardsToMove = new ArrayList<>();//建立一个将要移动的牌的队列cardsToMove
                for (AbstractCard c : this.player.drawPile.group)//遍历抽牌堆所有牌，并依次赋予变量c
                    cardsToMove.add(c);//将c加入cardsToMove队列：即抽牌堆的牌都进入该队列了
                for (AbstractCard c : cardsToMove) {//遍历cardsToMove所有牌，并依次赋予变量c
                    if (this.player.hand.size() == 10) {//如果手牌数为10
                        this.player.drawPile.moveToDiscardPile(c);//将抽牌堆的c加入弃牌堆
                        this.player.createHandIsFullDialog();//生成“手牌满了”对话框
                        continue;
                    }
                    this.player.drawPile.moveToHand(c, this.player.drawPile);//将抽牌堆的c加入手牌
                }
                this.isDone = true;
                return;
            }
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);//抽牌堆大于DTM或可选择的情况，创建temp牌组
            for (AbstractCard c : this.player.drawPile.group)
                temp.addToTop(c);//将所有抽牌堆的牌放入temp
            temp.sortAlphabetically(true);//按字母排序
            temp.sortByRarityPlusStatusCardType(false);//不按稀有度排序
            if (this.numberOfCards == 1) {//如果抽1张牌
                if (this.optional) {//如果抽1张牌且可以选择
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[0]);
                } else {//如果抽1张牌且不可以选择
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[0], false);
                }
            } else if (this.optional) {//如果抽多于1张牌，且可以选择
                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[1] + this.numberOfCards + TEXT[2]);
            } else {//如果抽多于1张牌，且不可以选择
                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
            }
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (this.player.hand.size() == 10) {
                    this.player.drawPile.moveToDiscardPile(c);
                    this.player.createHandIsFullDialog();
                    continue;
                }
                this.player.drawPile.moveToHand(c, this.player.drawPile);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        this.isDone = true;
    }
}
