package theDefault.cards;

import basemod.abstracts.CustomCard;

public abstract class AbstractDefaultCard extends CustomCard {
    
    public int defaultSecondMagicNumber;
    public int defaultBaseSecondMagicNumber;
    public boolean upgradedDefaultSecondMagicNumber;
    public boolean isDefaultSecondMagicNumberModified;
    
    public AbstractDefaultCard(final String id,
                               final String name,
                               final String img,
                               final int cost,
                               final String rawDescription,
                               final CardType type,
                               final CardColor color,
                               final CardRarity rarity,
                               final CardTarget target) {
        
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
        isDefaultSecondMagicNumberModified = false;
    }
    
    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedDefaultSecondMagicNumber) {
            defaultSecondMagicNumber = defaultBaseSecondMagicNumber;
            isDefaultSecondMagicNumberModified = true;
        }
    }
    
    public void upgradeDefaultSecondMagicNumber(int amount) {
        defaultBaseSecondMagicNumber += amount;
        defaultSecondMagicNumber = defaultBaseSecondMagicNumber;
        upgradedDefaultSecondMagicNumber = true;
    }
}