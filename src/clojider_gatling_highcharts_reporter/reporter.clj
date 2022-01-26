(ns clojider-gatling-highcharts-reporter.reporter
  (:require [clojure-csv.core :refer [write-csv]]))

;; This should always match the version of gatling-charts referenced in project.clj
(def gatling-version "3.7.4")

(defn- flatten-one-level [coll]
  (mapcat #(if (sequential? %) % [%]) coll))

(defn- map-request [request]
  (let [start (str (:start request))
        end (str (:end request))
        result (if (:result request) "OK" "KO")
        exception (:exception request)
        vec-prefix ["REQUEST" "" (:name request) start end result]]
    (if exception
      (conj vec-prefix exception "\u0020")
      (conj vec-prefix "\u0020"))))

(defn- scenario->rows [scenario]
  (let [start (str (:start scenario))
        end (str (:end scenario))
        scenario-start ["USER" (:name scenario) "START" start]
        scenario-end ["USER" (:name scenario) "END" end]
        requests (mapcat #(vector (map-request %)) (:requests scenario))]
    (concat [scenario-start] requests [scenario-end])))

(defn gatling-csv-lines [start-time simulation _ results]
  (let [timestamp (-> start-time .toInstant .toEpochMilli str)
        header ["RUN" "clj-gatling" (:name simulation) timestamp "\u0020" gatling-version]]
    (conj (flatten-one-level (mapcat #(vector (scenario->rows %)) results)) header)))

(defn csv-writer [path start-time simulation idx results]
  (let [result-lines (gatling-csv-lines start-time simulation idx results)
        csv (write-csv result-lines :delimiter "\t" :end-of-line "\n")
        file-name (str path "/simulation" idx ".log")]
    (spit file-name csv)
    [file-name]))
