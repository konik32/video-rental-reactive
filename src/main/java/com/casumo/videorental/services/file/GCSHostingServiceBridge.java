package com.casumo.videorental.services.file;

import com.casumo.videorental.model.File;
import com.netflix.hystrix.HystrixCommandProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class GCSHostingServiceBridge implements FileHostingService {

    private final GCSHostingService service;


    @Override
    public Mono<File> moveFileToPersistentLocation(File file) {
        return HystrixCommands.from(Mono.fromCallable(() -> service.moveFileToPersistentLocation(file)))
                .commandName("GCSHostingServiceBridge.moveFileToPersistentLocation")
                .commandProperties(setter -> setter.withCircuitBreakerEnabled(true)
                        .withExecutionIsolationThreadInterruptOnTimeout(true)
                        .withExecutionTimeoutInMilliseconds(1000 * 10)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
                .toMono();
    }
}
