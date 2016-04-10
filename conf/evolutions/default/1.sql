# DC schema

# --- !Ups
CREATE TABLE "RESERVATION" (
    "ID" serial NOT NULL PRIMARY KEY,
    "START" timestamp NOT NULL,
    "LENGTH" integer NOT NULL,
    "BROADCAST" varchar(8) NOT NULL,
    "CHANNEL" varchar(8) NOT NULL,
    "TVTUNER" varchar(8) NOT NULL,
    "CREATED" timestamp NOT NULL,
    "UPDATED" timestamp NOT NULL
);

# --- !Downs

DROP TABLE "RESERVATION";
DROP TABLE "TASK";
DROP TABLE "PROJECT";
