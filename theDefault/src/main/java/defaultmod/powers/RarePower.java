package defaultmod.powers;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;

import defaultmod.DefaultMod;
import defaultmod.cards.DefaultRareAttack;

public class RarePower extends AbstractPower {
    public AbstractCreature source;

    public static final String POWER_ID = defaultmod.DefaultMod.makeID("RarePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = "defaultModResources/images/powers/placeholder_power.png";

    public RarePower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        type = PowerType.DEBUFF;
        isTurnBased = false;
        img = ImageMaster.loadImage(IMG);
        this.source = source;
    }

    @Override
    public void atStartOfTurn() { // At the start of your turn
        AbstractCard playCard = new DefaultRareAttack(); // Declare Card	
        AbstractMonster targetMonster = AbstractDungeon.getRandomMonster(); // Declare Target - Random Monster

        playCard.freeToPlayOnce = true; //Self Explanatory

        if (playCard.type != AbstractCard.CardType.POWER) {
            playCard.purgeOnUse = true;
        }
            // Remove completely on use (Not Exhaust). A note - you don't need the '{}'
            // in this if statement, as it's just 1 line directly under. You can remove them, if you want.

        AbstractDungeon.actionManager.addToBottom(new QueueCardAction(playCard, targetMonster)); // Play the card on the target.
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }

        else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

}
