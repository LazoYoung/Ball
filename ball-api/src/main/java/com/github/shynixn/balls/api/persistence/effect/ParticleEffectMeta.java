package com.github.shynixn.balls.api.persistence.effect;

import java.util.Collection;

/**
 * Handles particleEffects for players.
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
public interface ParticleEffectMeta<Location, Player, Material, T extends ParticleEffectMeta> extends EffectMeta<T> {
    /**
     * Sets the RGB colors of the particleEffect.
     *
     * @param red   red
     * @param green green
     * @param blue  blue
     * @return builder
     */
    T setColor(int red, int green, int blue);

    /**
     * Sets the color of the particleEffect.
     *
     * @param particleColor particleColor
     * @return builder
     */
    T setColor(ParticleColor particleColor);

    /**
     * Sets the color for note particleEffect.
     *
     * @param color color
     * @return builder
     */
    T setNoteColor(int color);

    /**
     * Sets the amount of particles of the particleEffect.
     *
     * @param amount amount
     * @return builder
     */
    T setAmount(int amount);

    /**
     * Sets the speed of the particleEffect.
     *
     * @param speed speed
     * @return builder
     */
    T setSpeed(double speed);

    /**
     * Sets the offsetX of the particleEffect.
     *
     * @param offsetX offsetX
     * @return builder
     */
    T setOffsetX(double offsetX);

    /**
     * Sets the offsetY of the particleEffect.
     *
     * @param offsetY offsetY
     * @return builder
     */
    T setOffsetY(double offsetY);

    /**
     * Sets the offsetZ of the particleEffect.
     *
     * @param offsetZ offsetZ
     * @return builder
     */
    T setOffsetZ(double offsetZ);

    /**
     * Sets the offset of the particleEffect.
     *
     * @param offsetX offsetX
     * @param offsetY offsetY
     * @param offsetZ offsetZ
     * @return instance
     */
    T setOffset(double offsetX, double offsetY, double offsetZ);

    /**
     * Sets the effectType of the particleEffect.
     *
     * @param name name
     * @return builder
     */
    T setEffectName(String name);

    /**
     * Sets the effectType of the particlEffect.
     *
     * @param type type
     * @return builder
     */
    T setEffectType(ParticleEffectType type);

    /**
     * Sets the blue of the RGB color.
     *
     * @param blue blue
     * @return builder
     */
     T setBlue(int blue);

    /**
     * Sets the red of the RGB color.
     *
     * @param red red
     * @return builder
     */
    T setRed(int red);

    /**
     * Sets the green of the RGB color.
     *
     * @param green green
     * @return builder
     */
    T setGreen(int green);

    /**
     * Sets the material of the particleEffect.
     *
     * @param material material
     * @return builder
     */
    T setMaterial(Material material);

    /**
     * Sets the data of the material of the particleEffect.
     *
     * @param data data
     * @return builder
     */
    T setData(Byte data);

    /**
     * Returns the effect of the particleEffect.
     *
     * @return effectName
     */
    String getEffectName();

    /**
     * Returns the particleEffectType of the particleEffect.
     *
     * @return effectType
     */
    ParticleEffectType getEffectType();

    /**
     * Returns the amount of particles of the particleEffect.
     *
     * @return amount
     */
    int getAmount();

    /**
     * Returns the speed of the particleEffect.
     *
     * @return speed
     */
    double getSpeed();

    /**
     * Returns the offsetX of the particleEffect.
     *
     * @return offsetX
     */
    double getOffsetX();

    /**
     * Returns the offsetY of the particleEffect.
     *
     * @return offsetY
     */
    double getOffsetY();

    /**
     * Returns the offsetZ of the particleEffect.
     *
     * @return offsetZ
     */
    double getOffsetZ();

    /**
     * Returns the RGB color blue of the particleEffect.
     *
     * @return blue
     */
    int getBlue();

    /**
     * Returns the RGB color red of the particleEffect.
     *
     * @return red
     */
    int getRed();

    /**
     * Returns the RGB color green of the particleEffect.
     *
     * @return green
     */
    int getGreen();

    /**
     * Returns the material of the particleEffect.
     *
     * @return material
     */
    Material getMaterial();

    /**
     * Returns the data of the particleEffect.
     *
     * @return data
     */
    Byte getData();

    /**
     * Copies the current builder.
     *
     * @return copy
     */
    ParticleEffectMeta copy();

    /**
     * Returns if the particleEffect is a color particleEffect.
     *
     * @return isColor
     */
    boolean isColorParticleEffect();

    /**
     * Returns if the particleEffect is a note particleEffect.
     *
     * @return isNote
     */
    boolean isNoteParticleEffect();

    /**
     * Returns if the particleEffect is a materialParticleEffect.
     *
     * @return isMaterial
     */
    boolean isMaterialParticleEffect();

    /**
     * Plays the effect at the given location to the given players.
     *
     * @param location location
     * @param players  players
     */
    void apply(Location location, Collection<Player> players);

    /**
     * Plays the effect at the given location to the given players.
     *
     * @param location location
     */
    void apply(Location location);

    /**
     * ParticleColors.
     */
    enum ParticleColor {
        BLACK(0, 0, 0),
        DARK_BLUE(0, 0, 170),
        DARK_GREEN(0, 170, 0),
        DARK_AQUA(0, 170, 170),
        DARK_RED(170, 0, 0),
        DARK_PURPLE(170, 0, 170),
        GOLD(255, 170, 0),
        GRAY(170, 170, 170),
        DARK_GRAY(85, 85, 85),
        BLUE(85, 85, 255),
        GREEN(85, 255, 85),
        AQUA(85, 255, 255),
        RED(255, 85, 85),
        LIGHT_PURPLE(255, 85, 255),
        YELLOW(255, 255, 85),
        WHITE(255, 255, 255);

        private final int red;
        private final int green;
        private final int blue;

        /**
         * Initializes a new particleColor.
         *
         * @param red   red
         * @param green green
         * @param blue  blue
         */
        ParticleColor(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        /**
         * Returns the RGB value red.
         *
         * @return red
         */
        public int getRed() {
            return this.red;
        }

        /**
         * Returns the RGB value green.
         *
         * @return green
         */
        public int getGreen() {
            return this.green;
        }

        /**
         * Returns the RGB value blue.
         *
         * @return blue
         */
        public int getBlue() {
            return this.blue;
        }
    }

    /**
     * ParticleEffectTypes
     */
    enum ParticleEffectType {
        EXPLOSION_NORMAL("explode", 0, "explosion"),
        EXPLOSION_LARGE("largeexplode", 1, "large_explosion"),
        EXPLOSION_HUGE("hugeexplosion", 2, "huge_explosion"),
        FIREWORKS_SPARK("fireworksSpark", 3, "fireworks_spark"),
        WATER_BUBBLE("bubble", 4, "water_bubble"),
        WATER_SPLASH("splash", 5, "water_splash"),
        WATER_WAKE("wake", 6, "water_wake"),
        SUSPENDED("suspended", 7, "suspended"),
        SUSPENDED_DEPTH("depthsuspend", 8, "suspended_depth"),
        CRIT("crit", 9, "critical_hit"),
        CRIT_MAGIC("magicCrit", 10, "magic_critical_hit"),
        SMOKE_NORMAL("smoke", 11, "smoke"),
        SMOKE_LARGE("largesmoke", 12, "large_smoke"),
        SPELL("spell", 13, "spell"),
        SPELL_INSTANT("instantSpell", 14, "instant_spell"),
        SPELL_MOB("mobSpell", 15, "instant_spell"),
        SPELL_MOB_AMBIENT("mobSpellAmbient", 16, "mob_spell"),
        SPELL_WITCH("witchMagic", 17, "witch_spell"),
        DRIP_WATER("dripWater", 18, "drip_water"),
        DRIP_LAVA("dripLava", 19, "drip_lava"),
        VILLAGER_ANGRY("angryVillager", 20, "angry_villager"),
        VILLAGER_HAPPY("happyVillager", 21, "happy_villager"),
        TOWN_AURA("townaura", 22, "town_aura"),
        NOTE("note", 23, "note"),
        PORTAL("portal", 24, "portal"),
        ENCHANTMENT_TABLE("enchantmenttable", 25, "enchanting_glyphs"),
        FLAME("flame", 26, "flame"),
        LAVA("lava", 27, "lava"),
        FOOTSTEP("footstep", 28, "footstep"),
        CLOUD("cloud", 29, "cloud"),
        REDSTONE("reddust", 30, "redstone_dust"),
        SNOWBALL("snowballpoof", 31, "snowball"),
        SNOW_SHOVEL("snowshovel", 32, "snow_shovel"),
        SLIME("slime", 33, "slime"),
        HEART("heart", 34, "heart"),
        BARRIER("barrier", 35, "barrier"),
        ITEM_CRACK("iconcrack", 36, "item_crack"),
        BLOCK_CRACK("blockcrack", 37, "block_crack"),
        BLOCK_DUST("blockdust", 38, "block_dust"),
        WATER_DROP("droplet", 39, "water_drop"),
        ITEM_TAKE("take", 40, "instant_spell"),
        MOB_APPEARANCE("mobappearance", 41, "guardian_appearance"),
        DRAGON_BREATH("dragonbreath", 42, "dragon_breath"),
        END_ROD("endRod", 43, "end_rod"),
        DAMAGE_INDICATOR("damageIndicator", 44, "damage_indicator"),
        SWEEP_ATTACK("sweepAttack", 45, "sweep_attack"),
        FALLING_DUST("fallingdust", 46, "falling_dust"),
        TOTEM("totem", 47, "instant_spell"),
        NONE("none", 102, "none"),
        SPIT("spit", 48, "instant_spell");

        private final String simpleName;
        private final int id;
        private String minecraftId;

        /**
         * Initializes a new particleEffectType.
         *
         * @param name name
         * @param id   id
         */
        ParticleEffectType(String name, int id, String minecraftId) {
            this.simpleName = name;
            this.id = id;
            this.minecraftId = minecraftId;
        }

        /**
         * Returns the vanilla minecraft id.
         *
         * @return vanilla id
         */
        public String getMinecraftId() {
            return this.minecraftId;
        }

        /**
         * Returns the id of the particleEffectType.
         *
         * @return id
         */
        public int getId() {
            return this.id;
        }

        /**
         * Returns the name of the particleEffectType.
         *
         * @return name
         */
        public String getSimpleName() {
            return this.simpleName;
        }
    }
}
