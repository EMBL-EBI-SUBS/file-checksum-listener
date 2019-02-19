package uk.ac.ebi.subs.fileupload.fileprocessinglistener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.fileupload.fileprocessinglistener.config.EnvParamsBuilder;
import uk.ac.ebi.subs.fileupload.fileprocessinglistener.config.FileCheckSumCalculatorConfig;
import uk.ac.ebi.subs.fileupload.fileprocessinglistener.messaging.FileProcessingListenerMessagingConfiguration;
import uk.ac.ebi.subs.fileupload.fileprocessinglistener.messaging.message.ChecksumGenerationMessage;

import java.io.IOException;
import java.util.StringJoiner;

/**
 * This {@link Service} is a {@link RabbitListener} listening on a queue
 * and executing a checksum calculator task using the sent file ID from the message as the task's parameter.
 */
@Service
@RequiredArgsConstructor
public class FileChecksumListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileChecksumListener.class);

    @NonNull
    private FileCheckSumCalculatorConfig fileCheckSumCalculatorConfig;

    @RabbitListener(queues = FileProcessingListenerMessagingConfiguration.FILE_CHECKSUM_GENERATION)
    public void handleChecksumGenerationRequest(ChecksumGenerationMessage checksumGenerationMessage) throws IOException {
        final String generatedTusId = checksumGenerationMessage.getGeneratedTusId();
        LOGGER.info(
                "Received file checksum generation message with TUS ID: {}", generatedTusId);
        StringJoiner sj = new StringJoiner(" ");
        sj.add(fileCheckSumCalculatorConfig.getJobName()).add(generatedTusId)
                .add("--spring.profiles.active=" + fileCheckSumCalculatorConfig.getProfile())
                .add(fileCheckSumCalculatorConfig.getConfigLocation());
        String appAndParameters = sj.toString();

        String envExportCommands = EnvParamsBuilder.builder()
                .logHome(fileCheckSumCalculatorConfig.getAppLogDir())
                .grayLogHost(fileCheckSumCalculatorConfig.getGraylogHost())
                .grayLogPort(fileCheckSumCalculatorConfig.getGraylogPort())
                .appName(fileCheckSumCalculatorConfig.getAppName())
                .profile(fileCheckSumCalculatorConfig.getProfile())
                .build()
                .buildEnvExportCommand();

        String commandForComputeMD5OnLSF = envExportCommands
                + "bsub -e " + fileCheckSumCalculatorConfig.getErrLogDir()
                + " -o " + fileCheckSumCalculatorConfig.getOutLogDir()
                + fileCheckSumCalculatorConfig.getMemoryUsage()
                + appAndParameters;


        LOGGER.info(
                "Executing the following command on LSF: {}", commandForComputeMD5OnLSF);
        java.lang.Runtime rt = java.lang.Runtime.getRuntime();
        rt.exec(commandForComputeMD5OnLSF);
    }
}
