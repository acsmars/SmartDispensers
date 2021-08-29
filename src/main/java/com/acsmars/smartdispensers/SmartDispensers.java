package com.acsmars.smartdispensers;

import com.acsmars.smartdispensers.interactions.InteractionType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SmartDispensers extends JavaPlugin {

    private DispenserListener dispenserListener;

    @Override
    public void onEnable() {
        getLogger().info("Smart Dispensers Starting");
        saveDefaultConfig();

        // Load config
        FileConfiguration config = this.getConfig();

        Map<Material, List<InteractionType>> materialInteractions = parseMaterialInteractions(config);
        getLogger().info(String.format("Loaded interactions for %d dispenser interactions", materialInteractions.size()));
        dispenserListener = new DispenserListener(this, materialInteractions);

        getServer().getPluginManager().registerEvents(dispenserListener, this);

        getLogger().info("Smart Dispensers Started");
    }

    @Override
    public void onDisable() {
        getLogger().info("Smart Dispensers Disabled");
    }

    private Map<Material, List<InteractionType>> parseMaterialInteractions(FileConfiguration config) {
        Map<Material, List<InteractionType>> map = new HashMap<>();
        List<Map<?, ?>> rawInteractions = (List<Map<?, ?>>) config.getMapList("interactions");
        for (Map<?, ?> interaction : rawInteractions) {
            Map.Entry entry = interaction.entrySet().stream().findFirst().get();
            String materialName = (String) entry.getKey();
            List<String> possibleInteractions = (List<String>) entry.getValue();
            map.put(
                    Material.matchMaterial(materialName),
                    possibleInteractions.stream()
                            .map(InteractionType::fromName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList()));
        }

        getLogger().info("" + map);
        return map;
    }
}
