package com.knowyourknot.chiseldecor;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.knowyourknot.chiseldecor.config.BlockPack;
import com.knowyourknot.chiseldecor.config.ChiselDecorConfig;
import com.knowyourknot.chiseldecor.data.Recipes;
import com.knowyourknot.chiseldecor.item.ItemChisel;
import com.knowyourknot.chiseldecor.resource.DefaultResources;
import com.oroarmor.config.command.ConfigCommand;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ChiselDecorEntryPoint implements ModInitializer {
	public static final Logger LOGGER = LogManager.getFormatterLogger("Chisel Decor");
	public static final ChiselDecorConfig CONFIG = new ChiselDecorConfig();
	public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(Ref.MOD_ID + ":blocks");
	public static final Item ITEM_CHISEL = new ItemChisel(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1));

	public static final List<Block> TRANSPARENT_BLOCKS = new ArrayList<>();
	
	@Override
	public void onInitialize()
	{
		DefaultResources.Download("https://github.com/3prm3-Org/Blockpacks/releases/download/v2.0.0.0/malg.zip");
		CommandRegistrationCallback.EVENT.register(new ConfigCommand(CONFIG)::register);

		String[] blockPackDirs = CONFIG.getBlockPackDirs();
		for (int i = 0; i < blockPackDirs.length; i++) {
			if (blockPackDirs[i].equals("")) {
				continue;
			}
			new BlockPack(blockPackDirs[i]).register(RESOURCE_PACK);
		}

		Registry.register(Registry.ITEM, new Identifier(Ref.MOD_ID, "chisel"), ITEM_CHISEL);
		Recipes.addChiselRecipe(RESOURCE_PACK);

		// RESOURCE_PACK.dump(FileSystemView.getFileSystemView().getHomeDirectory()); // for checking errors
		RRPCallback.EVENT.register(a -> a.add(RESOURCE_PACK));
	}
}
