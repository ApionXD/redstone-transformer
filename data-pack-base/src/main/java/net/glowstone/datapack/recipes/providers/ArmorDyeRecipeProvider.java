package net.glowstone.datapack.recipes.providers;

import com.destroystokyo.paper.MaterialTags;
import net.glowstone.datapack.recipes.StaticResultRecipe;
import net.glowstone.datapack.recipes.inputs.ArmorDyeRecipeInput;
import net.glowstone.datapack.tags.ExtraMaterialTags;
import net.glowstone.datapack.utils.DyeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ArmorDyeRecipeProvider extends DynamicRecipeProvider<ArmorDyeRecipeInput> {

    public ArmorDyeRecipeProvider(String namespace, String key) {
        super(
            ArmorDyeRecipeInput.class,
            new NamespacedKey(namespace, key)
        );
    }

    @Override
    public Optional<Recipe> getRecipeFor(ArmorDyeRecipeInput input) {
        ItemStack armor = null;
        List<Color> colors = new ArrayList<>();

        for (ItemStack item : input.getInput()) {
            if (itemStackIsEmpty(item)) {
                continue;
            }

            if (MaterialTags.DYES.isTagged(item.getType())) {
                Color color = DyeUtils.getDyeColor(item.getType()).getColor();
                colors.add(color);
                continue;
            }

            if (ExtraMaterialTags.DYABLE_ARMOR.isTagged(item.getType())) {
                if (armor != null) {
                    return Optional.empty(); // Can't dye more than one item
                }
                armor = item;
                continue;
            }

            return Optional.empty(); // Non-armor item
        }

        if (armor == null) {
            return Optional.empty(); // No armor
        }
        if (colors.isEmpty()) {
            return Optional.empty(); // No colors
        }

        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        Color base = meta.getColor();
        if (meta.getColor() == Bukkit.getItemFactory().getDefaultLeatherColor()) {
            base = colors.remove(0);
        }

        Color newColor = base.mixColors(colors.toArray(new Color[0]));

        ItemStack ret = armor.clone();
        LeatherArmorMeta retMeta = (LeatherArmorMeta) ret.getItemMeta();
        retMeta.setColor(newColor);
        ret.setItemMeta(retMeta);

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
