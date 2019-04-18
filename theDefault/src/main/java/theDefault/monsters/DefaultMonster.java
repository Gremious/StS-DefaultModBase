package theDefault.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.actions.animations.SetAnimationAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import theDefault.DefaultMod;
    // Hello, welcome to the default monster. For now, It'll show you how to add the Jaw Worm, as it was kindly PR'd to me.
    // Later on, I might add any additional/unique things, if requested.

public class DefaultMonster extends AbstractMonster {
    public static final String ID = DefaultMod.makeID("DefaultMonster"); // Monster ID (remember the prefix - yourModID:DefaultMonster)
    private static final MonsterStrings monsterstrings = CardCrawlGame.languagePack.getMonsterStrings(ID); // Grab the string
    
    public static final String NAME = monsterstrings.NAME; // The name of the monster
    public static final String[] MOVES = monsterstrings.MOVES; // The names of the moves
    public static final String[] DIALOG = monsterstrings.DIALOG; // The dialogue (if any)
    
    private static final int HP_MIN = 84; // 1. The minimum and maximum amount of HP.
    private static final int HP_MAX = 87; // 2. Every monsters hp is "slightly random", falling between these values.
    
    private static final int A7_HP_MIN = 90; // HP moves up at Ascension 7.
    private static final int A7_HP_MAX = 94;
    
    private static final float HB_X = 0.0F;     // The hitbox X coordinate/position (relative to the monster)
    private static final float HB_Y = -25.0F;   // The Y position
    private static final float HB_W = 260.0F;   // Hitbox width
    private static final float HB_H = 170.0F;   // Hitbox Height
    
    private static final int ATTACK_1_DMG = 15;     // The damage of "attack 1".
    private static final int A2_ATTACK_1_DMG = 20;  // The damage of said attack increases past Ascension 2
    
    private static final int ATTACKBLOCK_DMG = 10;      // The damage value of the "attack and block" move (think Jaw Worm)
    private static final int ATTACKBLOCK_BLOCK = 10;    // The block value of the "attack and block" move (think Jaw Worm)
    
    private static final int BUFF_STR = 5;
    private static final int A_2_BUFF_STR = 5;
    private static final int A_17_BUFF_STR = 10; // and some on Ascension 17.
    private static final int BUFF_BLOCK = 5;
    private static final int A_17_BUFF_BLOCK = 10;
    
    private int buffBlock; // Defining the movement values before they're initalized depending on difficulty.
    private int attack1Dmg;
    private int attackBlockDmg;
    private int attackBlockBlock;
    private int buffStr;
    
    private static final byte ATTACK1 = 1; // These bytes are referred to for attacks.
    private static final byte BUFF = 2;
    private static final byte ATTACKBLOCK = 3;
    
    private boolean firstTurn = true;

    public DefaultMonster(float x, float y) {
        super(NAME, "JawWorm", 44, HB_X, HB_Y, HB_W, HB_H, null, x, y); // Initializes the monster.

        if (AbstractDungeon.ascensionLevel >= 7) { // Checks if your Ascension is 7 or above...
            this.setHp(A7_HP_MIN, A7_HP_MAX); // and increases HP if so.
        } else {
            this.setHp(HP_MIN, HP_MAX); // Provides regular HP values here otherwise.
        }

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.buffStr = A_17_BUFF_STR; // Here are where the move values for Ascension 17+ are set.
            this.buffBlock = A_17_BUFF_BLOCK;
            this.attack1Dmg = A2_ATTACK_1_DMG;
            this.attackBlockDmg = ATTACKBLOCK_DMG;
            this.attackBlockBlock = ATTACKBLOCK_BLOCK;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.buffStr = A_2_BUFF_STR; // Here are where the move values for Ascension 2-17 are set.
            this.buffBlock = BUFF_BLOCK;
            this.attack1Dmg = A2_ATTACK_1_DMG;
            this.attackBlockDmg = ATTACKBLOCK_DMG;
            this.attackBlockBlock = ATTACKBLOCK_BLOCK;
        } else {
            this.buffStr = BUFF_STR; // Here is where the regular movement values are set.
            this.buffBlock = BUFF_BLOCK;
            this.attack1Dmg = ATTACK_1_DMG;
            this.attackBlockDmg = ATTACKBLOCK_DMG;
            this.attackBlockBlock = ATTACKBLOCK_BLOCK;
        }

        this.damage.add(new DamageInfo(this, this.attack1Dmg)); // Creates a damageInfo for each attack.
        this.damage.add(new DamageInfo(this, this.attackBlockDmg));
        this.loadAnimation("images/monsters/theBottom/jawWorm/skeleton.atlas", "images/monsters/theBottom/jawWorm/skeleton.json", 1.0F); // Loads enemy animation skeletons.
        TrackEntry e = this.state.setAnimation(0, "idle", true); // Sets initial animation state.
        e.setTime(e.getEndTime() * MathUtils.random()); // Randomizes the animation start point, so multiple identical enemies aren't in sync.
    }

    //public void usePreBattleAction() {
        //This can be used to have a monster do something at the beginning of the fight. Like enemies that start with certain Powers. Just actionmanager add to bottom the power application here!
    // }

    public void takeTurn() {
        if (this.firstTurn) { // If this is the first turn,
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 0.5F, 2.0F)); // Speak the stuff in DIALOG[0],
            this.firstTurn = false; // Then ensure it's no longer the first turn.
        }

        switch(this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new SetAnimationAction(this, "chomp")); // Sets attack animation.
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.3F)); // Plays visual effects for attack.
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AttackEffect.NONE)); // Deals the big damage.
                break;
            case 2:
                this.state.setAnimation(0, "tailslam", false);
                this.state.addAnimation(0, "idle", true, 0.0F); // Adds an animation.
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_JAW_WORM_BELLOW")); // Plays a sound.
                AbstractDungeon.actionManager.addToBottom(new ShakeScreenAction(0.2F, ShakeDur.SHORT, ShakeIntensity.MED)); // Shakes the screen.
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F)); // Waits between actions.
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.buffStr), this.buffStr)); // Gains strength for the enemy.
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.buffBlock)); // Gives the enemy block.
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new AnimateHopAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.attackBlockBlock));
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) { // Gets a number for movement. This is set once at the beginning of combat and never changed. It's seeded. You can use this to make a few "archetype AI"s for enemies.
            if (num < 25) { // Checks if the number is below 25.
                if (this.lastMove((byte)1)) { // Checks if the last attack was a regular attack.
                    if (AbstractDungeon.aiRng.randomBoolean(0.5625F)) { // Checks a random chance, in this case a little more than 1/2.
                        this.setMove(MOVES[0], (byte)2, Intent.DEFEND_BUFF); // Sets the monster's intent to the buff if the chance goes through.
                    } else {
                        this.setMove((byte)3, Intent.ATTACK_DEFEND, ((DamageInfo)this.damage.get(1)).base); // Sets the monster's intent to the attack and block if the chance fails.
                    }
                } else {
                    this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base); // Sets the move to regular attack if the last move wasn't regular attack.
                }
            } else if (num < 55) { // checks if the number is below 55.
                if (this.lastTwoMoves((byte)3)) { // Checks if the last two moves were both attack and block.
                    if (AbstractDungeon.aiRng.randomBoolean(0.357F)) { // Checks a random chance. This case is around 35%.
                        this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base); // Sets the monster's intent to the regular attack.
                    } else {
                        this.setMove(MOVES[0], (byte)2, Intent.DEFEND_BUFF); // Sets the monster's intent to the defensive buff.
                    }
                } else {
                    this.setMove((byte)3, Intent.ATTACK_DEFEND, ((DamageInfo)this.damage.get(1)).base); // Sets the intent to attack and block.
                }
            } else if (this.lastMove((byte)2)) { // Checks if the last move was the buff.
                if (AbstractDungeon.aiRng.randomBoolean(0.416F)) { // Checks a random boolean - in this case slightly less than 1/2.
                    this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base); // Sets the intent to the regular attack.
                } else {
                    this.setMove((byte)3, Intent.ATTACK_DEFEND, ((DamageInfo)this.damage.get(1)).base); // Sets the intent to the attaak and block.
                }
            } else {
                this.setMove(MOVES[0], (byte)2, Intent.DEFEND_BUFF); // If none of the above things were done, sets the intent to the defensive buff.
            }
        }

    public void die() { // When this monster dies...
        super.die(); // It, uh, dies...
        CardCrawlGame.sound.play("JAW_WORM_DEATH"); // And it croaks too.
    }

}// You made it! End of monster.
