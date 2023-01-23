package com.batch.sql.partition.batchsqlpartition.partition;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class ColumnRangePartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {//2
        // int min = 1;
        // int max = 1000000;
        // int targetSize = (max - min) / gridSize + 1;//500
        // System.out.println("targetSize : " + targetSize);
        // Map<String, ExecutionContext> result = new HashMap<>();

        // int number = 0;
        // int start = min;
        // int end = start + targetSize - 1;
        // //1 to 500
        // // 501 to 1000
        // while (start <= max) {
        //     ExecutionContext value = new ExecutionContext();
        //     result.put("partition" + number, value);

        //     if (end >= max) {
        //         end = max;
        //     }
        //     value.putInt("minValue", start);
        //     value.putInt("maxValue", end);
        //     start += targetSize;
        //     end += targetSize;
        //     number++;
        // }
        // System.out.println("partition result:" + result.toString());
        // return result;
        Map<String, ExecutionContext> result 
                       = new HashMap<String, ExecutionContext>();

		int range = 25000;
		int fromId = 1;
		int toId = range;

		for (int i = 1; i <= gridSize; i++) {
			ExecutionContext value = new ExecutionContext();

			System.out.println("\nStarting : Thread" + i);
			System.out.println("fromId : " + fromId);
			System.out.println("toId : " + toId);

			value.putInt("fromId", fromId);
			value.putInt("toId", toId);

			// give each thread a name, thread 1,2,3
			value.putString("name", "Thread" + i);

			result.put("partition" + i, value);

			fromId = toId + 1;
			toId += range;

		}

		return result;
    }
}
