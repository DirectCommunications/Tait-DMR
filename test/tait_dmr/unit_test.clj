(ns tait-dmr.unit-test
  (:require [tait-dmr.unit :refer :all]
            [clojure.test :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as check-clojure-test]
            [schema.core :as s]
            (schema.experimental [complete :as c]
                                 [generators :as g]))
  (:import org.joda.time.DateTime))

(def fleet-number-digits
  (gen/choose 0 9))

(def three-nat-gen
  (gen/fmap #(apply str %) (gen/tuple  fleet-number-digits
                                       fleet-number-digits
                                       fleet-number-digits)))

(def four-nat-gen
  (gen/fmap #(apply str %) (gen/tuple fleet-number-digits
                                      fleet-number-digits
                                      fleet-number-digits
                                      fleet-number-digits)))

(def fleet-number-gen
  (gen/fmap #(apply str %) (gen/fmap #(interpose "-" %)
                                     (gen/tuple three-nat-gen
                                                four-nat-gen
                                                three-nat-gen))))

(def latitude-gen
  (gen/double* {:min -90 :max 90 :NaN? false}))

(def longitude-gen
  (gen/double* {:min -180 :max 180 :NaN? false}))

(def speed-gen
  (gen/choose 0 410))

(def address-gen
  (gen/choose 1 10000))

(defn generate-test-unit
  "This is just an example of generating a test unit."
  []
  (g/generate DmrUnit
              {DateTime (gen/return (DateTime.))
               Latitude latitude-gen
               Longitude longitude-gen
               FleetAddress fleet-number-gen}))

(def unit-generator
  (g/generator DmrUnit
               {DateTime (gen/return (DateTime.))
                Latitude latitude-gen
                Longitude longitude-gen
                Speed speed-gen
                Address address-gen
                FleetAddress fleet-number-gen}))

(check-clojure-test/defspec simple-schema-test
  100
  (prop/for-all [u unit-generator]
                (not (s/check DmrUnit u))))
