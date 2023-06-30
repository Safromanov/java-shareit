package ru.practicum.shareit.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ResponseItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestServiceImpl itemRequestService;

    private EasyRandom generator;
    private final ObjectMapper objectMapper;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private ResponseItemRequest responseItemRequest;
    private List<ResponseItemRequest> responseItemRequestList;

    @BeforeEach
    void setUp() {
        generator = new EasyRandom();

        responseItemRequestList = generator.objects(ResponseItemRequest.class, 5).collect(Collectors.toList());
        responseItemRequest = generator.nextObject(ResponseItemRequest.class);
    }

    @Test
    void createRequest() throws Exception {
        when(itemRequestService.createItemRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(responseItemRequest);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, eq(anyLong()))
                        .content(objectMapper.writeValueAsString(generator.nextObject(ItemRequestDto.class))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseItemRequest.getId()))
                .andExpect(jsonPath("$.description").value(responseItemRequest.getDescription()))
                .andExpect(jsonPath("$.created").value(responseItemRequest.getCreated().format(formatter)));
    }

    @Test
    void getRequests() throws Exception {
        when(itemRequestService.getRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(responseItemRequestList);

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseItemRequestList.get(0).getId()))
                .andExpect(jsonPath("$[3].description").value(responseItemRequestList.get(3).getDescription()))
                .andExpect(jsonPath("$[4].created").value(responseItemRequestList.get(4).getCreated().format(formatter)));
    }

    @Test
    void getOtherUsersRequests() throws Exception {
        when(itemRequestService.getOtherUsersRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(responseItemRequestList);

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseItemRequestList.get(0).getId()))
                .andExpect(jsonPath("$[3].description").value(responseItemRequestList.get(3).getDescription()))
                .andExpect(jsonPath("$[4].created").value(responseItemRequestList.get(4).getCreated().format(formatter)));
    }

    @Test
    void getItemRequest() throws Exception {
        when(itemRequestService.getItemRequest(anyLong(), anyLong()))
                .thenReturn(responseItemRequest);

        mockMvc.perform(get("/requests/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseItemRequest.getId()))
                .andExpect(jsonPath("$.description").value(responseItemRequest.getDescription()))
                .andExpect(jsonPath("$.created").value(responseItemRequest.getCreated().format(formatter)))
                .andExpect(jsonPath("$.items[0].id").value(responseItemRequest.getItems().get(0).getId()));
    }
}