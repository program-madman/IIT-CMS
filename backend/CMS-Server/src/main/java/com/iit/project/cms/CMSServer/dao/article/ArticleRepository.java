package com.iit.project.cms.CMSServer.dao.article;

import com.iit.project.cms.CMSServer.dao.JdbcRepository;
import com.iit.project.cms.CMSServer.entity.article.Article;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepository extends JdbcRepository {
    public boolean createArticle(Article article) {
        String sql = "INSERT INTO article (user_id, category_name, title, content, publish_time) " +
                "VALUES (?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, article.getUserId(), article.getCategoryName(),
                article.getTitle(), article.getContent(), article.getPublishTime());

        return rowsAffected > 0; // 返回是否插入成功
    }

    public Article getArticleById(Long articleId) {
        String sql = "SELECT * FROM article WHERE article_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{articleId}, BeanPropertyRowMapper.newInstance(Article.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的文章，返回null
        }
    }

    public List<Article> getAllArticles() {
        String sql = "SELECT * FROM article";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Article.class));
    }

    public boolean updateArticle(Article article) {
        String sql = "UPDATE article SET user_id = ?, category_name = ?, title = ?, content = ?, publish_time = ?, " +
                "update_time = ? WHERE article_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, article.getUserId(), article.getCategoryName(),
                article.getTitle(), article.getContent(), article.getPublishTime(), article.getUpdateTime(),
                article.getArticleId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    public boolean deleteArticle(Long articleId) {
        String sql = "DELETE FROM article WHERE article_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, articleId);

        return rowsAffected > 0; // 返回是否删除成功
    }
}