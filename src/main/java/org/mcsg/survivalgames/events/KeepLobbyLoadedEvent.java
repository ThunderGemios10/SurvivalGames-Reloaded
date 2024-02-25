package org.mcsg.survivalgames.events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.mcsg.survivalgames.LobbyManager;



public class KeepLobbyLoadedEvent implements Listener{
    
    Plugin plugin;
    
    public KeepLobbyLoadedEvent(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e){
        LobbyManager.getInstance();
		if(LobbyManager.lobbychunks.contains(e.getChunk())){
            Chunk chunk = e.getChunk();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!chunk.isLoaded()) {
                    chunk.load();
                }
            }, 1L);
        }
        //System.out.println("Chunk unloading");
    }

}
