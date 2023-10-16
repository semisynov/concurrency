package course.concurrency.m2_async.cf;

import course.concurrency.m2_async.cf.report.ReportServiceCF;
import course.concurrency.m2_async.cf.report.ReportServiceExecutors;
import course.concurrency.m2_async.cf.report.ReportServiceVirtual;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReportServiceTests {

    private final ReportServiceExecutors reportService = new ReportServiceExecutors();
    private final ReportServiceCF reportServiceCF = new ReportServiceCF();
//    private final ReportServiceVirtual reportServiceVirtual = new ReportServiceVirtual();

    @Test
    public void testMultipleTasks() throws InterruptedException {
        int poolSize = Runtime.getRuntime().availableProcessors() * 3;
        int iterations = 5;

        CountDownLatch latch = new CountDownLatch(1);
        long start;
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < poolSize; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                } catch (InterruptedException ignored) {}
                for (int it = 0; it < iterations; it++) {
                    var report = reportService.getReport();
                }
            });
        }

        start = System.currentTimeMillis();
        latch.countDown();
        executor.shutdown();
        var isTerminated = executor.awaitTermination(5, TimeUnit.MINUTES);
        System.out.println("Is terminated: " + isTerminated);
        long end = System.currentTimeMillis();

        System.out.println("Execution time: " + (end - start));
    }

    @Test
    public void testMultipleTasksCF() throws InterruptedException {
        int poolSize = Runtime.getRuntime().availableProcessors() * 3;
        int iterations = 5;

        CountDownLatch latch = new CountDownLatch(1);
        long start;
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < poolSize; i++) {
            executor.submit(() -> {
                try {
                    latch.await();
                } catch (InterruptedException ignored) {}
                for (int it = 0; it < iterations; it++) {
                    var report = reportServiceCF.getReport();
                }
            });
        }

        start = System.currentTimeMillis();
        latch.countDown();
        executor.shutdown();
        var isTerminated = executor.awaitTermination(5, TimeUnit.MINUTES);
        System.out.println("Is terminated: " + isTerminated);
        long end = System.currentTimeMillis();
        System.out.println("Execution time: " + (end - start));
    }

//    @Test
//    public void testMultipleTasksVirtual() throws InterruptedException {
//        int poolSize = Runtime.getRuntime().availableProcessors() * 3;
//        int iterations = 5;
//
//        CountDownLatch latch = new CountDownLatch(1);
//        long start;
//        try (ExecutorService executor = Executors.newFixedThreadPool(poolSize)) {
//            for (int i = 0; i < poolSize; i++) {
//                executor.submit(() -> {
//                    try {
//                        latch.await();
//                    } catch (InterruptedException ignored) {}
//                    for (int it = 0; it < iterations; it++) {
//                        var report = reportServiceVirtual.getReport();
//                    }
//                });
//            }
//
//            start = System.currentTimeMillis();
//            latch.countDown();
//            executor.shutdown();
//            var isTerminated = executor.awaitTermination(5, TimeUnit.MINUTES);
//            System.out.println("Is terminated: " + isTerminated);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("Execution time: " + (end - start));
//    }
}
