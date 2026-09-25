package com.example.taskmanager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc class TaskManagerIntegrationTest {
 @Autowired MockMvc mvc;@Autowired ObjectMapper json;
 String register(String email)throws Exception{
  String response=mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(Map.of("name","Test","email",email,"password","Password123!"))))
   .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
  return json.readTree(response).get("token").asText();
 }
 @Test void registerCreateAndReadOwnTask()throws Exception{
  String token=register("alice@example.com");
  mvc.perform(post("/api/tasks").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Learn Hibernate\",\"status\":\"TODO\",\"priority\":\"HIGH\"}"))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.title").value("Learn Hibernate"));
  mvc.perform(get("/api/tasks").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1));
 }
 @Test void cannotAccessOtherUsersTasks()throws Exception{
  String a=register("bob@example.com");String b=register("charlie@example.com");
  String result=mvc.perform(post("/api/tasks").header("Authorization","Bearer "+a).contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Private\",\"status\":\"TODO\",\"priority\":\"LOW\"}"))
   .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
  long id=json.readTree(result).get("id").asLong();
  mvc.perform(get("/api/tasks/"+id).header("Authorization","Bearer "+b)).andExpect(status().isNotFound());
 }
 @Test void unauthorizedRequestsAreRejected()throws Exception{mvc.perform(get("/api/tasks")).andExpect(status().isUnauthorized());}
 @Test void invalidRegistrationIsRejected()throws Exception{
  mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"X\",\"email\":\"invalid\",\"password\":\"short\"}"))
   .andExpect(status().isBadRequest());
 }
}
