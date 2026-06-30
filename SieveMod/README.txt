=====================================
  SieveMod - Automated Sieve Mod
  Minecraft Forge 1.12.2
=====================================

About this mod
--------------

This is my first Minecraft mod. I created it from scratch — learned Java modding,
designed the 3D block model in Blockbench, built the GUI, wrote all the recipes
and integrated JEI support.

The mod adds an Automated Sieve — a machine that processes common blocks into
valuable resources. You put Dirt, Gravel, Sand or Soul Sand into it, supply RF
energy, and it sifts them into random items like diamonds, iron, gold, quartz
and more.

Each block type has its own loot table with different drop chances. Some drops
are common, others are rare — it all depends on what you put in.


How it works
------------

1. Craft the Automated Sieve from iron ingots and redstone
2. Place it in the world
3. Connect an RF energy source (cable from Ender IO, Thermal Expansion, etc.)
4. Put blocks into the input slot
5. The machine processes them one by one, consuming energy
6. Result items appear in the output slot

You can install Speed Upgrades to make it process faster, or Chance Upgrades
to improve your luck with rare drops. Up to 3 of each can be installed.


What can be sifted
------------------

Dirt
  The most common block. Mostly gives coal and gold nuggets.
  Rare drops include iron ingots and even diamonds.

Gravel
  Yields flint as the main drop, plus iron nuggets, gold nuggets and coal.

Sand
  Gives cocoa beans, bones, clay balls.
  Gold nuggets are the rare drop here.

Soul Sand
  The Nether block. Main drops are quartz and nether wart.
  You can also get blaze powder and ghast tears (rare).


Required mods
-------------

Minecraft 1.12.2
  https://www.minecraft.net/en-us/store/minecraft-java-edition

Minecraft Forge 1.12.2-14.23.5.2838
  https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html


Recommended mods
----------------

JEI (Just Enough Items) 1.12.2-4.16.1.302
  Lets you browse all sifting recipes in-game
  https://www.curseforge.com/minecraft/mc-mods/jei/files?version=1.12.2


Compatible energy mods
----------------------

The sieve uses RF (Redstone Flux) energy. It works with any mod that provides
RF power:

  Thermal Expansion
    https://www.curseforge.com/minecraft/mc-mods/thermal-expansion

  Ender IO
    https://www.curseforge.com/minecraft/mc-mods/ender-io

  RFTools
    https://www.curseforge.com/minecraft/mc-mods/rftools

  Immersive Engineering
    https://www.curseforge.com/minecraft/mc-mods/immersive-engineering


Compatible item transport mods
------------------------------

The sieve supports the standard Forge item handler, so it works with:

  Ender IO Item Conduits
    https://www.curseforge.com/minecraft/mc-mods/ender-io

  Thermal Dynamics Itemducts
    https://www.curseforge.com/minecraft/mc-mods/thermal-dynamics

  Any other mod using Forge IItemHandler capability


Installation
------------

1. Install Minecraft 1.12.2
2. Install Forge 14.23.5.2838
3. (Optional) Install JEI
4. Download SieveMod-1.0.jar
5. Place it in .minecraft/mods/
6. Launch the game with Forge profile


Crafting recipes
----------------

Automated Sieve:

  I R I
  I   I
  I I I

  I = Iron Ingot
  R = Redstone

Speed Upgrade:

  I R I

  I = Iron Ingot
  R = Redstone

Chance Upgrade:

  G R G

  G = Gold Ingot
  R = Redstone


Credits
-------

  Block model: made in Blockbench (https://www.blockbench.net/)
  Mod loader: Minecraft Forge (https://www.minecraftforge.net/)
  Recipe browser: JEI (https://www.curseforge.com/minecraft/mc-mods/jei)


License
-------

This mod is provided as-is for educational and personal use.
