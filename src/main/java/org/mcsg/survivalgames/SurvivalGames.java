package org.mcsg.survivalgames;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcsg.survivalgames.events.*;
import org.mcsg.survivalgames.hooks.HookManager;
import org.mcsg.survivalgames.logging.LoggingManager;
import org.mcsg.survivalgames.logging.QueueManager;
import org.mcsg.survivalgames.stats.StatsManager;
import org.mcsg.survivalgames.util.ChestRatioStorage;
import org.mcsg.survivalgames.util.DatabaseManager;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class SurvivalGames extends JavaPlugin {
	public static Logger logger;
	public static boolean dbcon = false;
	public static boolean config_todate = false;
	public static int config_version = 3;

	public static List < String > auth = Arrays.asList(new String[] {
			"Double0negative", "iMalo", "Medic0987", "alex_markey", "skitscape", "AntVenom", "YoshiGenius", "pimpinpsp", "WinryR", "Jazed2011",
			"KiwiPantz", "blackracoon", "CuppingCakes", "4rr0ws", "Fawdz", "Timothy13", "rich91", "ModernPrestige", "Snowpool", "egoshk", 
			"nickm140",  "chaseoes", "Oceangrass", "GrailMore", "iAngelic", "Lexonia", "ChaskyT", "Anon232", "IngeniousGamer", "ThunderGemios10" //:) -Bryce
	});

	private final SurvivalGames plugin = this;
	private static File datafolder;
	private static boolean disabling = false;

	public void onDisable() {
		disabling = false;
		PluginDescriptionFile pdfFile = plugin.getDescription();
		SettingsManager.getInstance().saveSpawns();
		SettingsManager.getInstance().saveSystemConfig();
		for (Game g: GameManager.getInstance().getGames()) {
			try {
				g.disable();
			} catch(Exception e) {
				//will throw useless "tried to register task blah blah error." Use the method below to reset the arena without a task.
			}
			QueueManager.getInstance().rollback(g.getID(), true);
		}

		logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " has now been disabled and reset");
	}

	public void onEnable() {
		logger = getLogger();

		//ensure that all worlds are loaded. Fixes some issues with Multiverse loading after this plugin had started
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Startup(), 10);
		new Metrics(this, 21072);
	}

	class Startup implements Runnable {
		public void run() {
			datafolder = plugin.getDataFolder();

			PluginManager pm = getServer().getPluginManager();
			setCommands();

			SettingsManager.getInstance().setup(plugin);
			MessageManager.getInstance().setup();
			GameManager.getInstance().setup(plugin);

			try { // try loading everything that uses SQL. 
				FileConfiguration c = SettingsManager.getInstance().getConfig();
				if (c.getBoolean("stats.enabled")) DatabaseManager.getInstance().setup(plugin);
				QueueManager.getInstance().setup();
				StatsManager.getInstance().setup(plugin, c.getBoolean("stats.enabled"));
				dbcon = true;
			} catch (Exception e) {
				dbcon = false;
				e.printStackTrace();
				logger.severe("!!!Failed to connect to the database. Please check the settings and try again!!!");
				return;
			} finally {
				LobbyManager.getInstance().setup(plugin);
			}

			ChestRatioStorage.getInstance().setup();
			HookManager.getInstance().setup();
			pm.registerEvents(new PlaceEvent(), plugin);
			pm.registerEvents(new BreakEvent(), plugin);
			pm.registerEvents(new DeathEvent(), plugin);
			pm.registerEvents(new MoveEvent(), plugin);
			pm.registerEvents(new CommandCatch(), plugin);
			pm.registerEvents(new SignClickEvent(), plugin);
			pm.registerEvents(new ChestReplaceEvent(), plugin);
			pm.registerEvents(new LogoutEvent(), plugin);
			pm.registerEvents(new JoinEvent(plugin), plugin);
			pm.registerEvents(new TeleportEvent(), plugin);
			pm.registerEvents(LoggingManager.getInstance(), plugin);
			pm.registerEvents(new SpectatorEvents(), plugin);
			pm.registerEvents(new BandageUse(), plugin);
			pm.registerEvents(new KitEvents(), plugin);
			pm.registerEvents(new KeepLobbyLoadedEvent(plugin), plugin);


			for (Player p: Bukkit.getOnlinePlayers()) {
				if (GameManager.getInstance().getBlockGameId(p.getLocation()) != -1) {
					p.teleport(SettingsManager.getInstance().getLobbySpawn());
				}
			}

			//   new Webserver().start();
		}
	}

	public void setCommands() {
		getCommand("survivalgames").setExecutor(new CommandHandler(plugin));
	}

	public static File getPluginDataFolder() {
		return datafolder;
	}

	public static boolean isDisabling() {
		return disabling;
	}

	public WorldEditPlugin getWorldEdit() {
		Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
		if (worldEdit instanceof WorldEditPlugin) {
			return (WorldEditPlugin) worldEdit;
		}
		return null;
	}

	public static void $(String msg) {
		logger.log(Level.INFO, msg);
	}

	public static void $(Level l, String msg) {
		logger.log(l, msg);
	}

	public static void debug(String msg) {
		FileConfiguration config = SettingsManager.getInstance().getConfig();
		if (config.getBoolean("debug", false)) {
			$("[Debug] "+msg);
		}
	}

	public static void debug(int a) {
		FileConfiguration config = SettingsManager.getInstance().getConfig();

		if (config.getBoolean("debug", false)) {
			debug(a+"");
		}
	}
}
