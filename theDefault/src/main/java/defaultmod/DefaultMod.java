package defaultmod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.nio.charset.StandardCharsets;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;

import defaultmod.patches.TheDefaultEnum;
import defaultmod.patches.AbstractCardEnum;
import defaultmod.relics.*;
import defaultmod.cards.*;
import defaultmod.characters.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* 
 * Welcome to this mildly over-commented Slay the Spire modding base. 
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (Character, 
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, 1 relic, etc.
 * If you're new to modding, I highly recommend going though the BaseMod wiki for whatever you wish to add 
 * https://github.com/daviscook477/BaseMod/wiki  and work your way thought your made with this base. 
 * Feel free to use this in any way you like, of course. Happy modding!
 */

@SpireInitializer
public class DefaultMod implements EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber,EditCharactersSubscriber, PostInitializeSubscriber {
  	public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
  	
  	//This is for the in-game mod settings pannel.
  	private static final String MODNAME = "Default Mod";
    private static final String AUTHOR = "Gremious";
    private static final String DESCRIPTION = "A base for Slay the Spire to start your own mod from, feat. the Default.";
    
    // =============== IMPUT TEXTURE LOCATION =================
   
    // Color
    public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);

    // Image folder name
    private static final String DEFAULT_MOD_ASSETS_FOLDER = "defaultModResources/images";
    
    // card backgrounds
    private static final String ATTACK_DEAFULT_GRAY = "512/bg_attack_default_gray.png";
    private static final String POWER_DEAFULT_GRAY = "512/bg_power_default_gray.png";
    private static final String SKILL_DEAFULT_GRAY = "512/bg_skill_default_gray.png";
    private static final String ENERGY_ORB_DEAFULT_GRAY = "512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";

    private static final String ATTACK_DEAFULT_GRAY_PORTRAIT = "1024/bg_attack_default_gray.png";
    private static final String POWER_DEAFULT_GRAY_PORTRAIT = "1024/bg_power_default_gray.png";
    private static final String SKILL_DEAFULT_GRAY_PORTRAIT = "1024/bg_skill_default_gray.png";
    private static final String ENERGY_ORB_DEAFULT_GRAY_PORTRAIT = "1024/card_default_gray_orb.png";

    // Card images
    public static final String DEFAULT_COMMON_ATTACK = "cards/Attack.png";
    public static final String DEFAULT_COMMON_SKILL = "cards/Skill.png";
    public static final String DEFAULT_COMMON_POWER = "cards/Power.png";
    public static final String DEFAULT_UNCOMMON_ATTACK = "cards/Attack.png";
    public static final String DEFAULT_UNCOMMON_SKILL = "cards/Skill.png";
    public static final String DEFAULT_UNCOMMON_POWER = "cards/Power.png";
    public static final String DEFAULT_RARE_ATTACK = "cards/Attack.png";
    public static final String DEFAULT_RARE_SKILL = "cards/Skill.png";
    public static final String DEFAULT_RARE_POWER = "cards/Power.png";


    // Power images
    public static final String COMMON_POWER = "powers/placeholder_power.png";
    public static final String UNCOMMON_POWER = "powers/placeholder_power.png";
    public static final String RARE_POWER = "powers/placeholder_power.png";

    
    // Relic images  
    public static final String PLACEHOLDER_RELIC = "relics/placeholder_relic.png";
    public static final String PLACEHOLDER_RELIC_OUTLINE = "relics/outline/placeholder_relic.png";
	
    // Character assets
    private static final String THE_DEFAULT_BUTTON = "charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "charSelect/DeafultCharacterPortraitBG.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "char/defaultCharacter/corpse.png";
    
	//Mod Badge
    public static final String BADGE_IMAGE = "Badge.png";
    
	// Animations atlas and JSON files
    public static final String THE_DEFAULT_SKELETON_ATLAS = "char/defaultCharacter/skeleton.atlas"; 
    public static final String THE_DEFAULT_SKELETON_JSON = "char/defaultCharacter/skeleton.json"; 
    
    
    // =============== /IMPUT TEXTURE LOCATION/ =================
    
    /**
     * Makes a full path for a resource path
     * @param resource the resource, must *NOT* have a leading "/"
     * @return the full path
     */
    public static final String makePath(String resource) {
    	return  DEFAULT_MOD_ASSETS_FOLDER + "/" + resource;
    }

    // =============== SUBSCRIBE, CREATE THE COLOR, INITIALIZE =================

    public DefaultMod() {
		logger.info("Subscribe to basemod hooks");

		BaseMod.subscribe(this);

		logger.info("Done subscribing");

		
		logger.info("Creating the color " + AbstractCardEnum.DEFAULT_GRAY.toString());

		BaseMod.addColor(AbstractCardEnum.DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
				makePath(ATTACK_DEAFULT_GRAY), makePath(SKILL_DEAFULT_GRAY), makePath(POWER_DEAFULT_GRAY), makePath(ENERGY_ORB_DEAFULT_GRAY),
				makePath(ATTACK_DEAFULT_GRAY_PORTRAIT), makePath(SKILL_DEAFULT_GRAY_PORTRAIT), makePath(POWER_DEAFULT_GRAY_PORTRAIT),
				makePath(ENERGY_ORB_DEAFULT_GRAY_PORTRAIT), makePath(CARD_ENERGY_ORB));

		logger.info("Done Creating the color");
    }

    
	@SuppressWarnings("unused") 
    public static void initialize(){
        logger.info("========================= Initializing Default Mod. Hi. =========================");
    	DefaultMod defaultmod = new DefaultMod();	
        logger.info("========================= /Default Mod Initialized/ =========================");
    }
    
	// ============== /SUBSCRIBE, CREATE THE COLOR, INITIALIZE/ =================
	
	
	// =============== LOAD THE CHARACTER =================
	
	@Override
	public void receiveEditCharacters() {
		logger.info("begin editing characters");

		logger.info("add " + TheDefaultEnum.THE_DEFAULT.toString());

		BaseMod.addCharacter(new TheDefault("the Default", TheDefaultEnum.THE_DEFAULT), 
				makePath(THE_DEFAULT_BUTTON), makePath(THE_DEFAULT_PORTRAIT), TheDefaultEnum.THE_DEFAULT);

		logger.info("done editing characters");
	}

	// =============== /LOAD THE CHARACTER/ =================
	
		  
    // =============== LOAD THE MOD BADGE AND MENU =================

	@Override
	public void receivePostInitialize() {
		logger.info("Load Badge Image and mod options");

		Texture badgeTexture = new Texture(makePath(BADGE_IMAGE));

		ModPanel settingsPanel = new ModPanel();
		settingsPanel.addUIElement(new ModLabel("DefaultMod doesn't have any settings!", 400.0f, 700.0f, settingsPanel, (me) -> {}));
		BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

		logger.info("Done loading badge Image and mod options");

	}

	// =============== /LOAD THE MOD BADGE AND MENU/ =================

	

	// ================ ADD RELICS ===================
	
	@Override
	public void receiveEditRelics() {
		logger.info("Add relics");

		BaseMod.addRelicToCustomPool(new PlaceholderRelic(), AbstractCardEnum.DEFAULT_GRAY);

		logger.info("done adding relics!");
	}
	
	// ================ /ADD RELICS/ ===================
	  
	  
    // ================ ADD CARDS ===================
	
	@Override
	public void receiveEditCards() {
		
		logger.info("Add Cards");
		// Add the cards
		BaseMod.addCard(new DefaultCommonAttack());
		BaseMod.addCard(new DefaultCommonSkill());
		BaseMod.addCard(new DefaultCommonPower());
		BaseMod.addCard(new DefaultUncommonSkill());
		BaseMod.addCard(new DefaultUncommonAttack());
		BaseMod.addCard(new DefaultUncommonPower());
		BaseMod.addCard(new DefaultRareAttack());
		BaseMod.addCard(new DefaultRareSkill());
		BaseMod.addCard(new DefaultRarePower());

		logger.info("Making sure the cards are unlocked.");
		// Unlock the cards
		UnlockTracker.unlockCard(DefaultCommonAttack.ID);
		UnlockTracker.unlockCard(DefaultCommonSkill.ID);
		UnlockTracker.unlockCard(DefaultCommonPower.ID);
		UnlockTracker.unlockCard(DefaultUncommonSkill.ID);
		UnlockTracker.unlockCard(DefaultUncommonAttack.ID);
		UnlockTracker.unlockCard(DefaultUncommonPower.ID);
		UnlockTracker.unlockCard(DefaultRareAttack.ID);
		UnlockTracker.unlockCard(DefaultRareSkill.ID);
		UnlockTracker.unlockCard(DefaultRarePower.ID);

		logger.info("Cards - added!");
	}
	
    // ================ /ADD CARDS/ ===================

	        
	// ================ LOAD THE TEXT ===================
	
	@Override
	public void receiveEditStrings() {
		logger.info("begin editting strings");

		// CardStrings
		BaseMod.loadCustomStringsFile(CardStrings.class,
				"defaultModResources/localization/DefaultMod-Card-Strings.json");

		// PowerStrings
		BaseMod.loadCustomStringsFile(PowerStrings.class,
				"defaultModResources/localization/DefaultMod-Power-Strings.json");

		// RelicStrings
		BaseMod.loadCustomStringsFile(RelicStrings.class,
				"defaultModResources/localization/DefaultMod-Relic-Strings.json");

		logger.info("done edittting strings");
	}
	
	// ================ /LOAD THE TEXT/ ===================
	    
	// ================ LOAD THE KEYWORDS ===================

	@Override
	public void receiveEditKeywords() {
		final String[] placeholder = { "keyword", "keywords" };
		BaseMod.addKeyword(placeholder, "Whenever you play a card, gain 1 dexterity this turn only.");

	}
	
    // ================ /LOAD THE KEYWORDS/ ===================    

	// this adds "ModName: " before the ID of any card/relic/power etc.
	// in order to avoid conflics if any other mod uses the same ID.
	public static String makeID(String idText) {
		return "theDefault: " + idText;
	}

}
