CREATE DATABASE logistic_management_2;

USE logistic_management_2;

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
(9, 'Báo cáo', 'REPORTS', NOW(), NOW());

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
        
CREATE TABLE `roles_permissions` (
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

-- Role ADMIN (role_id = 1)
INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `can_view`, `can_write`, `can_delete`, `can_approve`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 1, 1, 1, 1, NOW(), NOW()),
(2, 1, 9, 1, 1, 1, 1, NOW(), NOW()),
(3, 1, 8, 1, 1, 1, 1, NOW(), NOW()),
(4, 1, 7, 1, 1, 1, 1, NOW(), NOW()),
(5, 1, 6, 1, 1, 1, 1, NOW(), NOW()),
(6, 1, 5, 1, 1, 1, 1, NOW(), NOW()),
(7, 1, 4, 1, 1, 1, 1, NOW(), NOW()),
(8, 1, 3, 1, 1, 1, 1, NOW(), NOW()),
(9, 1, 2, 1, 1, 1, 1, NOW(), NOW());

-- Role ACCOUNTANT (role_id = 2)
INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `can_view`, `can_write`, `can_delete`, `can_approve`, `created_at`, `updated_at`) VALUES
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
INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `can_view`, `can_write`, `can_delete`, `can_approve`, `created_at`, `updated_at`) VALUES
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
INSERT INTO `roles_permissions` (`id`, `role_id`, `permission_id`, `can_view`, `can_write`, `can_delete`, `can_approve`, `created_at`, `updated_at`) VALUES
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

CREATE TABLE `salary_received` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến người dùng",
  `phone_allowance` FLOAT DEFAULT 0 COMMENT "Tiền phụ cấp",
  `basic_salary` FLOAT DEFAULT 0 COMMENT "Lương cơ bản",
  `period` VARCHAR(7) NOT NULL COMMENT "Chu kỳ thanh toán lương, format: YYYY-MM",
  `job_allowance` FLOAT DEFAULT 0 COMMENT "Tiền phụ cấp công việc",
  `bonus` FLOAT DEFAULT 0 COMMENT "Tiền thưởng",
  `monthly_paid_leave` FLOAT DEFAULT 0 COMMENT "Lương ngày nghỉ trong tháng",
  `ot` FLOAT DEFAULT 0 COMMENT "Làm thêm ngày nghỉ lễ/tết",
  `snn` FLOAT DEFAULT 0 COMMENT "BHXH đau ốm/ thai sản",
  `union_contribution` FLOAT DEFAULT 0 COMMENT "Công đoàn công ty",
  `travel_expenses_reimbursement` FLOAT DEFAULT 0 COMMENT "Thanh toán tiền đi đường",
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `salary_deduction` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến người dùng",
  `mandatory_insurance` FLOAT DEFAULT 0 COMMENT "Bảo hiểm bắt buộc",
  `trade_union` FLOAT DEFAULT 0 COMMENT "Phí công đoàn",
  `advance` FLOAT DEFAULT 0 COMMENT "Tiền tạm ứng",
  `error_of_driver` FLOAT DEFAULT 0 COMMENT "Tiền phạt do lỗi tài xế",
  `snn` FLOAT DEFAULT 0 COMMENT "Thu bảo hiểm",
  `period` VARCHAR(7) DEFAULT 0 COMMENT "Tháng nào: YYYY-MM",
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `trucks` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `driver_id` VARCHAR(255) COMMENT "Khóa ngoại đến user, null nếu là mooc",
  `license_plate` VARCHAR(255) UNIQUE NOT NULL,
  `capacity` FLOAT NOT NULL DEFAULT 0 COMMENT "Tải trọng của xe",
  `note` TEXT COMMENT "Ghi chú thêm",
  `type` INT NOT NULL DEFAULT 0 COMMENT "Loại: 0 - xe tải, 1 - mooc",
  `status` INT NOT NULL DEFAULT 1 COMMENT "Trạng thái của xe: -1: Bảo trì, 0 - Không có sẵn, 1 - Có sẵn",
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
  `arrival_time` TIMESTAMP NOT NULL COMMENT "Thời gian hoàn thành",
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

-- Bảng users
INSERT INTO `users` (`id`, `full_name`, `username`, `password`, `phone`, `date_of_birth`, `note`, `role_id`, `status`) VALUES
('user_001', 'Nguyễn Văn A', 'nguyenvana', 'hashed_password_1', '0901234567', '1990-05-15', 'Nhân viên mới', 1, 1),
('user_002', 'Trần Thị B', 'tranthib', 'hashed_password_2', '0912345678', '1995-10-20', 'Kế toán trưởng', 2, 1),
('user_003', 'Lê Văn C', 'levanc', 'hashed_password_3', '0923456789', '1988-03-10', NULL, 3, 1),
('user_004', 'Phạm Thị D', 'phamthid', 'hashed_password_4', '0934567890', '1992-07-25', 'Tài xế xe tải', 4, 1),
('user_005', 'Hoàng Văn E', 'hoangvane', 'hashed_password_5', '0945678901', '1998-12-01', 'Tài xế mooc', 4, 1),
('user_006', 'Đào Thị F', 'daothif', 'hashed_password_6', '0956789012', '1991-09-18', NULL, 2, 1),
('user_007', 'Vũ Văn G', 'vuvang', 'hashed_password_7', '0967890123', '1997-02-05', 'Tài xế tập sự', 4, 0);

-- Bảng trucks
INSERT INTO `trucks` (`driver_id`, `license_plate`, `capacity`, `note`, `type`, `status`) VALUES
('user_004', '51C-12345', 10000, 'Xe tải thùng kín', 0, 1),
('user_005', '51D-67890', 20000, 'Mooc container', 1, 1),
('user_003', '51E-54321', 15000, 'Xe tải ben', 0, 0),
(NULL, '51F-98765', 30000, 'Mooc chở vật liệu', 1, 1),
('user_007', '51G-24680', 8000, 'Xe tải nhỏ', 0, -1),
('user_004', '52H-13579', 12000, 'Xe thùng mui bạt', 0, 1),
(NULL, '52K-86420', 35000, 'Mooc chở hàng rời', 1, 0);

-- Bảng schedule_configs
INSERT INTO `schedule_configs` (`id`, `place_a`, `place_b`, `amount`, `note`) VALUES
('config_001', 'Hồ Chí Minh', 'Đồng Nai', 1500000, 'Chuyến đi hàng ngày'),
('config_002', 'Hồ Chí Minh', 'Bình Dương', 2000000, 'Chuyến đi hàng tuần'),
('config_003', 'Hồ Chí Minh', 'Vũng Tàu', 2500000, 'Chuyến đi hàng tháng'),
('config_004', 'Đồng Nai', 'Bình Dương', 1800000, 'Chuyến đi hàng ngày'),
('config_005', 'Bình Dương', 'Vũng Tàu', 2200000, 'Chuyến đi hàng tuần'),
('config_006', 'Hà Nội', 'Hải Phòng', 2700000, 'Chuyến đi đường dài'),
('config_007', 'Đà Nẵng', 'Huế', 1200000, 'Chuyến đi ngắn');

-- Bảng schedules
INSERT INTO `schedules` (`id`, `schedule_config_id`, `truck_license`, `mooc_license`, `departure_time`, `arrival_time`, `note`, `type`, `status`) VALUES
('schedule_001', 'config_001', '51C-12345', '51D-67890', '2023-11-15 08:00:00', '2023-11-15 12:00:00', 'Giao hàng cho khách A', 1, 2),
('schedule_002', 'config_002', '51E-54321', '51F-98765', '2023-11-16 10:00:00', '2023-11-16 14:00:00', 'Giao hàng cho khách B', 1, 1),
('schedule_003', 'config_003', '51C-12345', '51D-67890', '2023-11-17 09:00:00', '2023-11-17 16:00:00', 'Giao hàng cho khách C', 1, 0),
('schedule_004', 'config_004', '52H-13579', '52K-86420', '2023-11-18 12:00:00', '2023-11-18 17:00:00', 'Giao hàng cho khách D', 1, -1),
('schedule_005', NULL, '51G-24680', '52H-13579', '2023-11-19 11:00:00', '2023-11-19 15:00:00', 'Chạy nội bộ', 0, 2),
('schedule_006', 'config_005', '52H-13579', '52K-86420', '2023-11-20 07:00:00', '2023-11-20 14:00:00', 'Giao hàng cho khách E', 1, 1),
('schedule_007', 'config_006', '51C-12345', '51D-67890', '2023-11-21 06:00:00', '2023-11-22 18:00:00', 'Giao hàng cho khách F', 1, 0);

-- Bảng expenses_configs
INSERT INTO `expenses_configs` (`id`, `type`, `note`) VALUES
('exp_config_001', 'Nhiên liệu', 'Chi phí xăng dầu'),
('exp_config_002', 'Thay dầu', 'Chi phí thay dầu định kỳ'),
('exp_config_003', 'Sửa chữa', 'Chi phí sửa chữa xe'),
('exp_config_004', 'Phí cầu đường', 'Chi phí đi đường'),
('exp_config_005', 'Lưu trú', 'Chi phí lưu trú cho tài xế'),
('exp_config_006', 'Ăn uống', 'Chi phí ăn uống của tài xế'),
('exp_config_007', 'Phí bảo trì', 'Chi phí bảo trì xe');

-- Bảng expenses
INSERT INTO `expenses` (`id`, `schedule_id`, `expenses_config_id`, `amount`, `note`, `img_path`, `status`) VALUES
('exp_001', 'schedule_001', 'exp_config_001', 500000, 'Đổ xăng cho xe', '/path/to/image1.jpg', 1),
('exp_002', 'schedule_001', 'exp_config_004', 100000, 'Phí cầu đường', NULL, 1),
('exp_003', 'schedule_002', 'exp_config_002', 300000, 'Thay dầu xe', NULL, 0),
('exp_004', 'schedule_003', 'exp_config_001', 600000, 'Đổ xăng cho xe', '/path/to/image2.jpg', 0),
('exp_005', 'schedule_004', 'exp_config_003', 800000, 'Sửa chữa nhỏ', NULL, -1),
('exp_006', 'schedule_005', 'exp_config_006', 300000, 'Ăn uống dọc đường', NULL, 1),
('exp_007', 'schedule_006', 'exp_config_004', 100000, 'Phí cầu đường', NULL, 0),
('exp_008', 'schedule_006', 'exp_config_004', 100000, 'Phí cầu đường', NULL, 0);

-- Bảng expense_advances
INSERT INTO `expense_advances` (`driver_id`, `period`, `advance`, `remaining_balance`, `note`) VALUES
('user_004', '2023-10', 1000000, 200000, 'Ứng tiền đi đường'),
('user_005', '2023-10', 1500000, 0, 'Ứng tiền trước chuyến đi'),
('user_007', '2023-10', 800000, 100000, 'Ứng tiền xăng xe'),
('user_004', '2023-11', 1200000, 0, 'Ứng cho chuyến đi mới'),
('user_005', '2023-11', 1300000, 100000, 'Ứng lương'),
('user_007', '2023-11', 700000, 150000, 'Ứng tiền sửa xe');

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

INSERT INTO `warehouses` (id, name, note, created_at, updated_at)
VALUES ("K001", "Kho A", "Kho chứa vật liệu xây dựng", now(), now()),
		("K002", "Kho B", "Kho chứa vật liệu nổ", now(), now());

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

INSERT INTO `goods` (id, warehouse_id, name, quantity, amount, created_at, updated_at)
VALUES ("H001", "K001", "Gạch", 30, 5000000, now(), now()),
		("H002", "K001", "Đá", 10, 3500000, now(), now()),
        ("H003", "K001", "Xi măng", 50, 7000000, now(), now()),
        ("H004", "K002", "Thuốc nổ", 5, 7000000, now(), now());
        
CREATE TABLE `transactions` (
  `id` VARCHAR(255) UNIQUE NOT NULL,
  `ref_user_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến người dùng - Người chịu trách nhiệm",
  `goods_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến hàng hóa",
  `quantity` FLOAT NOT NULL DEFAULT 0,
  `transaction_time` TIMESTAMP NOT NULL,
  `origin` BIT NOT NULL DEFAULT 0 COMMENT "0 - Giao dịch nhập, 1 - Giao dịch xuất",
  `destination` VARCHAR(255),
  `deleted` BIT NOT NULL DEFAULT 0 COMMENT "0 - Chưa xóa, 1 - Đã xóa",
  `created_at` TIMESTAMP NOT NULL DEFAULT now(),
  `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`ref_user_id`) REFERENCES `users`(`id`),
  FOREIGN KEY (`goods_id`) REFERENCES `goods`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `notification` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` TEXT NOT NULL COMMENT "Tiêu đề thông báo",
  `type` TEXT COMMENT "Loại thông báo",
  `ref_id` INT UNSIGNED NOT NULL COMMENT "ID tham chiếu nội dung",
  `status` INT DEFAULT 0 COMMENT "Trạng thái của thông báo: 0 - Chưa đọc, 1 - Đã đọc",
  `created_at` TIMESTAMP NOT NULL DEFAULT now(),
  `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- Chi tiết lương
-- select
-- 	s.user_id, u.username, u.role_id,
--     s.basic_salary, s.allowance, s.advance,
--     (select coalesce(sum(commission), 0) from schedule sch, schedule_config sc
-- 		where sch.schedule_config_id = sc.id and sch.driver_id = s.user_id)
--         as totalCommission,
-- 	(select coalesce(sum(amount), 0) from schedule sch, expenses e
-- 		where sch.id = e.schedule_id and sch.driver_id = s.user_id)
--         as totalExpenses
-- from salary s
-- left join user u on s.user_id = u.id
-- where s.period < "2025-01" and s.period > "2024-01";

-- -- Tổng quan tiền lương
-- select
--     u.role_id,
--     count(distinct s.user_id) as numberOfUser,
--     sum(basic_salary) as totalBasicSalary,
--     sum(allowance) as totalAllowance,
--     (select sum(commission) from schedule_config sc, schedule sch
-- 		where sc.id = sch.schedule_config_id and sch.driver_id = s.user_id)
--         as totalCommission,
--     sum(
-- 		(select coalesce(sum(amount), 0) from schedule sch, expenses e
-- 		where sch.id = e.schedule_id and sch.driver_id = s.user_id)
-- 	) as totalExpenses,
--     sum(advance) as totalAdvance
-- from salary s
-- left join user u on s.user_id = u.id
-- where s.period < "2025-01" and s.period > "2024-01"
-- group by u.role_id, s.user_id;

-- SELECT
--     u.role_id,
--     COUNT(s.user_id) AS numberOfUser, -- Đếm số lượng người dùng duy nhất
--     SUM(s.basic_salary) AS totalBasicSalary, -- Tổng basic_salary
--     SUM(s.allowance) AS totalAllowance, -- Tổng allowance
--     (
--         SELECT SUM(sc.commission)
--         FROM schedule_config sc
--         JOIN schedule sch ON sc.id = sch.schedule_config_id
--         WHERE sch.driver_id IN (
--             SELECT s1.user_id
--             FROM salary s1
--             JOIN user u1 ON s1.user_id = u1.id
--             WHERE s1.period < "2025-01"
--               AND s1.period > "2024-01"
--               AND u1.role_id = u.role_id
--         )
--     ) AS totalCommission, -- Tổng commission theo role_id
--     (
--         SELECT SUM(e.amount)
--         FROM schedule sch
--         JOIN expenses e ON sch.id = e.schedule_id
--         WHERE sch.driver_id IN (
--             SELECT s1.user_id
--             FROM salary s1
--             JOIN user u1 ON s1.user_id = u1.id
--             WHERE s1.period < "2025-01"
--               AND s1.period > "2024-01"
--               AND u1.role_id = u.role_id
--         )
--     ) AS totalExpenses, -- Tổng amount từ bảng expenses theo role_id
--     SUM(s.advance) AS totalAdvance -- Tổng advance
-- FROM salary s
-- LEFT JOIN user u ON s.user_id = u.id
-- WHERE s.period < "2025-01" AND s.period > "2024-01"
-- GROUP BY u.role_id;

-- select
--    *
-- from salary s
-- left join user u on s.user_id = u.id
-- left join schedule sch on sch.driver_id = u.id
-- left join schedule_config sc on sch.schedule_config_id = sc.id
-- left join expenses e on e.schedule_id = sch.id
-- where s.period < "2025-01" and s.period > "2024-01";

-- select * from salary s
-- left join user u on s.user_id = u.id
-- where u.role_id = 4;

-- select * from expenses e, schedule s
-- where e.schedule_id = s.id and s.driver_id = 3;
-- select * from salary where id = 2;
