package dev.rusthero.timingcomparator.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
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
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    private static Long lastActivate = 0L;
    private static Long lastDeactivate = 0L;
    private static Long lastActivateMessage = 0L;
    private static Long lastDeactivateMessage = 0L;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            at = @At("HEAD"),
            method = "processWorldEvent(Lnet/minecraft/entity/player/PlayerEntity;ILnet/minecraft/util/math/BlockPos;I)V"
    )
    public void onTrapdoorUpdate(PlayerEntity source, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (client.player == null) return;
        if (eventId != 1036 && eventId != 1037) return;

        String status;
        final long now = System.currentTimeMillis();
        long passedTime;
        boolean sendMessage = false;

        if (eventId == 1037) {
            status = "§aactivated";
            passedTime = now - lastActivate;
            lastActivate = now;

            if (passedTime < 50 && now - lastActivateMessage >= 50) {
                lastActivateMessage = now;
                sendMessage = true;
            }
        } else {
            status = "§cdeactivated";
            passedTime = now - lastDeactivate;
            lastDeactivate = now;

            if (passedTime < 50 && now - lastDeactivateMessage >= 50) {
                lastDeactivateMessage = now;
                sendMessage = true;
            }
        }

        if (sendMessage) {
            client.player.sendMessage(Text.of(format("§eTrapdoors %s§e at the same time.", status)), false);
        }
    }
}
