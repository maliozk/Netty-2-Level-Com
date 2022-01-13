package tr.com.net.center;

import tr.com.net.center.config.TupleConnectionConfigID;

import java.util.*;

public class CenterConfig {

    public static List<TupleConnectionConfigID> startConfig = new ArrayList<>();
    public static List<TupleConnectionConfigID> endConfig = new ArrayList<>();

    public static Map<Integer, List<Integer>> startToEnd = new HashMap<>();

    static {
        TupleConnectionConfigID start = new TupleConnectionConfigID(20, "127.0.0.1", 5110, 5111);
        startConfig.add(start);

        TupleConnectionConfigID end = new TupleConnectionConfigID(50, "127.0.0.1", 6110, 6111);
        endConfig.add(end);

        startToEnd.put(20, Collections.singletonList(50));
    }
}
