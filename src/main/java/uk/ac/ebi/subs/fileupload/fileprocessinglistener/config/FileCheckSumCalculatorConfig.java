package uk.ac.ebi.subs.fileupload.fileprocessinglistener.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class FileCheckSumCalculatorConfig {

    @Value("${fileProcessing-listener.checksum-calculator.errLogDir}")
    private String errLogDir;

    @Value("${fileProcessing-listener.checksum-calculator.outLogDir}")
    private String outLogDir;

    @Value("${fileProcessing-listener.checksum-calculator.appLogDir}")
    private String appLogDir;

    @Value("${fileProcessing-listener.checksum-calculator.memoryUsage}")
    private String memoryUsage;

    @Value("${fileProcessing-listener.checksum-calculator.jobName}")
    private String jobName;

    @Value("#{environment.SPRING_PROFILE}")
    private String profile;
}
