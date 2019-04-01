package xyz.vsngamer.elevator;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//@Mod.EventBusSubscriber(modid = Ref.MOD_ID)
public class Test {

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent e) {
        e.getWorld().spawnEntity(new EntityItem(e.getWorld(), e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), new ItemStack(Items.DIAMOND, 1)));
        //e.getWorld().spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, true, e.getPos().getX(), e.getPos().getY(), e.getPos().getZ(), 0.01D, 0.01D, 0.01D, 100);
    }

    /*@SubscribeEvent
    public static void onHit(ProjectileImpactEvent.Arrow e) {
            World w = e.getArrow().getEntityWorld();
            BlockPos pos = e.getEntity().getPosition();
            w.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 100F, true);

    }*/
}
