package com.github.shynixn.balls.bukkit.core.logic.persistence.entity;

import com.github.shynixn.balls.api.bukkit.persistence.entity.BukkitParticleEffectMeta;
import com.github.shynixn.balls.api.persistence.effect.ParticleEffectMeta;
import com.github.shynixn.balls.bukkit.core.logic.business.CoreManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

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
public class ParticleEffectData extends EffectData<BukkitParticleEffectMeta> implements BukkitParticleEffectMeta, ConfigurationSerializable {
    private String effect;
    private int amount;
    private double speed;
    private double offsetX;
    private double offsetY;
    private double offsetZ;

    private Integer material;
    private Byte data;

    /**
     * Initializes a new ParticleEffectData
     */
    public ParticleEffectData() {
        super();
    }

    /**
     * Initializes a new ParticleEffectData with the given params
     *
     * @param effectName effect
     * @param amount     amount
     * @param speed      speed
     * @param offsetX    x
     * @param offsetY    y
     * @param offsetZ    z
     */
    public ParticleEffectData(String effectName, int amount, double speed, double offsetX, double offsetY, double offsetZ) {
        super();
        if (effectName == null)
            throw new IllegalArgumentException("Effect cannot be null!");
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be less than 0");
        if (getParticleEffectFromName(effectName) == null)
            throw new IllegalArgumentException("Cannot find particleEffect for name!");
        this.effect = effectName;
        this.amount = amount;
        this.speed = speed;
        this.offsetX = (float) offsetX;
        this.offsetY = (float) offsetY;
        this.offsetZ = (float) offsetZ;
    }

    /**
     * Parses the potioneffect out of the map
     *
     * @param items items
     * @throws Exception mapParseException
     */
    public ParticleEffectData(Map<String, Object> items) throws Exception {
        super(items);
        this.effect = (String) items.get("name");
        this.amount = (int) items.get("amount");
        this.speed = (double) items.get("speed");
        if (items.containsKey("offx"))
            this.offsetX = (float) (double) items.get("offx");
        if (items.containsKey("offy"))
            this.offsetY = (float) (double) items.get("offy");
        if (items.containsKey("offz"))
            this.offsetZ = ((float) (double) items.get("offz"));
        if (items.containsKey("id"))
            this.material = (Integer) items.get("id");
        if (items.containsKey("damage"))
            this.data = (byte) (int) (Integer) items.get("damage");
        if (items.containsKey("red"))
            this.setRed((Integer) items.get("red"));
        if (items.containsKey("green"))
            this.setGreen((Integer) items.get("green"));
        if (items.containsKey("blue"))
            this.setBlue((Integer) items.get("blue"));
    }

    /**
     * Sets the RGB colors of the particleEffect
     *
     * @param red   red
     * @param green green
     * @param blue  blue
     * @return builder
     */
    @Override
    public ParticleEffectData setColor(int red, int green, int blue) {
        this.setRed(red);
        this.setBlue(blue);
        this.setGreen(green);
        return this;
    }

    /**
     * Sets the color of the particleEffect
     *
     * @param particleColor particleColor
     * @return builder
     */
    @Override
    public ParticleEffectData setColor(ParticleEffectData.ParticleColor particleColor) {
        if (particleColor == null)
            throw new IllegalArgumentException("Color cannot be null!");
        this.setColor(particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue());
        return this;
    }

    /**
     * Sets the color for note particleEffect
     *
     * @param color color
     * @return builder
     */
    @Override
    public ParticleEffectData setNoteColor(int color) {
        if (color > 20 || color < 0) {
            this.offsetX = 5;
        } else {
            this.offsetX = color;
        }
        return this;
    }

    /**
     * Sets the amount of particles of the particleEffect
     *
     * @param amount amount
     * @return builder
     */
    @Override
    public ParticleEffectData setAmount(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount cannot be less than 0");
        this.amount = amount;
        return this;
    }

    /**
     * Sets the speed of the particleEffect
     *
     * @param speed speed
     * @return builder
     */
    @Override
    public ParticleEffectData setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    /**
     * Sets the offsetX of the particleEffect
     *
     * @param offsetX offsetX
     * @return builder
     */
    @Override
    public ParticleEffectData setOffsetX(double offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    /**
     * Sets the offsetY of the particleEffect
     *
     * @param offsetY offsetY
     * @return builder
     */
    @Override
    public ParticleEffectData setOffsetY(double offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    /**
     * Sets the offsetZ of the particleEffect
     *
     * @param offsetZ offsetZ
     * @return builder
     */
    @Override
    public ParticleEffectData setOffsetZ(double offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    /**
     * Sets the offset of the particleEffect
     *
     * @param offsetX offsetX
     * @param offsetY offsetY
     * @param offsetZ offsetZ
     * @return instance
     */
    @Override
    public ParticleEffectData setOffset(double offsetX, double offsetY, double offsetZ) {
        this.setOffsetX(offsetX);
        this.setOffsetY(offsetY);
        this.setOffsetZ(offsetZ);
        return this;
    }

    /**
     * Sets the effectType of the particleEffect
     *
     * @param name name
     * @return builder
     */
    @Override
    public ParticleEffectData setEffectName(String name) {
        if (name == null)
            throw new IllegalArgumentException("Name cannot be null!");
        this.effect = name;
        return this;
    }

    /**
     * Sets the effectType of the particlEffect
     *
     * @param type type
     * @return builder
     */
    @Override
    public ParticleEffectData setEffectType(ParticleEffectData.ParticleEffectType type) {
        if (type == null)
            throw new IllegalArgumentException("Type cannot be null!");
        this.effect = type.getSimpleName();
        return this;
    }

    /**
     * Sets the blue of the RGB color
     *
     * @param blue blue
     * @return builder
     */
    @Override
    public ParticleEffectData setBlue(int blue) {
        this.offsetZ = blue / 255.0F;
        return this;
    }

    /**
     * Sets the red of the RGB color
     *
     * @param red red
     * @return builder
     */
    @Override
    public ParticleEffectData setRed(int red) {
        this.offsetX = red / 255.0F;
        if (red == 0) {
            this.offsetX = Float.MIN_NORMAL;
        }
        return this;
    }

    /**
     * Sets the green of the RGB color
     *
     * @param green green
     * @return builder
     */
    @Override
    public ParticleEffectData setGreen(int green) {
        this.offsetY = green / 255.0F;
        return this;
    }

    /**
     * Sets the material of the particleEffect.
     *
     * @param material material
     * @return builder
     */
    @Override
    public BukkitParticleEffectMeta setMaterial(Material material) {
        if (material != null) {
            this.material = material.getId();
        } else {
            this.material = null;
        }
        return this;
    }

    /**
     * Sets the data of the material of the particleEffect
     *
     * @param data data
     * @return builder
     */
    @Override
    public ParticleEffectData setData(Byte data) {
        this.data = data;
        return this;
    }

    /**
     * Returns the effect of the particleEffect
     *
     * @return effectName
     */
    @Override
    public String getEffectName() {
        return this.effect;
    }

    /**
     * Returns the particleEffectType of the particleEffect
     *
     * @return effectType
     */
    @Override
    public ParticleEffectData.ParticleEffectType getEffectType() {
        return getParticleEffectFromName(this.effect);
    }

    /**
     * Returns the amount of particles of the particleEffect
     *
     * @return amount
     */
    @Override
    public int getAmount() {
        return this.amount;
    }

    /**
     * Returns the speed of the particleEffect
     *
     * @return speed
     */
    @Override
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Returns the offsetX of the particleEffect
     *
     * @return offsetX
     */
    @Override
    public double getOffsetX() {
        return this.offsetX;
    }

    /**
     * Returns the offsetY of the particleEffect
     *
     * @return offsetY
     */
    @Override
    public double getOffsetY() {
        return this.offsetY;
    }

    /**
     * Returns the offsetZ of the particleEffect
     *
     * @return offsetZ
     */
    @Override
    public double getOffsetZ() {
        return this.offsetZ;
    }

    /**
     * Returns the RGB color blue of the particleEffect
     *
     * @return blue
     */
    @Override
    public int getBlue() {
        return (int) this.offsetZ * 255;
    }

    /**
     * Returns the RGB color red of the particleEffect
     *
     * @return red
     */
    @Override
    public int getRed() {
        return (int) this.offsetX * 255;
    }

    /**
     * Returns the RGB color green of the particleEffect
     *
     * @return green
     */
    @Override
    public int getGreen() {
        return (int) this.offsetY * 255;
    }

    /**
     * Returns the material of the particleEffect.
     *
     * @return material
     */
    @Override
    public Material getMaterial() {
        if (this.material == null || Material.getMaterial(this.material) == null)
            return null;
        return Material.getMaterial(this.material);
    }

    /**
     * Returns the data of the particleEffect
     *
     * @return data
     */
    @Override
    public Byte getData() {
        return this.data;
    }

    /**
     * Copies the current builder.
     *
     * @return copy
     */
    @Override
    public ParticleEffectMeta copy() {
        final ParticleEffectData particle = new ParticleEffectData();
        particle.effect = this.effect;
        particle.amount = this.amount;
        particle.offsetX = this.offsetX;
        particle.offsetY = this.offsetY;
        particle.offsetZ = this.offsetZ;
        particle.speed = this.speed;
        particle.material = this.material;
        particle.data = this.data;
        return particle;
    }

    /**
     * Returns if the particleEffect is a color particleEffect
     *
     * @return isColor
     */
    @Override
    public boolean isColorParticleEffect() {
        return this.effect.equalsIgnoreCase(ParticleEffectData.ParticleEffectType.SPELL_MOB.getSimpleName())
                || this.effect.equalsIgnoreCase(ParticleEffectData.ParticleEffectType.SPELL_MOB_AMBIENT.getSimpleName())
                || this.effect.equalsIgnoreCase(ParticleEffectData.ParticleEffectType.REDSTONE.getSimpleName())
                || this.effect.equalsIgnoreCase(ParticleEffectData.ParticleEffectType.NOTE.getSimpleName());
    }

    /**
     * Returns if the particleEffect is a note particleEffect
     *
     * @return isNote
     */
    @Override
    public boolean isNoteParticleEffect() {
        return this.effect.equalsIgnoreCase(ParticleEffectData.ParticleEffectType.NOTE.getSimpleName());
    }

    /**
     * Returns if the particleEffect is a materialParticleEffect
     *
     * @return isMaterial
     */
    @Override
    public boolean isMaterialParticleEffect() {
        return this.effect.equalsIgnoreCase(ParticleEffectData.ParticleEffectType.BLOCK_CRACK.getSimpleName())
                || this.effect.equalsIgnoreCase(ParticleEffectData.ParticleEffectType.BLOCK_DUST.getSimpleName())
                || this.effect.equalsIgnoreCase(ParticleEffectData.ParticleEffectType.ITEM_CRACK.getSimpleName());
    }

    /**
     * Plays the effect at the given location to the given players.
     *
     * @param location location
     * @param players  players
     */
    @Override
    public void apply(Location location, Collection<Player> players) {
        if (players == null)
            throw new IllegalArgumentException("Players cannot be null!");
        this.applyTo(location, players.toArray(new Player[players.size()]));
    }

    /**
     * Plays the effect at the given location to the given players.
     *
     * @param location location
     */
    @Override
    public void apply(Location location) {
        this.applyTo(location, location.getWorld().getPlayers().toArray(new Player[location.getWorld().getPlayers().size()]));
    }

    /**
     * Plays the effect at the given location to the given players.
     *
     * @param location location
     * @param players  players
     */
    private void applyTo(Location location, Player... players) {
        try {
            if (location == null)
                throw new IllegalArgumentException("Location cannot be null!");
            if (this.effect.equals("none"))
                return;
            final Player[] playingPlayers;
            if (players.length == 0) {
                playingPlayers = location.getWorld().getPlayers().toArray(new Player[location.getWorld().getPlayers().size()]);
            } else {
                playingPlayers = players;
            }
            final float speed;
            final int amount;
            if (this.effect.equals(ParticleEffectData.ParticleEffectType.REDSTONE.getSimpleName()) || this.effect.equals(ParticleEffectData.ParticleEffectType.NOTE.getSimpleName())) {
                amount = 0;
                speed = 1.0F;
            } else {
                amount = this.getAmount();
                speed = (float) this.getSpeed();
            }
            final Object enumParticle = invokeMethod(null, findClass("net.minecraft.server.VERSION.EnumParticle"), "valueOf", new Class[]{String.class}, new Object[]{this.getEffectType().name().toUpperCase()});
            int[] additionalInfo = null;
            if (this.getMaterial() != null) {
                if (this.getEffectType() == ParticleEffectData.ParticleEffectType.ITEM_CRACK) {
                    additionalInfo = new int[]{this.material, this.getData()};
                } else {
                    additionalInfo = new int[]{this.material, this.getData() << 12};
                }
            }
            final Object packet = invokeConstructor(findClass("net.minecraft.server.VERSION.PacketPlayOutWorldParticles"), new Class[]{
                    enumParticle.getClass(),
                    boolean.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    float.class,
                    int.class,
                    int[].class
            }, new Object[]{
                    enumParticle,
                    isLongDistance(location, players),
                    (float) location.getX(),
                    (float) location.getY(),
                    (float) location.getZ(),
                    (float) this.getOffsetX(),
                    (float) this.getOffsetY(),
                    (float) this.getOffsetZ(),
                    speed,
                    amount,
                    additionalInfo
            });
            for (final Player player : playingPlayers) {
                sendPacket(player, packet);
            }
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            CoreManager.getLogger().log(Level.WARNING, "Failed to send packet.", e);
        }
    }

    /**
     * Checks if 2 builders are equal
     *
     * @param o secondBuilder
     * @return isSame
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final ParticleEffectData that = (ParticleEffectData) o;
        return this.amount == that.amount
                && Double.compare(that.speed, this.speed) == 0
                && Double.compare(that.offsetX, this.offsetX) == 0
                && Double.compare(that.offsetY, this.offsetY) == 0
                && Double.compare(that.offsetZ, this.offsetZ) == 0
                & (this.effect != null ? this.effect.equals(that.effect) : that.effect == null)
                && Objects.equals(this.material, that.material) && (this.data != null ? this.data.equals(that.data) : that.data == null);
    }

    /**
     * Displays the builder as string
     *
     * @return string
     */
    @Override
    public String toString() {
        return "effect {" + "name " + this.effect + " amound " + this.amount + " speed " + this.speed + '}';
    }

    /**
     * Serializes the particleEffect data to be stored to the filesystem
     *
     * @return serializedContent
     */
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> map = super.serialize();
        map.put("effect", this.effect.toUpperCase());
        map.put("amount", this.amount);
        map.put("speed", this.speed);
        final Map<String, Object> tmp3 = new LinkedHashMap<>();
        tmp3.put("x", this.offsetX);
        tmp3.put("y", this.offsetY);
        tmp3.put("z", this.offsetZ);
        map.put("size", tmp3);
        final Map<String, Object> tmp2 = new LinkedHashMap<>();
        if (this.material != null) {
            tmp2.put("material", this.material);
            tmp2.put("damage", this.data);
            map.put("block", tmp2);
        }
        return map;
    }

    /**
     * Returns a text of all particleEffects to let the user easily view them
     *
     * @return potionEffects
     */
    public static String getParticlesText() {
        final StringBuilder builder = new StringBuilder();
        for (final ParticleEffectData.ParticleEffectType particleEffect : ParticleEffectData.ParticleEffectType.values()) {
            if (builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(particleEffect.getSimpleName());
        }
        return builder.toString();
    }

    /**
     * Returns the particleEffectType from name
     *
     * @param name name
     * @return particleEffectType
     */
    public static ParticleEffectData.ParticleEffectType getParticleEffectFromName(String name) {
        for (final ParticleEffectData.ParticleEffectType particleEffect : ParticleEffectData.ParticleEffectType.values()) {
            if (name != null && particleEffect.getSimpleName().equalsIgnoreCase(name))
                return particleEffect;
        }
        return null;
    }

    /**
     * Sends a packet to the client player
     *
     * @param player player
     * @param packet packet
     * @throws ClassNotFoundException    exception
     * @throws IllegalAccessException    exception
     * @throws NoSuchMethodException     exception
     * @throws InvocationTargetException exception
     * @throws NoSuchFieldException      exception
     */

    private static void sendPacket(Player player, Object packet) throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        final Object craftPlayer = findClass("org.bukkit.craftbukkit.VERSION.entity.CraftPlayer").cast(player);
        final Object entityPlayer = invokeMethod(craftPlayer, craftPlayer.getClass(), "getHandle", new Class[]{}, new Object[]{});
        final Field field = findClass("net.minecraft.server.VERSION.EntityPlayer").getDeclaredField("playerConnection");
        field.setAccessible(true);
        final Object connection = field.get(entityPlayer);
        invokeMethod(connection, connection.getClass(), "sendPacket", new Class[]{packet.getClass().getInterfaces()[0]}, new Object[]{packet});
    }

    /**
     * Invokes a constructor by the given parameters
     *
     * @param clazz      clazz
     * @param paramTypes paramTypes
     * @param params     params
     * @return instance
     * @throws NoSuchMethodException     exception
     * @throws IllegalAccessException    exception
     * @throws InvocationTargetException exception
     * @throws InstantiationException    exception
     */
    private static Object invokeConstructor(Class<?> clazz, Class[] paramTypes, Object[] params) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Constructor constructor = clazz.getDeclaredConstructor(paramTypes);
        constructor.setAccessible(true);
        return constructor.newInstance(params);
    }

    /**
     * Invokes a method by the given parameters
     *
     * @param instance   instance
     * @param clazz      clazz
     * @param name       name
     * @param paramTypes paramTypes
     * @param params     params
     * @return returnedObject
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException    exception
     * @throws NoSuchMethodException     exception
     */
    private static Object invokeMethod(Object instance, Class<?> clazz, String name, Class[] paramTypes, Object[]
            params) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final Method method = clazz.getDeclaredMethod(name, paramTypes);
        method.setAccessible(true);
        return method.invoke(instance, params);
    }

    /**
     * Finds a class regarding of the server Version
     *
     * @param name name
     * @return clazz
     * @throws ClassNotFoundException exception
     */
    private static Class<?> findClass(String name) throws ClassNotFoundException {
        return Class.forName(name.replace("VERSION", Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]));
    }

    /**
     * Checks if longDistance attribute is necessary
     *
     * @param location location
     * @param players  players
     * @return isNecessary
     */
    private static boolean isLongDistance(Location location, Player[] players) {
        for (final Player player : players) {
            if (location.getWorld().getName().equals(player.getLocation().getWorld().getName())
                    && player.getLocation().distanceSquared(location) > 65536) {
                return true;
            }
        }
        return false;
    }
}