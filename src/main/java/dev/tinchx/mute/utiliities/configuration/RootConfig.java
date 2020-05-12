package dev.tinchx.mute.utiliities.configuration;

import com.google.common.collect.Lists;
import dev.tinchx.mute.MutePlugin;
import dev.tinchx.mute.utiliities.chat.ColorText;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class RootConfig {

    private JavaPlugin plugin = MutePlugin.getInstance();
    private Logger logger = Logger.getLogger("Minecraft");
    private File file;
    @Getter
    private YamlConfiguration configuration;

    public RootConfig(String fileName, ConfigExt ext) {
        if (fileName == null || fileName.isEmpty()) {
            logger.warning("File's name cannot be null.");
        } else if (ext == null) {
            logger.warning("File's extension cannot be null.");
        } else {
            file = new File(plugin.getDataFolder(), fileName + ext.getExtension());

            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdir();
                logger.fine("Data folder directory created.");
            }

            if (!file.exists()) {
                try {
                    file.createNewFile();
                    plugin.saveResource(fileName + ext.getExtension(), true);
                    logger.fine("File named '" + fileName + "' : extension '" + ext.getExtension() + "', created...");
                } catch (IOException e) {
                    logger.warning("File named '" + fileName + "' could not be created for (" + e.getMessage() + ')');
                }
            }

            configuration = YamlConfiguration.loadConfiguration(file);
        }
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        save();
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public boolean contains(String s) {
        return configuration.contains(s);
    }

    public List<String> getStringList(String path) {
        if (contains(path)) {
            return configuration.getStringList(path);
        }
        return Collections.singletonList("Path '" + path + "' was not found on '" + file.getName() + "'.");
    }

    public String getString(String path) {
        return ColorText.translate(configuration.getString(path, "Path '" + path + "' was not found on '" + file.getName() + "'."));
    }

    public List<Integer> getIntList(String path) {
        if (contains(path)) {
            return configuration.getIntegerList(path);
        }
        return Lists.newArrayList();
    }

    public int getInt(String path) {
        return configuration.getInt(path, 0);
    }

    public List<Boolean> getBooleanList(String path) {
        if (contains(path)) {
            return configuration.getBooleanList(path);
        }
        return Lists.newArrayList();
    }

    public boolean getBoolean(String path) {
        return configuration.getBoolean(path, false);
    }

    public List<Double> getDoubleList(String path) {
        if (contains(path)) {
            return configuration.getDoubleList(path);
        }
        return Lists.newArrayList();
    }

    public double getDouble(String path) {
        return configuration.getDouble(path, 0);
    }

    public ItemStack getItemByPath(String path) {
        return configuration.getItemStack(path, null);
    }

    public void set(String path, Object o) {
        configuration.set(path, o);
    }

    public void header(String header) {
        configuration.options().header(header);
    }
}
