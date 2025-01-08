# Hướng dẫn tạo docker image : ))

## first note: win chạy cái compose-up bị cmt lại ấy, uncmt đi rồi cmt lại đoạn code ko bị uncmt rồi chạy

## hehe

* docker-compose build
* docker-compose up

## done hehe

## note
trước đó thì vào docker-compose.yml rồi điền giá trị cho 5 thuộc tính
* MYSQL_ROOT_PASSWORD
* SECRET_KEY
* UPLOAD_DIR
* DB_USERNAME
* DB_PASSWORD

## note nữa
- "nhớ dấu cách nhé"
- VD: DB_PASSWORD: dellcomk

  'DB_PASSWORD:(cách)dellcomk'
- cái DB_URL thì đừng động j cả, nó chạy z ổn lắm r :)

#------------------------------------------------------------------------------------------------------------------------------------------------------------------

## image docker 
docker pull trung381/logistic_management

- chạy lệnh trên:>
- chạy docker compose dưới đây

version: '3.8'
services:
  mysql:
    image: mysql:8.0.29
    container_name: mysql
    ports:
      - 3306:3306
    networks:
      - spring-boot-network
    environment:
      MYSQL_ROOT_PASSWORD:
    volumes:
      - mysql-data:/var/lib/mysql
  app:
    image: trung381/logistic_management:1.0
    build:
      context: .
      dockerfile: Dockerfile
    container_name: logistic-app
    ports:
      - 8080:8080
    networks:
      - spring-boot-network
    environment:
      SECRET_KEY:
      UPLOAD_DIR:
      DB_URL: jdbc:mysql://mysql:3306/logistic_management?createDatabaseIfNotExist=true
      DB_USERNAME:
      DB_PASSWORD:
    depends_on:
      - mysql
networks:
  spring-boot-network:
volumes:
  mysql-data:
