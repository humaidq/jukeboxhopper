package ae.humaidq.jukeboxhopper.mixin;

import ae.humaidq.jukeboxhopper.JukeboxHopperMod;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.world.WorldEvents;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;

@Mixin(HopperBlockEntity.class)
public class HopperBlockMixin  {

	/*
	 * This segment is injected into the head of the HopperBlockEntity insert 
	 * method. In the default code, if the block doesn't have an inventory, 
	 * it will not "move" the item to the block (as, of course, it doesn't 
	 * have an inventory!).
	 * 
	 * This adds a special branch, if the hopper is facing a jukebox. It
	 * decrements the item stack if it is a Music Disc, and inserts it into
	 * the Jukebox using the `setRecord()` method. This is not enough to make
	 * the music play, so that is done by running a world event using the
	 * `syncWorldEvent()` method.
	 */
	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/block/entity/HopperBlockEntity;insert(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/inventory/Inventory;)Z")
	private static void insert(World world, BlockPos pos, BlockState state, Inventory inventory, final CallbackInfoReturnable<Optional<Boolean>> cir) {
		// We get the block that it is facing and check if it is a jukebox.
		BlockPos facingBlockPos = pos.offset(state.get(HopperBlock.FACING));
		BlockEntity en = world.getBlockEntity(facingBlockPos);
		if(en != null && en instanceof JukeboxBlockEntity) {
			JukeboxBlockEntity jb = (JukeboxBlockEntity) en;
			
			// We don't do anything if the jukebox already has a record.
			if(world.getBlockState(facingBlockPos).get(JukeboxBlock.HAS_RECORD))
				return;

			// Go through the items in the hopper, find a music disc we can insert.
			for (int i = 0; i < inventory.size(); ++i) {
				if (inventory.getStack(i).isEmpty()) continue;
				if (!(inventory.getStack(i).getItem() instanceof MusicDiscItem)) continue;
				JukeboxHopperMod.LOGGER.debug("Music disc inserted into hopper!");
				
				// Set the record and decrement the item from the hopper.
				ItemStack itemStack = inventory.getStack(i).copy();
				jb.setRecord(itemStack);
				inventory.getStack(i).decrement(1);
				world.setBlockState(facingBlockPos, world.getBlockState(facingBlockPos).with(JukeboxBlock.HAS_RECORD, true));
				world.syncWorldEvent(null, WorldEvents.MUSIC_DISC_PLAYED, facingBlockPos, Item.getRawId(itemStack.getItem()));
				return;
			}
		}
	}
}