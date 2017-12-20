package com.github.shynixn.balls.api.persistence.effect;

import java.util.Collection;

/**
 * Handles soundEffects for players.
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
public interface SoundEffectMeta<Location, Player, T extends SoundEffectMeta> extends EffectMeta<T> {

    /**
     * Applies the sound at the given location.
     *
     * @param location location
     * @throws Exception ex
     */
    void applyToLocation(Location location) throws Exception;

    /**
     * Applies the sound to the given players.
     *
     * @param players players
     * @throws Exception ex
     */
    void applyToPlayers(Collection<Player> players) throws Exception;

    /**
     * Returns the name of the sound.
     *
     * @return name
     */
    String getName();

    /**
     * Sets the name of the sound.
     *
     * @param name name
     * @return builder
     */
    <T extends SoundEffectMeta> T setName(String name);

    /**
     * Returns the volume of the sound.
     *
     * @return volume
     */
    double getVolume();

    /**
     * Sets the volume of the sound.
     *
     * @param volume volume
     * @return builder
     */
    <T extends SoundEffectMeta> T setVolume(double volume);

    /**
     * Returns the pitch of the sound.
     *
     * @return pitch
     */
    double getPitch();

    /**
     * Sets the pitch of the sound.
     *
     * @param pitch pitch
     * @return builder
     */
    <T extends SoundEffectMeta> T setPitch(double pitch);

    /**
     * Clones the current object.
     *
     * @return clone
     */
    SoundEffectMeta clone();
}
