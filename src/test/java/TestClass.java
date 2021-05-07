import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClass {

    @Test
    void testMethod() {
        List<String> strings = new ArrayList<>();
        assertEquals(0, strings.size());
    }
}
