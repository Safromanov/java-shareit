package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
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
import ru.practicum.shareit.booking.dto.BookingGetResponse;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.jeasy.random.TypePredicates.ofType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemServiceImpl itemService;

    private EasyRandom generator;
    private final ObjectMapper objectMapper;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private ItemDto itemDto;
    private List<ItemDto> itemDtoList;
    private long id = 1;

    @BeforeEach
    public void setUp() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(Long.class, () -> id++)
                .stringLengthRange(5, 50)
                .excludeField(FieldPredicates.named("comments"))
                .excludeType(ofType(BookingGetResponse.class));
        generator = new EasyRandom(parameters);
        itemDto = generator.nextObject(ItemDto.class);
        itemDtoList = generator.objects(ItemDto.class, 5).collect(Collectors.toList());
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, eq(anyLong()))
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$.lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemDto.getNextBooking()));
    }

    @Test
    void createItem_withEmptyName() throws Exception {
        itemDto.setName("");
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDto);
        mockMvc.perform(get("/items/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, eq(anyLong()))
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$.lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemDto.getNextBooking()));
    }

    @Test
    void findByItemNameOrDesc() throws Exception {
        when(itemService.findByItemNameOrDesc(anyString(), anyInt(), anyInt()))
                .thenReturn(itemDtoList);
        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "search text"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDtoList.get(0).getId()))
                .andExpect(jsonPath("$[4].id").value(itemDtoList.get(4).getId()));
    }

    @Test
    void getAllItemsByUserId() throws Exception {
        when(itemService.getAllItemsByUserId(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemDtoList);
        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDtoList.get(0).getId()))
                .andExpect(jsonPath("$[4].id").value(itemDtoList.get(4).getId()));
    }

    @Test
    void getAllItemsByUserId_withParam() throws Exception {
        when(itemService.getAllItemsByUserId(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemDtoList);
        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", "0")
                        .param("size", "10")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDtoList.get(0).getId()))
                .andExpect(jsonPath("$[4].id").value(itemDtoList.get(4).getId()));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(any(ItemDto.class), anyLong(), anyLong()))
                .thenReturn(itemDto);
        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, id)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()))
                .andExpect(jsonPath("$.lastBooking").value(itemDto.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemDto.getNextBooking()));
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = generator.nextObject(CommentDto.class);
        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(commentDto);
        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, 1L)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.created").value(commentDto.getCreated().format(formatter)));
    }
}