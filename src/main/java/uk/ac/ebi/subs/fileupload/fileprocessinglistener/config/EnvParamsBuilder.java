package uk.ac.ebi.subs.fileupload.fileprocessinglistener.config;

import lombok.Builder;

import java.util.StringJoiner;

@Builder
public class EnvParamsBuilder {

    private String logHome;
    private String grayLogHost;
    private String grayLogPort;
    private String appName;
    private String profile;

    public String buildEnvExportCommand() {
        StringJoiner exportCommands = new StringJoiner(" ");
        exportCommands.add("LOG_HOME=" + logHome);
        exportCommands.add("GRAYLOG_HOST=" + grayLogHost);
        exportCommands.add("GRAYLOG_PORT=" + grayLogPort);
        exportCommands.add("SPRING_APP=" + appName);
        exportCommands.add("SPRING_PROFILE=" + profile);

        return exportCommands.toString();
    }
}
