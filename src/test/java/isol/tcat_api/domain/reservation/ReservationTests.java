package isol.tcat_api.domain.reservation;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class ReservationTests {

    @BeforeEach
    public void before() {
        System.out.println("##### BeforeEach #####");
    }

    @AfterEach
    public void after() {
        System.out.println("##### AfterEach #####");
    }

    @Test
    @DisplayName("TEST1")
    public void test1() throws Exception {
        // Given
        System.out.println("Given");

        // When
        System.out.println("When");

        // Then
        System.out.println("Then");
    }

    @Test
    @DisplayName("TEST2")
    public void test2() throws Exception {
        // Given
        System.out.println("Given");

        // When
        System.out.println("When");

        // Then
        System.out.println("Then");
    }

}
