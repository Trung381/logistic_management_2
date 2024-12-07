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
VALUES (1, "Admin", "Quản trị viên", now(), now()),
		(2, "Accountant", "Kế toán", now(), now()),
        (3, "Manager", "Quản lý", now(), now()),
        (4, "Driver", "Tài xế", now(), now());

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

INSERT INTO users (id, full_name, username, password, phone, date_of_birth, role_id, created_at, updated_at)
VALUES ("US001", "Nguyen Van A", "anguyen", "122222", "11111", "2000-11-11", 4, now(), now()),
		("US002", "Tran Van B", "btran", "001010101", "133333", "2000-10-10", 4, now(), now());

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

INSERT INTO trucks (id, driver_id, license_plate, capacity, type, created_at, updated_at)
VALUES (1, "US001", "10A00000", 10, 0, now(), now()),
		(2, "US002", "10A11111", 10, 0, now(), now()),
        (3, null, "10A22222", 10, 1, now(), now());

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

INSERT INTO `schedule_configs` (id, place_a, place_b, amount, note, deleted, created_at, updated_at)
VALUES ("HT001", "Hà Nội", "Thành phố Hồ Chí Minh", 10000000, "Tuyến: Hà Nội - Hồ Chí Minh", b'0', now(), now()),
		("HT002", "Hà Nội", "Đà Nẵng", 7000000, "Tuyến: Hà Nội - Đà Nẵng", b'0', now(), now()),
        ("HT003", "Hà Nội", "Thừa Thiên - Huế", 5500000, "Tuyến: Hà Nội - Thừa Thiên-Huế", b'0', now(), now()),
        ("HT004", "Hà Nội", "Bình Dương", 9000000, "Tuyến: Hà Nội - Bình Dương", b'0', now(), now()),
        ("HT005", "Hà Nội", "Bắc Giang", 4000000, "Tuyến: Hà Nội - Bắc Giang", b'0', now(), now()),
        ("HT006", "Hà Nội", "Lào Cai", 4500000, "Tuyến: Hà Nội - Lào Cai", b'0', now(), now());

CREATE TABLE `schedules` (
  `id` VARCHAR(255) UNIQUE NOT NULL,
  `schedule_config_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến cấu hình lịch trình",
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

INSERT INTO schedules (id, schedule_config_id, truck_license, mooc_license, departure_time, arrival_time, created_at, updated_at)
VALUES ("HT-001", "HT001", "10A00000", "10A33333", now(), now(), now(), now()),
		("HT-002", "HT002", "10A11111", "10A33333", now(), now(), now(), now());

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

INSERT INTO `expenses_configs` (id, type, note, deleted, created_at, updated_at)
VALUES ("CP001", "Nhiên liệu", "Chi phí nhiên liệu", b'0', now(), now()),
		("CP002", "Sữa chữa", "Chi phí sửa chữa xe", b'0', now(), now()),
        ("CP003", "Cúng xe", "Chi phí cúng xe", b'0', now(), now());
        
INSERT INTO `expenses_configs` (id, type, note, deleted, created_at, updated_at)
VALUES ("CP004", "Nhiên liệu", "Chi phí nhiên liệu", b'0', now(), now());

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
        
CREATE TABLE `outbound_transactions` (
  `id` VARCHAR(255) UNIQUE NOT NULL,
  `creator_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến người dùng - Người chịu trách nhiệm",
  `approver_id` VARCHAR(255) NOT NULL COMMENT "Người duyệt",
  `schedule_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến lịch trình",
  `approved_time` TIMESTAMP COMMENT "Thời gian duyệt",
  `goods_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến hàng hóa",
  `quantity` FLOAT NOT NULL DEFAULT 0,
  `status` INT NOT NULL DEFAULT 0 COMMENT "Trạng thái giao dịch: (-1) - Không duyệt, 0 - Chờ duyệt, 1 - Đã duyệt, 2 - Đã hoàn thành",
  `created_at` TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`creator_id`) REFERENCES `users`(`id`),
  FOREIGN KEY (`approver_id`) REFERENCES `users`(`id`),
  FOREIGN KEY (`goods_id`) REFERENCES `goods`(`id`),
  FOREIGN KEY (`schedule_id`) REFERENCES `schedules`(`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `inbound_transactions` (
  `id` VARCHAR(255) UNIQUE NOT NULL,
  `intake_user_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến người dùng - Người chịu trách nhiệm cho lần giao dịch",
  `intake_time` TIMESTAMP NOT NULL COMMENT "Thời gian giao dịch",
  `goods_id` VARCHAR(255) NOT NULL COMMENT "Khóa ngoại đến hàng hóa",
  `quantity` FLOAT NOT NULL DEFAULT 0 COMMENT "Số lượng/Khối lượng hàng hóa trên hóa đơn nhập vào",
  `created_at` TIMESTAMP NOT NULL DEFAULT now(),
  `updated_at` TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`intake_user_id`) REFERENCES `users`(`id`),
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
