CREATE TABLE `permissions` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT now(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `permissions` (`id`, `title`, `name`, `created_at`, `updated_at`) VALUES
    (1, 'Quản lý người dùng', 'USERS', NOW(), NOW()),
    (2, 'Phân quyền', 'PERMISSIONS', NOW(), NOW()),
    (3, 'Quản lý cấu hình', 'CONFIGS', NOW(), NOW()),
    (4, 'Quản lý xe tải', 'TRUCKS', NOW(), NOW()),
    (5, 'Quản lý chi phí', 'EXPENSES', NOW(), NOW()),
    (6, 'Quản lý lịch trình', 'SCHEDULES', NOW(), NOW()),
    (7, 'Quản lý lương', 'SALARIES', NOW(), NOW()),
    (8, 'Quản lý giao dịch', 'TRANSACTIONS', NOW(), NOW()),
    (9, 'Báo cáo', 'REPORTS', NOW(), NOW()),
    (10, 'Quản lý hàng hoá', 'GOODS', NOW(), NOW()),
    (11, 'Quản lý kho hàng', 'WAREHOUSES', NOW(), NOW());
    
select * from permissions where name = "SCHEDULE";

CREATE TABLE `roles` (
 `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
 `name` VARCHAR(255) NOT NULL COMMENT "Vai trò",
 `title` VARCHAR(255) NOT NULL COMMENT "Tên vai trò",
 `created_at` TIMESTAMP NOT NULL DEFAULT now(),
 `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
 PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `roles` (id, name, title, created_at, updated_at)
VALUES (1, "ADMIN", "Quản trị viên", now(), now()),
       (2, "ACCOUNTANT", "Kế toán", now(), now()),
       (3, "MANAGER", "Quản lý", now(), now()),
       (4, "DRIVER", "Tài xế", now(), now());

CREATE TABLE `role_permission` (
   `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
   `role_id` INT UNSIGNED NOT NULL COMMENT "Khóa ngoại đến vai trò",
   `permission_id` INT UNSIGNED NOT NULL COMMENT "Khóa ngoại đến quyền hạn",
   `can_write` BIT NOT NULL DEFAULT b'0' COMMENT "Quyền ghi: 0 - Không được phép, 1 - Được phép",
   `can_view` BIT NOT NULL DEFAULT b'0' COMMENT "Quyền xem: 0 - Không được phép, 1 - Được phép",
   `can_approve` BIT NOT NULL DEFAULT b'0' COMMENT "Quyền duyệt: 0 - Không được phép, 1 - Được phép",
   `can_delete` BIT NOT NULL DEFAULT b'0' COMMENT "Quyền xóa: 0 - Không được phép, 1 - Được phép",
   `created_at` TIMESTAMP NOT NULL DEFAULT now(),
   `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
   PRIMARY KEY (`id`),
   FOREIGN KEY (`permission_id`) REFERENCES `permissions`(`id`),
   FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`, `can_view`, `can_write`, `can_delete`, `can_approve`, `created_at`, `updated_at`) VALUES
    (37, 1, 10, 1, 1, 1, 1, NOW(), NOW()),
    (38, 3, 10, 1, 1, 1, 1, NOW(), NOW()),
    (39, 1, 11, 1, 1, 1, 1, NOW(), NOW()),
    (40, 3, 11, 1, 1, 1, 1, NOW(), NOW());


-- Role ADMIN (role_id = 1)
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`, `can_view`, `can_write`, `can_delete`, `can_approve`, `created_at`, `updated_at`) 
VALUES (1, 1, 1, 1, 1, 1, 1, NOW(), NOW()),
   (2, 1, 9, 1, 1, 1, 1, NOW(), NOW()),
   (3, 1, 8, 1, 1, 1, 1, NOW(), NOW()),
   (4, 1, 7, 1, 1, 1, 1, NOW(), NOW()),
   (5, 1, 6, 1, 1, 1, 1, NOW(), NOW()),
   (6, 1, 5, 1, 1, 1, 1, NOW(), NOW()),
   (7, 1, 4, 1, 1, 1, 1, NOW(), NOW()),
   (8, 1, 3, 1, 1, 1, 1, NOW(), NOW()),
   (9, 1, 2, 1, 1, 1, 1, NOW(), NOW());

-- Role ACCOUNTANT (role_id = 2)
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`, `can_view`, `can_write`, `can_delete`, `can_approve`, `created_at`, `updated_at`) 
VALUES
   (10, 2, 1, 0, 0, 0, 0, NOW(), NOW()),
   (11, 2, 9, 1, 1, 0, 0, NOW(), NOW()),
   (12, 2, 8, 0, 0, 0, 0, NOW(), NOW()),
   (13, 2, 7, 1, 1, 1, 0, NOW(), NOW()),
   (14, 2, 6, 1, 0, 0, 1, NOW(), NOW()),
   (15, 2, 5, 1, 1, 1, 1, NOW(), NOW()),
   (16, 2, 4, 0, 0, 0, 0, NOW(), NOW()),
   (17, 2, 3, 0, 0, 0, 0, NOW(), NOW()),
   (18, 2, 2, 0, 0, 0, 0, NOW(), NOW());

-- Role MANAGER (role_id = 3)
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`, `can_view`, `can_write`, `can_delete`, `can_approve`, `created_at`, `updated_at`) VALUES
   (19, 3, 1, 0, 0, 0, 0, NOW(), NOW()),
   (20, 3, 9, 1, 1, 0, 0, NOW(), NOW()),
   (21, 3, 8, 1, 1, 1, 0, NOW(), NOW()),
   (22, 3, 7, 0, 0, 0, 0, NOW(), NOW()),
   (23, 3, 6, 1, 0, 0, 1, NOW(), NOW()),
   (24, 3, 5, 1, 0, 0, 1, NOW(), NOW()),
   (25, 3, 4, 1, 0, 0, 0, NOW(), NOW()),
   (26, 3, 3, 0, 0, 0, 0, NOW(), NOW()),
   (27, 3, 2, 0, 0, 0, 0, NOW(), NOW());

-- Role DRIVER (role_id = 4)
INSERT INTO `role_permission` (`id`, `role_id`, `permission_id`, `can_view`, `can_write`, `can_delete`, `can_approve`, `created_at`, `updated_at`) VALUES
   (28, 4, 1, 0, 0, 0, 0, NOW(), NOW()),
   (29, 4, 9, 0, 0, 0, 0, NOW(), NOW()),
   (30, 4, 8, 0, 0, 0, 0, NOW(), NOW()),
   (31, 4, 7, 0, 0, 0, 0, NOW(), NOW()),
   (32, 4, 6, 1, 1, 1, 0, NOW(), NOW()),
   (33, 4, 5, 1, 1, 1, 0, NOW(), NOW()),
   (34, 4, 4, 1, 0, 0, 0, NOW(), NOW()),
   (35, 4, 3, 0, 0, 0, 0, NOW(), NOW()),
   (36, 4, 2, 0, 0, 0, 0, NOW(), NOW());

CREATE TABLE `users` (
 `id` VARCHAR(255) UNIQUE NOT NULL,
 `full_name` VARCHAR(255) NOT NULL,
 `username` VARCHAR(255) NOT NULL,
 `password` VARCHAR(255) NOT NULL,
 `phone` VARCHAR(20) NOT NULL,
 `date_of_birth` DATE NOT NULL,
 `note` VARCHAR(255),
 `role_id` INT UNSIGNED NOT NULL COMMENT "Khóa ngoại đến vai trò",
 `status` INT NOT NULL DEFAULT 1 COMMENT "Trạng thái người dùng: 0 - Không hoạt động, 1 - Đang hoạt động",
 `created_at` TIMESTAMP NOT NULL DEFAULT NOW(),
 `updated_at` TIMESTAMP NOT NULL DEFAULT NOW(),
 PRIMARY KEY (`id`),
 FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `salary` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `user_id` VARCHAR(255) NOT NULL COMMENT 'Khóa ngoại đến người dùng',
  `period` VARCHAR(7) NOT NULL COMMENT 'Chu kỳ thanh toán lương, format: YYYY-MM',

    -- Fields from salary_received
  `phone_allowance` FLOAT DEFAULT 0 COMMENT 'Tiền phụ cấp',
  `basic_salary` FLOAT DEFAULT 0 COMMENT 'Lương cơ bản',
  `job_allowance` FLOAT DEFAULT 0 COMMENT 'Tiền phụ cấp công việc',
  `bonus` FLOAT DEFAULT 0 COMMENT 'Tiền thưởng',
  `monthly_paid_leave` FLOAT DEFAULT 0 COMMENT 'Lương ngày nghỉ trong tháng',
  `ot` FLOAT DEFAULT 0 COMMENT 'Làm thêm ngày nghỉ lễ/tết',
  `received_snn` FLOAT DEFAULT 0 COMMENT 'BHXH đau ốm/ thai sản',
  `union_contribution` FLOAT DEFAULT 0 COMMENT 'Công đoàn công ty',
  `travel_expenses_reimbursement` FLOAT DEFAULT 0 COMMENT 'Thanh toán tiền đi đường',

    -- Fields from salary_deduction
  `mandatory_insurance` FLOAT DEFAULT 0 COMMENT 'Bảo hiểm bắt buộc',
  `trade_union` FLOAT DEFAULT 0 COMMENT 'Phí công đoàn',
  `advance` FLOAT DEFAULT 0 COMMENT 'Tiền tạm ứng',
  `error_of_driver` FLOAT DEFAULT 0 COMMENT 'Tiền phạt do lỗi tài xế',
  `deduction_snn` FLOAT DEFAULT 0 COMMENT 'Thu bảo hiểm',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='Bảng tổng hợp lương nhận và khấu trừ';


CREATE TABLE `trucks` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `driver_id` VARCHAR(255) COMMENT "Khóa ngoại đến user, null nếu là mooc",
  `license_plate` VARCHAR(255) UNIQUE NOT NULL,
  `capacity` FLOAT NOT NULL DEFAULT 0 COMMENT "Tải trọng của xe",
  `note` TEXT COMMENT "Ghi chú thêm",
  `type` INT NOT NULL DEFAULT 0 COMMENT "Loại: 0 - xe tải, 1 - mooc",
  `status` INT NOT NULL DEFAULT 1 COMMENT "Trạng thái của xe: -1: Bảo trì, 0 - Đang trong hành trình, 1 - Có sẵn, 2 - Chờ duyệt lịch trình",
  `deleted` BIT NOT NULL DEFAULT b'0' COMMENT "Đã xóa: 0 - false, 1 - true",
  `created_at` TIMESTAMP NOT NULL DEFAULT now(),
  `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`driver_id`) REFERENCES `users`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `schedule_configs` (
    `id` VARCHAR(255) UNIQUE NOT NULL,
    `place_a` VARCHAR(255) NOT NULL,
    `place_b` VARCHAR(255) NOT NULL,
    `amount` FLOAT NOT NULL DEFAULT 0 COMMENT "Giá tiền cho mỗi hành trình",
    `note` TEXT COMMENT "Ghi chú cho hành trình",
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT "Đã xóa: 0 - false, 1 - true",
    `created_at` TIMESTAMP NOT NULL DEFAULT now(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `schedules` (
 `id` VARCHAR(255) UNIQUE NOT NULL,
 `schedule_config_id` VARCHAR(255) COMMENT "Khóa ngoại đến cấu hình lịch trình, null nếu chạy nội bộ",
    -- `truck_id` INT UNSIGNED NOT NULL COMMENT "Khóa ngoại đến xe tải (đầu xe)",
--   `mooc_id` INT UNSIGNED NOT NULL COMMENT "Khóa ngoại đến xe tải (rơ-mooc)",
 `truck_license` VARCHAR(255) NOT NULL,
 `mooc_license` VARCHAR(255) NOT NULL,
 `attach_document` VARCHAR(255) COMMENT "Đường dẫn lưu tài liệu bổ sung",
 `departure_time` TIMESTAMP NOT NULL COMMENT "Thời gian khởi hành",
 `arrival_time` TIMESTAMP COMMENT "Thời gian hoàn thành",
 `note` TEXT COMMENT "Ghi chú của mỗi hành trình",
 `type` INT NOT NULL DEFAULT 1 COMMENT "Loại lịch trình: 0 - Chạy nội bộ, 1 - Tính lương",
 `status` INT NOT NULL DEFAULT 0 COMMENT "Trạng thái lịch trình: -1 - Không duyệt, 0 - Đang chờ, 1 - Đã duyệt và chưa hoàn thành, 2 - Đã hoàn thành",
 `deleted` BIT NOT NULL DEFAULT b'0' COMMENT "Đã xóa: 0 - false, 1 - true",
 `created_at` TIMESTAMP NOT NULL DEFAULT now(),
 `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
 PRIMARY KEY (`id`),
    -- FOREIGN KEY (`truck_id`) REFERENCES `trucks`(`id`),
--   FOREIGN KEY (`mooc_id`) REFERENCES `trucks`(`id`),
-- FOREIGN KEY (`truck_license`) REFERENCES `trucks`(`license_plate`),
-- FOREIGN KEY (`mooc_license`) REFERENCES `trucks`(`license_plate`),
 FOREIGN KEY (`schedule_config_id`) REFERENCES `schedule_configs`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `expenses_configs` (
    `id` VARCHAR(255) UNIQUE NOT NULL,
    `type` TEXT NOT NULL COMMENT "Loại chi phí: Nhiên liệu, Thay dầu,...",
    `note` TEXT COMMENT "Ghi chú của cấu hình chi phí",
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT "Trạng thái: 0 - Chưa xóa, 1 - Đã xóa",
    `created_at` TIMESTAMP NOT NULL DEFAULT now(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `expenses` (
    `id` VARCHAR(255) NOT NULL,
    `schedule_id`  VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến lịch trình",
    `expenses_config_id`  VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến cấu hình chi phí",
    `amount` FLOAT NOT NULL DEFAULT 0 COMMENT "Giá tiền",
    `note` TEXT COMMENT "Ghi chú của chi phí",
    `img_path` VARCHAR(255) DEFAULT NULL COMMENT "Đường dẫn lưu ảnh hóa đơn",
    `status` INT NOT NULL DEFAULT 0 COMMENT "Trạng thái của chi phí: 0 - Chưa duyệt, 1 - Đã duyệt",
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT "Trạng thái: 0 - Chưa xóa, 1 - Đã xóa",
    `created_at` TIMESTAMP NOT NULL DEFAULT now(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`schedule_id`) REFERENCES `schedules`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `expense_advances` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `driver_id`  VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến tài xế (bảng user)",
    `period`  VARCHAR(7) NOT NULL COMMENT "Chu kỳ ứng tiền - yyyy-MM",
    `advance` FLOAT NOT NULL DEFAULT 0 COMMENT "Tiền ứng",
    `remaining_balance` FLOAT DEFAULT 0 COMMENT "Tiền dư sau khi tính toán các chi phí phát sinh",
    `note` TEXT COMMENT "Ghi chú",
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT "Trang thái: 0 - chưa xóa, 1 - đã xóa",
    `created_at` TIMESTAMP NOT NULL DEFAULT now(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`driver_id`) REFERENCES `users`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `warehouses` (
  `id` VARCHAR(255) UNIQUE NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `note` TEXT COMMENT "Ghi chú",
  `created_at` TIMESTAMP NOT NULL DEFAULT now(),
  `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `goods` (
 `id` VARCHAR(255) UNIQUE NOT NULL,
 `warehouse_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến kho hàng",
 `name` VARCHAR(255) NOT NULL,
 `quantity` FLOAT NOT NULL DEFAULT 0 COMMENT "Số lượng trong mỗi kho",
 `amount` FLOAT NOT NULL DEFAULT 0,
 `created_at` TIMESTAMP NOT NULL DEFAULT now(),
 `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
 PRIMARY KEY (`id`),
 FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE `transactions` (
    `id` VARCHAR(255) UNIQUE NOT NULL,
    `ref_user_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến người dùng - Người chịu trách nhiệm",
    `customer_name` VARCHAR(255) NOT NULL COMMENT "Khach hang",
    `goods_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến hàng hóa",
    `quantity` FLOAT NOT NULL DEFAULT 0,
    `transaction_time` TIMESTAMP NOT NULL,
    `origin` BIT NOT NULL DEFAULT 0 COMMENT "0 - Giao dịch nhập, 1 - Giao dịch xuất",
    `destination` VARCHAR(255),
    `deleted` BIT NOT NULL DEFAULT 0 COMMENT "0 - Chưa xóa, 1 - Đã xóa",
    `created_at` TIMESTAMP NOT NULL DEFAULT now(),
    `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
    `image` VARCHAR(255),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`ref_user_id`) REFERENCES `users`(`id`),
    FOREIGN KEY (`goods_id`) REFERENCES `goods`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- Insert data into `users` table
INSERT INTO `users` (`id`, `full_name`, `username`, `password`, `phone`, `date_of_birth`, `note`, `role_id`, `status`, `created_at`, `updated_at`) VALUES
('US001', 'Nguyen Van A', 'nguyenvana', '123456', '0901234567', '1990-05-15', 'Admin user', 1, 1, NOW(), NOW()),
('US002', 'Tran Thi B', 'tranthib', '123456', '0912345678', '1992-10-20', 'Accountant user', 2, 1, NOW(), NOW()),
('US003', 'Le Van C', 'levanc', '123456', '0923456789', '1988-03-25', 'Manager user', 3, 1, NOW(), NOW()),
('US005', 'Le Van C', 'admin123', '$2a$10$RjVfmafLNmmjvWKOSYQFI.zcJ7jxjyZYBwtg/fKWSqbtVYqB7pG86', '0923456789', '1988-03-25', 'Manager user', 1, 1, NOW(), NOW()),
('US004', 'Pham Van D', 'phamvand', '123456', '0934567890', '1995-07-10', 'Driver user', 4, 1, NOW(), NOW());


-- Insert data into `salary` table
INSERT INTO `salary` (`user_id`, `period`, `phone_allowance`, `basic_salary`, `job_allowance`, `bonus`, `monthly_paid_leave`, `ot`, `received_snn`, `union_contribution`, `travel_expenses_reimbursement`, `mandatory_insurance`, `trade_union`, `advance`, `error_of_driver`, `deduction_snn`) 
VALUES
 ('US004', '2024-05', 100000, 7000000, 500000, 200000, 500000, 100000, 0, 100000, 0, 500000, 50000, 1000000, 0, 0),
 ('US004', '2024-04', 100000, 7000000, 500000, 200000, 500000, 100000, 0, 100000, 0, 500000, 50000, 1000000, 0, 0),
 ('US004', '2024-03', 100000, 7000000, 500000, 200000, 500000, 100000, 0, 100000, 0, 500000, 50000, 1000000, 0, 0),
 ('US003', '2024-05', 100000, 10000000, 1000000, 500000, 500000, 100000, 0, 100000, 0, 500000, 50000, 1000000, 0, 0),
 ('US002', '2024-05', 100000, 9000000, 500000, 200000, 500000, 100000, 0, 100000, 0, 500000, 50000, 1000000, 0, 0);

-- Insert data into `trucks` table
INSERT INTO `trucks` (`driver_id`, `license_plate`, `capacity`, `note`, `type`, `status`, `deleted`, `created_at`, `updated_at`) VALUES
 ('US004', '51C-12345', 10, 'Xe tải chính', 0, 1, b'0', NOW(), NOW()),
 ('US001', '51R-67890', 20, 'Mooc chính', 1, 1, b'0', NOW(), NOW()),
 ('US001', '51R-11223', 20, 'Mooc phụ', 1, 0, b'0', NOW(), NOW());


-- Insert data into `schedule_configs` table
INSERT INTO `schedule_configs` (`id`, `place_a`, `place_b`, `amount`, `note`, `deleted`, `created_at`, `updated_at`) VALUES
 ('SCDC001', 'Kho A', 'Kho B', 5000000, 'Chuyến hàng cố định', b'0', NOW(), NOW()),
 ('SCDC002', 'Kho C', 'Kho D', 7000000, 'Chuyến hàng đặc biệt', b'0', NOW(), NOW()),
 ('SCDC003', 'Kho A', 'Kho C', 6000000, 'Chuyến hàng thường', b'0', NOW(), NOW());

-- Insert data into `schedules` table
INSERT INTO `schedules` (`id`, `schedule_config_id`, `truck_license`, `mooc_license`, `attach_document`, `departure_time`, `arrival_time`, `note`, `type`, `status`, `deleted`, `created_at`, `updated_at`) VALUES
 ('SCD001', 'SCDC001', '51C-12345', '51R-67890', '/path/to/document1.pdf', '2024-05-10', '2024-05-11', 'Chuyến hàng 1', 1, 2, b'0', NOW(), NOW()),
 ('SCD002', 'SCDC002', '51C-12345', '51R-11223', '/path/to/document2.pdf', '2024-05-10', '2024-05-11', 'Chuyến hàng 2', 1, 1, b'0', NOW(), NOW()),
 ('SCD003', 'SCDC003', '51C-12345', '51R-67890', '/path/to/document3.pdf', '2024-05-10', '2024-05-11', 'Chuyến hàng 3', 1, 0, b'0', NOW(), NOW()),
 ('SCD004', 'SCDC002', '51C-12345', '51R-67890', NULL, '2024-05-10', '2024-05-11', 'Chuyến hàng nội bộ 1', 0, 0, b'0', NOW(), NOW()),
 ('SCD005', 'SCDC002', '51C-12345', '51R-67890', NULL, '2024-05-10', '2024-05-11', 'Chuyến hàng nội bộ 2', 0, 0, b'0', NOW(), NOW());


-- Insert data into `expenses_configs` table
INSERT INTO `expenses_configs` (`id`, `type`, `note`, `deleted`, `created_at`, `updated_at`) VALUES
 ('EXC001', 'Nhiên liệu', 'Chi phí nhiên liệu', b'0', NOW(), NOW()),
 ('EXC002', 'Thay dầu', 'Chi phí thay dầu', b'0', NOW(), NOW()),
 ('EXC003', 'Sửa chữa', 'Chi phí sửa chữa xe', b'0', NOW(), NOW());

-- Insert data into `expenses` table
INSERT INTO `expenses` (`id`, `schedule_id`, `expenses_config_id`, `amount`, `note`, `img_path`, `status`, `deleted`, `created_at`, `updated_at`) VALUES
 ('EX001', 'SCD001', 'EXC001', 1500000, 'Chi phí nhiên liệu chuyến 1', '/path/to/image1.jpg', 1, b'0', NOW(), NOW()),
 ('EX002', 'SCD002', 'EXC002', 500000, 'Chi phí thay dầu chuyến 2', '/path/to/image2.jpg', 1, b'0', NOW(), NOW()),
 ('EX003', 'SCD003', 'EXC003', 1000000, 'Chi phí sửa chữa chuyến 3', '/path/to/image3.jpg', 0, b'0', NOW(), NOW()),
 ('EX004', 'SCD001', 'EXC003', 1000000, 'Chi phí sửa chữa chuyến 1', '/path/to/image4.jpg', 0, b'0', NOW(), NOW()),
 ('EX005', 'SCD004', 'EXC001', 1000000, 'Chi phí nhiên liệu chuyến nội bộ 1', '/path/to/image5.jpg', 0, b'0', NOW(), NOW()),
 ('EX006', 'SCD005', 'EXC001', 1000000, 'Chi phí nhiên liệu chuyến nội bộ 2', '/path/to/image6.jpg', 0, b'0', NOW(), NOW());


-- Insert data into `expense_advances` table
INSERT INTO `expense_advances` (`driver_id`, `period`, `advance`, `remaining_balance`, `note`, `deleted`, `created_at`, `updated_at`) VALUES
 ('US004', '2024-05', 2000000, 1000000, 'Ứng trước tháng 5', b'0', NOW(), NOW()),
 ('US004', '2024-04', 1000000, 500000, 'Ứng trước tháng 4', b'0', NOW(), NOW()),
 ('US004', '2024-03', 2000000, 0, 'Ứng trước tháng 3', b'0', NOW(), NOW());

-- Insert data into `warehouses` table
INSERT INTO `warehouses` (`id`, `name`, `note`, `created_at`, `updated_at`) VALUES
 ('WH001', 'Kho hàng 1', 'Kho chính', NOW(), NOW()),
 ('WH002', 'Kho hàng 2', 'Kho phụ', NOW(), NOW());

-- Insert data into `goods` table
INSERT INTO `goods` (`id`, `warehouse_id`, `name`, `quantity`, `amount`, `created_at`, `updated_at`) VALUES
 ('GS001', 'WH001', 'Gạo', 1000, 15000, NOW(), NOW()),
 ('GS002', 'WH001', 'Đường', 500, 20000, NOW(), NOW()),
 ('GS003', 'WH002', 'Muối', 2000, 10000, NOW(), NOW()),
 ('GS004', 'WH002', 'Bột ngọt', 1000, 20000, NOW(), NOW());

-- Insert data into `transactions` table
INSERT INTO `transactions` (`id`, `ref_user_id`, `goods_id`, `quantity`, `transaction_time`, `origin`, `destination`, `deleted`, `created_at`, `updated_at`, `customer_name`,`image`) VALUES
 ('TRANS001', 'US001', 'GS001', 100, '2024-05-10', b'0', 'Kho A', b'0', NOW(), NOW(),'giang', 'C:\giang'),
 ('TRANS002', 'US002', 'GS002', 50, '2024-05-10 14:00:00', b'0', 'Kho A', b'0', NOW(), NOW(), 'giang', 'C:\giang'),
 ('TRANS003', 'US003', 'GS003', 200, '2024-05-11 09:00:00', b'1', 'Kho A', b'0', NOW(), NOW(), 'giang', 'C:\giang'),
 ('TRANS004', 'US003', 'GS004', 200, '2024-05-11 10:00:00', b'1', 'Kho B', b'0', NOW(), NOW(), 'giang', 'C:\giang');
 
-- Triger trên bảng schedules - UPDATE
DELIMITER //
-- Cập nhật trạng thái của xe tải hoặc mooc khi trạng thái lịch trình thay đổi
CREATE TRIGGER UD_schedules_after_update_trigger
AFTER UPDATE ON schedules
FOR EACH ROW
BEGIN
	-- Trạng thái lịch trình: -1 - Không duyệt, 0 - Đang chờ, 1 - Đã duyệt và chưa hoàn thành, 2 - Đã hoàn thành
	-- Trạng thái xe: `status`: -1: Bảo trì, 0 - Không có sẵn, 1 - Có sẵn
    DECLARE newStatus INT;
    -- Khi status của lịch trình thay đổi
    IF OLD.status != NEW.status THEN
		SET newStatus = CASE 
							WHEN NEW.status IN (-1, 2) THEN 1
							WHEN NEW.status = 1 THEN 0
                        END;
		-- Cập nhật trạng thái xe
		UPDATE `trucks` SET `status` = newStatus WHERE `license_plate` = OLD.truck_license OR `license_plate` = OLD.mooc_license;
	END IF;
END;//
DELIMITER ;
