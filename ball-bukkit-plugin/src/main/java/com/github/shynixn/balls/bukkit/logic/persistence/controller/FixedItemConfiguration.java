package com.github.shynixn.balls.bukkit.logic.persistence.controller;

import com.github.shynixn.balls.api.persistence.controller.IFileController;
import com.github.shynixn.balls.bukkit.BallPlugin;
import com.github.shynixn.balls.bukkit.logic.persistence.entity.ItemContainer;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Copyright 2017 Shynixn
 * <p>
 * Do not remove this header!
 * <p>
 * Version 1.0
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class FixedItemConfiguration implements IFileController<ItemContainer> {

    private Plugin plugin;
    private final Map<String, ItemContainer> items = new HashMap<>();

    /**
     * Initializes a new engine repository
     *
     * @param plugin plugin
     */
    public FixedItemConfiguration(Plugin plugin) {
        super();
        if (plugin == null)
            throw new IllegalArgumentException("Plugin cannot be null!");
        this.plugin = plugin;
    }

    /**
     * Stores a new a item in the repository
     *
     * @param item item
     */
    @Override
    public void store(ItemContainer item) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Removes an item from the repository
     *
     * @param item item
     */
    @Override
    public void remove(ItemContainer item) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Returns the amount of items in the repository
     *
     * @return size
     */
    @Override
    public int size() {
        return this.items.size();
    }

    /**
     * Clears all items in the repository.
     */
    @Override
    public void clear() {
        this.items.clear();
    }

    /**
     * Returns all items from the repository as unmodifiableList
     *
     * @return items
     */
    @Override
    public List<ItemContainer> getAll() {
        return new ArrayList<>(this.items.values());
    }

    /**
     * Reloads the content from the fileSystem
     */
    @Override
    public void reload() {
        this.items.clear();
        this.plugin.reloadConfig();
        final Map<String, Object> data = ((MemorySection) this.plugin.getConfig().get("gui.items")).getValues(false);
        for (final String key : data.keySet()) {
            try {
                final ItemContainer container = new ItemContainer(0, ((MemorySection) data.get(key)).getValues(false));
                this.items.put(key, container);
            } catch (final Exception e) {
                BallPlugin.logger().log(Level.WARNING, "Failed to load guiItem " + key + '.', e);
            }
        }
    }

    /**
     * Returns the guiItem by the given name
     *
     * @param name name
     * @return item
     */
    public ItemContainer getGUIItemByName(String name) {
        if (this.items.containsKey(name))
            return this.items.get(name);
        return null;
    }

    /**
     * Returns if the given itemStack is a guiItemStack with the given name
     *
     * @param itemStack itemStack
     * @param name      name
     * @return itemStack
     */
    public boolean isGUIItem(Object itemStack, String name) {
        if (itemStack == null || name == null)
            return false;
        final ItemContainer container = this.getGUIItemByName(name);
        final ItemStack mItemStack = (ItemStack) itemStack;
        return mItemStack.getItemMeta() != null && container.getDisplayName().isPresent()
                && mItemStack.getItemMeta().getDisplayName() != null
                && mItemStack.getItemMeta().getDisplayName().equalsIgnoreCase(container.getDisplayName().get());
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     * However, implementers of this interface are strongly encouraged
     * to make their {@code close} methods idempotent.
     *
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public void close() throws Exception {
        this.plugin = null;
        this.items.clear();
    }
}
