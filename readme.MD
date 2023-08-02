# Spring 3 - Challenges

Instant is now with hibernate 6.x saved by using the SqlType TIMESTAMP_UTC, which changes the behavior if you have
a database set in some other timezone. 

Yesterday when doing an Instant.now() 8.ish in the morning saved a timestamp like '2023-08-02T08:12:00.987975000'. 
Today after migration doing the same at the same time saves a timestamp like '2023-08-02T06:12:00.987975000'. 

This you can get around by setting preffered type on the driver, and also tell the driver which timezone the database
has. 

There are som tests in this repo that can be used to look at the behavior, both with and without these settings. 

**The tests deleting and finding on a period of time shows the problem quite clear.** 

When not setting these properties, they fail miserable, when setting them, tests are running OK.  

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

Some tests will never work (findByTimestamp), they just show how timestamp is handled when using oracle, and not really the problem with TIMESTAMP_UTC. 
The above was true "on my machine", that is a PC, Win 10 with:
```
java 17.0.6 2023-01-17 LTS
Java(TM) SE Runtime Environment (build 17.0.6+9-LTS-190)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.6+9-LTS-190, mixed mode, sharing)
```
On my machine instants are represented like this: '2023-08-02T10:43:42.213678800Z'

It is not true on Ravis machine which is a Mac with jdk 18.0.1.1. 
On his machine instants are represented like this: '2023-08-02T09:02:45.480227Z', 
so for him, tests are running OK. 

When running code in the workflow/IT-tests instant is represented like this: 
'2023-08-02T12:34:26.637597542Z' (at 14:34 local time).  

When running code on OCP instant is represented like this:
'2023-08-02T13:06:07.154042242Z'

You can also change and run on a postgres (change active profile to local) and start the docker container
and look at the behavior.

## Other settings that might add on to strange behavior. 

When running integration-tests for one of our services TimeZone is UTC, (cause we haven't set it to anything). 

sun.util.calendar.ZoneInfo[id="UTC",offset=0,dstSavings=0,useDaylight=false,transitions=0,lastRule=null]
Instants are represented like this in the actual IT-test: '2023-08-02T10:53:50.766555073Z'
Instants saved in database through service are represented like this: '2023-08-02T10:53:50.766555Z'
When running tests at 12:53 local time. This is wrong, when comparing to how the service in production and test is 
working but all tests were OK. This has nothing to do with the Spring 3 migration though. 

In this case we need to set the TZ=Europe/Stockholm on both the service and the integration-tests.

TZ is set to Europe/Stockholm on all our deployments on OCP. 


## Database - Oracle
Timezone is set to GMT+2

SELECT DBTIMEZONE FROM DUAL;


