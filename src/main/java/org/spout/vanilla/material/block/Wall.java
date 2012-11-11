package org.spout.vanilla.material.block;

import java.util.HashMap;
import java.util.Map;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.source.DataSource;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.controlled.SignBase;
import org.spout.vanilla.material.block.misc.Torch;
import org.spout.vanilla.material.block.solid.Wool.WoolColor;

public class Wall extends VanillaBlockMaterial{
    
    public static final Wall MOSS_STONE_WALL = new Wall("Moss Stone Wall", WallType.MOSS);
    public static final Wall STONE_WALL = new Wall("Stone Wall", WallType.STONE);

    public Wall(String name, WallType type) {
        super((short)type.getData(), name, 139);
    }

    @Override
    public boolean canSupport(BlockMaterial material, BlockFace face) {
        if (material instanceof SignBase) {
            return true;
        }
        if (face == BlockFace.TOP) {
            if (material instanceof Torch) {
                return true;
            }
        }
        return false;
    }
    
    public static enum WallType implements DataSource {
        STONE(0),
        MOSS(1);
        private final short data;
        private static final Map<Short, WallType> ids = new HashMap<Short, WallType>();
        
        private WallType(int data) {
            this.data = (short) data;
        }

        @Override
        public short getData() {
            return data;
        }
        
        public static WallType getById(short id) {
            return ids.get(id);
        }

        static {
            for (WallType type : WallType.values()) {
                ids.put(type.getData(), type);
            }
        }
        
    }

}
