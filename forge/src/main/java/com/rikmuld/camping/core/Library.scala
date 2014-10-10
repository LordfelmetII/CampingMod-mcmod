package com.rikmuld.camping.core

import org.lwjgl.input.Keyboard

object ModInfo {
  final val MOD_ID = "camping"
  final val MOD_NAME = "The Camping Mod 2.0"
  final val MOD_VERSION = "2.1"
  final val MOD_LANUAGE = "scala"
  final val MOD_DEPENDENCIES = "required-after:Forge@[v10.13.0.1180,)"
  final val MOD_SERVER_PROXY = "com.rikmuld.camping.core.ProxyServer"
  final val MOD_CLIENT_PROXY = "com.rikmuld.camping.core.ProxyClient"
  final val MOD_GUIFACTORY = "com.rikmuld.camping.core.GuiFactory"
  final val PACKET_CHANEL = MOD_ID
}

object NBTInfo {
  final val INV_CAMPING = "campInv";
}

object GuiInfo {
  final val GUI_CAMPFIRE = 0
  final val GUI_BACKPACK = 1
  final val GUI_KIT = 2
  final val GUI_CAMPFIRE_COOK = 3
  final val GUI_CAMPINV = 4
  final val GUI_INVENTORY = 5
  final val GUI_CAMPINV_CRAFT = 6
}

object ConfigInfo {
  final val CAT_ANIMALS = "animals"
  final val CAT_WORLD = "world genaration"
  final val CAT_TOOLS = "tools"
  final val CAT_FOOD = "food"
  final val CAT_GENERAL = "general"
}

object ModelInfo {
  final val MODEL_LOCATION = ModInfo.MOD_ID + ":models/"
  final val CAMPFIRE = MODEL_LOCATION + "ModelCampfireDeco.tcn"
  final val LOG = MODEL_LOCATION + "ModelLog.tcn"
}

object TextureInfo {
  final val GUI_LOCATION = ModInfo.MOD_ID + ":textures/gui/"
  final val MODEL_LOCATION = ModInfo.MOD_ID + ":textures/models/"
  final val SPRITE_LOCATION = ModInfo.MOD_ID + ":textures/sprite/"
  final val MC_GUI_LOCATION = "minecraft:textures/gui/"

  final val MODEL_CAMPFIRE = MODEL_LOCATION + "ModelCampfireDeco.png"
  final val SPRITE_FX = SPRITE_LOCATION + "SpriteFX.png"
  final val GUI_CAMPFIRE = GUI_LOCATION + "GuiCampfireDeco.png"
  final val GUI_CAMPINV_BACK = GUI_LOCATION + "GuiCampingBackpack.png"
  final val GUI_CAMPINV_TOOL = GUI_LOCATION + "GuiCampingTool.png"
  final val MC_INVENTORY = MC_GUI_LOCATION + "inventory.png"
  final val GUI_BAG = GUI_LOCATION + "GuiBackpack.png"
  final val GUI_KIT = GUI_LOCATION + "GuiKit.png"
  final val GUI_CAMPFIRE_COOK = GUI_LOCATION + "GuiCampfireCook.png"
  final val MODEL_GRILL = MODEL_LOCATION + "ModelGrill.png"
  final val MODEL_PAN = MODEL_LOCATION + "ModelPan.png"
  final val MODEL_LOG = MODEL_LOCATION + "ModelLog.png"
  final val MODEL_LOG2 = MODEL_LOCATION + "ModelLog2.png"
  final val MODEL_SPIT = MODEL_LOCATION + "ModelSpit.png"
  final val GUI_CAMPINV = GUI_LOCATION + "GuiCampinv.png"
  final val GUI_CAMPINV_CRAFT = GUI_LOCATION + "GuiCampinvCraft.png"
  final val RED_DOT = GUI_LOCATION + "GuiMapRed.png"
  final val BLUE_DOT = GUI_LOCATION + "GuiMapBlue.png"
  final val GUI_UTILS = GUI_LOCATION + "GuiUtils.png"
}

object TextInfo {
  final val COLOR_BLACK = "\u00a70"
  final val COLOR_BLUE_DARK = "\u00a71"
  final val COLOR_GREEN_DARK = "\u00a72"
  final val COLOR_AQUA_DARK = "\u00a73"
  final val COLOR_RED_DARK = "\u00a74"
  final val COLOR_PURPLE = "\u00a75"
  final val COLOR_ORANGE = "\u00a76"
  final val COLOR_GRAY = "\u00a77"
  final val COLOR_GRAY_DARK = "\u00a78"
  final val COLOR_BLUE = "\u00a79"
  final val COLOR_GREEN_LIGHT = "\u00a7a"
  final val COLOR_AQUA_LIGHT = "\u00a7b"
  final val COLOR_RED = "\u00a7c"
  final val COLOR_PINK = "\u00a7d"
  final val COLOR_YELLOW = "\u00a7e"
  final val COLOR_WHITE = "\u00a7f"

  final val FORMAT_OBFUSCATED = "\u00a7k"
  final val FORMAT_BOLD = "\u00a7l"
  final val FORMAT_THROUGH = "\u00a7m"
  final val FORMAT_UNDERLINE = "\u00a7n"
  final val FORMAT_ITALIC = "\u00a7o"

  final val RESET = "\u00a7r"
}