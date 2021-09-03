package com.coffee.roastnbrew.app;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.StringUtils;

public class AsyncActor {
    
    public static void perform(String purpose, Runnable runnable) {
        ExecutorService service = Executors
            .newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("async-" + getFormattedPurpose(purpose) + "-%d").build());
        service.submit(runnable);
        service.shutdown();
    }
    
    private static String getFormattedPurpose(String purpose) {
        String formattedPurpose = purpose.replaceAll("[^A-Za-z]", "-").toLowerCase();
        return StringUtils.truncate(formattedPurpose, MAX_PURPOSE_LENGTH);
    }
    
    private static final int MAX_PURPOSE_LENGTH = 20;
}
