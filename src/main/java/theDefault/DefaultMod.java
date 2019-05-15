package theDefault;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theDefault.cards.*;
import theDefault.characters.TheDefault;
import theDefault.events.IdentityCrisisEvent;
import theDefault.potions.PlaceholderPotion;
import theDefault.relics.BottledPlaceholderRelic;
import theDefault.relics.DefaultClickableRelic;
import theDefault.relics.PlaceholderRelic;
import theDefault.relics.PlaceholderRelic2;
import theDefault.util.IDCheckDontTouchPls;
import theDefault.util.TextureLoader;
import theDefault.variables.DefaultCustomVariable;
import theDefault.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class DefaultMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {
    
    public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
    private static String modID;
    
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true;
    
    private static final String MODNAME = "Default Mod";
    private static final String AUTHOR = "Gremious";
    private static final String DESCRIPTION = "A base for Slay the Spire to start your own mod from, feat. the Default.";
    
    public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);
    
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f);
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f);
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f);
    
    private static final String ATTACK_DEFAULT_GRAY = "theDefaultResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = "theDefaultResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = "theDefaultResources/images/512/bg_power_default_gray.png";
    
    private static final String ENERGY_ORB_DEFAULT_GRAY = "theDefaultResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "theDefaultResources/images/512/card_small_orb.png";
    
    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "theDefaultResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "theDefaultResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = "theDefaultResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "theDefaultResources/images/1024/card_default_gray_orb.png";
    
    private static final String THE_DEFAULT_BUTTON = "theDefaultResources/images/charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "theDefaultResources/images/charSelect/DefaultCharacterPortraitBG.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "theDefaultResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "theDefaultResources/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "theDefaultResources/images/char/defaultCharacter/corpse.png";
    
    public static final String BADGE_IMAGE = "theDefaultResources/images/Badge.png";
    
    public static final String THE_DEFAULT_SKELETON_ATLAS = "theDefaultResources/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "theDefaultResources/images/char/defaultCharacter/skeleton.json";
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    
    public DefaultMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
        
        setModID("theDefault");
        
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + TheDefault.Enums.COLOR_GRAY.toString());
        
        BaseMod.addColor(TheDefault.Enums.COLOR_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        
        
        theDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE");
        try {
            SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings);
            
            config.load();
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
    }
    
    public static void setModID(String ID) {
        Gson coolG = new Gson();
        
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        logger.info("You are attempting to set your mod ID as: " + ID);
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) {
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION);
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) {
            modID = EXCEPTION_STRINGS.DEFAULTID;
        } else {
            modID = ID;
        }
        logger.info("Success! ID is " + modID);
    }
    
    public static String getModID() {
        return modID;
    }
    
    private static void pathCheck() {
        Gson coolG = new Gson();
        
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        String packageName = DefaultMod.class.getPackage().getName();
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources");
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) {
            if (!packageName.equals(getModID())) {
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID());
            }
            if (!resourcePathExists.exists()) {
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources");
            }
        }
    }
    
    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        DefaultMod defaultmod = new DefaultMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheDefault.Enums.THE_DEFAULT.toString());
        
        BaseMod.addCharacter(new TheDefault("the Default", TheDefault.Enums.THE_DEFAULT),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, TheDefault.Enums.THE_DEFAULT);
        
        receiveEditPotions();
        logger.info("Added " + TheDefault.Enums.THE_DEFAULT.toString());
    }
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        
        ModPanel settingsPanel = new ModPanel();
        
        
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                enablePlaceholder,
                settingsPanel,
                (label) -> {
                },
                (button) -> {
                    
                    enablePlaceholder = button.enabled;
                    try {
                        
                        SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings);
                        config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        
        settingsPanel.addUIElement(enableNormalsButton);
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
        
        
        BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        
        
        logger.info("Done loading badge Image and mod options");
    }
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");
        
        
        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheDefault.Enums.THE_DEFAULT);
        
        logger.info("Done editing potions");
    }
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");
        
        
        BaseMod.addRelicToCustomPool(new PlaceholderRelic(), TheDefault.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), TheDefault.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), TheDefault.Enums.COLOR_GRAY);
        
        
        BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);
        
        
        UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        logger.info("Done adding relics!");
    }
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        
        pathCheck();
        
        logger.info("Add variabls");
        
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        
        logger.info("Adding cards");
        
        
        BaseMod.addCard(new OrbSkill());
        BaseMod.addCard(new DefaultSecondMagicNumberSkill());
        BaseMod.addCard(new DefaultCommonAttack());
        BaseMod.addCard(new DefaultAttackWithVariable());
        BaseMod.addCard(new DefaultCommonSkill());
        BaseMod.addCard(new DefaultCommonPower());
        BaseMod.addCard(new DefaultUncommonSkill());
        BaseMod.addCard(new DefaultUncommonAttack());
        BaseMod.addCard(new DefaultUncommonPower());
        BaseMod.addCard(new DefaultRareAttack());
        BaseMod.addCard(new DefaultRareSkill());
        BaseMod.addCard(new DefaultRarePower());
        
        logger.info("Making sure the cards are unlocked.");
        
        
        UnlockTracker.unlockCard(OrbSkill.ID);
        UnlockTracker.unlockCard(DefaultSecondMagicNumberSkill.ID);
        UnlockTracker.unlockCard(DefaultCommonAttack.ID);
        UnlockTracker.unlockCard(DefaultAttackWithVariable.ID);
        UnlockTracker.unlockCard(DefaultCommonSkill.ID);
        UnlockTracker.unlockCard(DefaultCommonPower.ID);
        UnlockTracker.unlockCard(DefaultUncommonSkill.ID);
        UnlockTracker.unlockCard(DefaultUncommonAttack.ID);
        UnlockTracker.unlockCard(DefaultUncommonPower.ID);
        UnlockTracker.unlockCard(DefaultRareAttack.ID);
        UnlockTracker.unlockCard(DefaultRareSkill.ID);
        UnlockTracker.unlockCard(DefaultRarePower.ID);
        
        logger.info("Done adding cards!");
    }
    
    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        
        
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Card-Strings.json");
        
        
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Power-Strings.json");
        
        
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Relic-Strings.json");
        
        
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Event-Strings.json");
        
        
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Potion-Strings.json");
        
        
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Character-Strings.json");
        
        
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/DefaultMod-Orb-Strings.json");
        
        logger.info("Done edittting strings");
    }
    
    @Override
    public void receiveEditKeywords() {
        
        
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/DefaultMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
    
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
