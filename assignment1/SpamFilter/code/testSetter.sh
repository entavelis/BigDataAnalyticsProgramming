#!/bin/bash
#<A HREF="#mol">mol</A>

machines=`curl "http://localhost:10080/index.php?display=uptime" | grep -oP '?<=<A HREF=\"#.*\">.*</A>' `

#machines=`curl "http://localhost:10080/index.php?display=uptime"`


##machines=`tr -d "\n" <  $machines | grep -oP '?<=<A HREF=\"#.*\">.*</A>' | grep -oP '?<=>.*<' `

echo "$machines"

