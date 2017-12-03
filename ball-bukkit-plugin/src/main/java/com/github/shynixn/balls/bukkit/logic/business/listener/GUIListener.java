package com.github.shynixn.balls.bukkit.logic.business.listener;

import com.github.shynixn.balls.bukkit.BallsPlugin;
import com.github.shynixn.balls.bukkit.core.logic.business.listener.SimpleListener;
import com.github.shynixn.balls.bukkit.logic.business.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

/**
 * Created by Shynixn 2017.
 * <p>
 * Version 1.1
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017 by Shynixn
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
public class GUIListener extends SimpleListener {
    /**
     * Initializes a new listener by plugin.
     *
     * @param plugin plugin
     */
    public GUIListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof GUI) {
            final GUI gui = (GUI) event.getInventory().getHolder();
            try {
           //     gui.close();
            } catch (final Exception e) {
                BallsPlugin.logger().log(Level.WARNING, "Failed to close inventory.", e);
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof GUI) {
            final GUI gui = (GUI) event.getInventory().getHolder();
            try {
                gui.click(event.getCurrentItem(), event.getSlot());
                event.setCancelled(true);
                ((Player)event.getWhoClicked()).updateInventory();
            } catch (final Exception e) {
                BallsPlugin.logger().log(Level.WARNING, "Failed to click in inventory.", e);
            }
        }
    }
}
