package dev.rusthero.timingcomparator.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static java.lang.String.format;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    private static Long lastActivate = 0L;
    private static Long lastDeactivate = 0L;

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private ClientWorld.Properties clientWorldProperties;

    @Inject(
            at = @At("HEAD"),
            method = "updateListeners(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;I)V"
    )
    public void onTrapdoorUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        if (client.player == null) return;
        if (oldState.getBlock() != newState.getBlock() || newState.getBlock() != Blocks.IRON_TRAPDOOR) return;

        boolean oldPowered = oldState.get(TrapdoorBlock.POWERED);
        boolean powered = newState.get(TrapdoorBlock.POWERED);
        if (oldPowered == powered) return;

        String status;
        final long now = System.currentTimeMillis();
        long passedTime;

        if (powered) {
            status = "§aactivated";
            passedTime = now - lastActivate;
            lastActivate = now;
        } else {
            status = "§cdeactivated";
            passedTime = now - lastDeactivate;
            lastDeactivate = now;
        }

        if (passedTime < 50)
            client.player.sendMessage(Text.of(format("§eTrapdoors %s§e at the same time.", status)), false);
    }
}
