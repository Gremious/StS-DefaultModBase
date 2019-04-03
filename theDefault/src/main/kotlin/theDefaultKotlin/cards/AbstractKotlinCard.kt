package theDefaultKotlin.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import theDefault.cards.AbstractDynamicCard

abstract class AbstractUtilityCard(id: String,
                                   img: String?,
                                   cost: Int,
                                   type: AbstractCard.CardType,
                                   color: AbstractCard.CardColor,
                                   rarity: AbstractCard.CardRarity,
                                   target: AbstractCard.CardTarget) :
        AbstractDynamicCard(
                id,
                img,
                cost,
                type,
                color,
                rarity,
                target
        ) {
    fun action(abstractGameAction: AbstractGameAction) {
        AbstractDungeon.actionManager.addToBottom(abstractGameAction)
    }
}