package theDefault.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

/*
 * Patches have a pretty detailed documentation. Go check em out here:
 *
 *  https://github.com/kiooeht/ModTheSpire/wiki/SpirePatch
 */

@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
public class BottledPlaceholderField {
    public static SpireField<Boolean> inBottledPlaceholderField = new SpireField<>(() -> false);
    // SpireField is a wonderful thing that lets us add our own fields to preexisting classes in the game.
    // In this scenario we're going to add a boolean named "inBottledPlaceholderField" to the "makeStatEquivalentCopy" method inside AbstractCard

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            // This is a postfix patch, meaning it'll be inserted at the very end of makeStatEquivalentCopy()

            inBottledPlaceholderField.set(result, inBottledPlaceholderField.get(self)); // Read:
            // set inBottledPlaceholderField to have the card and true/false depending on whether it's bottled or not.

            return result; // Return the bottled card.
        }
    }

}