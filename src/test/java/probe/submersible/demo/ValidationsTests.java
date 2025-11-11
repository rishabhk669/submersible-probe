package probe.submersible.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import probe.submersible.demo.config.SubmersibleException;
import probe.submersible.demo.model.SubmersibleInfo;
import probe.submersible.demo.service.SubmersibleService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidationsTests {

	@Test
	void initialSetup_HappyFlow() {
		var service = new SubmersibleService();

		var info = new SubmersibleInfo(100, 150, 30, 10
				, "W", null);

		assertDoesNotThrow(() -> service.validate(info));
	}
	@Test
	void initialSetup_invalidGridLen() {
		var service = new SubmersibleService();
		var info = new SubmersibleInfo(0, 150, 30, 10
				, "W", null);
		SubmersibleException ex = assertThrows(SubmersibleException.class, () -> service.validate(info));

		assertEquals("Grid length must be greater than 0", ex.getMessage());
	}
	@Test
	void initialSetup_negativeGridLen() {
		var service = new SubmersibleService();
		var info = new SubmersibleInfo(-100, 150, 30, 10
				, "W", null);
		SubmersibleException ex = assertThrows(SubmersibleException.class, () -> service.validate(info));

		assertEquals("Grid length must be greater than 0", ex.getMessage());
	}
	@Test
	void initialSetup_invalidGridHeight() {
		var service = new SubmersibleService();
		var info = new SubmersibleInfo(100, 0, 30, 10
				, "W", null);
		SubmersibleException ex = assertThrows(SubmersibleException.class, () -> service.validate(info));

		assertEquals("Grid height must be greater than 0", ex.getMessage());
	}
	@Test
	void initialSetup_invalidStartX() {
		var service = new SubmersibleService();
		var info = new SubmersibleInfo(100, 150, -1, 10
				, "W", null);
		SubmersibleException ex = assertThrows(SubmersibleException.class, () -> service.validate(info));

		assertEquals("Start position cannot be negative", ex.getMessage());
	}
	@Test
	void initialSetup_invalidStartY() {
		var service = new SubmersibleService();
		var info = new SubmersibleInfo(100, 150, 30, -1
				, "W", null);
		SubmersibleException ex = assertThrows(SubmersibleException.class, () -> service.validate(info));

		assertEquals("Start position cannot be negative", ex.getMessage());
	}
	@Test
	void initialSetup_invalidFacingDir() {
		var service = new SubmersibleService();
		var info = new SubmersibleInfo(100, 150, 30, 10
				, "A", null);
		SubmersibleException ex = assertThrows(SubmersibleException.class, () -> service.validate(info));

		assertEquals("Invalid facing direction", ex.getMessage());
	}
	@Test
	void initialSetup_invalidObsX() {
		var service = new SubmersibleService();
		var info = new SubmersibleInfo(100, 150, 30, 10
				, "W", List.of(new SubmersibleInfo.Obstacles(-20, 10)));
		SubmersibleException ex = assertThrows(SubmersibleException.class, () -> service.validate(info));

		assertEquals("Invalid obstacle position", ex.getMessage());
	}
	@Test
	void initialSetup_invalidObsY() {
		var service = new SubmersibleService();
		var info = new SubmersibleInfo(100, 150, 30, 10
				, "W", List.of(new SubmersibleInfo.Obstacles(20, -10)));
		SubmersibleException ex = assertThrows(SubmersibleException.class, () -> service.validate(info));

		assertEquals("Invalid obstacle position", ex.getMessage());
	}

}
