package org.mcsg.survivalgames.logging;

import java.io.Serializable;

import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public class GameData implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String world;
    private BlockData prevBlockData;
    private BlockData newBlockData;
    private int gameId;
    private int x, y, z;
    private ItemStack[] items;
    
    /**
     * 
     * @param gameId
     * @param world
     * @param prevBlockData
     * @param newBlockData
     * @param x
     * @param y
     * @param z
     * @param items
     * 
     * Provides a object for holding the data for block changes
     */
    public GameData(int gameId, String world, BlockData prevBlockData, BlockData newBlockData, int x, int y, int z, ItemStack[] items){
        this.gameId = gameId;
        this.world = world;
        this.prevBlockData = prevBlockData;
        this.newBlockData = newBlockData;
        this.x = x;
        this.y = y;
        this.z = z;
        this.items = items;
    }
    
    public int getGameId(){
        return gameId;
    }
    
    public String getWorld(){
        return world;
    }

    public BlockData getPrevBlockData() {
        return this.prevBlockData;
    }

    public BlockData getNewBlockData() {
        return this.newBlockData;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
    
    public ItemStack[] getItems(){
    	return items;
    }
}
