package com.fredoseep.mixin;


import com.fredoseep.client.MultiSeedContext;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {
    @Inject(method = "grantCriterion",at = @At("RETURN"))
    private void setAdvancementTime(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir){
        if(advancement.getId().toString().equals("minecraft:story/enter_the_nether")) MultiSeedContext.netherTime = InGameTimer.getInstance().getInGameTime();
        else if(advancement.getId().toString().equals("minecraft:nether/find_bastion"))MultiSeedContext.bastionTime = InGameTimer.getInstance().getInGameTime();
        else if(advancement.getId().toString().equals("minecraft:nether/find_fortress"))MultiSeedContext.fortressTime = InGameTimer.getInstance().getInGameTime();
        else if(advancement.getId().toString().equals("minecraft:story/follow_ender_eye"))MultiSeedContext.strongholdTime = InGameTimer.getInstance().getInGameTime();
        else if(advancement.getId().toString().equals("minecraft:story/enter_the_end"))MultiSeedContext.endTime = InGameTimer.getInstance().getInGameTime();

    }
}
