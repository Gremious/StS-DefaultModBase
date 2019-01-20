package defaultmod.orbs;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import defaultmod.DefaultMod;

public class DefaultOrb extends AbstractOrb {
    public static final String ORB_ID = DefaultMod.makeID("DefaultOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    public DefaultOrb() {
        this.ID = ORB_ID;
        this.img = ImageMaster.loadImage(Conspire.orbImage(ORB_ID));
        this.name = orbString.NAME;
        this.evokeAmount = this.baseEvokeAmount = 2;
        this.passiveAmount = this.basePassiveAmount = 1;
        this.updateDescription();
        this.angle = MathUtils.random(360.0f);
        this.channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription() {
        this.applyFocus();
        this.description = DESC[0] + this.evokeAmount + DESC[1];
    }

    @Override
    public void onEvoke() {
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, this.evokeAmount));
    }

    @Override
    public void applyFocus() {
        this.passiveAmount = this.basePassiveAmount;
        this.evokeAmount = this.baseEvokeAmount;
    }

    @Override
    public void onStartOfTurn() {
        OrbFlareEffect flareEffect = new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.FROST);
        ReflectionHacks.setPrivate(flareEffect, OrbFlareEffect.class, "color", Settings.BLUE_TEXT_COLOR.cpy());
        ReflectionHacks.setPrivate(flareEffect, OrbFlareEffect.class, "color2", Color.SKY.cpy());
        AbstractDungeon.actionManager.addToBottom(new VFXAction(flareEffect, 0.1f));
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, this.passiveAmount));
    }

    @Override
    public void triggerEvokeAnimation() {
        CardCrawlGame.sound.play("conspire:ORB_WATER_EVOKE", 0.1f);
        AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(this.cX, this.cY)); // TODO
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 45.0f;
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new WaterOrbPassiveEffect(this.cX, this.cY));
            this.vfxTimer = MathUtils.random(this.vfxIntervalMin, this.vfxIntervalMax);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, this.c.a / 2.0f));
        sb.draw(this.img, this.cX - 48.0f, this.cY - 48.0f + this.bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, this.scale + MathUtils.sin(this.angle / PI_4) * ORB_WAVY_DIST * Settings.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, this.c.a / 2.0f));
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.cX - 48.0f, this.cY - 48.0f + this.bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, this.scale, this.scale + MathUtils.sin(this.angle / PI_4) * ORB_WAVY_DIST * Settings.scale, - this.angle, 0, 0, 96, 96, false, false);
        sb.setBlendFunction(770, 771);
        this.renderText(sb);
        this.hb.render(sb);
    }

    @Override
    protected void renderText(SpriteBatch sb) {
        if (this.showEvokeValue) {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0f + NUM_Y_OFFSET - 4.0f * Settings.scale, new Color(0.2f, 1.0f, 1.0f, this.c.a), this.fontScale);
        }
    }

    @Override
    public void playChannelSFX() {
        CardCrawlGame.sound.play("conspire:ORB_WATER_CHANNEL", 0.1f);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new DefaultOrb();
    }
}
