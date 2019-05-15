package theDefault.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "returnRandomRelicKey"

)
public class DefaultInsertPatch {
    private static final Logger logger = LogManager.getLogger(DefaultInsertPatch.class.getName());
    
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"retVal"}
    )
    public static void thisIsOurActualPatchMethod(
            AbstractRelic.RelicTier tier, String retVal) {
        logger.info("Hey our patch triggered. The relic we're about to get is " + retVal);
    }
    
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(RelicLibrary.class, "getRelic");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}