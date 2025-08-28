CREATE TABLE classify_reason
(
    id          SERIAL PRIMARY KEY,                  -- Khóa chính
    code        VARCHAR(255),        -- Mã phân loại
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
    id                  SERIAL PRIMARY KEY,
    classify_reason_id  INT NOT NULL,
    system_id           INT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
    created_by  VARCHAR(255),                        -- Người tạo
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
    updated_by VARCHAR(255)                          -- Người cập nhật
CONSTRAINT fk_classify_reason
FOREIGN KEY (classify_reason_id) REFERENCES classify_reason(id) ON DELETE CASCADE
);

CREATE TABLE reason (
                        id SERIAL PRIMARY KEY,
                        code VARCHAR(30) ,                     -- Mã nguyên nhân
                        name VARCHAR(50) UNIQUE NOT NULL,      -- Tên nguyên nhân
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
--bảng Danh mục rủi ro (phân cấp cha - con)
CREATE TABLE risk_category (
                               id SERIAL PRIMARY KEY,
                               code VARCHAR(30),
                               name VARCHAR(50),
                               parent_id INT REFERENCES risk_category(id),
                               description text,
                               is_active BOOLEAN,

                               created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                               created_by  VARCHAR(255),                        -- Người tạo
                               updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                               updated_by VARCHAR(255)                          -- Người cập nhật

);
-- Bảng mapping risk_category với hệ thống ngoài
CREATE TABLE risk_category_map (
                            risk_category_id INT NOT NULL,
                            system_id INT NOT NULL,
                            PRIMARY KEY (risk_category_id, system_id),
                            CONSTRAINT fk_reason
                                FOREIGN KEY (risk_category_id) REFERENCES risk_category(id) ON DELETE CASCADE,
                            CONSTRAINT fk_system
                                FOREIGN KEY (system_id) REFERENCES system(id) ON DELETE CASCADE
);
--bảng Nhóm thuộc tính
CREATE TABLE attribute_group (
                                 id SERIAL PRIMARY KEY,
                                 code VARCHAR(30),
                                 name VARCHAR(50) UNIQUE,          -- Tên nhóm thuộc tính, unique
                                 type VARCHAR(30),
                                 description text,
                                 is_active BOOLEAN,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 created_by VARCHAR(255),
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_by VARCHAR(255)
);
-- Bảng attribute (thuộc tính đơn lẻ)
CREATE TABLE attribute (
                           id SERIAL PRIMARY KEY,
                           code VARCHAR(255),
                           name VARCHAR(255),
                           display_type VARCHAR(255), -- textbox, selectbox…
                           datatype VARCHAR(255),     -- string, number, date…
                           attribute_group_id INT REFERENCES attribute_group(id) ON DELETE SET NULL,
                           description VARCHAR(255),
                           is_active BOOLEAN DEFAULT TRUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           created_by VARCHAR(255),
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_by VARCHAR(255)
);

-- Bảng attribute_value (giá trị của thuộc tính, cho selectbox/multiselect)
CREATE TABLE attribute_value (
                                 id SERIAL PRIMARY KEY,
                                 value VARCHAR(255),
                                 attribute_id INT NOT NULL REFERENCES attribute(id) ON DELETE CASCADE,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 created_by VARCHAR(255),
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_by VARCHAR(255)
);