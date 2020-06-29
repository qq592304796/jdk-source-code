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
        List<CompletableFuture<Integer>> results = new ArrayList<>();
        IntStream.range(0, 10).forEach(value -> {
            results.add(CompletableFuture.supplyAsync(() -> {
                log.debug(String.valueOf(value));
                return value;
            }, EXECUTOR_SERVICE));
        });
        List<Integer> result = results.stream().map(CompletableFuture::join).collect(Collectors.toList());
        log.debug(result.toString());
        EXECUTOR_SERVICE.awaitTermination(10000, TimeUnit.MILLISECONDS);
    }

}
