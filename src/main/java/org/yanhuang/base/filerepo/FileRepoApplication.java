package org.yanhuang.base.filerepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.yanhuang.base.filerepo.config.Consts;
import org.yanhuang.base.filerepo.service.DiskDirectoryDataService;
import org.yanhuang.base.filerepo.service.DiskFileDataService;
import xyz.erupt.core.annotation.EruptScan;
import xyz.erupt.core.invoke.DataProcessorManager;

@SpringBootApplication
@EntityScan
@EruptScan
public class FileRepoApplication {

	public static void main(String[] args) {
		//register data service
		DataProcessorManager.register(Consts.DATA_SERVICE_FILE, DiskFileDataService.class);
		DataProcessorManager.register(Consts.DATA_SERVICE_DIR, DiskDirectoryDataService.class);
		final SpringApplication application = new SpringApplication(FileRepoApplication.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);
	}

}
