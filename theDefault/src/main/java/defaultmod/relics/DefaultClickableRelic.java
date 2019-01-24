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
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import defaultmod.DefaultMod;

public class DefaultClickableRelic extends CustomRelic implements ClickableRelic { // You must implement things you want to use from StSlib
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     * StSLib for Clickable Relics
     *
     * At the start of each combat, gain 1 strenght (i.e. Varja)
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("DefaultClickableRelic");
    public static final String IMG = "defaultModResources/images/relics/default_clickable_relic.png";
    public static final String OUTLINE = "defaultModResources/images/relics/outline/default_clickable_relic.png";

    private boolean usedThisTurn = false; // You can also have a relic be only usable once per combat. Check out Hubris for more examples, including other StSlib things.

    public DefaultClickableRelic() {
        super(ID, ImageMaster.loadImage(IMG), new Texture(OUTLINE), RelicTier.COMMON, LandingSound.MAGICAL);

        tips.clear();
        tips.add(new PowerTip(name, description));

    }


    @Override
    public void onRightClick() {// On right click
        if (!isObtained || usedThisTurn) {// If it has been used this turn, or the player doesn't actually have the relic (i.e. it's on display in the shop room)
            return; // Don't do anything.
        }
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) { // Only if you're in combat
            usedThisTurn = true; // Set relic as "Used this turn"
            flash(); // Flash
            stopPulse(); // And stop the pulsing animation (which is started in atPreBattle() below)

            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DESCRIPTIONS[1], 4.0f, 2.0f)); // Player speech bubble saying "YOU ARE MINE!" (See relic strings)
            AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF")); // Sound Effect Action of The Collector Nails
            AbstractDungeon.actionManager.addToBottom(new VFXAction( // Visual Effect Action of the nails applies on a random monster's position.
                    new CollectorCurseEffect(AbstractDungeon.getRandomMonster().hb.cX, AbstractDungeon.getRandomMonster().hb.cY), 2.0F));

            AbstractDungeon.actionManager.addToBottom(new EvokeOrbAction(1)); // Evoke your rightmost orb
        }
    }

    public void atTurnStart() {
        usedThisTurn = false;  // Resets the used this turn. You can remove this whole method to use a relic only once per combat rather than per turn.
        beginLongPulse(); // Pulse while the player can click on it.
    }

    @Override
    public void atPreBattle()
    {
        usedThisTurn = false; // Make sure usedThisTurn is set to false at the start of each combat.
        beginLongPulse();     // Pulse while the player can click on it.
    }

    @Override
    public void onVictory()
    {
        stopPulse(); // Don't keep pulsing past the victory screen/outside of combat.
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
