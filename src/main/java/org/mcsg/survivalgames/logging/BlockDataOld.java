package org.mcsg.survivalgames.logging;

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockDataOld implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String world;
    private Material prevMaterial;
    private Material newMaterial;
    private byte prevData, newData;
    private int x, y, z;
    private int gameId;
    private ItemStack[] items;
    
    /**
     * 
     * @param previd
     * @param newid
     * @param x
     * @param y
     * @param z
     * 
     * Provides a object for holding the data for block changes
     */
    public BlockDataOld(int gameId, String world, Material prevMaterial, byte prevData, Material newMaterial, byte newData, int x, int y, int z, ItemStack[] items){
        this.gameId = gameId;
        this.world = world;
        this.prevMaterial = prevMaterial;
        this.prevData = prevData;
        this.newMaterial = newMaterial;
        this.newData = newData;
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

    public byte getPrevData() {
        return prevData;
    }

    public byte getNewData() {
        return newData;
    }

    public Material getPrevMaterial() {
        return prevMaterial;
    }

    public Material getNewMaterial() {
        return newMaterial;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
    
    public ItemStack[] getItems(){
    	return items;
    }
}
