package theDefault.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import theDefault.DefaultMod;
import theDefault.characters.TheDefault;

import static theDefault.DefaultMod.makeCardPath;

public class DefaultUncommonSkill extends AbstractDynamicCard {
    
    public static final String ID = DefaultMod.makeID(DefaultUncommonSkill.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");
    
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;
    
    private static final int COST = 1;
    private static final int UPGRADE_REDUCED_COST = 0;
    
    private static final int BLOCK = 1;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    
    public DefaultUncommonSkill() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new PlatedArmorPower(p, block), block));
    }
    
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeBaseCost(UPGRADE_REDUCED_COST);
            initializeDescription();
        }
    }
}