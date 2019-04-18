# StS-Default Mod Base

Welcome to this extremely over-commented Slay the Spire modding base. 

This is a minimal "default clean slate" for creating Slay the spire mods. 

Use it to make your own mod of any type - If you want to add any standard in-game content (character, cards, relics, events, etc.), this is a good starting point.

It features 1 character (the Default) with a minimal set of things: 1 card of each type, 1 debuff, 1 relic, etc.

(Currently, there are still some stuff missing (such as game patching examples) which I plan to add later but I have other mods I wanna work on more.)

If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add, and you can work your way thought it with this base. Another very important thing is to look at other mods (get them from their github!), as well as the base-game code, and see how they do things. 

https://github.com/daviscook477/BaseMod/wiki

This base itself isn't going to help you code or anything!! Nor does it provide basic Java learning! While I have been putting comments noting what does what, this is just a nice starting point if you need a place to start learning from that's not an absolute empty canvas, or an overly-complicated, difficult to understand mod. But you still need to learn how the in-game code works and how to piece things together on your own. (i.e. this base will show you where to put the code for double-tap, but not what it is/how to write it/how it works. Look up the actual cards and backward-engineer them for that.)

Feel free to use this in any way you like, of course. 

If you have any issues or you want to recommend and/or add something to the mod that you feel would be helpful, feel free to submit an issue or a PR!

Happy modding!

***
Great big thank you to [Kobting](https://github.com/Kobting) and [alexdriedger](https://github.com/alexdriedger) for the original version of the following guide:

## Setting Up Your Development Environment

### Download the Default Mod Repository
Download this repository as a zip. Unzip where you want to set up your dev environment.

![](https://i.imgur.com/WnDw5GY.png)

*Do not clone or fork unless you really want to go through the hassle of changing git things later*

### Adding Dependencies
You will need a 3 other mods in order to be able to build this/your mod:
BaseMod, ModTheSpire, and (for this mod specifically but it's a great utility for everyone:)  StSlib

Download the latest release `.jar` for each project or subscribe to them on the Steam Workshop. If you're using workshop mods, the mod locations are as follows:

- Mod the Spire: `Steam\steamapps\workshop\content\646570\1605060445`
- BaseMod: `Steam\steamapps\workshop\content\646570\1605833019`
- StSlib: `Steam\steamapps\workshop\content\646570\1609158507`

The final dependency you will need is the actual game. There is a file called `desktop-1.0.jar` located where your game is installed.

> ModTheSpire and BaseMod only support the main branch of the game (i.e. not the beta branch).


### Import Project Into an IDE
This set up should be IDE agnostic (i.e. you can use whatever IDE you like). Here are steps for importing your project into the more popular IDE

**IntelliJ**

*File -> Import -> Select theDefault folder or your own mod that follows the structure of the theDefault -> Select Maven -> Press next until your project is built*.

Click on the Maven Projects tab on the right of the editor (ignoring that this image shows it to the left)

![](https://i.imgur.com/rZfME3t.png)

This should open your pom.xml (Look at the top and make sure that's the file opened)

Name your mod a nice name change the steam path to correctly reflect your steamapps location.
Check all the locations to make sure they point to existing files.

Click the refresh icon to load your dependencies from the pom.xml into your project. (If no Maven projects tab on the right *View -> Tool Windows -> Maven Projects*)

(If it asks you, just enable auto-import)

## Writing Your First Mod

### Package the Default Mod
If you have followed the steps so far, you should be able to package this mod that you downloaded from this repo (though you would not be able to play it yet, as it will crash).

This is a good point in time to go through the comments in the main file (DefaultMod.java). You will need to change the mod ID from mine to yours, and refactor the resources folder to match said ID. All of the information for that is available in the comments. After you do that, come back here.

Assuming you have changed the ID/Resources, it's time to package and play the Default!:

In IntelliJ, open the Maven project tab and select the "Execute maven goal" button.

![](https://i.imgur.com/OPNhpDc.png)

In the command line field, type `package`.
(Good practice is to do `clean` followed by `package`, especially if you are replacing a file with another one and they have an identical name.)

You can also asign a shortcut to the both those commands by opening the Lifecycle drop-down folder (also in the image above) and right-click > Asign shortcut on them.

You can now find your mod in `[slay the spire install folder]/mods/[name_of_mod].jar`. If you  can't find it, check at the bottom of your pom.xml for the location.

You should be able to run Slay the Spire through Steam by selecting `Play With Mods`.
BUT WAIT
If you wanna run it with IntelliJ and get cool clickable line numbers when you get a crash-log that'll instantly direct you to where your error is:

https://imgur.com/a/MO5oNuS

The only ammedment I should mention is that you should put the first directory as your mod the spire workshop one (as it says in the guide) but the second one should be your Slay the Spire install folder (the same place where the mods folder is, but NOT inside the mods folder).

## Going Further

You made a card/event/relic and wanna test it? Please remember that the console exists:
https://github.com/daviscook477/BaseMod/wiki/Console

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

* ~~CustomBottleRelic~~

* ~~CustomSavable (?)~~

* ~~Change all the tabs into spaces so that formatting is ok across all platforms~~

* ~~Patching Exmaples~~

* Other things I don't know about yet
