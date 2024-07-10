package com.teampotato.retargetato;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mod("retargetato")
public class ReTargetato {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> MOB_LIST, FILTERED_MOB_LIST, FILTERED_TARGET_LIST;
    public static ForgeConfigSpec.ConfigValue<String> LIST_TYPE;

    public ReTargetato() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
        MinecraftForge.EVENT_BUS.register(this);
    }

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Targetato").push("General");
        LIST_TYPE = CONFIG_BUILDER.comment("Use 'B' for blacklist, use any other word(s) (e.g.  I love you, PotatoBoy) for whitelist").define("listType", "B");
        MOB_LIST = CONFIG_BUILDER.defineList("mobList", Lists.newArrayList(), o -> o instanceof String);
        CONFIG_BUILDER.pop();
        CONFIG_BUILDER.push("Advanced Filter");
        FILTERED_MOB_LIST = CONFIG_BUILDER.defineList("filteredMobList", Lists.newArrayList(), o -> o instanceof String);
        FILTERED_TARGET_LIST = CONFIG_BUILDER.defineList("filteredTargetList", Lists.newArrayList(), o -> o instanceof String);
        CONFIG_BUILDER.pop();
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }

    public static void injectSetTarget(LivingEntity target, CallbackInfo ci, Mob mob) {
        if (!(mob instanceof Monster) || !(target instanceof Monster)) {
            return;
        }
        String mobType = mob.getType().toString();
        String targetType = target.getType().toString();
        int mobIndex = FILTERED_MOB_LIST.get().indexOf(mobType);
        int targetIndex = FILTERED_TARGET_LIST.get().indexOf(targetType);
        if (mobIndex == targetIndex && mobIndex != -1) return;
        boolean checker = MOB_LIST.get().contains(mobType);
        if (LIST_TYPE.get().equals("B")) {
            if (checker) return;
        } else {
            if (!checker) return;
        }
        ci.cancel();
    }
}
