package com.iit.project.cms.CMSServer.dao;
import com.iit.project.cms.CMSServer.entity.Favorite;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class FavoriteRepository extends JdbcRepository {
    // 添加文章到收藏
    public boolean addToFavorites(Favorite favorite) {
        String checkSql = "SELECT COUNT(*) FROM favorite WHERE user_id = ? AND article_id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, favorite.getUserId(), favorite.getArticleId());

        if (count > 0) {
            // 如果已存在相同的收藏记录，则从收藏中删除
            String deleteSql = "DELETE FROM favorite WHERE user_id = ? AND article_id = ?";
            int rowsAffected = jdbcTemplate.update(deleteSql, favorite.getUserId(), favorite.getArticleId());
            return rowsAffected > 0;
        } else {
            // 如果不存在相同的收藏记录，则执行添加操作
            String insertSql = "INSERT INTO favorite (user_id, article_id, time) VALUES (?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(insertSql, favorite.getUserId(), favorite.getArticleId(), new Date());
            return rowsAffected > 0;
        }
    }

    // 获取所有我的收藏
    public List<Favorite> getAllFavoritesByUserId(Long userId) {
        String sql = "SELECT * FROM favorite WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, BeanPropertyRowMapper.newInstance(Favorite.class));
    }


}
