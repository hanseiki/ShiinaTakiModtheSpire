package shiinatakimod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import shiinatakimod.orbs.STRStressOrb;
import shiinatakimod.orbs.ShiinaTakiOrb;
import shiinatakimod.orbs.TakiOrbsManager;

public class TakiChannelAction extends AbstractGameAction {
    private ShiinaTakiOrb  orbType;
    private boolean autoEvoke = false;
    public TakiChannelAction(ShiinaTakiOrb orbToChannel) {
        this(orbToChannel, false);
    }

    public TakiChannelAction(ShiinaTakiOrb orbToChannel, boolean autoEvoke) {
        this.orbType = orbToChannel;
        this.autoEvoke = autoEvoke;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.autoEvoke) {
                TakiOrbsManager.channelOrb(this.orbType);
            } else {
                for(AbstractOrb o : AbstractDungeon.player.orbs) {
                    if (o instanceof EmptyOrbSlot) {
                        TakiOrbsManager.channelOrb(this.orbType);
                        break;
                    }
                }
            }

            if (Settings.FAST_MODE) {
                this.isDone = true;
                return;
            }
        }

        this.tickDuration();
    }
}
