package config;

import org.jetbrains.annotations.Nullable;

/**
 * Komponenta za konfiguraciju. Koristi se za učitavanje i menadžment informacija o skladištu, menadžment korisnika,
 * privilegija, itd.
 * <p>
 * Konfiguracija se čuva u JSON formatu. {@link io.IODriver} se predaje samo String, i iz njega se samo čita String,
 * tako da implementaciona komponenta ne mora parsirati JSON već samo predaje "sirov" zapis komponenti.
 * <p>
 * Struktura JSON fajla je sledeća:
 * <pre>
 * {
 *     "storage": {
 *         "formatBlacklist": [],
 *         "maxSize": 4096
 *     },
 *     "users": [
 *         {
 *             "username": "",
 *             "password": "",
 *             "privileges": [
 *                 {
 *                     "object": "",
 *                     "type": "INIT_STORAGE"
 *                 }
 *             ]
 *         }
 *     ]
 * }
 * </pre>
 */
public interface IConfigManager {

    /**
     * Inicijalizuje konfiguraciju {@link IConfig} sa zadate putanje, preko IOAdaptera, u JSON formatu.
     *
     * @param path Putanja sa koje će se preuzeti ili inicijalizovati konfiguracioni fajl.
     * @return {@link IConfig} ukoliko je uspešno instancirano, null u protivnom.
     * @see io.IODriver
     */
    IConfig initConfig(String path);

    /**
     * Vraća trenutnu instancu konfiguracije {@link IConfig} ili null ukoliko nije inicijalizovana.
     *
     * @return Trenutna instanca konfiguracije {@link IConfig} ili null ukoliko nije inicijalizovana.
     */
    @Nullable
    IConfig getConfig();

    /**
     * Piše konfiguraciju za skladište putem {@link io.IODriver#writeConfig(String, String)} metode.
     */
    void saveConfig();
}
