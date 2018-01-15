package com.github.shynixn.ball.bukkit.logic.persistence.configuration

import com.github.shynixn.ball.bukkit.BallPlugin
import org.bukkit.ChatColor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

/**
 * Created by Shynixn 2018.
 * <p>
 * Version 1.2
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2018 by Shynixn
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
open internal class SimpleConfig {

    /** [plugin] is an instance of the Ball plugin. */
    var plugin: Plugin? = null
        private set

    /**
     * Reloads the plugin configuration.
     */
    open fun reload() {
        this.plugin = JavaPlugin.getPlugin(BallPlugin::class.java)
        this.plugin?.reloadConfig()
    }

    /**
     * Returns configuration data.
     *
     * @param path path
     * @return data
     */
    internal fun <T> getData(path: String): T? {
        var data = this.plugin?.config?.get(path)
        if (data != null && data is String) {
            data = ChatColor.translateAlternateColorCodes('&', data)
        }
        return data as T
    }
}