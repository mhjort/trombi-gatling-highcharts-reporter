(ns clojider-gatling-highcharts-reporter.core
  (:import [org.joda.time LocalDateTime]
           [scala.collection.mutable HashMap]
           [io.gatling.charts.report ReportsGenerator]
           [io.gatling.charts.result.reader FileDataReader]
           [io.gatling.core.config GatlingConfiguration]
           [org.joda.time LocalDateTime])
  (:require [clj-time.format :refer [formatter unparse-local]]
            [clojure.java.io :as io]
            [clojure-csv.core :refer [write-csv]]))

(defn- path-join [& paths]
  (.getCanonicalPath (apply io/file paths)))

(defn- flatten-one-level [coll]
  (mapcat #(if (sequential? %) % [%]) coll))

(defn- map-request [scenario-name request]
  (let [start (str (:start request))
        end (str (:end request))
        id (str (:id request))
        execution-start start
        request-end start
        response-start end
        execution-end end
        result (if (:result request) "OK" "KO")]
    [scenario-name id "REQUEST" "" (:name request) execution-start request-end response-start execution-end result "\u0020"]))

(defn- scenario->rows [scenario]
  (let [start (str (:start scenario))
        end (str (:end scenario))
        id (str (:id scenario))
        scenario-start [(:name scenario) id "USER" "START" start id]
        scenario-end [(:name scenario) id "USER" "END" end end]
        requests (mapcat #(vector (map-request (:name scenario) %)) (:requests scenario))]
    (concat [scenario-start] requests [scenario-end])))

(defn gatling-csv-lines [start-time simulation idx results]
  (let [timestamp (unparse-local (formatter "yyyyMMddhhmmss") start-time)
        header ["clj-gatling" (:name simulation) "RUN" timestamp "\u0020" "2.0"]]
    (conj (flatten-one-level (mapcat #(vector (scenario->rows %)) results)) header)))

(defn csv-writer [path start-time simulation idx results]
  (let [result-lines (gatling-csv-lines start-time simulation idx results)
        csv (write-csv result-lines :delimiter "\t" :end-of-line "\n")]
    (spit (str path "/simulation" idx ".log") csv)))

(defn create-chart [results-dir]
  (let [conf (HashMap.)]
    (.put conf "gatling.core.directory.results" results-dir)
    (GatlingConfiguration/setUp conf)
    (ReportsGenerator/generateFor "output" (FileDataReader. "input"))))

(defn gatling-highcharts-reporter [results-dir]
  (let [start-time (LocalDateTime.)]
    {:writer (partial csv-writer (path-join results-dir "input") start-time)
     :generator (fn [_]
                  (println "Creating report from files in" results-dir)
                  (create-chart results-dir)
                  (println (str "Open " results-dir "/index.html with your browser to see a detailed report." )))}))

