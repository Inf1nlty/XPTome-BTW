package btw.community.xptome;

import btw.BTWAddon;
import btw.AddonHandler;
import btw.block.BTWBlocks;
import btw.crafting.recipe.RecipeManager;
import btw.item.BTWItems;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import com.inf1nlty.xptome.ExpCommand;
import com.inf1nlty.xptome.XPTomeItems;

public class XPTomeAddon extends BTWAddon {
    @Override
    public void initialize() {
        AddonHandler.logMessage(getName() + " v" + getVersionString() + " Initializing...");

        RecipeManager.addShapelessRecipe(
                new ItemStack(XPTomeItems.xpTome, 1),
                new Object[]{
                        Item.enchantedBook,
                        BTWBlocks.dragonVessel,
                        BTWItems.soulUrn,
                }
        );
        AddonHandler.registerCommand(new ExpCommand(), false);
    }
}