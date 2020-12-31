package net.glowstone.datapack.recipes.providers;

import com.google.common.collect.ImmutableList;
import net.glowstone.datapack.recipes.StaticResultRecipe;
import net.glowstone.datapack.recipes.inputs.MapExtendingRecipeInput;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.glowstone.datapack.utils.ItemStackUtils.itemStackIsEmpty;

public class TippedArrowRecipeProvider extends DynamicRecipeProvider<MapExtendingRecipeInput> {
    private static final List<Material> RECIPE = ImmutableList.<Material>builder()
        .add(Material.ARROW, Material.ARROW, Material.ARROW)
        .add(Material.ARROW, Material.LINGERING_POTION, Material.ARROW)
        .add(Material.ARROW, Material.ARROW, Material.ARROW)
        .build();

    public TippedArrowRecipeProvider(String namespace, String key) {
        super(MapExtendingRecipeInput.class, new NamespacedKey(namespace, key));
    }

    @Override
    public Optional<Recipe> getRecipeFor(MapExtendingRecipeInput input) {
        if (input.getInput().length != RECIPE.size()) {
            return Optional.empty(); // Not big enough
        }

        ItemStack potion = null;

        for (int i = 0; i < RECIPE.size(); i++) {
            ItemStack item = input.getInput()[i];

            if (itemStackIsEmpty(item)) {
                return Optional.empty(); // No stacks can be empty
            }

            if (item.getType() != RECIPE.get(i)) {
                return Optional.empty(); // Item doesn't match recipe.
            }

            if (item.getType() == Material.LINGERING_POTION) {
                potion = item;
            }
        }

        if (potion == null) {
            return Optional.empty(); // Sanity check, should never happen.
        }

        ItemStack ret = new ItemStack(Material.TIPPED_ARROW);
        ret.setItemMeta(potion.getItemMeta().clone());

        return Optional.of(new StaticResultRecipe(getKey(), ret));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}
