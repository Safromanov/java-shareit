package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.User;

public class CommentMapper {
    public static CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getCommentator().getName(),
                comment.getText(),
                comment.getCreated());
    }

    public static Comment commentFromDto(CommentDto commentDto, User commenter, Item item) {
        return new Comment(commentDto.getId(),
                commenter,
                commentDto.getText(),
                item, commentDto.getCreated());
    }
}
