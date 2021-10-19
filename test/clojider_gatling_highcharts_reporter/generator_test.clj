(ns clojider-gatling-highcharts-reporter.generator-test
  (:require [clojure.test :refer :all]
            [clj-containment-matchers.clojure-test :refer :all]
            [clojure.java.io :as io]
            [clojider-gatling-highcharts-reporter.generator :refer [create-chart]])
  (:import [java.io File]))

(defn create-dir [^String dir]
    (.mkdirs (File. dir)))

(defn- copy-file [source-path dest-path]
  (io/copy (io/file source-path) (io/file dest-path)))

(defn- delete-file-if-exists [path]
  (when (.exists (io/as-file path))
    (io/delete-file path)))

(deftest creates-chart-from-simulation-file
  (create-dir "target/test-results/input")
  (copy-file "test/simulation.log" "target/test-results/input/simulation.log")
  (delete-file-if-exists "target/test-results/index.html")
  (create-chart "target/test-results")
  (is (.exists (io/as-file "target/test-results/index.html"))))
