package com.rikmuld.camping.core

import java.util.ArrayList
import java.util.UUID
import org.lwjgl.input.Mouse
import com.rikmuld.camping.client.gui.GuiMapHUD
import com.rikmuld.camping.common.inventory.gui.InventoryCampinv
import com.rikmuld.camping.common.network.NBTPlayer
import com.rikmuld.camping.common.network.OpenGui
import com.rikmuld.corerm.common.network.PacketSender
import com.rikmuld.camping.common.objs.block.Hemp
import com.rikmuld.camping.common.objs.item.IKnife
import com.rikmuld.camping.common.objs.tile.TileEntityCampfireCook
import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.client.event.ConfigChangedEvent
import cpw.mods.fml.common.eventhandler.Event
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent
import cpw.mods.fml.relauncher.ReflectionHelper
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiContainerCreative
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.BonemealEvent
import net.minecraftforge.event.entity.player.PlayerDropsEvent
import java.util.Random
import net.minecraft.item.ItemArmor
import com.rikmuld.camping.common.objs.item.ArmorFur
import cpw.mods.fml.common.gameevent.TickEvent.Phase
import com.rikmuld.camping.common.objs.tile.TileEntityTrap
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent
import com.rikmuld.corerm.core.CoreUtils._
import com.rikmuld.camping.core.Utils.CampInvUtils

class Events {
  var tickLight: Int = 0
  var marshupdate = 0
  val UUIDSpeedCamping = new UUID(new Random(83746763).nextLong, new Random(28647556).nextLong)

  @SubscribeEvent
  def onBoneMealUsed(event: BonemealEvent) {
    if (event.block == Objs.hemp) event.setResult(if (event.block.asInstanceOf[Hemp].grow(event.world, event.x, event.y, event.z)) Event.Result.ALLOW else Event.Result.DENY)
  }
  @SubscribeEvent
  def onConfigChanged(eventArgs: ConfigChangedEvent.OnConfigChangedEvent) {
    if (eventArgs.modID.equals(ModInfo.MOD_ID)) Objs.config.sync
  }
  @SubscribeEvent
  def onPlayerDeath(event: PlayerDropsEvent) {
    if(!Objs.config.coreOnly)InventoryCampinv.dropItems(event.entityPlayer)
  }
  @SubscribeEvent
  def onEntityDeath(event: LivingDeathEvent) {
    if (event.entity.isInstanceOf[EntityPlayer]) {
      if (event.entity.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
        val tag = event.entity.asInstanceOf[EntityPlayer].getEntityData.getCompoundTag("campInv")
        if(!event.entity.asInstanceOf[EntityPlayer].getEntityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG))event.entity.asInstanceOf[EntityPlayer].getEntityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound())
        event.entity.asInstanceOf[EntityPlayer].getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setTag("campInv", tag)
      }
    }
  }  
  @SubscribeEvent
  def onPlayerRespawn(event:PlayerRespawnEvent){
    if(!Objs.config.coreOnly){
      if(event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasKey("campInv")){
        event.player.getEntityData.setTag("campInv", event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("campInv"))
        event.player.getEntityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).removeTag("campInv")
      }
    }
  }
  @SubscribeEvent
  def onPlayerLogin(event: PlayerLoggedInEvent) {
    if (!Objs.config.coreOnly&&event.player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING) != null) {
      PacketSender.to(new NBTPlayer(event.player.getEntityData.getCompoundTag(NBTInfo.INV_CAMPING)), event.player.asInstanceOf[EntityPlayerMP])
    }
  }
  @SubscribeEvent
  def onItemCrafted(event: ItemCraftedEvent) {
    for (slot <- 0 until event.craftMatrix.getSizeInventory if event.craftMatrix.getStackInSlot(slot) != null) {
      val itemInSlot = event.craftMatrix.getStackInSlot(slot)
      if ((itemInSlot.getItem != null) && (itemInSlot.getItem.isInstanceOf[IKnife])) {
        val returnStack = itemInSlot.addDamage(1)
        if (returnStack != null) {
          returnStack.stackSize += 1
        }
        event.craftMatrix.setInventorySlotContents(slot, returnStack)
      }
      if ((itemInSlot.getItem != null) && (itemInSlot.getItem() == Items.bowl) && (event.crafting.getItem() == Objs.parts) && (event.crafting.getItemDamage == PartInfo.MARSHMALLOW)) {
        val returnStack = new ItemStack(Items.bowl, itemInSlot.stackSize + 1, 0)
        event.craftMatrix.setInventorySlotContents(slot, returnStack)
      }
      if ((itemInSlot.getItem != null) && (itemInSlot.getItem() == Items.potionitem) && (event.crafting.getItem() == Objs.parts) && (event.crafting.getItemDamage == PartInfo.MARSHMALLOW)) {
        if (!event.player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle))) {
          event.player.dropPlayerItemWithRandomChoice(new ItemStack(Items.glass_bottle), false)
        }
      }
      if ((event.crafting.getItem() == Item.getItemFromBlock(Objs.lantern)) && (event.crafting.getItemDamage == LanternInfo.LANTERN_ON)) {
        event.crafting.setTagCompound(new NBTTagCompound())
        event.crafting.getTagCompound.setInteger("time", 1500)
      }
      if (event.crafting.getItem() == Item.getItemFromBlock(Objs.tent)) {
        if (event.craftMatrix.getStackInSlot(slot).getItem() == Items.dye) {
          event.crafting.setTagCompound(new NBTTagCompound())
          event.crafting.getTagCompound.setInteger("color", event.craftMatrix.getStackInSlot(slot).getItemDamage)
        }
      }
    }
  }
  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent) {
    val player = event.player
    val world = player.worldObj

    if(!Objs.config.coreOnly){
      if (event.phase.equals(Phase.START)) {
        if (player.getEntityData().getInteger("isInTrap") <= 0) {
          player.getEntityData().setInteger("isInTrap", player.getEntityData().getInteger("isInTrap") - 1)
        } else if (player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(TileEntityTrap.UUIDSpeedTrap) != null) player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(TileEntityTrap.UUIDSpeedTrap))
      }
      if (!world.isRemote && player.hasLantarn()) {
        tickLight += 1
        if (tickLight >= 10) {
          tickLight = 0
          player.lanternTick()
          if (player.worldObj.getBlock(player.posX.toInt, player.posY.toInt - 1, player.posZ.toInt) == Blocks.air) {
            player.worldObj.setBlock(player.posX.toInt, player.posY.toInt - 1, player.posZ.toInt, Objs.light)
          } else if (player.worldObj.getBlock(player.posX.toInt, player.posY.toInt, player.posZ.toInt) == Blocks.air) {
            player.worldObj.setBlock(player.posX.toInt, player.posY.toInt, player.posZ.toInt, Objs.light)
          } else if (player.worldObj.getBlock(player.posX.toInt, player.posY.toInt + 1, player.posZ.toInt) == Blocks.air) {
            player.worldObj.setBlock(player.posX.toInt, player.posY.toInt + 1, player.posZ.toInt, Objs.light)
          }
        }
      }
      if (!world.isRemote && player.hasMap()) {
        val data = player.getCurrentMapData()
        PacketSender.to(new com.rikmuld.camping.common.network.Map(data.scale, data.xCenter, data.zCenter, data.colors), player.asInstanceOf[EntityPlayerMP])
      }
    }
    if (!world.isRemote && (player.getCurrentEquippedItem != null) &&
      player.getCurrentEquippedItem.getItem == Objs.parts && (player.getCurrentEquippedItem.getItemDamage == PartInfo.MARSHMALLOWSTICK)) {
      val movingobjectposition = player.getMOP
      if (movingobjectposition != null) {
        val x = movingobjectposition.blockX
        val y = movingobjectposition.blockY
        val z = movingobjectposition.blockZ
        if (world.getBlock(x, y, z) == Objs.campfireCook && (Vec3.createVectorHelper(x + 0.5F, y + 0.5F, z + 0.5F).distanceTo(Vec3.createVectorHelper(player.posX, player.posY, player.posZ)) <= 2.5F)) {
          if (marshupdate > 80) {
            player.getCurrentEquippedItem.stackSize -= 1
            if (player.getCurrentEquippedItem.stackSize <= 0) player.setCurrentItem(null)
            if (!player.inventory.addItemStackToInventory(new ItemStack(Objs.marshmallow))) {
              player.dropPlayerItemWithRandomChoice(new ItemStack(Objs.marshmallow), false)
            }
            marshupdate = 0
          }
          val tile = world.getTileEntity(x, y, z).asInstanceOf[TileEntityCampfireCook]
          if (tile.fuel > 0) marshupdate += 1
          else marshupdate = 0
        }
      }
    }
    if(!Objs.config.coreOnly){
      var campNum = 0.0f
      for (i <- 0 until 4 if (player.inventory.armorInventory(i) != null && player.inventory.armorInventory(i).getItem.isInstanceOf[ArmorFur])) campNum += 0.25f
      if (player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(UUIDSpeedCamping) != null) player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(UUIDSpeedCamping))
      player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(new AttributeModifier(UUIDSpeedCamping, "camping.speedBoost", 0.04 * campNum, 0))
    }
  }
}

@SideOnly(Side.CLIENT)
class EventsClient {
  var map: GuiMapHUD = _
  var pressFlag: Array[Boolean] = new Array[Boolean](1)

  @SubscribeEvent
  def guiOpenClient(event: GuiOpenEvent) {
    if(!Objs.config.coreOnly){
      if (event.gui.isInstanceOf[GuiContainerCreative]) {
        ReflectionHelper.setPrivateValue(classOf[GuiContainerCreative], event.gui.asInstanceOf[GuiContainerCreative], CreativeTabs.tabInventory.getTabIndex, 2)
      }
    }
  }
  @SubscribeEvent
  def onClientTick(event: ClientTickEvent) {
    if (event.phase != TickEvent.Phase.END) return
    if(!Objs.config.coreOnly){
      val mc = FMLClientHandler.instance().getClient
      if (mc.currentScreen.isInstanceOf[GuiInventory] || mc.currentScreen.isInstanceOf[GuiContainerCreative]) {
        val list: ArrayList[Any] = ReflectionHelper.getPrivateValue(classOf[GuiScreen], mc.currentScreen, 4)
        list.asInstanceOf[java.util.List[GuiButton]].add(new GuiButton(10, 5, 5, 100, 10, "Camping Inventory"))
        ReflectionHelper.setPrivateValue(classOf[GuiScreen], mc.currentScreen, list, 4)
      }
    }
  }
  @SubscribeEvent
  def onOverlayRender(event: RenderGameOverlayEvent) {
    if (event.`type` != ElementType.HOTBAR) return 
    if(!Objs.config.coreOnly){
      val mc = FMLClientHandler.instance().getClient
      if (mc.currentScreen.isInstanceOf[GuiInventory] || mc.currentScreen.isInstanceOf[GuiContainerCreative]) {
        if (Mouse.isButtonDown(0) && event.mouseX >= 5 && event.mouseX <= 105 && event.mouseY >= 5 && event.mouseY <= 15) {
          if (pressFlag(0)) {
            PacketSender.toServer(new OpenGui(GuiInfo.GUI_CAMPINV))
            pressFlag(0) = false
          }
        } else pressFlag(0) = true
      }
      if (map == null) map = new GuiMapHUD()
      if (mc.thePlayer.hasMap()) {
        map.setWorldAndResolution(mc, event.resolution.getScaledWidth, event.resolution.getScaledHeight)
        map.drawScreen(event.mouseX, event.mouseY, event.partialTicks)
      } 
    }
  }
}