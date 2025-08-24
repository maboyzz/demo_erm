CREATE TABLE system (
                        id SERIAL PRIMARY KEY,                           -- Khóa chính
                        name VARCHAR(255) UNIQUE NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Ngày giờ tạo
                        created_by VARCHAR(255),                         -- Người tạo
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Ngày giờ cập nhật
                        updated_by VARCHAR(255)                          -- Người cập nhật
);

-- CREATE System (Thêm bản ghi mới)
INSERT INTO system (name, created_at, created_by, updated_at, updated_by)
VALUES ('QMS', '2025-08-21 09:00:00', 'nthuy', '2025-08-21 09:00:00', 'nthuy');
INSERT INTO system (name, created_at, created_by, updated_at, updated_by)
VALUES ('HRM', '2025-08-21 09:00:00', 'nthuy', '2025-08-21 09:00:00', 'nthuy');

CREATE TABLE classify_reason
(
    id          SERIAL PRIMARY KEY,                  -- Khóa chính
    code        VARCHAR(255) UNIQUE NOT NULL,        -- Mã phân loại
    name        VARCHAR(255) UNIQUE NOT NULL,        -- Tên phân loại nguyên nhân
    system_id   VARCHAR(255),                        -- Mã hệ thống dùng để mapping
    description text,                        -- Mô tả chi tiết
    note       text,                        -- Ghi chú

    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
    created_by  VARCHAR(255),                        -- Người tạo
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
    updated_by VARCHAR(255)                          -- Người cập nhật
);
CREATE TABLE classify_reason_map (
    classify_reason_id  INT NOT NULL,
    system_id           INT NOT NULL,
PRIMARY KEY (classify_reason_id, system_id),
CONSTRAINT fk_classify_reason
FOREIGN KEY (classify_reason_id) REFERENCES classify_reason(id) ON DELETE CASCADE,
CONSTRAINT fk_system
FOREIGN KEY (system_id) REFERENCES system(id) ON DELETE CASCADE
);
CREATE TABLE reason (
                        id SERIAL PRIMARY KEY,
                        code VARCHAR(30) UNIQUE NOT NULL,          -- Mã nguyên nhân
                        name VARCHAR(50),                          -- Tên nguyên nhân
                        type VARCHAR(20) NOT NULL,             -- Loại nguyên nhân
                        classify_reason_id INT REFERENCES classify_reason(id) ON DELETE SET NULL,
                        origin VARCHAR NOT NULL,                -- Nguồn gốc (enum)
                        note VARCHAR(255),
                        is_active BOOLEAN DEFAULT TRUE,              -- Trạng thái hoạt động

                        created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                        created_by  VARCHAR(255),                        -- Người tạo
                        updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                        updated_by VARCHAR(255)                          -- Người cập nhật
);

-- Bảng mapping reason với hệ thống ngoài
CREATE TABLE reason_map (
                            reason_id INT NOT NULL,
                            system_id INT NOT NULL,
                            PRIMARY KEY (reason_id, system_id),
                            CONSTRAINT fk_reason
                                FOREIGN KEY (reason_id) REFERENCES reason(id) ON DELETE CASCADE,
                            CONSTRAINT fk_system
                                FOREIGN KEY (system_id) REFERENCES system(id) ON DELETE CASCADE
);

