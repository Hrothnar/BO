package com.tech.neo.bo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.neo.bo.domain.entity.User;
import com.tech.neo.bo.domain.entity.dto.EmailDto;
import com.tech.neo.bo.domain.entity.dto.PhoneDto;
import com.tech.neo.bo.domain.entity.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest()
@AutoConfigureMockMvc()
@Transactional()
public class UserControllerTest {
    @Autowired()
    private MockMvc mockMvc;
    @Autowired()
    private WebApplicationContext webApplicationContext;
    @Autowired()
    private ObjectMapper objectMapper;

    @BeforeEach()
    public void registerUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFullName("Dude Dudov");
        userDto.setEmail("dude@gmail.com");
        userDto.setLogin("ToughGuy");
        userDto.setPassword("CoolPassword");
        userDto.setInitialDeposit(new BigDecimal("3453.33"));
        userDto.setPhoneNumber("+74425346633");
        userDto.setBirthDate(LocalDate.of(1989, Month.APRIL, 5));

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Assertions.assertEquals("User has been registered", content);
    }

    @Test()
    public void should_update_user_phone_number() throws Exception {
        PhoneDto phoneDto = new PhoneDto("+74425346633", "+777777777");

        String json = objectMapper.writeValueAsString(phoneDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/user/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Phone number has been updated", content);
    }

    @Test()
    public void should_add_user_phone_number() throws Exception {
        PhoneDto phoneDto = new PhoneDto("+999999999");

        String json = objectMapper.writeValueAsString(phoneDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Phone number has been added", content);
    }

    @Test()
    public void should_delete_user_phone_number() throws Exception {
        String number = "+999999999";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/user/phone?phoneNumber=" + number)
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Phone number has been deleted", content);
    }

    @Test()
    public void should_update_user_email() throws Exception {
        EmailDto emailDto = new EmailDto("dude@gmail.com", "evenculleremail@hotmail.com");

        String json = objectMapper.writeValueAsString(emailDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/user/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Email has been updated", content);
    }

    @Test()
    public void should_add_user_email() throws Exception {
        EmailDto emailDto = new EmailDto("anotheremail@hotmail.com");

        String json = objectMapper.writeValueAsString(emailDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Email has been added", content);
    }

    @Test()
    public void should_delete_user_email() throws Exception {
        String email = "anotheremail@hotmail.com";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/user/email?email=" + email)
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Email has been deleted", content);
    }

    @Test()
    public void should_not_delete_last_contact() throws Exception {
        String phone = "+74425346633";

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/phone?phoneNumber=" + phone)
                .contentType(MediaType.TEXT_PLAIN)
                .header("Authorization", loginAndGetToken()));

        String email = "dude@gmail.com";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/user/email?email=" + email)
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Should be at least one contact left", content);
    }


    @Test()
    public void should_find_all_people_younger() throws Exception {
        LocalDate birthDate = LocalDate.of(1978, Month.DECEMBER, 21);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/birthDate?v=" + birthDate)
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        User[] users = objectMapper.readValue(content, User[].class);
        Assertions.assertEquals(1, users.length);
    }

    @Test()
    public void should_find_all_people_where_name_like() throws Exception {
        String name = "Dude D";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/fullName?v=" + name)
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        User[] users = objectMapper.readValue(content, User[].class);
        Assertions.assertEquals(1, users.length);
    }


    @Test()
    public void should_find_one_with_phone_number() throws Exception {
        String phoneNumber = "+74425346633";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/phoneNumber?v=" + phoneNumber)
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        User users = objectMapper.readValue(content, User.class);
        Assertions.assertNotNull(users);
    }

    @Test()
    public void should_find_one_with_email() throws Exception {
        String email = "dude@gmail.com";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/email?v=" + email)
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        User users = objectMapper.readValue(content, User.class);
        Assertions.assertNotNull(users);
    }

    @Test()
    public void should_transfer_money() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFullName("Another Dude");
        userDto.setEmail("fancy@email.com");
        userDto.setLogin("OrdinaryGuy");
        userDto.setPassword("solid");
        userDto.setInitialDeposit(new BigDecimal("6000"));
        userDto.setPhoneNumber("+999444555333");
        userDto.setBirthDate(LocalDate.of(2001, Month.MARCH, 7));

        String jsonRequest = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        long toAccountId = 4;
        double amount = 140.22;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/account/transfer?to=" + toAccountId + "&amount=" + amount)
                        .contentType(MediaType.TEXT_PLAIN)
                        .header("Authorization", loginAndGetToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Assertions.assertEquals("Money has been transferred successfully", content);
    }

    public String loginAndGetToken() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFullName("Dude Dudov");
        userDto.setLogin("ToughGuy");
        userDto.setPassword("CoolPassword");

        String json = objectMapper.writeValueAsString(userDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        return "Bearer " + objectMapper.readTree(content).get("token").asText();
    }
}
