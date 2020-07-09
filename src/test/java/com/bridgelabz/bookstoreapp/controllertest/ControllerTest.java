package com.bridgelabz.bookstoreapp.controllertest;

import com.bridgelabz.bookstoreapp.controller.AuthenticationController;
import com.bridgelabz.bookstoreapp.dto.BookDto;
import com.bridgelabz.bookstoreapp.payload.request.LoginRequest;
import com.bridgelabz.bookstoreapp.service.IBookStoreService;
import com.bridgelabz.bookstoreapp.utility.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private IBookStoreService iBookStoreService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationController authenticationController;

    @Test
    @WithMockUser( roles = {"ADMIN"})
    void whenLoadCsv_shouldReturn_DataLoadedSuccessfully() throws Exception {
        String username = "preranashinde";
        String password = "preranashinde";
        String value = "CSV loaded successfully";
        when(iBookStoreService.loadBookData()).thenReturn(value);
        MvcResult mvcResult = this.mockMvc.perform(get("/home/admin/loadcsv").header("Authorization", "Bearer ")).andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals(value, result);
    }

    @Test
    @WithMockUser( roles = {"ADMIN"})
    void whenGiveBookDto_ShouldReturnBookAdded() throws Exception {
        BookDto bookDto = new BookDto("Chetan Bhagat", "The 3 Mistakes Of My Life", "http:/www.mistakesofmylife.com/pic", 1999, "Description of Book");
        String convertToJson = objectMapper.writeValueAsString(bookDto);
        when(iBookStoreService.addNewBook(any())).thenReturn(convertToJson);
        MvcResult mvcResult = this.mockMvc.perform(post("/home/admin/addbook")
                .header("Authorization", "asdass")
                .content(convertToJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println(result);
        BookDto values = objectMapper.readValue(result, BookDto.class);
        Assert.assertEquals("Chetan Bhagat", values.getAuthor());
    }
//    @Test
//    void signin() throws Exception {
//        String username = "preranashinde";
//        String password = "preranashinde";
//        LoginRequest loginRequest = new LoginRequest(username, password);
//        String convertToJson = objectMapper.writeValueAsString(loginRequest);
//        when(authenticationController.authenticateUser(any())).thenReturn(new ResponseEntity("Sign in", HttpStatus.OK));
//        MvcResult mvcResult = this.mockMvc.perform(post("/api/auth/signin")
//                .content(convertToJson)
//                .contentType(MediaType.APPLICATION_JSON)).andReturn();
//        String result = mvcResult.getResponse().getContentAsString();
//        System.out.println(result);
//    }
}
