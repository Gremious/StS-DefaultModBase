package theDefault.cards;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractDynamicCard extends AbstractDefaultCard {

    // The reason we're extending abstract dynamic card, rather than abstract default card or simply custom card is:
    // Dynamic automatically generates ID and Dec

    public AbstractDynamicCard(final String id,
                               final String img,
                               final int cost,
                               final CardType type,
                               final CardColor color,
                               final CardRarity rarity,
                               final CardTarget target) {

        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);

    }
}