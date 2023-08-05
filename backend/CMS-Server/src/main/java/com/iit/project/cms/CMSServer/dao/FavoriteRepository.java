package com.iit.project.cms.CMSServer.dao;
import com.iit.project.cms.CMSServer.dto.GetFavArticlesResponse;
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

    public List<GetFavArticlesResponse> getMyFavArticlesOneSql(Long uid) {
        String sql = "SELECT " +
                "a.article_id AS articleId, " +
                "a.title AS articleTitle, " +
                "a.content AS articleContent, " +
                "CONCAT(u.first_name, ' ', u.last_name) AS authorName, " +
                "DATE_FORMAT(a.publish_time, '%Y-%m-%d %H:%i:%s') AS publishTime, " +
                "(SELECT COUNT(*) FROM article_read_status ars WHERE ars.article_id = a.article_id) AS readCount, " +
                "(SELECT COUNT(*) FROM comments cmt WHERE cmt.article_id = a.article_id) AS replyCount, " +
                "(SELECT COUNT(*) FROM article_like al WHERE al.article_id = a.article_id) AS likeCount, " +
                "(SELECT COUNT(*) FROM browsed_history bh WHERE bh.article_id = a.article_id) AS browseCount, " +
                "(SELECT GROUP_CONCAT(DISTINCT d.dept_name SEPARATOR ', ') FROM audience aud " +
                " INNER JOIN department d ON aud.dept_id = d.dept_id WHERE aud.article_id = a.article_id) AS targetDeptName, " +
                "(SELECT COUNT(*) > 0 FROM article_like al2 WHERE al2.article_id = a.article_id AND al2.user_id = ?) AS isLike, " +
                "(SELECT COUNT(*) > 0 FROM article_read_status ars2 WHERE ars2.article_id = a.article_id AND ars2.user_id = ?) AS isRead, " +
                "(SELECT COUNT(*) > 0 FROM favorite f2 WHERE f2.article_id = a.article_id AND f2.user_id = ?) AS isFav " +
                "FROM article a " +
                "LEFT JOIN user u ON a.user_id = u.user_id " +
                "WHERE a.article_id IN (SELECT article_id FROM favorite WHERE user_id = ?) " +
                "ORDER BY a.publish_time DESC";

        List<GetFavArticlesResponse> responseList = jdbcTemplate.query(
                sql, new Object[]{uid, uid, uid, uid}, BeanPropertyRowMapper.newInstance(GetFavArticlesResponse.class)
        );
        return responseList;
    }


}
