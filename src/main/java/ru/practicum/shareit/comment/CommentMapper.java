package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getCommentator().getName(),
                comment.getCreated());
    }

    public static Comment commentFromDto(CommentDto commentDto, User commentator, Item item) {
        return new Comment(commentDto.getId(),
                commentator,
                commentDto.getText(),
                item, LocalDateTime.now());
    }
}
