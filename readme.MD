# Spring 3 - Challenges

Instant is now with hibernate 6.x saved by using the SqlType TIMESTAMP_UTC, which changes the behavior if you have
a database set in some other timezone. 

Yesterday when doing an Instant.now() 8.ish in the morning saved a timestamp like '2023-08-02T08:12:00.987975000'. 
Today after migration doing the same at the same time saves a timestamp like '2023-08-02T06:12:00.987975000'. 

This you can get around by setting preffered type on the driver, and also tell the driver which timezone the database
has. 

There are som tests in this repo that can be used to look at the behavior, both with and without these settings. 
Some tests will never work (findByTimestamp), they just show how timestamp is handled when using oracle. 
But the test deleting and finding on a period of time shows the problem quite clear. 

When not setting these properties, they fail miserable.  

You can also change and run on a postgres (change active profile to local) and start the docker container
and look at the behavior. 

```
spring:  
  jpa:
    properties:
      hibernate:
        jdbc:
          time_zone: Europe/Stockholm
        type:
          preferred_instant_jdbc_type: TIMESTAMP
```


## Database - Oracle
Timezone is set to GMT+2

SELECT DBTIMEZONE FROM DUAL;


