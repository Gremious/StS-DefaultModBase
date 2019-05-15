package theDefault.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AutoplayCardAction;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theDefault.DefaultMod;
import theDefault.patches.relics.BottledPlaceholderField;
import theDefault.util.TextureLoader;

import java.util.Iterator;
import java.util.function.Predicate;

import static theDefault.DefaultMod.makeRelicOutlinePath;
import static theDefault.DefaultMod.makeRelicPath;

public class BottledPlaceholderRelic extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer> {
    
    private static AbstractCard card;
    private boolean cardSelected = true;
    
    public static final String ID = DefaultMod.makeID("BottledPlaceholderRelic");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BottledPlaceholder.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BottledPlaceholder.png"));
    
    public BottledPlaceholderRelic() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }
    
    @Override
    public Predicate<AbstractCard> isOnCard() {
        return BottledPlaceholderField.inBottledPlaceholderField::get;
    }
    
    @Override
    public Integer onSave() {
        if (card != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(card);
        } else {
            return -1;
        }
    }
    
    @Override
    public void onLoad(Integer cardIndex) {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                BottledPlaceholderField.inBottledPlaceholderField.set(card, true);
                setDescriptionAfterLoading();
            }
        }
    }
    
    @Override
    public void onEquip() {
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[3] + name + DESCRIPTIONS[2], false, false, false, false);
    }
    
    @Override
    public void onUnequip() {
        if (card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
            if (cardInDeck != null) {
                BottledPlaceholderField.inBottledPlaceholderField.set(cardInDeck, false);
            }
        }
    }
    
    @Override
    public void update() {
        super.update();
        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;
            card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            
            BottledPlaceholderField.inBottledPlaceholderField.set(card, true);
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading();
        }
    }
    
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        boolean fullHandDialog = false;
        for (Iterator<AbstractCard> it = AbstractDungeon.player.drawPile.group.iterator(); it.hasNext(); ) {
            AbstractCard card = it.next();
            if (BottledPlaceholderField.inBottledPlaceholderField.get(card)) {
                this.flash();
                it.remove();
                if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                    if (AutoplayField.autoplay.get(card)) {
                        AbstractDungeon.actionManager.addToBottom(new AutoplayCardAction(card, AbstractDungeon.player.hand));
                    }
                    card.triggerWhenDrawn();
                    AbstractDungeon.player.drawPile.moveToHand(card, AbstractDungeon.player.drawPile);
                    
                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        r.onCardDraw(card);
                    }
                } else {
                    if (!fullHandDialog) {
                        AbstractDungeon.player.createHandIsFullDialog();
                        fullHandDialog = true;
                    }
                    AbstractDungeon.player.drawPile.moveToDiscardPile(card);
                }
            }
        }
    }
    
    public void setDescriptionAfterLoading() {
        this.description = DESCRIPTIONS[1] + FontHelper.colorString(card.name, "y") + DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
    
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
