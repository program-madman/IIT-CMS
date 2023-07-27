package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.entity.Attachment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AttachmentRepository extends JdbcRepository {
    public boolean createAttachment(Attachment attachment) {
        String sql = "INSERT INTO attachment (article_id, file_name, file_type, size, attach_time) " +
                "VALUES (?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, attachment.getArticleId(), attachment.getFileName(),
                attachment.getFileType(), attachment.getSize(), attachment.getAttachTime());

        return rowsAffected > 0; // 返回是否插入成功
    }

    public Attachment getAttachmentById(Long attachmentId) {
        String sql = "SELECT * FROM attachment WHERE attachment_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{attachmentId}, BeanPropertyRowMapper.newInstance(Attachment.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的附件，返回null
        }
    }

    public List<Attachment> getAttachmentsByArticleId(Long articleId) {
        String sql = "SELECT * FROM attachment WHERE article_id = ?";
        return jdbcTemplate.query(sql, new Object[]{articleId}, BeanPropertyRowMapper.newInstance(Attachment.class));
    }

    public boolean updateAttachment(Attachment attachment) {
        String sql = "UPDATE attachment SET article_id = ?, file_name = ?, file_type = ?, size = ?, attach_time = ? " +
                "WHERE attachment_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, attachment.getArticleId(), attachment.getFileName(),
                attachment.getFileType(), attachment.getSize(), attachment.getAttachTime(), attachment.getAttachmentId());

        return rowsAffected > 0; // 返回是否更新成功
    }

    public boolean deleteAttachment(Long attachmentId) {
        String sql = "DELETE FROM attachment WHERE attachment_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, attachmentId);

        return rowsAffected > 0; // 返回是否删除成功
    }
}

