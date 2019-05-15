package theDefault.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static theDefault.DefaultMod.makeID;

public class DefaultCustomVariable extends DynamicVariable {
    
    @Override
    public String key() {
        return makeID("ENERGY_DAMAGE");
    }
    
    @Override
    public boolean isModified(AbstractCard card) {
        return card.isDamageModified;
    }
    
    @Override
    public int value(AbstractCard card) {
        return card.damage * EnergyPanel.getCurrentEnergy();
    }
    
    @Override
    public int baseValue(AbstractCard card) {
        return card.baseDamage * EnergyPanel.getCurrentEnergy();
    }
    
    @Override
    public boolean upgraded(AbstractCard card) {
        return card.upgradedDamage;
    }
}