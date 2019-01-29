# StS-Default Mod Base

Welcome to this mildly over-commented Slay the Spire modding base. 

This is a minimal "default clean slate" for creating Slay the spire mods. 

Use it to make your own mod of any type - If you want to add any standard in-game content (Character, cards, relics), this is a good starting point.

It features 1 character (the Default) with a minimal set of things: 1 card of each type, 1 debuff, 1 relic, etc.

(Currently, there are still some stuff missing (such as game patching examples) which I plan to add later but I have other mods I wanna work on more.)

If you're new to modding, you basically need the BaseMod wiki for whatever you wish to add, and you can work your way thought it with this base. Another very important thing is to look at other mods as well as the base-game code and see how they do things. Again, refer to the basemod wiki for that.

https://github.com/daviscook477/BaseMod/wiki

This base itself isn't going to help you code or anything!! While I have been put comments noting what does what, this is just a nice starting point if you need a place to start learning from that's not an absolute empty canvas, or an overly-complicated, difficult to understand mod. But you still need to learn how the in-game code works and how to piece things together on your own. (i.e. this base will show you where to put the code for double-tap, but not what it is/how to write it/how it works. Look up the actual cards and backward-engineer them for that.)

Feel free to use this in any way you like, of course. 

If you have any issues with the code/bugs etc. feel free to add an issue.
If you want to recommend and/or add something to the mod that you feel would be helpful, feel free to put up a request or a branch!

Happy modding!

***
Great big thank you to [Kobting](https://github.com/Kobting) for the following guide:

## Setting Up Your Development Environment

### Download the Default Mod Repository
Download this repository as a zip. Unzip where you want to set up your dev environment.

*Do not clone or fork unless you really want to go through the hassle of changing git things later*

### Adding Dependencies
The `lib` folder is where we will keep our dependencies. This is very useful if you want to have multiple STS modding projects.

Download the latest release `.jar` for each project and place in the lib folder. If you're using workshop mods, their locations are as follows:

- Mod the Spire: `Steam\steamapps\workshop\content\646570\1605060445`
- BaseMod: `Steam\steamapps\workshop\content\646570\1605833019`
- StSlib: `Steam\steamapps\workshop\content\646570\1609158507`

The final dependency you will need is the actual game. There is a file called `desktop-1.0.jar` located where your game is installed. Copy `desktop-1.0.jar` into the `lib` folder.

> ModTheSpire and BaseMod only support the main branch of the game (i.e. not the beta branch).

> When Slay The Spire updates, you will need to replace `desktop-1.0.jar` with the latest version.

### Import Project Into an IDE
This set up should be IDE agnostic (i.e. you can use whatever IDE you like). Here are steps for importing your project into the more popular IDE

**IntelliJ**

*File -> Project from Existing Sources -> Select theDefault folder or your own mod that follows the structure of the theDefault -> Select Maven -> Press next until your project is built*.

Click on the Maven Projects tab on the right of the editor and click the refresh icon to load your dependencies from the pom.xml into your project. (If no Maven projects tab on the right *View -> Tool Windows -> Maven Projects*)

## Setting up Slay The Spire for Mods
If you have played Slay the Spire with mods or have set up ModTheSpire, you can skip this step. In this tutorial, the directory that Slay the Spire is installed in will be refered to as the Slay the Spire directory.

Follow the section titles **Playing Mods** on [ModTheSpire's Wiki](https://github.com/kiooeht/ModTheSpire/wiki#playing-mods).

Copy `BaseMod.jar` from your `lib` folder into your `mods` folder in your Slay the Spire directory.

## Writing Your First Mod

### Package the Default Mod
If you have followed the steps so far, you should be able to package this mod that you downloaded from this repo.

In IntelliJ, open the Maven project tab and select the "Execute maven goal" button.

![](https://i.imgur.com/Axy3tr0.png)

In the command line field, type `package`.

You can now find your mod in `[Whatever folder you extracted the zip into]/mods/DefaultMod.jar`. If not, just go 2 directories back from `theDefault` folder (the one with pom.xml in it) and there should be a mods folder there. If you still can't find it, check at the bottom of your pom.xml.

Copy `DefaultMod.jar` into the `mods` folder in your Slay the Spire directory.

You should be able to run Slay the Spire by running `MTS.cmd` (`MTS.sh` on a mac).

## Going Further

### What Other Listeners Exist

Mods do a whole lot of things. A full list of listeners can be found in `src/main/java/basemod/interfaces` in BaseMod.

### Looking at the Game's Source Code

What else can I do with `AbstractCard` or `AbstractDungeon`? How does the Silent's `Choke` really work? How does Slay the Spire work under the hood. A lot of these questions can be answered by looking at the game's source code. You can do this by decompiling the game. 

IntelliJ comes with it's own internal decompiler. Once you set your lib folder correctly and start up the mod, you will see the `External Libraries` folder on the left-hand side. Basemod and the Slay the Spire's `Desktop-1-0.jar` we put into our lib folder are both there, and can simply be oppened and explored. Alternatively, you can always search up the card/class you are looking for in intelliJ's class search (`ctrl+shift+A` and switch to the class tab with `tab` or `shift+tab`). 

![](https://i.imgur.com/OausOaf.png)

Finally, you can also find the original declaration of something already in the code by right clicking on it and selecting `Go to -> Declaration`, or, even faster, ctrl+left clicking on it.

If you do not wish to use the built-in decompiler, follow [this guide](https://github.com/daviscook477/BaseMod/wiki/Decompiling-Your-Game) instead.

### Can I Make My Own Listener

You bet you can. Check out how listeners are made in BaseMod and check out the documentation about `SpirePatch` in the [ModTheSpire docs](https://github.com/kiooeht/ModTheSpire/wiki/SpirePatch)

***

Things I should add/do next:

* ~~Non-character specific relic~~

* ~~Custom potions~~

* ~~Dynamic Variables~~

* CustomBottleRelic

* CustomSavable (?)

* ~~Change all the tabs into spaces so that formatting is ok across all platforms~~

* Other things I don't know about yet
