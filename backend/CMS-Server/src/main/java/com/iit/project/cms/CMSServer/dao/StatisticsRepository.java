package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.dto.ArticleInformationStatisticsResponse;
import com.iit.project.cms.CMSServer.dto.AuthorArticleCountInRecentMonthResponse;
import com.iit.project.cms.CMSServer.dto.NoBrowsedUserYear;
import com.iit.project.cms.CMSServer.dto.TopThreeArticleMonthResponse;
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

    public List<AuthorArticleCountInRecentMonthResponse> getAuthorArticleCountInRecentMonth() {
        String sql = "SELECT\n" +
                "\tcurrent_month.user_id as userId,\n" +
                "\tCONCAT(u.first_name, \" \", u.last_name) as name, \n" +
                "\td.dept_name  as deptName,\n" +
                "\tIFNULL(previous_month.num_articles, 0) as previousMonthArticles,\n" +
                "\tIFNULL(current_month.num_articles, 0) as currentMonthArticles,\n" +
                "\tIFNULL(current_month.num_articles, 0) - IFNULL(previous_month.num_articles, 0) as difference\n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tuser_id,\n" +
                "\t\tCOUNT(*) as num_articles\n" +
                "\tFROM\n" +
                "\t\tarticle\n" +
                "\tWHERE\n" +
                "\t\tpublish_time >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)\n" +
                "\t\tAND publish_time < CURDATE()\n" +
                "\tGROUP BY\n" +
                "\t\tuser_id) as current_month\n" +
                "LEFT JOIN\n" +
                "    (\n" +
                "\tSELECT\n" +
                "\t\tuser_id,\n" +
                "\t\tCOUNT(*) as num_articles\n" +
                "\tFROM\n" +
                "\t\tarticle\n" +
                "\tWHERE\n" +
                "\t\tpublish_time >= DATE_SUB(CURDATE(), INTERVAL 2 MONTH)\n" +
                "\t\t\tAND publish_time < DATE_SUB(CURDATE(), INTERVAL 1 MONTH)\n" +
                "\t\tGROUP BY\n" +
                "\t\t\tuser_id) as previous_month\n" +
                "ON\n" +
                "\tcurrent_month.user_id = previous_month.user_id\n" +
                "LEFT JOIN user AS u ON current_month.user_id = u.user_id\n" +
                "LEFT JOIN department AS d ON d.dept_id = u.dept_id ";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AuthorArticleCountInRecentMonthResponse.class));
    }


    public List<TopThreeArticleMonthResponse>  queryTopThreeArticleMonth(){

        String sql = "\n" +
                "select title,month_time,browse_time,ranks\n" +
                "from (\n" +
                "select article_id,month_time,browse_time,ranks\n" +
                "from (\n" +
                "select article_id,month_time,browse_time,\n" +
                "row_number() over (partition by month_time order by browse_time desc) as ranks\n" +
                "from(\n" +
                "select count(user_id) as browse_time ,article_id,month(time) as month_time\n" +
                "from browsed_history\n" +
                "where year(time) = year(current_date())\n" +
                "group by article_id,month(time)\n" +
                "order by month_time\n" +
                ") as browsed_count_tb\n" +
                ") rank_tb\n" +
                "where ranks <= 3\n" +
                ") biz_tb\n" +
                "left join(\n" +
                "select article_id,title\n" +
                "from article\n" +
                ") as article_tb on biz_tb.article_id = article_tb.article_id;\n" +
                "\n";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TopThreeArticleMonthResponse.class));

    }


    public List<NoBrowsedUserYear>  queryNoBrowsedUserYear(){

        String sql = "select user.user_id as user_id,\n" +
                "user_name ,\n" +
                "browsed_user_id\n" +
                "\n" +
                "from user\n" +
                "left join (\n" +
                "select distinct user_id as browsed_user_id from browsed_history\n" +
                "where year(time) =  year(current_date())\n" +
                ") as browsed_tb on user.user_id = browsed_tb.browsed_user_id\n" +
                "where  browsed_user_id is null";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(NoBrowsedUserYear.class));

    }



}
