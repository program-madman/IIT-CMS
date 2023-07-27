
-- 获取文章列表 根据用户id查询所有该用户可以浏览的文章 --
SELECT * FROM article WHERE article_id IN
(SELECT article_id FROM cms.audience
WHERE dept_id =
(select dept_id FROM user WHERE user_id = 100));

-- 获取文章详情 根据文章id查询一些文章详情页面的信息 --
SELECT
    a.title AS articleTitle,
    a.content AS articleContent,
    CONCAT(u.first_name, ' ', u.last_name) AS authorName,
    DATE_FORMAT(a.publish_time, '%Y-%m-%d %H:%i:%s') AS publishTime,
    (SELECT COUNT(*) FROM article_read_status ars WHERE ars.article_id = a.article_id AND ars.is_read = 1) AS readCount,
    (SELECT COUNT(*) FROM comments cmt WHERE cmt.article_id = a.article_id) AS replayCount,
    (SELECT COUNT(*) FROM article_like al WHERE al.article_id = a.article_id) AS likeCount,
    (SELECT COUNT(*) FROM browsed_history bh WHERE bh.article_id = a.article_id) AS browseCount,
    (SELECT GROUP_CONCAT(DISTINCT d.dept_name SEPARATOR ', ') FROM audience aud INNER JOIN department d ON aud.dept_id = d.dept_id WHERE aud.article_id = a.article_id) AS targetDeptName
FROM
    article a
LEFT JOIN
    user u ON a.user_id = u.user_id
WHERE
    a.article_id = 100;