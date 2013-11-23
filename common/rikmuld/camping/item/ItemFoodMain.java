package rikmuld.camping.item;

import rikmuld.camping.core.lib.ItemInfo;
import rikmuld.camping.core.lib.ModInfo;
import rikmuld.camping.core.register.ModItems;
import rikmuld.camping.core.register.ModTabs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemFood;
import net.minecraft.util.Icon;

public class ItemFoodMain extends ItemFood{

	private Icon[] iconBuffer;
	private String[] metadata;

	public ItemFoodMain(String name, String[] meta,  String[] metaName, boolean renderMeta, int heal, float saturation, boolean wolf)
	{
		super(ItemInfo.id(name), heal, saturation, wolf);
		if(renderMeta)metadata = metaName;
		else this.setUnlocalizedName(name);
		this.setHasSubtypes(true);
		this.setCreativeTab(ModTabs.campingTab);
		ModItems.registerWithMeta(this, name, meta);
	}

	public ItemFoodMain(String name, int heal, float saturation, boolean wolf)
	{
		super(ItemInfo.id(name), heal, saturation, wolf);
		this.setUnlocalizedName(name);
		this.setCreativeTab(ModTabs.campingTab);
		ModItems.register(this, name);
	}

	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		if(this.metadata==null)
		{
			itemIcon = iconRegister.registerIcon(ModInfo.MOD_ID+":"+this.getUnlocalizedName().substring(5));
		}
		else
		{
			iconBuffer = new Icon[metadata.length+1];
			for(int x = 0; x<metadata.length; x++)
			{
				iconBuffer[x] = iconRegister.registerIcon(ModInfo.MOD_ID+":"+this.metadata[x].toString());
			}
		}
	}

	@Override
	public Icon getIconFromDamage(int metadata)
	{
		if(this.metadata!=null)
		{
			itemIcon = iconBuffer[metadata];
		}
		return this.itemIcon;
	}
}