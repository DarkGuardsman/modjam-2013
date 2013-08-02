package dark.common.api;

import net.minecraft.item.ItemStack;
import dark.common.hive.Hivemind;
import dark.common.prefab.PosWorld;

public interface IHiveSpire extends IHiveObject
{
    /** General location of the spire. Usually the base of the tower */
    public PosWorld getLocation();

    public Hivemind getHive();

    /** Reports that the hive object is alive */
    public void reportIn(Object obj);

    /** Reports that the hive object is no more */
    public void reportDeath(Object obj);

    /** Called when something gives the spire supplies general to build with */
    public void receivedItems(ItemStack stack, Object obj);
}
