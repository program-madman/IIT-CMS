package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.entity.ArticleLike;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ArticleLikeRepository extends JdbcRepository {

    // 添加文章点赞记录
    public boolean addArticleLike(ArticleLike articleLike) {
        // 判断用户是否已经点赞过
        ArticleLike existingLike = getArticleLikeById(articleLike.getArticleId(), articleLike.getUserId());
        if (existingLike != null) {
            // 用户已经点赞过，执行取消点赞操作
            return deleteArticleLike(articleLike.getArticleId(), articleLike.getUserId());
        } else {
            // 用户未点赞，执行添加点赞记录操作
            String sql = "INSERT INTO article_like (article_id, user_id, time) VALUES (?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql, articleLike.getArticleId(), articleLike.getUserId(), new Date());
            return rowsAffected > 0;
        }
    }

    // 删除文章点赞记录
    public boolean deleteArticleLike(Long articleId, Long userId) {
        String sql = "DELETE FROM article_like WHERE article_id = ? AND user_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, articleId, userId);
        return rowsAffected > 0;
    }

    // 根据文章ID和用户ID查询点赞记录
    public ArticleLike getArticleLikeById(Long articleId, Long userId) {
        String sql = "SELECT * FROM article_like WHERE article_id = ? AND user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{articleId, userId}, BeanPropertyRowMapper.newInstance(ArticleLike.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的点赞记录，返回null
        }
    }

}