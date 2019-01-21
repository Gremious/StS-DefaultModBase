package defaultmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import defaultmod.DefaultMod;

public class DefaultClickableRelic extends CustomRelic implements ClickableRelic {
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     * StSLib for Clickable Relics
     *
     * At the start of each combat, gain 1 strenght (i.e. Varja)
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("DefaultClickableRelic");
    public static final String IMG = DefaultMod.makePath(DefaultMod.DEFAULT_CLICKABLE_RELIC);
    public static final String OUTLINE = DefaultMod.makePath(DefaultMod.DEFAULT_CLICKABLE_RELIC_OUTLINE);

    private boolean usedThisTurn = false;

    public DefaultClickableRelic() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.COMMON, LandingSound.MAGICAL);

        tips.clear();
        tips.add(new PowerTip(name, description));

    }


    @Override
    public void onRightClick() {
        if (!isObtained || usedThisTurn) {
            return;
        }
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            usedThisTurn = true;
            this.flash();
            stopPulse();

            AbstractDungeon.actionManager.addToBottom(new SFXAction("TINGSHA"));

            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DESCRIPTIONS[1], 4.0f, 2.0f));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(AbstractDungeon.getRandomMonster().hb.cX, AbstractDungeon.getRandomMonster().hb.cY), 2.0F));

            AbstractDungeon.actionManager.addToBottom(new EvokeOrbAction(1));
        }
    }

    public void atTurnStart() {
        usedThisTurn = false;
    }

    @Override
    public void atPreBattle()
    {
        usedThisTurn = false;
        beginLongPulse();
    }

    @Override
    public void onVictory()
    {
        stopPulse();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    // Which relic to return on making a copy of this relic.
    @Override
    public AbstractRelic makeCopy() {
        return new DefaultClickableRelic();
    }
}
