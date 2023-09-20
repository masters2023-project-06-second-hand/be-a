DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS region;
DROP TABLE IF EXISTS image;
DROP TABLE IF EXISTS reaction;
DROP TABLE IF EXISTS chat_room;
DROP TABLE IF EXISTS chat_message;
DROP TABLE IF EXISTS chat_status;
DROP TABLE IF EXISTS report;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS member_region;
DROP TABLE IF EXISTS token;


CREATE TABLE product
(
    id              BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    region_id       BIGINT        NOT NULL,
    category_id     BIGINT        NOT NULL,
    member_id       BIGINT        NOT NULL,
    name            VARCHAR(20)   NOT NULL,
    price           BIGINT NULL,
    content         VARCHAR(1000) NOT NULL,
    created_at      DATETIME      NOT NULL,
    status          INT           NOT NULL DEFAULT 0,
    thumbnail_image VARCHAR(1000) NOT NULL,
    view_count      BIGINT        NOT NULL DEFAULT 0
);

CREATE TABLE category
(
    id      BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(20)   NOT NULL,
    img_url VARCHAR(1000) NOT NULL
);

CREATE TABLE region
(
    id   BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE image
(
    id         BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NULL,
    img_url    VARCHAR(1000) NOT NULL
);

CREATE TABLE reaction
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    member_id  BIGINT NOT NULL
);

CREATE TABLE chat_room
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    member_id  BIGINT NOT NULL
);

CREATE TABLE chat_message
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    chat_room_id BIGINT       NOT NULL,
    member_id    BIGINT       NOT NULL,
    message      VARCHAR(100) NOT NULL,
    send_at      DATETIME     NOT NULL,
    is_read      TINYINT(1) DEFAULT 0
);

CREATE TABLE chat_status
(
    id   BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE report
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL
);

CREATE TABLE member
(
    id              BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nickname        VARCHAR(20) NOT NULL,
    email           VARCHAR(50) NOT NULL,
    selected_region BiGINT NULL,
    profile_img     VARCHAR(1000) NULL
);

CREATE TABLE member_region
(
    id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    region_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL
);

CREATE TABLE token
(
    id            BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id     BIGINT        NOT NULL,
    refresh_token VARCHAR(1000) NOT NULL
);

-- category
INSERT INTO category (name, img_url)
VALUES ('인기매물', 'https://i.ibb.co/LSkHKbL/star.png'),
       ('부동산', 'https://i.ibb.co/41ScRXr/real-estate.png'),
       ('중고차', 'https://i.ibb.co/bLW8sd7/car.png'),
       ('디지털기기', 'https://i.ibb.co/cxS7Fhc/digital.png'),
       ('생활가전', 'https://i.ibb.co/F5z7vV9/domestic.png'),
       ('가구/인테리어', 'https://i.ibb.co/cyYH5V8/furniture.png'),
       ('유아동', 'https://i.ibb.co/VNKYZTK/baby.png'),
       ('유아도서', 'https://i.ibb.co/LrwjRdf/baby-book.png'),
       ('스포츠/레저', 'https://i.ibb.co/hXVgTyd/sports.png'),
       ('여성잡화', 'https://i.ibb.co/yPwkyg3/woman-accessories.png'),
       ('여성의류', 'https://i.ibb.co/4fvj6SC/woman-apparel.png'),
       ('남성패션/잡화', 'https://i.ibb.co/wwfyjyB/man-apparel.png'),
       ('게임/취미', 'https://i.ibb.co/cwJ74M4/game.png'),
       ('뷰티/미용', 'https://i.ibb.co/cXrrK0m/beauty.png'),
       ('반려동물용품', 'https://i.ibb.co/CbwHdNr/pet.png'),
       ('도서/음반', 'https://i.ibb.co/7WjKkdt/book.png'),
       ('티켓,교환권', 'https://i.ibb.co/kBhhs2p/ticket.png'),
       ('생활', 'https://i.ibb.co/T0mnp8m/kitchen.png'),
       ('가공식품', 'https://i.ibb.co/S0rSyxr/processed-foods.png'),
       ('식물', 'https://i.ibb.co/rwZhRqh/plant.png'),
       ('기타 중고물품', 'https://i.ibb.co/tCyMPf5/etc.png'),
       ('삽니다', 'https://i.ibb.co/g7Gc1w0/buy.png');


-- region
INSERT INTO region (name)
VALUES ('서울 강남동'),
       ('서울 강동동'),
       ('서울 강북동'),
       ('서울 강서동'),
       ('서울 관악동'),
       ('서울 광진동'),
       ('서울 구로동'),
       ('서울 금천동'),
       ('서울 노원동'),
       ('서울 도봉동');

-- member
INSERT INTO member (nickname, email, selected_region, profile_img)
VALUES ('김영희', 'younghee.kim@email.com', 1, 'img1.png'),
       ('이철수', 'cheolsoo.lee@email.com', 2, 'img2.png'),
       ('박지선', 'jiseon.park@email.com', 3, 'img3.png');

