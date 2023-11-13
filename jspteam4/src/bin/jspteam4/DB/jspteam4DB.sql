drop database if exists jspteam4;
create database jspteam4;
use jspteam4;
drop table if exists member;
create table member(
    mno int AUTO_INCREMENT,
    mid VARCHAR(20) UNIQUE not null,
    mpwd VARCHAR(20) not null,
    memail VARCHAR(30) UNIQUE not null,
    mnickname VARCHAR(30) UNIQUE not null,
     PRIMARY KEY(mno)
);
insert into member values('1','wjdgnsckck','1234','1234','1234');
insert into member values('2','asd','1235','1235','1235');
insert into member values('3','asdf','1236','1236','1236');
insert into member values('4','asdg','1237','1237','1237');
insert into member values('5','asdh','1238','1238','1238');
insert into member values('6','asdi','1239','1239','1239');
insert into member values('7','asdj','1230','1230','1230');
insert into member values('8','asdk','1231','1231','1231');
insert into member values('9','asdl','1232','1232','1232');
insert into member values('10','asdq','1233','1233','1233');

drop table if exists category;
create table category(
    cno int AUTO_INCREMENT PRIMARY KEY,
    cname VARCHAR(30) not NULL
);
insert into category values('1','술');
insert into category values('2','밥');
insert into category values('3','노래방');
insert into category values('4','피시방');
insert into category values('5','찜질방');



drop table if exists board;
create table board(
    bno int PRIMARY KEY AUTO_INCREMENT,
    btitle VARCHAR(30) not NULL,
    bcontent longtext not NULL,
    blat varchar(30) not NULL,
    blng FLOAT not NULL,
    bdate datetime default now(),
    mno int not null,
    cno int not null,
    cattingexist tinyint default 0, -- 0이면 생성x 1이면 채팅방 생성
    Foreign Key (mno) REFERENCES member(mno) on delete CASCADE,
    Foreign Key (cno) REFERENCES category(cno) on delete CASCADE
);
drop table if exists comment;
create Table comment(
    cmno int AUTO_INCREMENT PRIMARY KEY,
    cmcontent VARCHAR(200) not null,
    cdate DATETIME DEFAULT now(),
    mno int not null,
    bno int not null,
    
    Foreign Key (mno) REFERENCES member(mno),
    Foreign Key (bno) REFERENCES board(bno)
);

drop table if exists review;
create table review(
    rno int AUTO_INCREMENT PRIMARY KEY,
    rcontent VARCHAR(100) not NULL,
    rdate DATETIME DEFAULT now(),
    rsender int,
    rreceiver int,
    rscore int,
    Foreign Key (rsender) REFERENCES member(mno),
    Foreign Key (rreceiver) REFERENCES member(mno)
);