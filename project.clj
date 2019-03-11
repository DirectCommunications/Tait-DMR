(defproject au.com.directcommunications/tait-dmr "0.1.1-SNAPSHOT"
  :description "Functions related to handling specifics of the Tait DMR."
  :url "http://github.com/DirectCommunications/tait-dmr"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"
            :year 2017
            :key "mit"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [clj-time "0.14.2"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}})
