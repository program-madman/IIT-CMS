package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.entity.Comment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CommentRepository extends JdbcRepository {
    public boolean createComment(Comment comment) {
        String sql = "INSERT INTO comments (user_id, article_id, content, publish_time) " +
                "VALUES (?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, comment.getUserId(), comment.getArticleId(),
                comment.getContent(), new Date());

        return rowsAffected > 0; // 返回是否插入成功
    }

    public Comment getCommentById(Long commentId) {
        String sql = "SELECT * FROM comments WHERE comment_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{commentId}, BeanPropertyRowMapper.newInstance(Comment.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的评论，返回null
        }
    }

    public List<Comment> getCommentsByArticleId(Long articleId) {
        String sql = "SELECT * FROM comments WHERE article_id = ?";
        return jdbcTemplate.query(sql, new Object[]{articleId}, BeanPropertyRowMapper.newInstance(Comment.class));
    }

    public List<Comment> getCommentsByUserId(Long userId) {
        String sql = "SELECT * FROM comments WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, BeanPropertyRowMapper.newInstance(Comment.class));
    }

    public boolean updateComment(Comment comment) {
        String sql = "UPDATE comments SET user_id = ?, article_id = ?, content = ?, publish_time = ? " +
                "WHERE comment_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, comment.getUserId(), comment.getArticleId(),
                comment.getContent(), new Date(), comment.getCommentId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    public boolean deleteComment(Long commentId) {
        String sql = "DELETE FROM comments WHERE comment_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, commentId);

        return rowsAffected > 0; // 返回是否删除成功
    }
}
