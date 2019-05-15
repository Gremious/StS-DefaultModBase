package theDefault.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theDefault.DefaultMod;
import theDefault.cards.DefaultRareAttack;
import theDefault.util.TextureLoader;

public class RarePower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;
    
    public static final String POWER_ID = DefaultMod.makeID("RarePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private static final Texture tex84 = TextureLoader.getTexture("theDefaultResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theDefaultResources/images/powers/placeholder_power32.png");
    
    public RarePower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;
        
        this.owner = owner;
        this.amount = amount;
        this.source = source;
        
        type = PowerType.DEBUFF;
        isTurnBased = false;
        
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        
        updateDescription();
    }
    
    @Override
    public void atStartOfTurn() {
        AbstractCard playCard = new DefaultRareAttack();
        AbstractMonster targetMonster = AbstractDungeon.getRandomMonster();
        playCard.freeToPlayOnce = true;
        
        if (playCard.type != AbstractCard.CardType.POWER) {
            playCard.purgeOnUse = true;
        }
        
        AbstractDungeon.actionManager.addToBottom(new QueueCardAction(playCard, targetMonster));
    }
    
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }
    
    @Override
    public AbstractPower makeCopy() {
        return new RarePower(owner, source, amount);
    }
}
