package AutobattlerRelic.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.MayhemPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import AutobattlerRelic.AutobattlerRelicMod;
import AutobattlerRelic.util.TextureLoader;

import static AutobattlerRelic.AutobattlerRelicMod.makeRelicOutlinePath;
import static AutobattlerRelic.AutobattlerRelicMod.makeRelicPath;

public class AutobattlingModeRelic extends CustomRelic {
    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * At the start of each combat, gain 1 Strength (i.e. Vajra)
     */

    // ID, images, text.
    public static final String ID = AutobattlerRelicMod.makeID("Autobattling Mode");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic2.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic2.png"));

    public AutobattlingModeRelic() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }
    int autoplay = 5 + (AbstractDungeon.player.energy.energyMaster - 3); // Autoplay is default 5 + any extra energy from relics.



    public void onEquip() {
        AbstractDungeon.player.masterHandSize = 0; // This makes it by default that you will never draw a hand. External draws are allowed.
    }
    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MayhemPower(AbstractDungeon.player, autoplay)));
        // At the beginning of each act I want STR and DEX to decrease by 1 for each act number, to make the mod slightly more challenging.
        // Eventually I want this behavior to be optional/toggleable, but I need to understand the config menu and buttons first.
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new StrengthPower(AbstractDungeon.player, -AbstractDungeon.actNum)));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                new DexterityPower(AbstractDungeon.player, -AbstractDungeon.actNum)));
    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
