package probe.submersible.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import probe.submersible.demo.model.BaseResponse;
import probe.submersible.demo.service.SubmersibleService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SubmersibleService service;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void contextLoads() {
	}

	@Test
	void initialSetup() throws Exception {
		mockMvc.perform(
				post("/submersible")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								"gridLength" : 100,
								"gridHeight" : 150,
								"startX" : 30,
								"startY" : 40,
								"facing" : "E",
								"obstacles" : [ {"x":20, "Y":25}, {"x":35, "Y":40} ]
								}""")
		).andExpect(status().isOk());
		assertEquals(100, service.getInfo().getGridLength());
		assertEquals(150, service.getInfo().getGridHeight());
	}

	@Test
	void move1() throws Exception {
		mockMvc.perform(
				put("/submersible")
						.param("dir", "F")
		).andExpect(status().isOk());
		assertEquals(31, service.getInfo().getStartX());
		assertEquals(40, service.getInfo().getStartY());
	}

	@Test
	void move_incorrectDirection() throws Exception {
		var res = mockMvc.perform(
				put("/submersible")
						.param("dir", "A")
		).andExpect(status().isOk());
		var response = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), BaseResponse.class);
		assertEquals("400", response.getStatusCode());
	}

	@Test
	void changeDir() throws Exception {
		mockMvc.perform(
				patch("/submersible")
						.param("facing", "W")
		).andExpect(status().isOk());
		assertEquals("W", service.getInfo().getFacing());
	}

	@Test
	void changeDir_incorrectFacing() throws Exception {
		var res = mockMvc.perform(
				patch("/submersible")
						.param("facing", "A")
		).andExpect(status().isOk());
		var response = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), BaseResponse.class);
		assertEquals("400", response.getStatusCode());
	}

	@Test
	void getSummary() throws Exception {
		var res = mockMvc.perform(
				get("/submersible")
		).andExpect(status().isOk());
		var response = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), BaseResponse.class);
		assertEquals("200", response.getStatusCode());
	}

	@Test
	void reset() throws Exception {
		var res = mockMvc.perform(
				delete("/submersible")
		).andExpect(status().isOk());
		var response = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), BaseResponse.class);
		assertEquals("200", response.getStatusCode());
	}

	@Test
	void initialSetup_atEdgeY() throws Exception {
		mockMvc.perform(
				post("/submersible")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								"gridLength" : 100,
								"gridHeight" : 100,
								"startX" : 100,
								"startY" : 100,
								"facing" : "N",
								"obstacles" : [ {"x":20, "Y":25}, {"x":35, "Y":40} ]
								}""")
		).andExpect(status().isOk());

		var res = mockMvc.perform(
				put("/submersible")
						.param("dir", "F")
		).andExpect(status().isOk());


		var response = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), BaseResponse.class);
		assertEquals("Cannot move. At edge of Y Grid", response.getMessage());

	}

	@Test
	void initialSetup_atEdgeX() throws Exception {
		mockMvc.perform(
				post("/submersible")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								"gridLength" : 3,
								"gridHeight" : 3,
								"startX" : 3,
								"startY" : 0,
								"facing" : "E",
								"obstacles" : [ {"x":3, "Y":1} ]
								}""")
		).andExpect(status().isOk());

		var res = mockMvc.perform(
				put("/submersible")
						.param("dir", "F")
		).andExpect(status().isOk());


		var response = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), BaseResponse.class);
		assertEquals("Cannot move. At edge of X Grid", response.getMessage());

	}

	@Test
	void initialSetup_encounteredObstacleX() throws Exception {
		mockMvc.perform(
				patch("/submersible")
						.param("facing", "N")
		).andExpect(status().isOk());
		var res = mockMvc.perform(
				put("/submersible")
						.param("dir", "F")
		).andExpect(status().isOk());


		var response = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), BaseResponse.class);
		assertEquals("Cannot move. Obstacle encountered", response.getMessage());

	}

	@Test
	void initialSetup_atStartX() throws Exception {
		mockMvc.perform(
				post("/submersible")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								"gridLength" : 3,
								"gridHeight" : 3,
								"startX" : 0,
								"startY" : 0,
								"facing" : "W",
								"obstacles" : [ {"x":3, "Y":1} ]
								}""")
		).andExpect(status().isOk());

		var res = mockMvc.perform(
				put("/submersible")
						.param("dir", "F")
		).andExpect(status().isOk());


		var response = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), BaseResponse.class);
		assertEquals("Cannot move. At edge of X Grid", response.getMessage());

	}

	@Test
	void initialSetup_atStartY() throws Exception {
		mockMvc.perform(
				patch("/submersible")
						.param("facing", "S")
		).andExpect(status().isOk());
		var res = mockMvc.perform(
				put("/submersible")
						.param("dir", "F")
		).andExpect(status().isOk());


		var response = objectMapper.readValue(res.andReturn().getResponse().getContentAsString(), BaseResponse.class);
		assertEquals("Cannot move. At edge of Y Grid", response.getMessage());

	}

}
