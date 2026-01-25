package com.fredoseep.mixin;

import com.fredoseep.client.MultiSeedContext;
import com.mojang.brigadier.CommandDispatcher;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.InGameTimerUtils;
import com.redlimerl.speedrunigt.timer.TimerStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.SeedCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SeedCommand.class)
public class MixinSeedCommand {

    /**
     * @author MultiSeedMod
     * @reason 重写 /seed 指令，使其分别显示三个维度的种子
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(CommandManager.literal("seed")
                .requires((source) -> !dedicated || source.hasPermissionLevel(2))
                .executes((context) -> {
                    ServerCommandSource source = context.getSource();
                    MinecraftServer server = source.getMinecraftServer();

                    long overworldSeed = server.getWorld(World.OVERWORLD).getSeed();
                    long netherSeed = server.getWorld(World.NETHER).getSeed();
                    long endSeed = server.getWorld(World.END).getSeed();

                    MutableText message = new LiteralText("");

                    message.append(new LiteralText("主世界: ").formatted(Formatting.GREEN));
                    message.append(formatSeed(overworldSeed));
                    message.append(new LiteralText("\n")); // 换行

                    message.append(new LiteralText("下界: ").formatted(Formatting.RED));
                    message.append(formatSeed(netherSeed));
                    message.append(new LiteralText("\n")); // 换行

                    message.append(new LiteralText("末地: ").formatted(Formatting.LIGHT_PURPLE));
                    message.append(formatSeed(endSeed));

                    if(InGameTimer.getInstance().isCompleted()){
                        message.append(new LiteralText("\n"));
                        message.append(new LiteralText("具体分段："));
                        message.append(new LiteralText("\n"));
                        message.append("进入下界：     ").append((new LiteralText(InGameTimerUtils.timeToStringFormat(MultiSeedContext.netherTime)))).append(new LiteralText("\n"));
                        message.append("进入猪堡：     ").append((new LiteralText(InGameTimerUtils.timeToStringFormat(MultiSeedContext.bastionTime)))).append(new LiteralText("\n"));
                        message.append("进入下界要塞：").append((new LiteralText(InGameTimerUtils.timeToStringFormat(MultiSeedContext.fortressTime)))).append(new LiteralText("\n"));
                        message.append("盲传：          ").append((new LiteralText(InGameTimerUtils.timeToStringFormat(MultiSeedContext.blindTime)))).append(new LiteralText("\n"));
                        message.append("隔墙有眼：     ").append((new LiteralText(InGameTimerUtils.timeToStringFormat(MultiSeedContext.strongholdTime)))).append(new LiteralText("\n"));
                        message.append("进入末地：     ").append((new LiteralText(InGameTimerUtils.timeToStringFormat(MultiSeedContext.endTime))));
                    }
                    source.sendFeedback(message, false);

                    return (int)overworldSeed;
                })
        );
    }

    /**
     * 辅助方法：把种子数字变成绿色的、可点击复制的文本组件 (仿原版样式)
     */
    private static Text formatSeed(long seed) {
        return Texts.bracketed(
                new LiteralText(String.valueOf(seed)).styled((style) -> {
                    return style.withColor(Formatting.GREEN)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(seed)))
                            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.copy.click")));
                })
        );
    }
}