package uk.ac.ebi.subs.fileupload.filechecksumlistener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

@SpringBootApplication
public class FileChecksumListenerApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(FileChecksumListenerApplication.class);
		ApplicationPidFileWriter applicationPidFileWriter = new ApplicationPidFileWriter();
		springApplication.addListeners( applicationPidFileWriter );
		springApplication.run(args);
	}
}
