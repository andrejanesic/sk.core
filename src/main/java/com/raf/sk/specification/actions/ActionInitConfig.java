package com.raf.sk.specification.actions;

import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.exceptions.IActionUndoImpossibleException;

/**
 * Ovo je "početna" akcija koju program mora izvršiti kako bi inicijalizovao konfiguracionu komponentu
 * {@link com.raf.sk.specification.config.ConfigManager}.
 *
 * @see com.raf.sk.specification.config.ConfigManager
 * @see com.raf.sk.specification.io.IODriver
 * @see com.raf.sk.specification.user.UserManager
 */
public class ActionInitConfig implements IAction {

    /**
     * Putnaja do konfiguracionog fajla u sistemu.
     */
    private String path;

    /**
     * Podrazumevani konstruktor.
     *
     * @param path Putnaja do konfiguracionog fajla u sistemu.
     */
    public ActionInitConfig(String path) {
        this.path = path;
    }

    @Override
    public Object run() {
        if (Core.getInstance().ConfigManager().getConfig() != null)
            return true;

        Core.getInstance().ConfigManager().initConfig(path);
        return true;
    }

    @Override
    public Object undo() {
        throw new IActionUndoImpossibleException("configuration initialization");
    }
}
