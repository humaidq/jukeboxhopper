package ae.humaidq.minimod.jukehopper;
import net.minecraft.block.Block;
public class HopperUtils {

    public static boolean canTransferRecord(TileEntityHopper entityHopper) {
        return (getJukeboxFromHopper(entityHopper) != null);
    }

    public static void transferRecord(TileEntityHopper entityHopper) {
        for (int i = 0; i < entityHopper.getSizeInventory(); ++i) {
            if (!entityHopper.getStackInSlot(i).func_190926_b()) {
                ItemStack itemstack = entityHopper.getStackInSlot(i).copy();
                if (itemstack.getItem() instanceof ItemRecord) {
                    BlockJukebox.TileEntityJukebox jbe = getJukeboxFromHopper(entityHopper);
                    if (jbe != null) {
                        ItemStack decr = entityHopper.decrStackSize(i, 1);
                        BlockPos blockpos = getFacingBlockPos(entityHopper);
                        jbe.setRecord(itemstack.copy());
                        entityHopper.getWorld().setBlockState(blockpos, entityHopper.getWorld().getBlockState(blockpos).withProperty(BlockJukebox.HAS_RECORD, true), 2);
                        entityHopper.getWorld().playEvent(null, 1010, blockpos, Item.getIdFromItem(decr.getItem()));
                        decr.func_190918_g(1); // decrement
                        entityHopper.setInventorySlotContents(i, decr);
                    }
                }
            }

        }
    }

    private static BlockPos getFacingBlockPos(TileEntityHopper entityHopper) {
        EnumFacing enumfacing = BlockHopper.getFacing(entityHopper.getBlockMetadata());
        int x = MathHelper.floor(entityHopper.getXPos() + (double) enumfacing.getFrontOffsetX());
        int y = MathHelper.floor(entityHopper.getYPos() + (double) enumfacing.getFrontOffsetY());
        int z = MathHelper.floor(entityHopper.getZPos() + (double) enumfacing.getFrontOffsetZ());
        return new BlockPos(x, y, z);
    }

    private static BlockJukebox.TileEntityJukebox getJukeboxFromHopper(TileEntityHopper entityHopper) {
        BlockJukebox.TileEntityJukebox jb = null;
        BlockPos blockpos = getFacingBlockPos(entityHopper);
        World worldIn = entityHopper.getWorld();
        Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.hasTileEntity()) {
            TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof BlockJukebox.TileEntityJukebox) {
                IBlockState blockstate = worldIn.getBlockState(blockpos);
                if (!blockstate.getValue(BlockJukebox.HAS_RECORD)) {
                    jb = ((BlockJukebox.TileEntityJukebox) tileentity);
                }
            }
        }
        return jb;
    }
}
