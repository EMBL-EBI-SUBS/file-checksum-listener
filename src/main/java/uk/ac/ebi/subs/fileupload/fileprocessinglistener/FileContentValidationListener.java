package uk.ac.ebi.subs.fileupload.fileprocessinglistener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import uk.ac.ebi.subs.fileupload.fileprocessinglistener.config.FileContentValidatorConfig;
import uk.ac.ebi.subs.fileupload.fileprocessinglistener.messaging.FileProcessingListenerMessagingConfiguration;
import uk.ac.ebi.subs.fileupload.fileprocessinglistener.messaging.message.FileContentValidationMessage;

import java.io.IOException;
import java.util.StringJoiner;

/**
 * This {@link Service} is a {@link RabbitListener} listening on a queue
 * and executing a file content validation task using the sent file ID from the message as the task's parameter.
 */
@Service
@RequiredArgsConstructor
public class FileContentValidationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileContentValidationListener.class);

    @NonNull
    private FileContentValidatorConfig fileContentValidatorConfig;

    @RabbitListener(queues = FileProcessingListenerMessagingConfiguration.FILE_CONTENT_VALIDATION)
    public void handleFileContentValidationRequest(FileContentValidationMessage fileContentValidationMessage) throws IOException {
        LOGGER.info(
                "Received file content validation message with file: {}", fileContentValidationMessage.getFilePath());

        StringJoiner sj = new StringJoiner(" ");
        sj.add(fileContentValidatorConfig.getJobName())
                .add(assembleCommandLineParameters(fileContentValidationMessage))
                .add(fileContentValidatorConfig.getProfile());
        String appAndParameters = sj.toString();

        String commandForValidateFileContent =
                "bsub -e " + fileContentValidatorConfig.getErrLogDir()
                + " -o " + fileContentValidatorConfig.getOutLogDir()
                + fileContentValidatorConfig.getMemoryUsage()
                + appAndParameters;

        LOGGER.info(
                "Executing the following command on LSF: {}", commandForValidateFileContent);

        Runtime rt = Runtime.getRuntime();
        rt.exec(commandForValidateFileContent);
    }

    private String assembleCommandLineParameters(FileContentValidationMessage fileContentValidationMessage) {
        StringJoiner commandLineParameters = new StringJoiner(" ");
        commandLineParameters
                .add(fileContentValidationMessage.getFileUUID())
                .add(fileContentValidationMessage.getFilePath())
                .add(fileContentValidationMessage.getFileType())
                .add(fileContentValidationMessage.getValidationResultUUID())
                .add(fileContentValidationMessage.getValidationResultVersion());

        return commandLineParameters.toString();
    }
}
