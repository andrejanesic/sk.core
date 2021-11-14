package com.raf.sk.core.actions;

import com.raf.sk.core.core.Core;

/**
 * Akcija deinicijalizacije skladišta.
 */
public class ActionDeinitStorage implements IAction {
    @Override
    public Object run() {
        Core.getInstance().StorageManager().deinitStorage();
        return null;
    }

    @Override
    public Object undo() {
        return null;
    }
}
