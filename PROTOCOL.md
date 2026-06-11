# Messaging protocol definition

## Client -> Server

1. ROUTE|FROM|TO  -- asks for the shortest route from A to B
2. TRAFFIC|FROM|TO|MINUTES_CHANGED -- report addition or substraction of time going between two cities
3. BYE -- disconnect


## Server -> Client

1. OK|ROUTE|ETA_IN_MINUTES|CITY_1|CITY_2|...CITY_N -- route with eta and cities route
2. OK|TRAFFIC -- aknowledge received traffic informations
3. ERR|MESSAGE