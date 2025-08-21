CREATE TABLE system (
                        id SERIAL PRIMARY KEY,                           -- Khóa chính
                        name VARCHAR(255) UNIQUE NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Ngày giờ tạo
                        created_by VARCHAR(255),                         -- Người tạo
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Ngày giờ cập nhật
                        updated_by VARCHAR(255)                          -- Người cập nhật
);
CREATE TABLE classify_reason (
                                 id SERIAL PRIMARY KEY,                           -- Khóa chính
                                 code VARCHAR(255) UNIQUE NOT NULL,               -- Mã phân loại
                                 name VARCHAR(255) UNIQUE NOT NULL,               -- Tên phân loại nguyên nhân
                                 system_id VARCHAR(255),                          -- Mã hệ thống dùng để mapping
                                 description VARCHAR(255),                        -- Mô tả chi tiết
                                 note VARCHAR(255),                               -- Ghi chú

                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Ngày giờ tạo
                                 created_by VARCHAR(255),                         -- Người tạo
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Ngày giờ cập nhật
                                 updated_by VARCHAR(255)                          -- Người cập nhật
);
