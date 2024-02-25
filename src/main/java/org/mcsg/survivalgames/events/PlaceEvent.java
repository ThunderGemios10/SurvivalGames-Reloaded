package org.mcsg.survivalgames.events;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.mcsg.survivalgames.Game;
import org.mcsg.survivalgames.GameManager;
import org.mcsg.survivalgames.SettingsManager;

public class PlaceEvent implements Listener {
    public  ArrayList<String> allowedPlace = new ArrayList<String>();

    public PlaceEvent(){
        allowedPlace.addAll(SettingsManager.getInstance().getConfig().getStringList("block.place.whitelist"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        int id  = GameManager.getInstance().getPlayerGameId(p);

        if (id == -1) {
            int gameblockid = GameManager.getInstance().getBlockGameId(event.getBlock().getLocation());
            if (gameblockid != -1) {
                if(GameManager.getInstance().getGame(gameblockid).getGameMode() != Game.GameMode.DISABLED) {
                    event.setCancelled(true);
                }
            }
            return;
        }

        Game g = GameManager.getInstance().getGame(id);
        if (g.isPlayerinactive(p)) {
            return;
        }
        if (g.getMode() == Game.GameMode.DISABLED) {
            return;
        }
        if (g.getMode() != Game.GameMode.INGAME) {
            event.setCancelled(true);
            return;

        }

        if(!allowedPlace.contains(String.valueOf(event.getBlock().getType()))){
            event.setCancelled(true);
        }

    }
}