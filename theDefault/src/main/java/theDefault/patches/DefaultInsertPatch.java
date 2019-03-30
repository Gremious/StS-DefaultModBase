package theDefault.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Using SpirePatch, also known as patching, allows you to insert your own code into the basegame code.
 * It is an extremely powerful and useful tool that can appear complicated at first. If you have no experience with modding StS, and Especially
 * with Java, I recommend you skip this for a while until you have a semi-decent grip on things/until you feel like you need to use it.
 * That being said, at the end of the day, it is not very complex once you understand how it works.
 *
 * Keep in mind that every patch is very unique so making a "tutorial class" that goes beyond the basics is a bit difficult,
 * since there are too many unique cases. I'll leave it up to you to experiment and learn what you need for *your own* patch.
 *
 * You will ***need*** to follow the official SpirePatch documentation here as you read through this patch.
 * https://github.com/kiooeht/ModTheSpire/wiki/SpirePatch
 * https://github.com/kiooeht/ModTheSpire/wiki/Matcher
 * Comment with quotations are taken from the documentation.
 *
 * This is a good point to ctrl+Click on abstract dungeon and ctrl+f for returnRandomRelicKey() - that is the method that we will be patching.
 * Have a read through it's code. returnRandomRelicKey() is a method that is passed a rarity (relic tier) and returns the first relic
 * from the appropriate pool of that rarity (which are pre-shuffled), as well as removing it from the relic pool so that you never get it again.
 *
 * This is used whenever any combat gives you a relic reward - it is how the game grabs a random relic.
 * (On a sidenote returnEndRandomRelicKey() on the other hand returns the *last* relic form the same list - this is used for shops.
 * That way visiting a shop doesn't change what relics you would get/see otherwise.
 *
 * Now that we understand how that method works - This patch will do the following:
 * We will insert our piece of code above the line "return !RelicLibrary.getRelic(retVal).canSpawn() ? returnEndRandomRelicKey(tier) : retVal;"
 * which is at the very end of the method. (If you read through the official documentation, you will also know that you can simply use a postfix patch to do that.
 * Have a read through the documentation as to their differences - they all have their pros and cons.
 * For example postfix patches can't use @ByRef and don't have localvars. On the other hand, instead of needing to use SpireReturn they can just
 * put a return value in their patched method, and they can also be passed the original return value of the patched method.
 *
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * )
 * They're also
 *
 *
 *
 */

@SpirePatch(    // "Use the @SpirePatch annotation on the patch class." (public class DefaultInsertPatch is our patch class)
        clz = AbstractDungeon.class,
        method = "getCurrRoom"
)
public class DefaultInsertPatch { /*"A patch class must be a public static class."*/
    protected static final Logger logger = LogManager.getLogger(DefaultInsertPatch.class.getName());
    
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {}
    )
    public static void Insert(/*AbstractDungeon __instance if not static, original parameters, localvars*/) {/*"A patch method must be a public static method."*/
        logger.info(DefaultInsertPatch.class.getSimpleName() + " triggered");
    }
    
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MapRoomNode.class, "getRoom");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            // return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[0]};
        }
    }
}