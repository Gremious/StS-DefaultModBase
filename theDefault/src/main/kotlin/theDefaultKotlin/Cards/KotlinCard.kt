package theDefaultKotlin.Cards

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction
import com.megacrit.cardcrawl.actions.utility.SFXAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect
import theDefault.DefaultMod
import theDefault.DefaultMod.makeCardPath
import theDefault.cards.AbstractDynamicCard
import theDefault.characters.TheDefault

class KotlinCard : AbstractDynamicCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    // /STAT DECLARATION/

    companion object {
        val ID = DefaultMod.makeID(KotlinCard::class.java.simpleName)
        val IMG = makeCardPath("Skill.png")

        // STAT DECLARATION

        private val RARITY = AbstractCard.CardRarity.COMMON
        private val TARGET = AbstractCard.CardTarget.SELF
        private val TYPE = AbstractCard.CardType.SKILL
        val COLOR = TheDefault.Enums.COLOR_GRAY

        private val COST = 1

        private val MAGIC = 2
        private val UPGRADE_PLUS_MAGIC = 3

        private val DAMAGE = 5

        // /STAT DECLARATION/
    }

    init {
        baseMagicNumber = MAGIC
        magicNumber = baseMagicNumber

        baseDamage = DAMAGE

        isMultiDamage = true
    }

    // Actions the card should do.
    override fun use(p: AbstractPlayer, m: AbstractMonster?) {
        for (i in 0 until magicNumber) {
            AbstractDungeon.actionManager.addToBottom(SFXAction("ATTACK_PIERCING_WAIL"))
            if (Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToBottom(
                        VFXAction(p, ShockWaveEffect(p.hb.cX, p.hb.cY,
                                Settings.BLUE_TEXT_COLOR,
                                ShockWaveEffect.ShockWaveType.CHAOTIC),
                                0.3f))
            } else {
                AbstractDungeon.actionManager.addToBottom(
                        VFXAction(p, ShockWaveEffect(p.hb.cX, p.hb.cY,
                                Settings.BLUE_TEXT_COLOR,
                                ShockWaveEffect.ShockWaveType.CHAOTIC),
                                1.5f))
            }
        }
        AbstractDungeon.actionManager.addToBottom(DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE))
    }

    //Upgraded stats.
    override fun upgrade() {
        if (!upgraded) {
            upgradeName()
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC)
            initializeDescription()
        }
    }

}
