package theDefaultKotlin.cards

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction
import com.megacrit.cardcrawl.actions.utility.SFXAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect
import theDefault.DefaultMod
import theDefault.DefaultMod.makeCardPath
import theDefault.characters.TheDefault

class KotlinCard : AbstractUtilityCard(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET) {
    // /STAT DECLARATION/
    companion object {
        val ID = DefaultMod.makeID(KotlinCard::class.java.simpleName)
        val IMG: String? = makeCardPath("Skill.png")

        // STAT DECLARATION

        private val RARITY = AbstractCard.CardRarity.COMMON
        private val TARGET = AbstractCard.CardTarget.SELF
        private val TYPE = AbstractCard.CardType.SKILL
        val COLOR = TheDefault.Enums.COLOR_GRAY!!

        private const val COST = 1

        private const val MAGIC = 2
        private const val UPGRADE_PLUS_MAGIC = 3

        private const val DAMAGE = 5

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
            action(SFXAction("ATTACK_PIERCING_WAIL"))
            if (Settings.FAST_MODE) {
                action(VFXAction(p, ShockWaveEffect(p.hb.cX, p.hb.cY,
                        Settings.BLUE_TEXT_COLOR,
                        ShockWaveEffect.ShockWaveType.CHAOTIC),
                        0.3f))
            } else {
                action(VFXAction(p, ShockWaveEffect(p.hb.cX, p.hb.cY,
                        Settings.BLUE_TEXT_COLOR,
                        ShockWaveEffect.ShockWaveType.CHAOTIC),
                        1.5f))
            }
        }
        action(DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE))
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