import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        FarmRepositoryTest.class,
        ProducerRepositoryTest.class
})
public class AllTests {
}


