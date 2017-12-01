
# 3010F4
Group F4 for SYSC3010

Layout for the database used:
Database sysc3010
table collected_data


Field_________Type__________Null__Key____Default
sensor_id ----int(1)--------NO-----------NULL---
date----------date----------NO-----------NULL---
time----------int(1)--------NO----PRI----NULL---
temperature---decimal(5,2)--NO-----------NULL---
tilt----------decimal(10,5)-NO-----------NULL---


table info

+---------------+-------------+------+-----+---------+-------+
| Field         | Type        | Null | Key | Default | Extra |
+---------------+-------------+------+-----+---------+-------+
| sensor_id     | int(1)      | NO   | PRI | NULL    |       |
| name          | varchar(20) | YES  | UNI | NULL    |       |
| location      | varchar(20) | NO   |     | NULL    |       |
| time_interval | int(1)      | NO   |     | NULL    |       |
+---------------+-------------+------+-----+---------+-------+
