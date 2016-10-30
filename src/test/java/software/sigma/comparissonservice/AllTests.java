package software.sigma.comparissonservice;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import software.sigma.comparissonservice.controller.ConfigurationRestControllerTest;
import software.sigma.comparissonservice.service.ConfigurationServiceImplTest;

@RunWith(Suite.class)
@SuiteClasses({ ConfigurationRestControllerTest.class, ConfigurationServiceImplTest.class })
public class AllTests {

}
