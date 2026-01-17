package com.fredoseep.util;

import net.minecraft.util.math.BlockBox;

import java.util.ArrayList;
import java.util.List;


public class BlockBoxUtils {
    public static boolean contains(BlockBox container, BlockBox target, int epsilon) {
        return target.minX >= container.minX - epsilon && target.maxX <= container.maxX + epsilon && target.minY >= container.minY - epsilon && target.maxY <= container.maxY + epsilon && target.minZ >= container.minZ - epsilon && target.maxZ <= container.maxZ + epsilon;
    }

    private static boolean faceAligned(int min1, int max1, int min2, int max2, int epsilon) {
        int length1 = max1 - min1;
        int length2 = max2 - min2;
        return Math.abs(min1 - min2) <= epsilon && Math.abs(length1 - length2) <= epsilon;
    }

    public static boolean facesCanAlign(BlockBox a, BlockBox b, int epsilon) {
        boolean xAligned = faceAligned(a.minY, a.maxY, b.minY, b.maxY, epsilon) && faceAligned(a.minZ, a.maxZ, b.minZ, b.maxZ, epsilon);
        boolean yAligned = faceAligned(a.minX, a.maxX, b.minX, b.maxX, epsilon) && faceAligned(a.minZ, a.maxZ, b.minZ, b.maxZ, epsilon);
        boolean zAligned = faceAligned(a.minX, a.maxX, b.minX, b.maxX, epsilon) && faceAligned(a.minY, a.maxY, b.minY, b.maxY, epsilon);
        return xAligned || yAligned || zAligned;
    }

    public static boolean shouldMerge(BlockBox a, BlockBox b, int epsilon) {
        return contains(a, b, epsilon) || contains(b, a, epsilon) || facesCanAlign(a, b, epsilon);
    }

    public static BlockBox mergeBox(BlockBox a, BlockBox b) {
        return new BlockBox(Math.min(a.minX, b.minX), Math.min(a.minY, b.minY), Math.min(a.minZ, b.minZ), Math.max(a.maxX, b.maxX), Math.max(a.maxY, b.maxY), Math.max(a.maxZ, b.maxZ));
    }

    public static List<BlockBox> merge(List<BlockBox> boxes, int epsilon) {
        ArrayList result = new ArrayList(boxes);

        boolean mergedAny;
        ArrayList newResult;
        do {
            mergedAny = false;
            newResult = new ArrayList();

            while(!result.isEmpty()) {
                BlockBox current = (BlockBox)result.remove(result.size() - 1);
                boolean merged = false;

                for(int i = 0; i < result.size(); ++i) {
                    BlockBox other = (BlockBox)result.get(i);
                    if (shouldMerge(current, other, epsilon)) {
                        BlockBox mergedBox = mergeBox(current, other);
                        result.remove(i);
                        result.add(mergedBox);
                        merged = true;
                        mergedAny = true;
                        break;
                    }
                }

                if (!merged) {
                    newResult.add(current);
                }
            }

            result = newResult;
        } while(mergedAny);

        return newResult;
    }
}
