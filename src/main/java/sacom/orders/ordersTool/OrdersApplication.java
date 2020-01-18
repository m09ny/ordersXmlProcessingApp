package sacom.orders.ordersTool;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class OrdersApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OrdersApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(OrdersApplication.class, args);
	}

	@Autowired
	private ApplicationContext appContext;

	@Override
	public void run(String... args) throws Exception {
		String[] beans = appContext.getBeanDefinitionNames();
		Arrays.sort(beans);

		OrdersWatchService ows = appContext.getBean(OrdersWatchService.class);
		ows.watchFolder();

//		ReadOrderXmlFile beanReadOrder = appContext.getBean(ReadOrderXmlFile.class);
//		beanReadOrder.process();

	}
}
