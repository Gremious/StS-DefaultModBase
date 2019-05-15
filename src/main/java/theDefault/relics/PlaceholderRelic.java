package theDefault.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theDefault.DefaultMod;
import theDefault.util.TextureLoader;

import static theDefault.DefaultMod.makeRelicOutlinePath;
import static theDefault.DefaultMod.makeRelicPath;

public class PlaceholderRelic extends CustomRelic {
    
    public static final String ID = DefaultMod.makeID("PlaceholderRelic");
    
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));
    
    public PlaceholderRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }
    
    @Override
    public void atBattleStartPreDraw() {
        flash();
    }
    
    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += 1;
    }
    
    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
