package config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import core.Core;

public class ConfigManager implements IConfigManager {

    /**
     * Instanca konfiguracije.
     */
    private IConfig config;

    private ConfigManager() {
    }

    /**
     * Vraća instancu komponente.
     *
     * @return Instanca komponente.
     */
    public static ConfigManager getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public IConfig initConfig(String path) {
        String json = Core.getInstance().IODriver().readConfig(path);

        Gson gson = new Gson();
        if (json == null) {
            // nije uspelo čitanje ili fajla nema
            config = new Config();
            json = gson.toJson(config);
            Core.getInstance().IODriver().writeConfig(json, path);
            return config;
        }

        try {
            // čitanje
            config = gson.fromJson(json, Config.class);
        } catch (JsonSyntaxException e) {
            // nije uspelo čitanje, fajl koruptovan
            config = new Config();
            json = gson.toJson(config);
            Core.getInstance().IODriver().writeConfig(json, path);
        }
        return config;
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    @Override
    public String saveConfig() {
        return null;
    }

    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }
}
