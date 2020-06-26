package me.e404nnf.emeraldtools.materials.iron.item;

    
import java.util.function.Predicate;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;


public class IronBow extends BowItem {

    public IronBow(Settings settings) {
        super(settings);
        // TODO Auto-generated constructor stub
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
	    if (!(user instanceof PlayerEntity))
	      return; 
	    PlayerEntity playerEntity = (PlayerEntity)user;
	    boolean bl = (playerEntity.abilities.creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0);
	    ItemStack itemStack = playerEntity.getArrowType(stack);
	    if (itemStack.isEmpty() && !bl)
	      return; 
	    if (itemStack.isEmpty())
	      itemStack = new ItemStack(Items.ARROW); 
	    int i = getMaxUseTime(stack) - remainingUseTicks;
	    float f = getPullProgress(i);
	    if (f < 0.1D)
	      return; 
	    boolean bl2 = (bl && itemStack.getItem() == Items.ARROW);
	    if (!world.isClient) {
	      ArrowItem arrowItem = (itemStack.getItem() instanceof ArrowItem) ? (ArrowItem)itemStack.getItem() : (ArrowItem)Items.ARROW;
	      PersistentProjectileEntity projectileEntity = arrowItem.createArrow(world, itemStack, (LivingEntity)playerEntity);
	      projectileEntity.setProperties((Entity)playerEntity, playerEntity.pitch, playerEntity.yaw, 0.0F, f * 3.0F, 1.0F);
	        projectileEntity.setDamage(projectileEntity.getDamage() + projectileEntity.getDamage() * 1.125D + 1.125D); 
	      if (f == 1.0F)
	        projectileEntity.setCritical(true); 
	      int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
	      if (j > 0)
	        projectileEntity.setDamage(projectileEntity.getDamage() + j * 0.5D + 0.5D); 
	      int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
	      if (k > 0)
	        projectileEntity.setPunch(k); 
	      if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0)
	        projectileEntity.setOnFireFor(100); 
	      stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
	      if (bl2 || (playerEntity.abilities.creativeMode && (itemStack.getItem() == Items.SPECTRAL_ARROW || itemStack.getItem() == Items.TIPPED_ARROW)))
	        projectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY; 
	      world.spawnEntity((Entity)projectileEntity);
	    } 
	    world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (RANDOM.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
	    if (!bl2 && !playerEntity.abilities.creativeMode) {
	      itemStack.decrement(1);
	      if (itemStack.isEmpty())
	        playerEntity.inventory.removeOne(itemStack); 
	    } 
	    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
	  }

}