package uk.ac.ebi.subs.fileupload.filechecksumlistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.fileupload.filechecksumlistener.messaging.ChecksumListenerMessagingConfiguration;

import java.io.IOException;
import java.util.StringJoiner;

/**
 * This {@link Service} is a {@link RabbitListener} listening on a queue
 * and executing a checksum calculator task using the sent file ID from the message as the task's parameter.
 */
@Service
public class FileChecksumListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileChecksumListener.class);

    @Value("${fileChecksum-listener.errLogDir}")
    private String errLogDir;

    @Value("${fileChecksum-listener.outLogDir}")
    private String outLogDir;

    @Value("${fileChecksum-listener.appLogDir}")
    private String appLogDir;

    @Value("${fileChecksum-listener.memoryUsage}")
    private String memoryUsage;

    @Value("${fileChecksum-listener.jobName}")
    private String jobName;

    @Value("${fileChecksum-listener.profile}")
    private String profile;

    @Value("${fileChecksum-listener.configLocation}")
    private String configLocation;

    @RabbitListener(queues = ChecksumListenerMessagingConfiguration.FILE_CHECKSUM_GENERATION)
    public void handleChecksumGenerationRequest(ChecksumGenerationMessage checksumGenerationMessage) throws IOException {
        final String generatedTusId = checksumGenerationMessage.getGeneratedTusId();
        LOGGER.info(
                "Received file checksum generation message with TUS ID: {}", generatedTusId);
        StringJoiner sj = new StringJoiner(" ");
        sj.add(jobName).add(generatedTusId)
                .add("-DLOG_HOME=" + appLogDir)
                .add(profile)
                .add(configLocation);
        String appAndParameters = sj.toString();

        String commandForComputeMD5OnLSF = "bsub -e " + errLogDir + " -o " + outLogDir
                + memoryUsage
                + appAndParameters;

        java.lang.Runtime rt = java.lang.Runtime.getRuntime();
        rt.exec(commandForComputeMD5OnLSF);
    }
}
