INSERT INTO PERSONAL (ID, FIRST_NAME, LAST_NAME, ENABLED, EMAIL, USERNAME, PASSWORD) VALUES
(1111, 'halil', 'muti', 1, 'halilmu@garanti.com.tr', 'muti', 'secure'),
(2222, 'deneme', 'kullanici', 1, 'deneme@garanti.com.tr', 'deneme', 'q'),
(3333, 'admin', 'kullanici', 1, 'admin@garanti.com.tr', 'admin', 'admin'),
(4444, 'user', 'kullanici', 1, 'user@garanti.com.tr', 'user', 'user');


INSERT INTO ROLE (ID, ROLE) VALUES
(1, 'ADMIN'),
(2, 'USER');


INSERT INTO PERSONAL_ROLES (USER_ID, ROLE_ID) VALUES
(1111, 2),
(2222, 2),
(3333, 1),
(4444, 2);