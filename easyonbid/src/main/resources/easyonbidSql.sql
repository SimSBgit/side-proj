
SHOW DATABASES;

CREATE DATABASE IF NOT EXISTS easyonbid;
USE easyonbid;

SHOW TABLES;

SELECT * FROM auction_basic;
SELECT * FROM collateral_info;
SELECT * FROM goods_detail;
SELECT * FROM price_info;

DELETE FROM auction_basic;
DELETE FROM collateral_info;
DELETE FROM goods_detail;
DELETE FROM price_info;

DROP TABLE auction_basic;
DROP TABLE collateral_info;
DROP TABLE goods_detail;
DROP TABLE price_info;

SELECT * FROM auction_basic WHERE id = 1;
SELECT * FROM collateral_info WHERE id = 1;
SELECT * FROM goods_detail WHERE id = 1;
SELECT * FROM price_info WHERE id = 1;

-- 1. 공매 기본 정보 테이블 (메인 테이블)
CREATE TABLE auction_basic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid VARCHAR(100) NOT NULL UNIQUE DEFAULT (UUID()),
    plnm_no VARCHAR(50) NOT NULL UNIQUE COMMENT '공고번호',
    pbct_no VARCHAR(50) NOT NULL COMMENT '공매번호',
    pbct_cdtn_no VARCHAR(50) COMMENT '공매조건번호',
    bid_mnmt_no VARCHAR(50) COMMENT '입찰번호',
    cltr_mnmt_no VARCHAR(100) COMMENT '물건관리번호',
    scrn_grp_cd VARCHAR(10) COMMENT '화면그룹코드',
    ctgr_full_nm VARCHAR(200) COMMENT '용도명',
    pbct_cltr_stat_nm VARCHAR(50) COMMENT '물건상태',
    dpsl_mtd_cd VARCHAR(10) COMMENT '처분방식코드',
    dpsl_mtd_nm VARCHAR(50) COMMENT '처분방식명',
    bid_mtd_nm VARCHAR(100) COMMENT '입찰방법코드명',
    pbct_begn_dtm TIMESTAMP COMMENT '입찰시작일',
    pbct_cls_dtm TIMESTAMP COMMENT '입찰종료일',
    uscbd_cnt INTEGER DEFAULT 0 COMMENT '유찰횟수',
    iqry_cnt INTEGER DEFAULT 0 COMMENT '조회수',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_uuid (uuid),
    INDEX idx_plnm_no (plnm_no),
    INDEX idx_pbct_no (pbct_no),
    INDEX idx_pbct_begn_dtm (pbct_begn_dtm),
    INDEX idx_pbct_cls_dtm (pbct_cls_dtm)
) COMMENT '공매 기본 정보';


-- 2. 물건 정보 테이블
CREATE TABLE collateral_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auction_basic_id BIGINT NOT NULL,
    cltr_no VARCHAR(50) COMMENT '물건번호',
    cltr_hstr_no VARCHAR(50) COMMENT '물건이력번호',
    cltr_nm TEXT COMMENT '물건명',
    ldnm_adrs TEXT COMMENT '지번주소',
    nmrd_adrs TEXT COMMENT '도로명주소',
    ldnm_pnu VARCHAR(50) COMMENT '지번PNU',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (auction_basic_id) REFERENCES auction_basic(id) ON DELETE CASCADE,
    INDEX idx_cltr_no (cltr_no),
    INDEX idx_auction_basic_id (auction_basic_id)
) COMMENT '물건 정보';

-- 3. 가격 정보 테이블
CREATE TABLE price_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auction_basic_id BIGINT NOT NULL,
    min_bid_prc BIGINT COMMENT '최저입찰가',
    apsl_ases_avg_amt BIGINT COMMENT '감정가',
    fee_rate VARCHAR(20) COMMENT '최저입찰가율',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (auction_basic_id) REFERENCES auction_basic(id) ON DELETE CASCADE,
    INDEX idx_auction_basic_id (auction_basic_id)
) COMMENT '가격 정보';

-- 4. 물품 상세 정보 테이블
CREATE TABLE goods_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auction_basic_id BIGINT NOT NULL,
    goods_nm VARCHAR(200) COMMENT '물품명',
    manf VARCHAR(100) COMMENT '제조사',
    mdl VARCHAR(100) COMMENT '모델',
    nrgt VARCHAR(100) COMMENT '연식',
    grbx VARCHAR(100) COMMENT '기어박스',
    endpc VARCHAR(100) COMMENT '배기량',
    vhcl_mlge VARCHAR(100) COMMENT '차량주행거리',
    fuel VARCHAR(50) COMMENT '연료',
    scrt_nm VARCHAR(100) COMMENT '증권명',
    tpbz VARCHAR(100) COMMENT '택배',
    itm_nm VARCHAR(200) COMMENT '품목명',
    mmb_rgt_nm VARCHAR(100) COMMENT '회원권명',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (auction_basic_id) REFERENCES auction_basic(id) ON DELETE CASCADE,
    INDEX idx_auction_basic_id (auction_basic_id)
) COMMENT '물품 상세 정보';

-- 5. 이미지 파일 정보 테이블
CREATE TABLE collateral_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auction_basic_id BIGINT NOT NULL,
    image_url TEXT COMMENT '이미지URL',
    image_order INTEGER COMMENT '이미지순서',
    file_name VARCHAR(255) COMMENT '파일명',
    file_size BIGINT COMMENT '파일크기',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (auction_basic_id) REFERENCES auction_basic(id) ON DELETE CASCADE,
    INDEX idx_auction_basic_id (auction_basic_id),
    INDEX idx_image_order (auction_basic_id, image_order)
) COMMENT '물건 이미지 정보';

-- 6. API 호출 이력 테이블 (선택사항)
CREATE TABLE api_call_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    api_endpoint VARCHAR(255) COMMENT 'API엔드포인트',
    request_params TEXT COMMENT '요청파라미터',
    response_status INTEGER COMMENT '응답상태코드',
    response_data TEXT COMMENT '응답데이터',
    error_message TEXT COMMENT '에러메시지',
    call_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_call_timestamp (call_timestamp)
) COMMENT 'API 호출 이력';

-- 7. 데이터 동기화 이력 테이블 (선택사항)
CREATE TABLE sync_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plnm_no VARCHAR(50) COMMENT '공고번호',
    sync_type VARCHAR(20) COMMENT '동기화타입(INSERT/UPDATE/DELETE)',
    sync_status VARCHAR(20) COMMENT '동기화상태(SUCCESS/FAILED)',
    sync_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    error_detail TEXT COMMENT '에러상세',
    INDEX idx_plnm_no (plnm_no),
    INDEX idx_sync_timestamp (sync_timestamp)
) COMMENT '데이터 동기화 이력';

