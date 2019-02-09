package theDefault.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theDefault.DefaultMod;
import theDefault.patches.relics.BottledPlaceholderField;
import theDefault.util.TextureLoader;

import java.util.function.Predicate;

import static theDefault.DefaultMod.makeRelicOutlinePath;
import static theDefault.DefaultMod.makeRelicPath;

public class BottledPlaceholderRelic extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer> {
    // This file will show you how to use 2 things - The Custom Bottle Relic and the Custom Savable - they go hand in hand.

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Savable
     *
     * Choose a card. Whenever you take play any card, draw the chosen card.
     */

    // BasemodWiki Says: "When you need to store a value on a card or relic between runs that isn't a relic's counter value
    // or a card's misc value, you use a custom savable to save and load it between runs."

    private static AbstractCard card;  // The field value you wish to save.
    private boolean cardSelected = true;


    // ID, images, text.
    public static final String ID = DefaultMod.makeID("BottledPlaceholderRelic");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BottledPlaceholder.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BottledPlaceholder.png"));

    public BottledPlaceholderRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    // Now, for making Bottled cards we need a small patch - our own custom SpireField
    // I've included that already in patches.relics.BottledPlaceholderField

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
    public void onEquip() { // 1. When we acquire the relic
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) { // 2. If the map is open - hide it.
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE; // 3. Set the room to INCOMPLETE - don't allow us to use the map, etc.
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck); // 4. Get a card group of all currently unbottled cards
        AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[3] + name + DESCRIPTIONS[2], false, false, false, false);
        // 5. Open the grid selection screen with the cards from the CardGroup we specified above. The description reads "Select a card to bottle for" + (relic name) + "."
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
        if (!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            BottledPlaceholderField.inBottledPlaceholderField.set(card, true);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.setDescriptionAfterLoading();
        }
    }



    // Change description after relic is already loaded to reflect the bottled card.
    public void setDescriptionAfterLoading() {
        this.description = DESCRIPTIONS[1] + FontHelper.colorString(card.name, "y") + DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    // Standard description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
