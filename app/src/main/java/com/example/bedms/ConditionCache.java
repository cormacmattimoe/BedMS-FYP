package com.example.bedms;
import java.util.ArrayList;

public class ConditionCache {

    public static ArrayList<String> conditionCache = new ArrayList<>();
    public static void SetupCache() {
        loadConditions();
    }

    private static void loadConditions(){
        new RetrieveConditionsTask().execute("https://api.infermedica.com/v3/concepts?types=condition");
    }
}
