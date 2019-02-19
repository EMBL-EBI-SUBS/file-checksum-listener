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
        StringJoiner exportCommands = new StringJoiner("; ");
        exportCommands.add("export LOG_HOME=" + logHome);
        exportCommands.add("export GRAYLOG_HOST=" + grayLogHost);
        exportCommands.add("export GRAYLOG_PORT=" + grayLogPort);
        exportCommands.add("export SPRING_APP=" + appName);
        exportCommands.add("export SPRING_PROFILE=" + profile);

        return exportCommands.toString() + "; ";
    }
}
