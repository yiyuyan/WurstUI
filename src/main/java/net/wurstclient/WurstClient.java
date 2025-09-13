/*
 * Copyright (c) 2014-2025 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.client.MinecraftClient;
import net.wurstclient.analytics.PlausibleAnalytics;
import net.wurstclient.clickgui.ClickGui;
import net.wurstclient.event.EventManager;
import net.wurstclient.events.GUIRenderListener;
import net.wurstclient.events.PostMotionListener;
import net.wurstclient.events.PreMotionListener;
import net.wurstclient.hack.HackList;
import net.wurstclient.hud.IngameHUD;
import net.wurstclient.keybinds.KeybindList;
import net.wurstclient.mixinterface.IMinecraftClient;
import net.wurstclient.other_feature.OtfList;
import net.wurstclient.settings.SettingsFile;
import net.wurstclient.util.json.JsonException;

public enum WurstClient
{
	INSTANCE;
	
	public static MinecraftClient MC;
	public static IMinecraftClient IMC;
	
	public static final String VERSION = "7.50.1";
	public static final String MC_VERSION = "1.21.8";
	
	private PlausibleAnalytics plausible;
	private EventManager eventManager;
	private HackList hax;
	private OtfList otfs;
	private SettingsFile settingsFile;
	private Path settingsProfileFolder;
	private KeybindList keybinds;
	private ClickGui gui;
	private IngameHUD hud;
	private RotationFaker rotationFaker;
	private FriendsList friends;
	private WurstTranslator translator;
	
	private boolean enabled = true;
	private static boolean guiInitialized;
	private Path wurstFolder;
	
	public void initialize()
	{
		System.out.println("Starting Wurst UI...");
		
		MC = MinecraftClient.getInstance();
		IMC = (IMinecraftClient)MC;
		wurstFolder = createWurstFolder();
		
		Path analyticsFile = wurstFolder.resolve("analytics.json");
		plausible = new PlausibleAnalytics(analyticsFile);
		plausible.pageview("/");
		
		eventManager = new EventManager(this);
		
		Path enabledHacksFile = wurstFolder.resolve("enabled-hacks.json");
		hax = new HackList(enabledHacksFile);

		
		otfs = new OtfList();
		
		Path settingsFile = wurstFolder.resolve("settings.json");
		settingsProfileFolder = wurstFolder.resolve("settings");
		this.settingsFile = new SettingsFile(settingsFile, hax,otfs);
		this.settingsFile.load();
		
		Path keybindsFile = wurstFolder.resolve("keybinds.json");
		keybinds = new KeybindList(keybindsFile);
		
		Path guiFile = wurstFolder.resolve("windows.json");
		gui = new ClickGui(guiFile);
		
		Path preferencesFile = wurstFolder.resolve("preferences.json");
		
		Path friendsFile = wurstFolder.resolve("friends.json");
		friends = new FriendsList(friendsFile);
		friends.load();
		
		translator = new WurstTranslator();
		
		/*KeybindProcessor keybindProcessor =
			new KeybindProcessor(hax, keybinds);
		eventManager.add(KeyPressListener.class, keybindProcessor);*/
		
		hud = new IngameHUD();
		eventManager.add(GUIRenderListener.class, hud);
		
		rotationFaker = new RotationFaker();
		eventManager.add(PreMotionListener.class, rotationFaker);
		eventManager.add(PostMotionListener.class, rotationFaker);
		
		/*updater = new WurstUpdater();
		eventManager.add(UpdateListener.class, updater);
		
		problematicPackDetector = new ProblematicResourcePackDetector();
		problematicPackDetector.start();*/
		
		/*Path altsFile = wurstFolder.resolve("alts.encrypted_json");
		Path encFolder = Encryption.chooseEncryptionFolder();
		altManager = new AltManager(altsFile, encFolder);*/
	}
	
	private Path createWurstFolder()
	{
		Path dotMinecraftFolder = MC.runDirectory.toPath().normalize();
		Path wurstFolder = dotMinecraftFolder.resolve("wurstui");
		
		try
		{
			Files.createDirectories(wurstFolder);
			
		}catch(IOException e)
		{
			throw new RuntimeException(
				"Couldn't create .minecraft/wurstui folder.", e);
		}
		
		return wurstFolder;
	}
	
	public String translate(String key, Object... args)
	{
		return translator.translate(key, args);
	}
	
	public PlausibleAnalytics getPlausible()
	{
		return plausible;
	}
	
	public EventManager getEventManager()
	{
		return eventManager;
	}
	
	public void saveSettings()
	{
		settingsFile.save();
	}
	
	public ArrayList<Path> listSettingsProfiles()
	{
		if(!Files.isDirectory(settingsProfileFolder))
			return new ArrayList<>();
		
		try(Stream<Path> files = Files.list(settingsProfileFolder))
		{
			return files.filter(Files::isRegularFile)
				.collect(Collectors.toCollection(ArrayList::new));
			
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void loadSettingsProfile(String fileName)
		throws IOException, JsonException
	{
		settingsFile.loadProfile(settingsProfileFolder.resolve(fileName));
	}
	
	public void saveSettingsProfile(String fileName)
		throws IOException, JsonException
	{
		settingsFile.saveProfile(settingsProfileFolder.resolve(fileName));
	}
	
	public HackList getHax()
	{
		return hax;
	}
	
/*	public CmdList getCmds()
	{
		return cmds;
	}*/
	
	public OtfList getOtfs()
	{
		return otfs;
	}
	
	public Feature getFeatureByName(String name)
	{
		/*Hack hack = getHax().getHackByName(name);
		if(hack != null)
			return hack;
		
		Command cmd = getCmds().getCmdByName(name.substring(1));
		if(cmd != null)
			return cmd;
		
		OtherFeature otf = getOtfs().getOtfByName(name);
		return otf;*/
		return null;
	}
	
	public KeybindList getKeybinds()
	{
		return keybinds;
	}
	
	public ClickGui getGui()
	{
		if(!guiInitialized)
		{
			guiInitialized = true;
			gui.init();
		}
		
		return gui;
	}
	
	/*public Navigator getNavigator()
	{
		return navigator;
	}*/
	
/*	public CmdProcessor getCmdProcessor()
	{
		return cmdProcessor;
	}*/
	
	public IngameHUD getHud()
	{
		return hud;
	}
	
	public RotationFaker getRotationFaker()
	{
		return rotationFaker;
	}
	
	public FriendsList getFriends()
	{
		return friends;
	}
	
	public WurstTranslator getTranslator()
	{
		return translator;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/*public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
		
		if(!enabled)
		{
			hax.panicHack.setEnabled(true);
			hax.panicHack.onUpdate();
		}
	}
	
	public WurstUpdater getUpdater()
	{
		return updater;
	}
	*/
	/*public ProblematicResourcePackDetector getProblematicPackDetector()
	{
		return problematicPackDetector;
	}*/
	
	public Path getWurstFolder()
	{
		return wurstFolder;
	}
	
	/*public AltManager getAltManager()
	{
		return altManager;
	}*/
}
