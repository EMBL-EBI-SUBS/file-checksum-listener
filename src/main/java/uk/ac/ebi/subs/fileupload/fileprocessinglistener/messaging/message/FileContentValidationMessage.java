package uk.ac.ebi.subs.fileupload.fileprocessinglistener.messaging.message;

import lombok.Data;

@Data
public class FileContentValidationMessage {

    private String fileUUID;
    private String fileType;
    private String filePath;
    private String validationResultUUID;
    private String validationResultVersion;

}
