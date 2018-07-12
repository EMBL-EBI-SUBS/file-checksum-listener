package uk.ac.ebi.subs.fileupload.fileprocessinglistener.messaging.message;

import lombok.Data;

/**
 * This class represents a RabbitMQ JSON message.
 * It only contains 1 key-value pair that contains the file's generated ID by TUS.
 */
@Data
public class ChecksumGenerationMessage {

    private String generatedTusId;
}
