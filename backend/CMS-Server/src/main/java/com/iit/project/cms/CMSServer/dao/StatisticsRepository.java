package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.dto.ArticleInformationStatisticsResponse;
import com.iit.project.cms.CMSServer.entity.RoleFunction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatisticsRepository extends JdbcRepository {

    public List<ArticleInformationStatisticsResponse> getArticleInformation() {
        String sql = "WITH ranked_likes AS (\n" +
                "SELECT\n" +
                "\tarticle_id ,\n" +
                "\tlike_count ,\n" +
                "\tROW_NUMBER() OVER (\n" +
                "ORDER BY\n" +
                "\tlike_count DESC) AS likes_rank\n" +
                "FROM\n" +
                "\tstatistics_article\n" +
                "),\n" +
                "ranked_comments AS (\n" +
                "SELECT\n" +
                "\tarticle_id,\n" +
                "\tcomment_count,\n" +
                "\tRANK() OVER (\n" +
                "ORDER BY\n" +
                "\tcomment_count DESC) AS comments_rank\n" +
                "FROM\n" +
                "\tstatistics_article\n" +
                ")\n" +
                "SELECT\n" +
                "\ta.title,\n" +
                "\tCONCAT(u.first_name, \" \", u.last_name) AS author,\n" +
                "\td.dept_name ,\n" +
                "\tCOALESCE(rc.comment_count, 0) AS commentCount,\n" +
                "\tCOALESCE(sa.browsed_count , 0) AS viewsCount,\n" +
                "\trl.like_count AS likeCount,\n" +
                "\trl.likes_rank AS likesRank,\n" +
                "\tCOALESCE(sa.favorite_count , 0) AS favoriteCount,\n" +
                "\tao.operate_type AS lastOperation,\n" +
                "\tao.time AS lastOperationTime\n" +
                "FROM\n" +
                "\tarticle a\n" +
                "JOIN user u ON\n" +
                "\ta.user_id = u.user_id\n" +
                "JOIN department d ON\n" +
                "\td.dept_id = u.dept_id\n" +
                "LEFT JOIN ranked_comments rc ON\n" +
                "\ta.article_id = rc.article_id\n" +
                "LEFT JOIN statistics_article sa ON\n" +
                "\ta.article_id = sa.article_id\n" +
                "LEFT JOIN ranked_likes rl ON\n" +
                "\ta.article_id = rl.article_id\n" +
                "LEFT JOIN (\n" +
                "\tSELECT\n" +
                "\t\tarticle_id ,\n" +
                "\t\toperate_type,\n" +
                "\t\ttime\n" +
                "\tFROM\n" +
                "\t\toperate_history\n" +
                "\tWHERE\n" +
                "\t\t(article_id,\n" +
                "\t\ttime) IN (\n" +
                "\t\tSELECT\n" +
                "\t\t\tarticle_id,\n" +
                "\t\t\tMAX(time)\n" +
                "\t\tFROM\n" +
                "\t\t\toperate_history\n" +
                "\t\tGROUP BY\n" +
                "\t\t\tarticle_id)\n" +
                ") ao ON\n" +
                "\ta.article_id = ao.article_id;\n";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ArticleInformationStatisticsResponse.class));
    }


}
