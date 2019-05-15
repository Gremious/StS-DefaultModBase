package theDefault.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import theDefault.DefaultMod;
import theDefault.util.TextureLoader;

import static theDefault.DefaultMod.makeOrbPath;

public class DefaultOrb extends AbstractOrb {
    
    public static final String ORB_ID = DefaultMod.makeID("DefaultOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;
    
    private static final Texture IMG = TextureLoader.getTexture(makeOrbPath("default_orb.png"));
    
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;
    
    public DefaultOrb() {
        ID = ORB_ID;
        name = orbString.NAME;
        img = IMG;
        evokeAmount = baseEvokeAmount = 1;
        passiveAmount = basePassiveAmount = 3;
        updateDescription();
        angle = MathUtils.random(360.0f);
        channelAnimTimer = 0.5f;
    }
    
    @Override
    public void updateDescription() {
        applyFocus();
        description = DESC[0] + evokeAmount + DESC[1] + passiveAmount + DESC[2];
    }
    
    @Override
    public void applyFocus() {
        passiveAmount = basePassiveAmount;
        evokeAmount = baseEvokeAmount;
    }
    
    @Override
    public void onEvoke() {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(AbstractDungeon.player,
                        DamageInfo.createDamageMatrix(evokeAmount, true, true),
                        DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
        
        AbstractDungeon.actionManager.addToBottom(new SFXAction("TINGSHA"));
    }
    
    @Override
    public void onStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(
                new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.FROST), 0.1f));
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(AbstractDungeon.player, passiveAmount));
    }
    
    @Override
    public void updateAnimation() {
        
        super.updateAnimation();
        angle += Gdx.graphics.getDeltaTime() * 45.0f;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new DarkOrbPassiveEffect(cX, cY));
            vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
    }
    
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, c.a / 2.0f));
        sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, scale, angle, 0, 0, 96, 96, false, false);
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, this.c.a / 2.0f));
        sb.setBlendFunction(770, 1);
        sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, -angle, 0, 0, 96, 96, false, false);
        sb.setBlendFunction(770, 771);
        renderText(sb);
        hb.render(sb);
    }
    
    @Override
    public void triggerEvokeAnimation() {
        AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(cX, cY));
    }
    
    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
    }
    
    @Override
    public AbstractOrb makeCopy() {
        return new DefaultOrb();
    }
}
