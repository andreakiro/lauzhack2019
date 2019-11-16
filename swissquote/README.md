# Pre-requisites
* [JDK 8+][jdk]
* [Maven 3+][mvn]
* [IDE][ide]

# First Steps

* Fork / clone the repo

* At `com.swissquote.lauzhack.evolution.sq.team` fill in the right properties (specially "path" and "team")

* As a first test run : `mvn clean install`

* You can then launch `App.main()`

# Links

* [Viewer online][viewer]
* [API Sources][sources]

# Trial configuration

* Profile : RANDOM
* Steps : 5000
* Interval : 1

# Formulas

**Client trades "Q" _EUR/CHF_ :**
* `EUR : -Q`
* `CHF : Q.r.(1+M) + 10`

**Client trades "Q" _CHF/EUR_:**
* `EUR : Q / (r.(1-M))`
* `CHF : -Q + 10`

**Client trades "Q" _EUR/CHF_:**
* `EUR : -Q`
* `CHF : Q.r.(1+M) - 100`

**Client trades "Q" _CHF/EUR_:**
* `EUR : -Q / (r.(1-M))`
* `CHF : Q - 100`

[viewer]: https://astat.github.io/sq-evolution-viewer/
[jdk]: https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[mvn]: https://maven.apache.org/download.cgi
[ide]: https://www.jetbrains.com/idea/download/
[sources]: https://github.com/Astat/sq-evolution-sources
