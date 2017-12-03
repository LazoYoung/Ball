package com.github.shynixn.balls.bukkit.core.nms.v1_12_R1;

import com.github.shynixn.balls.api.bukkit.event.BallWallCollideEvent;
import com.github.shynixn.balls.api.business.entity.Ball;
import com.github.shynixn.balls.api.persistence.BounceObject;
import com.github.shynixn.balls.bukkit.core.logic.business.helper.ReflectionUtils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Rabbit hitbox implementation for minecraft 1.12.0-1.12.2.
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
public final class CustomHitbox extends EntityArmorStand {

    private Vector vectorCache;
    private int rollingTimes = 20;

    private final Ball ball;

    private int knockBackBumper;

    public CustomHitbox(Location location, Ball ball) {
        super(((CraftWorld) location.getWorld()).getHandle());
        this.ball = ball;
        final World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        mcWorld.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("invulnerable", true);
        compound.setBoolean("Invisible", true);
        compound.setBoolean("PersistenceRequired", true);
        compound.setBoolean("NoBasePlate", true);
        this.a(compound);
        this.getSpigotEntity().setCustomName("ResourceBallsPlugin");
        this.getSpigotEntity().setCustomNameVisible(false);
    }

    ArmorStand getSpigotEntity() {
        return (ArmorStand) this.getBukkitEntity();
    }

    private void spigotTimings(boolean started) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("org.bukkit.craftbukkit.v1_12_R1.SpigotTimings");
        } catch (final ClassNotFoundException ignored) {

        }
        if (clazz != null) {
            final Object moveTimer;
            try {
                moveTimer = ReflectionUtils.invokeFieldByClass(clazz, "entityMoveTimer");
                if (started) {
                    ReflectionUtils.invokeMethodByObject(moveTimer, "startTiming", new Class[]{}, new Object[]{});
                } else {
                    ReflectionUtils.invokeMethodByObject(moveTimer, "stopTiming", new Class[]{}, new Object[]{});
                }
            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                //  BlockBallPlugin.logger().log(Level.WARNING, "Failed to invoke entityMoveTimer.");
            }
        }
    }

    public void setVelocity(Vector velocity) {
        this.getSpigotEntity().setVelocity(velocity);
        this.rollingTimes = 60;
        this.vectorCache = velocity;
        System.out.println("VECTOR: " + velocity);
    }

    @Override
    public void move(EnumMoveType enummovetype, double d0, double d1, double d2) {

        Vector starter = this.getSpigotEntity().getVelocity();
        if (this.rollingTimes >= 0 && this.vectorCache != null) {
          /*  this.setVelocity(vectorCache);
            this.vectorCache.setX(this.motX / 28);
            this.vectorCache.setY(this.motY / 28);
            this.vectorCache.setZ(this.motZ / 28);*/

            this.rollingTimes--;
        }

        this.spigotTimings(true);
         if(this.knockBackBumper > 0) {
            this.knockBackBumper--;
        }


        if (this.noclip) {
            this.a(this.getBoundingBox().d(d0, d1, d2));
            this.recalcPosition();
        } else {
            try {
                this.checkBlockCollisions();
            } catch (final Throwable var79) {
                final CrashReport crashreport = CrashReport.a(var79, "Checking entity block collision");
                final CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity being checked for collision");
                this.appendEntityCrashDetails(crashreportsystemdetails);
                throw new ReportedException(crashreport);
            }

            if (d0 == 0.0D && d1 == 0.0D && d2 == 0.0D && this.isVehicle() && this.isPassenger()) {
                return;
            }

            this.world.methodProfiler.a("move");
            if (this.E) {
                this.E = false;
                d0 *= 0.25D;
                d1 *= 0.05000000074505806D;
                d2 *= 0.25D;
                this.motX = 0.0D;
                this.motY = 0.0D;
                this.motZ = 0.0D;
            }

            double d6 = d0;
            final double d7 = d1;
            double d8 = d2;
            final List list = this.world.getCubes(this, this.getBoundingBox().b(d0, d1, d2));
            final AxisAlignedBB axisalignedbb = this.getBoundingBox();
            int i;
            int j;
            if (d1 != 0.0D) {
                i = 0;

                for (j = list.size(); i < j; ++i) {
                    d1 = ((AxisAlignedBB) list.get(i)).b(this.getBoundingBox(), d1);
                }

                this.a(this.getBoundingBox().d(0.0D, d1, 0.0D));
            }

            if (d0 != 0.0D) {
                i = 0;

                for (j = list.size(); i < j; ++i) {
                    d0 = ((AxisAlignedBB) list.get(i)).a(this.getBoundingBox(), d0);
                }

                if (d0 != 0.0D) {
                    this.a(this.getBoundingBox().d(d0, 0.0D, 0.0D));
                }
            }

            if (d2 != 0.0D) {
                i = 0;

                for (j = list.size(); i < j; ++i) {
                    d2 = ((AxisAlignedBB) list.get(i)).c(this.getBoundingBox(), d2);
                }

                if (d2 != 0.0D) {
                    this.a(this.getBoundingBox().d(0.0D, 0.0D, d2));
                }
            }

            final boolean flag1 = this.onGround || d1 != d7 && d1 < 0.0D;
            double d10;
            if (this.P > 0.0F && flag1 && (d6 != d0 || d8 != d2)) {
                final double d11 = d0;
                final double d12 = d1;
                final double d13 = d2;
                final AxisAlignedBB event = this.getBoundingBox();
                this.a(axisalignedbb);
                d1 = (double) this.P;
                final List event1 = this.world.getCubes(this, this.getBoundingBox().b(d6, d1, d8));
                AxisAlignedBB axisalignedbb2 = this.getBoundingBox();
                final AxisAlignedBB f = axisalignedbb2.b(d6, 0.0D, d8);
                d10 = d1;
                int k = 0;

                for (final int l = event1.size(); k < l; ++k) {
                    d10 = ((AxisAlignedBB) event1.get(k)).b(f, d10);
                }

                axisalignedbb2 = axisalignedbb2.d(0.0D, d10, 0.0D);
                double d14 = d6;
                int i1 = 0;

                for (final int j1 = event1.size(); i1 < j1; ++i1) {
                    d14 = ((AxisAlignedBB) event1.get(i1)).a(axisalignedbb2, d14);
                }

                axisalignedbb2 = axisalignedbb2.d(d14, 0.0D, 0.0D);
                double d15 = d8;
                int k1 = 0;

                for (final int axisalignedbb4 = event1.size(); k1 < axisalignedbb4; ++k1) {
                    d15 = ((AxisAlignedBB) event1.get(k1)).c(axisalignedbb2, d15);
                }

                axisalignedbb2 = axisalignedbb2.d(0.0D, 0.0D, d15);
                AxisAlignedBB var85 = this.getBoundingBox();
                double d16 = d1;
                int i2 = 0;

                for (final int j2 = event1.size(); i2 < j2; ++i2) {
                    d16 = ((AxisAlignedBB) event1.get(i2)).b(var85, d16);
                }

                var85 = var85.d(0.0D, d16, 0.0D);
                double d17 = d6;
                int k2 = 0;

                for (final int l2 = event1.size(); k2 < l2; ++k2) {
                    d17 = ((AxisAlignedBB) event1.get(k2)).a(var85, d17);
                }

                var85 = var85.d(d17, 0.0D, 0.0D);
                double d18 = d8;
                int i3 = 0;

                for (final int j3 = event1.size(); i3 < j3; ++i3) {
                    d18 = ((AxisAlignedBB) event1.get(i3)).c(var85, d18);
                }

                var85 = var85.d(0.0D, 0.0D, d18);
                final double d19 = d14 * d14 + d15 * d15;
                final double d20 = d17 * d17 + d18 * d18;
                if (d19 > d20) {
                    d0 = d14;
                    d2 = d15;
                    d1 = -d10;
                    this.a(axisalignedbb2);
                } else {
                    d0 = d17;
                    d2 = d18;
                    d1 = -d16;
                    this.a(var85);
                }

                int k3 = 0;

                for (final int l3 = event1.size(); k3 < l3; ++k3) {
                    d1 = ((AxisAlignedBB) event1.get(k3)).b(this.getBoundingBox(), d1);
                }

                this.a(this.getBoundingBox().d(0.0D, d1, 0.0D));
                if (d11 * d11 + d13 * d13 >= d0 * d0 + d2 * d2) {
                    d0 = d11;
                    d1 = d12;
                    d2 = d13;
                    this.a(event);
                }
            }

            this.world.methodProfiler.b();
            this.world.methodProfiler.a("rest");
            this.recalcPosition();
            this.positionChanged = d6 != d0 || d8 != d2;
            this.B = d1 != d7;
            this.onGround = this.B && d7 < 0.0D;
            this.C = this.positionChanged || this.B;
            j = MathHelper.floor(this.locX);
            final int i4 = MathHelper.floor(this.locY - 0.20000000298023224D);
            final int j4 = MathHelper.floor(this.locZ);
            BlockPosition blockposition = new BlockPosition(j, i4, j4);
            IBlockData iblockdata = this.world.getType(blockposition);
            if (iblockdata.getMaterial() == Material.AIR) {
                final BlockPosition block1 = blockposition.down();
                final IBlockData flag2 = this.world.getType(block1);
                final Block var80 = flag2.getBlock();
                if (var80 instanceof BlockFence || var80 instanceof BlockCobbleWall || var80 instanceof BlockFenceGate) {
                    iblockdata = flag2;
                    blockposition = block1;
                }
            }

            this.a(d1, this.onGround, iblockdata, blockposition);
            if (d6 != d0) {
                this.motX = 0.0D;
            }

            if (d8 != d2) {
                this.motZ = 0.0D;
            }

            final Block var86 = iblockdata.getBlock();
            if (d7 != d1) {
                var86.a(this.world, this);
            }

            if (this.positionChanged) {
                org.bukkit.block.Block var81 = this.world.getWorld().getBlockAt(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
                if (d6 > d0) {
                    var81 = var81.getRelative(BlockFace.EAST);
                    final Vector n = new Vector(-1, 0, 0);
                    this.applyKnockBack(starter, n);
                } else if (d6 < d0) {
                    var81 = var81.getRelative(BlockFace.WEST);
                    final Vector n = new Vector(1, 0, 0);
                    this.applyKnockBack(starter, n);
                } else if (d8 > d2) {
                    var81 = var81.getRelative(BlockFace.SOUTH);
                    final Vector n = new Vector(0, 0, -1);
                    System.out.println("SOUTH: " + n);
                    this.applyKnockBack(starter, n);

                } else if (d8 < d2) {
                    var81 = var81.getRelative(BlockFace.NORTH);
                    final Vector n = new Vector(0, 0, 1);
                    this.applyKnockBack(starter, n);
                }
                if (var81 != null) {
                    Bukkit.getPluginManager().callEvent(new BallWallCollideEvent(this.ball, var81));
                }
            }
        }
        this.spigotTimings(false);
    }

    private void applyKnockBack(Vector starter, Vector n) {
        if (this.knockBackBumper <= 0) {
            final Vector r = starter.clone().subtract(n.multiply(2 * starter.dot(n)));
            this.getSpigotEntity().setVelocity(r);
            this.knockBackBumper = 20;
        }
    }

    @Override
    public void recalcPosition() {
        final AxisAlignedBB axisalignedbb = this.getBoundingBox();
        this.locX = (axisalignedbb.a + axisalignedbb.d) / 2.0D;
        this.locY = axisalignedbb.b;
        this.locZ = (axisalignedbb.c + axisalignedbb.f) / 2.0D;
    }
}
