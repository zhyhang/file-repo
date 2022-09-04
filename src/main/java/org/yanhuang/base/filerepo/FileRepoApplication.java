package org.yanhuang.base.filerepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import xyz.erupt.core.annotation.EruptScan;

@SpringBootApplication
@EntityScan
@EruptScan
public class FileRepoApplication {

	public static void main(String[] args) {
		final SpringApplication application = new SpringApplication(FileRepoApplication.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);
	}

}
