package theDefaultKotlin

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import theDefault.DefaultMod
import theDefault.DefaultMod.makeCardPath
import theDefault.cards.AbstractDefaultCard
import theDefault.characters.TheDefault

class KotlinCard : AbstractDefaultCard(id, name, img, cost, rawDescription, type, color, rarity, target) {

    companion object {
        //@JvmStatic?
        val id: String = DefaultMod.makeID("KotlinCard")
        val name: String = "SCREAM"
        val img: String? = makeCardPath("Attack.png")
        val cost = 0
        val rawDescription = "AAAAAAAAAA !M! AAAAA"
        val type = CardType.SKILL
        val color = TheDefault.Enums.COLOR_GRAY
        val rarity = CardRarity.RARE
        val target = CardTarget.SELF
        var UPGRADE_PLUS_DMG = 3
    }

    init {
        baseMagicNumber = 2
        magicNumber = 2
    }

    override fun use(p: AbstractPlayer?, m: AbstractMonster?) {
        for (i in 1..10) {
            println("i is $i")
            println("Card id: $id Card name: $name")
        }

        test()

        for (n: AbstractMonster in AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.actionManager.addToBottom(
                    DamageAction(m, DamageInfo(p, magicNumber, damageTypeForTurn)
                            , AbstractGameAction.AttackEffect.SLASH_DIAGONAL))
        }

    }


    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeDamage(UPGRADE_PLUS_DMG)
            initializeDescription()
        }
    }

}

fun test() {
    println("Kotlin tastes like a cup of tea. The cup part. Hello from the Default.")
}