(defproject clojider-gatling-highcharts-reporter "0.1.1"
  :description "Gatling Highcharts Reporter for clj-gatling"
  :url "https://github.com/mhjort/clojider-gatling-highcharts-reporter"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojure-csv/clojure-csv "2.0.2"]
                 [clj-time "0.13.0"]
                 [io.gatling/gatling-charts "2.0.3"
                  :exclusions [com.typesafe.akka/akka-actor_2.10
                               org.jodd/jodd-lagarto
                               com.fasterxml.jackson.core/jackson-databind
                               net.sf.saxon/Saxon-HE]]
                 [io.gatling.highcharts/gatling-charts-highcharts "2.0.3"
                  :exclusions [io.gatling/gatling-app io.gatling/gatling-recorder]]]
  :repositories { "excilys" "http://repository.excilys.com/content/groups/public" }
  :profiles {:dev {:dependencies [[clj-containment-matchers "1.0.1"]] }})


