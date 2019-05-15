package theDefault.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theDefault.DefaultMod;

import static theDefault.DefaultMod.makeEventPath;

public class IdentityCrisisEvent extends AbstractImageEvent {
    
    public static final String ID = DefaultMod.makeID("IdentityCrisisEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("IdentityCrisisEvent.png");
    
    private int screenNum = 0;
    
    private float HEALTH_LOSS_PERCENTAGE = 0.03F;
    private float HEALTH_LOSS_PERCENTAGE_LOW_ASCENSION = 0.05F;
    
    private int healthdamage;
    
    public IdentityCrisisEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);
        
        if (AbstractDungeon.ascensionLevel >= 15) {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE);
        } else {
            healthdamage = (int) ((float) AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENTAGE_LOW_ASCENSION);
        }
        
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1] + healthdamage + OPTIONS[2]);
        imageEventText.setDialogOption(OPTIONS[3], new Apotheosis());
        imageEventText.setDialogOption(OPTIONS[4]);
    }
    
    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch (i) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        AbstractRelic relicToAdd = RelicLibrary.starterList.get(AbstractDungeon.relicRng.random(RelicLibrary.starterList.size() - 1)).makeCopy();
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relicToAdd);
                        break;
                    case 1:
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                        CardCrawlGame.sound.play("BLUNT_FAST");
                        AbstractDungeon.player.decreaseMaxHealth(healthdamage);
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                            AbstractDungeon.gridSelectScreen.open(
                                    CardGroup.getGroupWithoutBottledCards(
                                            AbstractDungeon.player.masterDeck.getPurgeableCards()),
                                    1, OPTIONS[6], false, false, false, true);
                        }
                        
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        break;
                    case 2:
                        AbstractCard c = new Apotheosis().makeCopy();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        break;
                    case 3:
                        imageEventText.loadImage("theDefaultResources/images/events/IdentityCrisisEvent2.png");
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        break;
                }
                break;
            case 1:
                switch (i) {
                    case 0:
                        openMap();
                        break;
                }
                break;
        }
    }
    
    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
            AbstractDungeon.player.masterDeck.removeCard(c);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }
}
