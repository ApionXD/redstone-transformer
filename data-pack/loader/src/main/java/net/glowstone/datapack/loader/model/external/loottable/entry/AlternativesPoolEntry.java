package net.glowstone.datapack.loader.model.external.loottable.entry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.glowstone.datapack.loader.model.external.loottable.condition.Condition;

import java.util.List;

public class AlternativesPoolEntry extends PoolEntry {
    public static final String TYPE_ID = "minecraft:alternatives";

    private final List<PoolEntry> children;

    @JsonCreator
    public AlternativesPoolEntry(
        @JsonProperty("conditions") List<Condition> conditions,
        @JsonProperty("weight") int weight,
        @JsonProperty("quality") int quality,
        @JsonProperty("children") List<PoolEntry> children) {
        super(conditions, weight, quality);
        this.children = children;
    }

    public List<PoolEntry> getChildren() {
        return children;
    }
}
