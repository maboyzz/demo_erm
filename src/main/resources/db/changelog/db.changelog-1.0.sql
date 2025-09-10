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
    updated_by VARCHAR(255)    ,                      -- Người cập nhật
CONSTRAINT fk_classify_reason FOREIGN KEY (classify_reason_id) REFERENCES classify_reason(id) ON DELETE CASCADE
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
                                     id          SERIAL PRIMARY KEY,
                                     reason_id   INT NOT NULL,
                                     system_id   INT NOT NULL,
                                     created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                                     created_by  VARCHAR(50),                        -- Người tạo
                                     updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                                     updated_by  VARCHAR(50),                        -- Người cập nhật
                                     CONSTRAINT fk_reason FOREIGN KEY (reason_id) REFERENCES reason(id) ON DELETE CASCADE
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
                                   id          SERIAL PRIMARY KEY,
                                   risk_category_id   INT NOT NULL,
                                   system_id   INT NOT NULL,
                                   created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                                   created_by  VARCHAR(50),                        -- Người tạo
                                   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                                   updated_by  VARCHAR(50),                        -- Người cập nhật
                                   CONSTRAINT fk_risk_category FOREIGN KEY (risk_category_id) REFERENCES risk_category(id) ON DELETE CASCADE
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
-- Bảng bien pháp phòng ngừa
CREATE TABLE handling_measure (
                                  id SERIAL PRIMARY KEY,                        -- Khóa chính, tự động tăng
                                  code VARCHAR(50) UNIQUE,                            -- Mã biện pháp
                                  name VARCHAR(50) UNIQUE,                             -- Tên biện pháp
                                  description TEXT,                             -- Mô tả chi tiết, text thay vì varchar cho linh hoạt
                                  is_active BOOLEAN,                            -- Trạng thái hoạt động
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tạo mặc định
                                  created_by VARCHAR(255),                      -- Người tạo
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian cập nhật mặc định
                                  updated_by VARCHAR(255)                       -- Người cập nhật
);
CREATE TABLE risk_type (
                           id SERIAL PRIMARY KEY,
                           code VARCHAR(50) UNIQUE ,
                           name VARCHAR(50) UNIQUE ,
                           risk_origin VARCHAR(255),
                           note text,
                           object VARCHAR(255),
                           is_active BOOLEAN,
                           created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                           created_by  VARCHAR(50),                        -- Người tạo
                           updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                           updated_by  VARCHAR(50)                         -- Người cập nhật
);
-- Bảng mapping risk_type với hệ thống ngoài
CREATE TABLE risk_type_map (
                                   id          SERIAL PRIMARY KEY,
                                   risk_type_id   INT NOT NULL,
                                   system_id   INT NOT NULL,
                                   created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                                   created_by  VARCHAR(50),                        -- Người tạo
                                   updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                                   updated_by  VARCHAR(50),                        -- Người cập nhật
                                   CONSTRAINT fk_risk_type FOREIGN KEY (risk_type_id) REFERENCES risk_type(id) ON DELETE CASCADE
);
-- Bảng mapping risk_type với attribute
CREATE TABLE attribute_risk_type (
                                     id SERIAL PRIMARY KEY,
                                     risk_type_id INT NOT NULL,
                                     attribute_group_id INT NOT NULL,
                                     attribute_id INT NOT NULL,
                                     created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                                     created_by  VARCHAR(50),                        -- Người tạo
                                     updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                                     updated_by  VARCHAR(50),
                                     CONSTRAINT fk_risk_type
                                         FOREIGN KEY (risk_type_id) REFERENCES risk_type(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_attribute_group
                                         FOREIGN KEY (attribute_group_id) REFERENCES attribute_group(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_attribute
                                         FOREIGN KEY (attribute_id) REFERENCES attribute(id) ON DELETE CASCADE
);
--// Giá trị thuộc tính risk_type
CREATE TABLE attribute_risk_type_value (
                                           id SERIAL PRIMARY KEY,
                                           attribute_risk_type_id INT NOT NULL,
                                           attribute_value_id INT NOT NULL,
                                           created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                                           created_by  VARCHAR(50),                        -- Người tạo
                                           updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                                           updated_by  VARCHAR(50),
                                           CONSTRAINT fk_attribute_risk_type
                                               FOREIGN KEY (attribute_risk_type_id) REFERENCES attribute_risk_type(id) ON DELETE CASCADE,
                                           CONSTRAINT fk_attribute_value
                                               FOREIGN KEY (attribute_value_id) REFERENCES attribute_value(id) ON DELETE CASCADE
);
-- Hành động mẫu
CREATE TABLE sample_action (
                               id SERIAL PRIMARY KEY,
                               code VARCHAR(50) UNIQUE,
                               name VARCHAR(50) UNIQUE,
                               risk_type_id INT  REFERENCES risk_type(id) ON DELETE SET NULL,
                               classify_reason_id INT REFERENCES classify_reason(id) ON DELETE SET NULL,
                               note text,
                               is_active BOOLEAN DEFAULT TRUE,
                               created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                               created_by  VARCHAR(50),                        -- Người tạo
                               updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                               updated_by  VARCHAR(50)
);

-- Chi tiết hành động (line)
CREATE TABLE sample_action_line (
                                    id SERIAL PRIMARY KEY,
                                    sample_action_id INT NOT NULL REFERENCES sample_action(id) ON DELETE CASCADE,
                                    code VARCHAR(255),
                                    name VARCHAR(255),
                                    action_type VARCHAR(255),
                                    department_id INT,
                                    content VARCHAR(255),
                                    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                                    created_by  VARCHAR(50),                        -- Người tạo
                                    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                                    updated_by  VARCHAR(50)
);
CREATE TABLE sample_action_line_map (
                               id          SERIAL PRIMARY KEY,
                               sample_action_line_id   INT NOT NULL,
                               system_id   INT NOT NULL,
                               created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                               created_by  VARCHAR(50),                        -- Người tạo
                               updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                               updated_by  VARCHAR(50),                        -- Người cập nhật
                               CONSTRAINT fk_sample_action_line FOREIGN KEY (sample_action_line_id) REFERENCES risk_type(id) ON DELETE CASCADE
);
CREATE TABLE risk (
                      id SERIAL PRIMARY KEY,
                      code VARCHAR(50) UNIQUE,
                      name VARCHAR(50) UNIQUE,
                      system_id INT NOT NULL,
                      risk_type_id INT NOT NULL REFERENCES risk_type(id) ON DELETE SET NULL,
                      risk_category_id INT REFERENCES risk_category(id) ON DELETE SET NULL,
                      reporter_id INT NOT NULL,
                      recognition_time TIMESTAMP,
                      priority_level VARCHAR(100),
                      description text,
                      expected_consequences text,
                      level INT,
                      point INT,
                      created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                      created_by  VARCHAR(50),                        -- Người tạo
                      updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                      updated_by  VARCHAR(50)                        -- Người cập nhật
);
CREATE TABLE risk_line (
                           id SERIAL PRIMARY KEY,
                           risk_id INT NOT NULL REFERENCES risk(id) ON DELETE CASCADE,
                           attribute_id INT NOT NULL REFERENCES attribute(id) ON DELETE CASCADE,
                           attribute_group_id INT REFERENCES attribute_group(id) ON DELETE SET NULL,
                           created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                           created_by  VARCHAR(50),                        -- Người tạo
                           updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                           updated_by  VARCHAR(50)                        -- Người cập nhật
);
CREATE TABLE risk_line_value (
                                 id SERIAL PRIMARY KEY,
                                 risk_line_id INT NOT NULL REFERENCES risk_line(id) ON DELETE CASCADE,
                                 attribute_value_id INT REFERENCES attribute_value(id) ON DELETE SET NULL,
                                 text_value text,
                                 created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                                 created_by  VARCHAR(50),                        -- Người tạo
                                 updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                                 updated_by  VARCHAR(50)                        -- Người cập nhật
);

CREATE TABLE risk_file (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255),
                           url VARCHAR(255),
                           risk_id INT NOT NULL REFERENCES risk(id) ON DELETE CASCADE,
                           created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                           created_by  VARCHAR(50),                        -- Người tạo
                           updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                           updated_by  VARCHAR(50)                        -- Người cập nhật
);
CREATE TABLE tag (
                     id SERIAL PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     color VARCHAR(255),
                     created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                     created_by  VARCHAR(50),                        -- Người tạo
                     updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                     updated_by  VARCHAR(50)
);
CREATE TABLE risk_tag (
                          id SERIAL PRIMARY KEY,
                          tag_id INT NOT NULL REFERENCES tag(id) ON DELETE CASCADE,
                          risk_id INT NOT NULL REFERENCES risk(id) ON DELETE CASCADE,
                          created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                          created_by  VARCHAR(50),                        -- Người tạo
                          updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                          updated_by  VARCHAR(50),
                          CONSTRAINT uq_risk_tag UNIQUE (tag_id, risk_id)
);
CREATE TABLE tracking_reason (
                                 id SERIAL PRIMARY KEY,
                                 classify_reason_id INT REFERENCES classify_reason(id) ON DELETE SET NULL,
                                 reason_id INT NOT NULL REFERENCES reason(id) ON DELETE SET NULL,
                                 count INT,
                                 object_applicable_type VARCHAR(255),
                                 state VARCHAR(255),
                                 sample_action_id INT REFERENCES sample_action(id) ON DELETE SET NULL,
                                 created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                                 created_by  VARCHAR(50),                        -- Người tạo
                                 updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                                 updated_by  VARCHAR(50)
);
CREATE TABLE risk_tracking_reason (
                             id SERIAL PRIMARY KEY,
                             risk_id INT NOT NULL REFERENCES risk(id) ON DELETE CASCADE,
                             tracking_reason_id INT NOT NULL REFERENCES  tracking_reason(id) ON DELETE CASCADE,
                             created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ tạo
                             created_by  VARCHAR(50),                        -- Người tạo
                             updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày giờ cập nhật
                             updated_by  VARCHAR(50)
);
