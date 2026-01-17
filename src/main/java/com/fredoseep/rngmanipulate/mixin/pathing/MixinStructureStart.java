package com.fredoseep.rngmanipulate.mixin.pathing;



import com.fredoseep.rngmanipulate.pathing.WorldPiglinPathState;
import com.fredoseep.util.BlockBoxUtils;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin({StructureStart.class})
public abstract class MixinStructureStart {
   @Shadow
   @Final
   private StructureFeature<?> feature;

   @Shadow
   public abstract List<StructurePiece> getChildren();

   @Inject(
      method = {"generateStructure"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/structure/StructureStart;setBoundingBoxFromChildren()V",
   shift = Shift.AFTER
)}
   )
   public void onGenerate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockBox boundingBox, ChunkPos pos, CallbackInfo ci) {
      if (this.feature == StructureFeature.BASTION_REMNANT) {
         Chunk chunk = world.getChunk(pos.x, pos.z, ChunkStatus.STRUCTURE_STARTS);
         StructureStart<?> structureStart = structureAccessor.getStructureStart(ChunkSectionPos.from(pos, 0), this.feature, chunk);
         if (this.equals(structureStart)) {
            List<BlockBox> boxes = new ArrayList();
            Iterator var11 = this.getChildren().iterator();

            while(var11.hasNext()) {
               StructurePiece child = (StructurePiece)var11.next();
               boxes.add(new BlockBox(child.getBoundingBox().minX - 6, child.getBoundingBox().minY - 4, child.getBoundingBox().minZ - 6, child.getBoundingBox().maxX + 6, child.getBoundingBox().maxY + 4, child.getBoundingBox().maxZ + 6));
            }

            List<BlockBox> newBoxes = BlockBoxUtils.merge(boxes, 4);
            Iterator var15 = newBoxes.iterator();

            while(var15.hasNext()) {
               BlockBox blockBox = (BlockBox)var15.next();
               WorldPiglinPathState.fromWorld((ServerWorld)world.getWorld()).addBlockBox(blockBox);
            }
         }

      }
   }
}
