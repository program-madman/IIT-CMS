package com.iit.project.cms.CMSServer.dao;

import com.iit.project.cms.CMSServer.entity.Media;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MediaRepository extends JdbcRepository {

    // 添加媒体记录
    public boolean addMedia(Media media) {
        String sql = "INSERT INTO media (attachment_id, save_name, file_key, url, save_time) VALUES (?, ?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, media.getAttachmentId(), media.getSaveName(), media.getFileKey(),
                media.getUrl(), media.getSaveTime());
        return rowsAffected > 0;
    }

    // 更新媒体记录
    public boolean updateMedia(Media media) {
        String sql = "UPDATE media SET save_name = ?, file_key = ?, url = ?, save_time = ? WHERE attachment_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, media.getSaveName(), media.getFileKey(), media.getUrl(),
                media.getSaveTime(), media.getAttachmentId());
        return rowsAffected > 0;
    }

    // 删除媒体记录
    public boolean deleteMedia(Long attachmentId) {
        String sql = "DELETE FROM media WHERE attachment_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, attachmentId);
        return rowsAffected > 0;
    }

    // 根据attachment_id查询媒体记录
    public Media getMediaByAttachmentId(Long attachmentId) {
        String sql = "SELECT * FROM media WHERE attachment_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{attachmentId}, BeanPropertyRowMapper.newInstance(Media.class));
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            return null; // 找不到对应的媒体记录，返回null
        }
    }

    // 获取所有媒体记录
    public List<Media> getAllMedia() {
        String sql = "SELECT * FROM media";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Media.class));
    }
}
