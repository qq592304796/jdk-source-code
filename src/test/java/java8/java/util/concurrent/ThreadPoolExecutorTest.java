package java8.java.util.concurrent;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author jiangxinjun
 * @date 2020/06/29
 */
@Slf4j
public class ThreadPoolExecutorTest {

    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(1, 2,
                1000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(5), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    @SneakyThrows
    @Test
    public void test() {
        List<Integer> result1 = new CopyOnWriteArrayList<>();
        List<Integer> result2 = new ArrayList<>();
        List<CompletableFuture<Integer>> results = new ArrayList<>();
        IntStream.range(0, 100).forEach(value -> {
            CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
                log.debug(String.valueOf(value));
                return value;
            }, EXECUTOR_SERVICE);
            supplyAsync.thenAccept(integer -> {
                log.debug(String.valueOf(value));
                result1.add(integer);
                result2.add(integer);
            });
            results.add(supplyAsync);
        });
        List<Integer> result = results.stream().map(CompletableFuture::join).collect(Collectors.toList());
        log.debug(result.toString());
        log.debug(result1.toString());
        log.debug(result2.toString());
        EXECUTOR_SERVICE.awaitTermination(10000, TimeUnit.MILLISECONDS);
    }

}
