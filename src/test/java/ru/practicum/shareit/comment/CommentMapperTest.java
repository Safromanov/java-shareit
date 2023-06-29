package ru.practicum.shareit.comment;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentMapperTest {

    private final EasyRandom generator = new EasyRandom();

    @Test
    void commentToDto() {
        Comment comment = generator.nextObject(Comment.class);
        CommentDto commentDto = CommentMapper.commentToDto(comment);
        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
        assertThat(commentDto.getAuthorName()).isEqualTo(comment.getCommentator().getName());
        assertThat(commentDto.getCreated()).isEqualTo(comment.getCreated());
    }

    @Test
    void commentFromDto() {
        CommentDto commentDto = generator.nextObject(CommentDto.class);
        User commentator = generator.nextObject(User.class);
        Item item = generator.nextObject(Item.class);
        Comment comment = CommentMapper.commentFromDto(commentDto, commentator, item);
        assertThat(comment.getId()).isEqualTo(comment.getId());
        assertThat(comment.getText()).isEqualTo(comment.getText());
        assertThat(comment.getCommentator().getName()).isEqualTo(comment.getCommentator().getName());
        assertThat(comment.getCreated()).isEqualTo(comment.getCreated());
        assertThat(comment.getItem()).isEqualTo(item);
        assertThat(comment.getCommentator()).isEqualTo(commentator);
    }
}