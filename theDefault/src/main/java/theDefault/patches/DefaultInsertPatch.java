package theDefault.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Using SpirePatch, also known as patching, allows you to insert your own code into the basegame code.
 * It is an extremely powerful and useful tool that can appear complicated at first. If you have no experience with modding StS, and especially
 * with Java, I recommend you skip this for a while until you have a semi-decent grip on things/until you feel like you need to use it.
 * That being said, at the end of the day, it is not very complex once you understand how it works.
 *
 * Keep in mind that every patch is very unique so making a "tutorial class" that goes beyond the basics is a bit difficult,
 * since there are too many unique cases. I'll leave it up to you to experiment and learn what you need for *your own* patch.
 *
 * You will ***NEED*** to follow the official SpirePatch documentation here as you read through this patch.
 * https://github.com/kiooeht/ModTheSpire/wiki/SpirePatch
 * https://github.com/kiooeht/ModTheSpire/wiki/Matcher
 * Comments with quotations are taken from the documentation.
 *
 * This is a good time to Ctrl+Click on AbstractDungeon down there and Ctrl+f for returnRandomRelicKey() - that is the method that we will be patching.
 * Have a read through it's code. returnRandomRelicKey() is a method that is passed a rarity (relic tier) and returns the first relic
 * from the appropriate pool of that rarity (which are pre-shuffled), as well as removing it from the relic pool so that you never get it again.
 *
 * This is used whenever any combat gives you a relic reward - it is how the game grabs a random relic.
 * (On a sidenote returnEndRandomRelicKey() on the other hand returns the *last* relic form the same list - this is used for shops.)
 * That way visiting a shop doesn't change what relics you would get/see otherwise.
 *
 * Now that we understand how that method works - This patch will do the following:
 * We will insert our piece of code above the line "return !RelicLibrary.getRelic(retVal).canSpawn() ? returnEndRandomRelicKey(tier) : retVal;"
 * which is at the very end of the method. (If you read through the official documentation, you will also know that you can simply use a postfix patch to do that.)
 * Have a read through the documentation as to their differences - they all have their pros and cons.
 * For example postfix patches can't use @ByRef and doesn't have localvars. On the other hand, instead of needing to use SpireReturn they can just
 * put a return value in their patched method, and they can also be passed the original return value of the patched method.
 *
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 * *NEVER USE REPLACE PATCHES. DON'T REPLACE GAME FILES EITHER (by putting a file with the same name in the same location as a basegame one).*
 *
 * So:
 * We will insert our piece of code above the line "return !RelicLibrary.getRelic(retVal).canSpawn() ? returnEndRandomRelicKey(tier) : retVal;"
 * We will the put in a logger inside that prints out the the value of the local variable "retVal" of that method.
 * That's about it.
 *
 * Let's get to it!
 */

@SpirePatch(    // "Use the @SpirePatch annotation on the patch class."
        clz = AbstractDungeon.class, // This is the class where the method we will be patching is. In our case - Abstract Dungeon
        method = "returnRandomRelicKey" // This is the name of the method we will be patching.
        /*
        Now let's imagine for a second that there were two methods named returnRandomRelicKey()
        The one we're patching - "String returnRandomRelicKey(RelicTier tier)" - that grabs a relic of specific tier
        and a fictional one - "String returnRandomRelicKey(RelicTier tier, LandingSound sound)" - that grabs a relic of a specific tier AND with a specific landing sound.
        How would we tell the code which of the two methods to put our patch in? We use paramtypez (read the docs too they have a good example!)
        Let's say we wanted to patch the second fictional one - we would add
        paramtypez={
                AbstractRelic.RelicTier.class,
                AbstractRelic.LandingSound.class
        }
        to this annotation, after the method parameter. (If we wanted to patch the first one, we'd only put "AbstractRelic.RelicTier.class".
        */
)
public class DefaultInsertPatch {// Don't worry about the "never used" warning - *You* usually don't use/call them anywhere. Mod The Spire does.
    
    // You can have as many inner classes with patches as you want inside this one - you don't have to separate each patch into it's own file.
    // So if you need to put 4 patches all for 1 purpose (for example they all make a specific relic effect happen) - you can keep them organized together.
    // Do keep in mind that "A patch class must be a public static class."
    
    private static final Logger logger = LogManager.getLogger(DefaultInsertPatch.class.getName()); // This is our logger! It prints stuff out in the console.
    // It's like a very fancy System.out.println();
    
    @SpireInsertPatch( // This annotation of our patch method specifies the type of patch we will be using. In our case - a Spire Insert Patch
            
            locator = Locator.class, // Spire insert patches require a locator - this isn't something you import - this is something we write.
            // (Or as is usually the case with them - copy paste cause they're always nearly the same thing.
            // In fact, most insert patches are fairly boiler-plate. You could easily make an insert patch template, if you'd like.)
            // You can find our Locator class just below, as an inner class, underneath our actual patch method.
            
            localvars = {"retVal"} // The method we're patching, returnRandomRelicKey(), has a local variable that we'd like to access and manipulate -
            // "String retVal = null;". So, we simply write out it's name here and then add it as a parameter to our patch method.
            // Keep in mind that localvars can also be used to capture class variables, not just local method ones. This also includes private ones.
    )
    //"A patch method must be a public static method."
    public static void thisIsOurActualPatchMethod(
            // 1. "Patch methods are passed all the arguments of the original method,
            // 2. as well as the instance if original method is not static (instance first, then parameters).
            // 3. localvars are passed as arguments, appearing the in parameter list after the original method's parameters."
            
            // Example: if returnEndRandomRelicKey(RelicTier tier) were NOT static we would write our method parameters as such:
            // thisIsOurActualPatchMethod(AbstractDungeon __instance, AbstractRelic.RelicTier tier, String retVal)
            // As it stands, that method is static so it's not tied to a specific instance of AbstractDungeon. (Read up on what "static" means in java
            // if you don't understand this part).
            // As such we write our method parameters like this instead:
            AbstractRelic.RelicTier tier, String retVal) {
        
        // Wow time to actually put stuff in the basegame code!!! Everything here will be executed exactly as written, at the line which we specified.
        // You can change retVal (using @byRef) to always return the same relic, or return a specific relic if it passes some check.
        // You can execute any other static method you have, you can save retVal to your personal public static variable to always be able to
        // reference the last relic you grabbed - etc. etc. The possibilities are endless. We're gonna do the following:
        logger.info("Hey our patch triggered. The relic we're about to get is " + retVal);
        // Incredible.
        
        // Let's talk about @byRef for a bit.
        // https://github.com/kiooeht/ModTheSpire/wiki/@ByRef - Read the documentation it is really helpful!
        // If you grabbed retVal right now and did:
        
        // retVal = Anchor().relicID
        // logger.info("Hey our patch triggered. The relic we're about to get is " + retVal);
        
        // The logger would correctly print out saying "we're about to get Anchor".
        // However you wouldn't get anchor. You would get the standard relic roll.
        // The reason behind that is because by default, in patches, all variables are passed by Value, not by Reference.
        // (You can google what this means in java if you don't understand).
        // This means that while we can change retVal within our own method, it won't change in the actual, original basegame method.
        // If you want to do that, you'll need to use @byRef on the variable you want to change - this makes it get passed by reference.
        // In our case that would be retVal - so we can annotate it with @byRef in our parameters. Another thing to note is that non-array
        // objects must be converted to arrays.
        
        // So if our current method parameters are:
        // (AbstractRelic.RelicTier tier, String retVal)
        // We would instead have:
        // (AbstractRelic.RelicTier tier, @ByRef String[] retVal)
        
        // Then, when we want to use it, can just access the (for us) one and only value in that array of Strings - which would be placed at index 0.
        // retVal[0] = Anchor().relicID
        // Then the retVal would actually be changed outside of this method - inside returnRandomRelicKey();
    }
    
    private static class Locator extends SpireInsertLocator { // Hey welcome to our SpireInsertLocator class!
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {// All the locator has and needs is an override of the Locate method
            // In simple terms, the locator works like this:
            // We give is something to match with, and it returns the line number that it finds the ting on,
            // inside the method which we specified wayyyy early on in our @SpirePatch annotation.
            // The Locate method is of type int[] - it returns an array of ints. These ints are actually the matching line numbers.
            
            // This is where we open up the https://github.com/kiooeht/ModTheSpire/wiki/Matcher documentation.
            
            // The line in the original method, "return !RelicLibrary.getRelic(retVal).canSpawn() ? returnEndRandomRelicKey(tier) : retVal;"
            // is just a simple ternary operator, check out http://www.cafeaulait.org/course/week2/43.html
            // or https://stackoverflow.com/questions/8898590/short-form-for-java-if-statement or simply ask google.
            // if you can't spawn the relic(note the "!"), grab a new relic from the end of the list instead
            // (call the returnEndRandomRelicKey() method) - otherwise return the relic.
            
            // We want to insert our code immediately above it so we'll need to use a matcher against something in that line.
            // We have a few of options for this 1 particular line. Before you proceed, read the docs and see how many you can personally spot.
            
            // 1. RelicLibrary.getRelic - is calling the the getRelic() method of the RelicLibrary class.
            // (You can also see that by Ctrl+clicking on getRelic)
            // 2. getRelic(retVal).canSpawn() - is calling the canSpawn() method of the AbstractRelic class.
            // (get relic() returns an AbstractRelic so we just use canSpawn() directly from it to check if we can spawn it)
            // 3. returnEndRandomRelicKey(tier) - is calling the returnEndRandomRelicKey method of the AbstractDungeon class.
            // At the end of the day, all three of these are MethodCallMatchers.
            
            /*
             * BUT WAIT!
             * Did you know that the third option is *actually* on a completely different line than the first one?
             * Decompiling the code with a different decompiler shows that the last line is actually a lot more like this:
             *
             * if (!RelicLibrary.getRelic(retVal).canSpawn()) {
             *      return returnEndRandomRelicKey(tier);
             * }
             *  return retVal;
             *
             *  Which means that if we use the third matcher we will insert code inside the if statement, while if we use 1. or 2. - just outside of it.
             *
             *  Follow this guide to learn how to decompile your game.
             *  https://github.com/daviscook477/BaseMod/wiki/Decompiling-Your-Game
             *  Essentially - you would want to use JD-GUI and Luyten *both* to get a precise look.
             *  (You can 100% still totally use the IntelliJ for quick-referencing code, it is still very fast and convenient)
             *
             *  On a sidenote, you should enable debug lines in intelliJ both for bugfixing and seeing what thing is *really* on what line
             *  To do so:
             * 1. Ctrl+Shift+A
             * 2. Registry
             * 3. Scroll down and set decompiler.dump.original.lines to true
             */
            
            // A good way to choose, usually, would be to pick the matcher least likely to appear elsewhere in the code of the method
            // i.e. the rarest one. In this case, it doesn't really matter as it's 3 of the same matcher, and none of their methods
            // ever appear again anywhere else, so let's just go for the first one:
            // As the documentation says, put the Class type and the method name (as a string) as your parameters:
            
            Matcher finalMatcher = new Matcher.MethodCallMatcher(RelicLibrary.class, "getRelic");
            
            // Now we just have to return the line number corresponding to that particular method call.
            // We have 2 options:
            // 1. findInOrder - Returns the first line number that matches the description of the matcher
            // (i.e. the very first time it finds RelicLibrary.getRelic() in the method we're patching.)
            
            // 2. findAllInOrder - Returns an array of ints - all of the line numbers matching the description.
            // (This is, for example, if the method we're patching had used "RelicLibrary.getRelic()" 3 different times,
            // and we want to insert our code right before ALL of the matches, or before a particular one of them.)
            
            // In our case "RelicLibrary.getRelic()" is called only once, in that particular return statement, so we can just return it.
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            
            // If we wanted to use findAllInOrder instead, we would do it like this:
            // return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[0]};
            // The [0] in this case indicates the index of the line number in the array (in the order they were found)
            // The first (and for us, only) instance of "RelicLibrary.getRelic()" would be at index 0. The second at index 1, and so on.
            
            // Finally, if we wanted to insert our code before *every* line with a match, we would just skip the index and return the whole list of lines:
            // return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)
        }
    }
}