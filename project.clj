(defproject clojider-gatling-highcharts-reporter "0.5.0-beta1"
  :description "Gatling Highcharts Reporter for clj-gatling"
  :url "https://github.com/mhjort/clojider-gatling-highcharts-reporter"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [clojure-csv/clojure-csv "2.0.2"]
                 [io.gatling/gatling-charts "3.7.4"
                  :exclusions [com.typesafe.akka/akka-actor_2.10
                               org.jodd/jodd-lagarto
                               com.fasterxml.jackson.core/jackson-databind
                               net.sf.saxon/Saxon-HE]]
                 [io.gatling.highcharts/gatling-charts-highcharts "3.7.4"
                  :exclusions [io.gatling/gatling-app io.gatling/gatling-recorder]]]
  :profiles {:dev {:dependencies [[clj-containment-matchers "1.0.1"]]}})
