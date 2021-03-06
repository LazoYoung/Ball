package com.github.shynixn.ball.bukkit.core.nms.v1_9_R2;

import com.github.shynixn.ball.api.bukkit.business.entity.BukkitBall;
import com.github.shynixn.ball.api.bukkit.business.event.BallMoveEvent;
import com.github.shynixn.ball.api.bukkit.business.event.BallWallCollideEvent;
import com.github.shynixn.ball.api.persistence.BounceObject;
import com.github.shynixn.ball.bukkit.core.logic.business.helper.ReflectionUtils;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.SpigotTimings;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

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
    private static final int[] excludedRelativeItems = new int[]{85,101,102,107,113,139,160,183,184,185,186,187,188,189,190,191,192};

    private final BukkitBall ball;

    private int knockBackBumper;
    private Vector reduceVector;
    private Vector originVector;
    private int times;

    CustomHitbox(Location location, BukkitBall ball) {
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

    void setVelocity(Vector velocity) {
        try {
            this.times = (int) (50 * this.ball.getMeta().getModifiers().getRollingDistanceModifier());
            this.getSpigotEntity().setVelocity(velocity);
            final Vector normalized = velocity.clone().normalize();
            this.originVector = velocity.clone();
            this.reduceVector = new Vector(normalized.getX() / this.times
                    , 0.0784 * this.ball.getMeta().getModifiers().getGravityModifier()
                    , normalized.getZ() / this.times);
        } catch (IllegalArgumentException ignored) {

        }
    }

    private void applyKnockBack(Vector starter, Vector n, org.bukkit.block.Block block, BlockFace blockFace) {
        if (block == null || block.getType() == org.bukkit.Material.AIR) {
            return;
        }

        if (this.knockBackBumper <= 0) {
            final Optional<BounceObject> optBounce = this.ball.getMeta().getBounceObjectController().getBounceObjectFromBlock(block);
            if (optBounce.isPresent() || this.ball.getMeta().isAlwaysBounceBack()) {
                Vector r = starter.clone().subtract(n.multiply(2 * starter.dot(n))).multiply(0.75);
                if (optBounce.isPresent()) {
                    r = r.multiply(optBounce.get().getBounceModifier());
                }
                else {
                    r = r.multiply(this.ball.getMeta().getModifiers().getBounceModifier());
                }
                final BallWallCollideEvent event = new BallWallCollideEvent(this.ball, block, blockFace, r.clone(), starter.clone());
                Bukkit.getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    this.setVelocity(r);
                    ((CustomDesign) this.ball).revertAnimation = !((CustomDesign) this.ball).revertAnimation;
                    this.knockBackBumper = 5;
                }
            }
        }
    }

    @Override
    public void move(double d0, double d1, double d2) {
        final BallMoveEvent cevent = new BallMoveEvent(this.ball);
        Bukkit.getPluginManager().callEvent(cevent);
        if (cevent.isCancelled()) {
            return;
        }
        final Vector starter;
        if ((this.times > 0 || !this.onGround) && this.originVector != null) {
            this.originVector = this.originVector.subtract(this.reduceVector);
            if (this.times > 0) {
                this.motX = this.originVector.getX();
                this.motZ = this.originVector.getZ();
            }
            this.motY = this.originVector.getY();
            this.times--;
            starter = new Vector(this.motX, this.motY, this.motZ);
        } else {
            starter = new Vector(d0, d1, d2);
        }

        this.spigotTimings(true);
        if (this.knockBackBumper > 0) {
            this.knockBackBumper--;
        }

        if (this.noclip) {
            this.a(this.getBoundingBox().c(d0, d1, d2));
            this.recalcPosition();
        } else {
            try {
                this.checkBlockCollisions();
            } catch (Throwable var79) {
                CrashReport crashreport = CrashReport.a(var79, "Checking entity block collision");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity being checked for collision");
                this.appendEntityCrashDetails(crashreportsystemdetails);
                throw new ReportedException(crashreport);
            }

            if (d0 == 0.0D && d1 == 0.0D && d2 == 0.0D && this.isVehicle() && this.isPassenger()) {
                return;
            }

            this.world.methodProfiler.a("move");
            double d3 = this.locX;
            double d4 = this.locY;
            double d5 = this.locZ;
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
            double d7 = d1;
            double d8 = d2;
            boolean flag = this.onGround && this.isSneaking() && false;
            if (flag) {
                double d9;
                for(d9 = 0.05D; d0 != 0.0D && this.world.getCubes(this, this.getBoundingBox().c(d0, -1.0D, 0.0D)).isEmpty(); d6 = d0) {
                    if (d0 < d9 && d0 >= -d9) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= d9;
                    } else {
                        d0 += d9;
                    }
                }

                for(; d2 != 0.0D && this.world.getCubes(this, this.getBoundingBox().c(0.0D, -1.0D, d2)).isEmpty(); d8 = d2) {
                    if (d2 < d9 && d2 >= -d9) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= d9;
                    } else {
                        d2 += d9;
                    }
                }

                for(; d0 != 0.0D && d2 != 0.0D && this.world.getCubes(this, this.getBoundingBox().c(d0, -1.0D, d2)).isEmpty(); d8 = d2) {
                    if (d0 < d9 && d0 >= -d9) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= d9;
                    } else {
                        d0 += d9;
                    }

                    d6 = d0;
                    if (d2 < d9 && d2 >= -d9) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= d9;
                    } else {
                        d2 += d9;
                    }
                }
            }

            List list = this.world.getCubes(this, this.getBoundingBox().a(d0, d1, d2));
            AxisAlignedBB axisalignedbb = this.getBoundingBox();
            int i = 0;

            int j;
            for(j = list.size(); i < j; ++i) {
                d1 = ((AxisAlignedBB)list.get(i)).b(this.getBoundingBox(), d1);
            }

            this.a(this.getBoundingBox().c(0.0D, d1, 0.0D));
            boolean flag1 = this.onGround || d7 != d1 && d7 < 0.0D;
            j = 0;

            int k;
            for(k = list.size(); j < k; ++j) {
                d0 = ((AxisAlignedBB)list.get(j)).a(this.getBoundingBox(), d0);
            }

            this.a(this.getBoundingBox().c(d0, 0.0D, 0.0D));
            j = 0;

            for(k = list.size(); j < k; ++j) {
                d2 = ((AxisAlignedBB)list.get(j)).c(this.getBoundingBox(), d2);
            }

            this.a(this.getBoundingBox().c(0.0D, 0.0D, d2));
            double d13;
            double d10;
            if (this.P > 0.0F && flag1 && (d6 != d0 || d8 != d2)) {
                double d11 = d0;
                double d12 = d1;
                d13 = d2;
                AxisAlignedBB axisalignedbb1 = this.getBoundingBox();
                this.a(axisalignedbb);
                d1 = (double)this.P;
                List list1 = this.world.getCubes(this, this.getBoundingBox().a(d6, d1, d8));
                AxisAlignedBB axisalignedbb2 = this.getBoundingBox();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.a(d6, 0.0D, d8);
                d10 = d1;
                int l = 0;

                for(int i1 = list1.size(); l < i1; ++l) {
                    d10 = ((AxisAlignedBB)list1.get(l)).b(axisalignedbb3, d10);
                }

                axisalignedbb2 = axisalignedbb2.c(0.0D, d10, 0.0D);
                double d14 = d6;
                int j1 = 0;

                for(int k1 = list1.size(); j1 < k1; ++j1) {
                    d14 = ((AxisAlignedBB)list1.get(j1)).a(axisalignedbb2, d14);
                }

                axisalignedbb2 = axisalignedbb2.c(d14, 0.0D, 0.0D);
                double d15 = d8;
                int l1 = 0;

                for(int i2 = list1.size(); l1 < i2; ++l1) {
                    d15 = ((AxisAlignedBB)list1.get(l1)).c(axisalignedbb2, d15);
                }

                axisalignedbb2 = axisalignedbb2.c(0.0D, 0.0D, d15);
                AxisAlignedBB axisalignedbb4 = this.getBoundingBox();
                double d16 = d1;
                int j2 = 0;

                for(int k2 = list1.size(); j2 < k2; ++j2) {
                    d16 = ((AxisAlignedBB)list1.get(j2)).b(axisalignedbb4, d16);
                }

                axisalignedbb4 = axisalignedbb4.c(0.0D, d16, 0.0D);
                double d17 = d6;
                int l2 = 0;

                for(int i3 = list1.size(); l2 < i3; ++l2) {
                    d17 = ((AxisAlignedBB)list1.get(l2)).a(axisalignedbb4, d17);
                }

                axisalignedbb4 = axisalignedbb4.c(d17, 0.0D, 0.0D);
                double d18 = d8;
                int j3 = 0;

                for(int k3 = list1.size(); j3 < k3; ++j3) {
                    d18 = ((AxisAlignedBB)list1.get(j3)).c(axisalignedbb4, d18);
                }

                axisalignedbb4 = axisalignedbb4.c(0.0D, 0.0D, d18);
                double d19 = d14 * d14 + d15 * d15;
                double d20 = d17 * d17 + d18 * d18;
                if (d19 > d20) {
                    d0 = d14;
                    d2 = d15;
                    d1 = -d10;
                    this.a(axisalignedbb2);
                } else {
                    d0 = d17;
                    d2 = d18;
                    d1 = -d16;
                    this.a(axisalignedbb4);
                }

                int l3 = 0;

                for(int i4 = list1.size(); l3 < i4; ++l3) {
                    d1 = ((AxisAlignedBB)list1.get(l3)).b(this.getBoundingBox(), d1);
                }

                this.a(this.getBoundingBox().c(0.0D, d1, 0.0D));
                if (d11 * d11 + d13 * d13 >= d0 * d0 + d2 * d2) {
                    d0 = d11;
                    d1 = d12;
                    d2 = d13;
                    this.a(axisalignedbb1);
                }
            }

            this.world.methodProfiler.b();
            this.world.methodProfiler.a("rest");
            this.recalcPosition();
            this.positionChanged = d6 != d0 || d8 != d2;
            this.B = d7 != d1;
            this.onGround = this.B && d7 < 0.0D;
            this.C = this.positionChanged || this.B;
            j = MathHelper.floor(this.locX);
            k = MathHelper.floor(this.locY - 0.20000000298023224D);
            int j4 = MathHelper.floor(this.locZ);
            BlockPosition blockposition = new BlockPosition(j, k, j4);
            IBlockData iblockdata = this.world.getType(blockposition);
            if (iblockdata.getMaterial() == Material.AIR) {
                BlockPosition blockposition1 = blockposition.down();
                IBlockData iblockdata1 = this.world.getType(blockposition1);
                net.minecraft.server.v1_9_R2.Block block = iblockdata1.getBlock();
                if (block instanceof BlockFence || block instanceof BlockCobbleWall || block instanceof BlockFenceGate) {
                    iblockdata = iblockdata1;
                    blockposition = blockposition1;
                }
            }

            this.a(d1, this.onGround, iblockdata, blockposition);
            if (d6 != d0) {
                this.motX = 0.0D;
            }

            if (d8 != d2) {
                this.motZ = 0.0D;
            }

            net.minecraft.server.v1_9_R2.Block block1 = iblockdata.getBlock();
            if (d7 != d1) {
                block1.a(this.world, this);
            }

            try {
                if (this.positionChanged) {
                    org.bukkit.block.Block var81 = this.world.getWorld().getBlockAt(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
                    if (d6 > d0) {
                        if (this.isValidKnockBackBlock(var81)) {
                            var81 = var81.getRelative(BlockFace.EAST);
                        }

                        final Vector n = new Vector(-1, 0, 0);
                        this.applyKnockBack(starter, n, var81, BlockFace.EAST);
                    } else if (d6 < d0) {
                        if (this.isValidKnockBackBlock(var81)) {
                            var81 = var81.getRelative(BlockFace.WEST);
                        }

                        final Vector n = new Vector(1, 0, 0);
                        this.applyKnockBack(starter, n, var81, (BlockFace.WEST));
                    } else if (d8 > d2) {
                        if (this.isValidKnockBackBlock(var81)) {
                            var81 = var81.getRelative(BlockFace.SOUTH);
                        }

                        final Vector n = new Vector(0, 0, -1);
                        this.applyKnockBack(starter, n, var81, BlockFace.SOUTH);
                    } else if (d8 < d2) {
                        if (this.isValidKnockBackBlock(var81)) {
                            var81 = var81.getRelative(BlockFace.NORTH);
                        }

                        final Vector n = new Vector(0, 0, 1);
                        this.applyKnockBack(starter, n, var81, BlockFace.NORTH);
                    }
                }
            } catch (final Exception ex) {
                Bukkit.getLogger().log(Level.WARNING, "Critical exception.", ex);
            }
        }
       spigotTimings(false);
    }

    private boolean isValidKnockBackBlock(org.bukkit.block.Block block) {
        final int id = block.getTypeId();
        for (final int i : excludedRelativeItems) {
            if (i == id) {
                return false;
            }
        }

        return true;
    }

    private void spigotTimings(boolean started) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("org.bukkit.craftbukkit.v1_9_R2.SpigotTimings");
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
                Bukkit.getLogger().log(Level.WARNING, "Failed to invoke entityMoveTimer.", e);
            }
        }
    }

    @Override
    public void recalcPosition() {
        final AxisAlignedBB axisalignedbb = this.getBoundingBox();
        this.locX = (axisalignedbb.a + axisalignedbb.d) / 2.0D;
        this.locY = axisalignedbb.b + this.ball.getMeta().getHitBoxRelocationDistance();
        this.locZ = (axisalignedbb.c + axisalignedbb.f) / 2.0D;
    }
}
