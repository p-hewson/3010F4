
# 3010F4
Group F4 for SYSC3010

Layout for the database used:

Database sysc3010

table collected_data

Database sysc3010

table collected_data

+-------------+---------------+------+-----+---------+-------+

| Field       | Type          | Null | Key | Default | Extra |

+-------------+---------------+------+-----+---------+-------+

| sensor_id   | int(1)        | NO   |     | NULL    |       |

| date        | date          | NO   |     | NULL    |       |

| time        | int(1)        | NO   | PRI | NULL    |       |

| temperature | decimal(5,2)  | NO   |     | NULL    |       |

| tilt        | decimal(10,5) | NO   |     | NULL    |       |

+-------------+---------------+------+-----+---------+-------+

table info

+---------------+-------------+------+-----+---------+-------+

| Field         | Type        | Null | Key | Default | Extra |

+---------------+-------------+------+-----+---------+-------+

| sensor_id     | int(1)      | NO   | PRI | NULL    |       |

| name          | varchar(20) | YES  | UNI | NULL    |       |

| location      | varchar(20) | NO   |     | NULL    |       |

| time_interval | int(1)      | NO   |     | NULL    |       |

+---------------+-------------+------+-----+---------+-------+

Application Setup:
1.	Start a new project
2.	Add .java files to app>java>com.example.***.***
3.	Add .xml files to app>res>layout
4.	Change database credentials (see figure 10)
5.	Right click app in file view, add new module, and select the mysql connector folder (on github)
6.	Add this line to the gradle dependencies:
compile project(':mysql-connector-java-3.0.17-ga-bin')

