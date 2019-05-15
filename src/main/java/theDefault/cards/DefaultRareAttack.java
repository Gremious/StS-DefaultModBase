package theDefault.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import theDefault.DefaultMod;
import theDefault.characters.TheDefault;

import static theDefault.DefaultMod.makeCardPath;

public class DefaultRareAttack extends AbstractDynamicCard {
    
    public static final String ID = DefaultMod.makeID(DefaultRareAttack.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");
    
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;
    
    private static final int COST = 2;
    
    private static final int DAMAGE = 30;
    private static final int UPGRADE_PLUS_DMG = 5;
    
    public DefaultRareAttack() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY)));
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.NONE));
    }
    
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}