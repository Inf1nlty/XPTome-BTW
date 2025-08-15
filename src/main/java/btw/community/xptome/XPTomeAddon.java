package btw.community.xptome;

import btw.BTWAddon;
import btw.AddonHandler;
import btw.crafting.recipe.RecipeManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import com.inf1nlty.xptome.ExpCommand;
import com.inf1nlty.xptome.XPTomeItems;

public class XPTomeAddon extends BTWAddon {
    @Override
    public void initialize() {
        AddonHandler.logMessage(getName() + " v" + getVersionString() + " Initializing...");
        XPTomeItems.registerItems();
        AddonHandler.registerCommand(new ExpCommand(), false);

        RecipeManager.addShapelessRecipe(
                new ItemStack(Item.enchantedBook, 2),
                new Object[]{
                        Item.enchantedBook,
                        Item.writableBook
                }
        );
    }
}