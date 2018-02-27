package uk.ac.ebi.subs.fileupload.filechecksumlistener.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.subs.messaging.ExchangeConfig;
import uk.ac.ebi.subs.messaging.Queues;

@Configuration
@ComponentScan(basePackageClasses = ExchangeConfig.class)
public class ChecksumListenerMessagingConfiguration {

    public static final String FILE_CHECKSUM_GENERATION = "file-checksum-generation";
    private static final String EVENT_FILE_CHECKSUM_GENERATION = "file.checksum.generation";

    /**
     * Instantiate a {@link Queue} for exexuting a job for computing the MD5 checksum of a given file.
     *
     * @return an instance of a {@link Queue} for execute checksum generation job.
     */
    @Bean
    Queue fileChecksumGenerationQueue() {
        return Queues.buildQueueWithDlx(FILE_CHECKSUM_GENERATION);
    }

    /**
     * Create a {@link Binding} between the exchange and file checksum generation queue
     * using the routing key.
     *
     * @param fileChecksumGenerationQueue {@link Queue} for generating file checksum
     * @param submissionExchange {@link TopicExchange}
     * @return a {@link Binding} between the exchange and the file checksum generation queue
     * using the routing key of the file checksum generation.
     */
    @Bean
    Binding fileChecksumGenerationBinding(Queue fileChecksumGenerationQueue, TopicExchange submissionExchange) {
        return BindingBuilder.bind(fileChecksumGenerationQueue).to(submissionExchange)
                .with(EVENT_FILE_CHECKSUM_GENERATION);
    }
}
