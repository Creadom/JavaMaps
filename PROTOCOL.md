# Messaging protocol definition

## Client -> Server

1. ROUTE|FROM|TO  -- asks for the shortest route from A to B
2. TRAFFIC|FROM|TO|OBSERVED_MINUTES -- report the travel time a driver just observed on a direct road segment. The server SETS the segment's current time to this value (it does not add to it), clamped so it never goes below the segment's base time from the map data. Reports are idempotent: two drivers observing the same jam converge on the same value. FROM and TO must share a direct road, otherwise the server replies ERR.
3. BYE -- disconnect


## Server -> Client

1. OK|ROUTE|ETA_IN_MINUTES|CITY_1|CITY_2|...CITY_N -- route with eta and cities route
2. OK|TRAFFIC -- aknowledge received traffic informations
3. ERR|MESSAGE