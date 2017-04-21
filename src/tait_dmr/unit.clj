;; ## DMR Unit Schema
;;
;; This namespace just contains the Prismatic schema used to describe
;; the data for representing a vehicle unit.

(ns tait-dmr.unit
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as gen]
            [clj-time.coerce])
  (:import org.joda.time.DateTime))

(def fleet-address-regex #"[0-9]{3}-[0-9]{4}-[0-9]{3}")

(def fleet-number-digits (gen/choose 0 9))

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

(s/def ::fleet-address (s/with-gen (s/and string?
                                          #(re-matches fleet-address-regex %))
                         (fn [] fleet-number-gen)))

(s/def ::latitude (s/double-in :min -90.0 :max 90 :NaN? false :infinite? false))
(s/def ::longitude (s/double-in :min -180.0 :max 180 :NaN? false :infinite? false))

;; Top speed of 410, we don't expect to be tracking anything faster
;; than a Veyron.
(s/def ::speed (s/int-in 0 410))

(s/def ::address (s/and int? pos?))

(def date-time-generator
  (gen/fmap clj-time.coerce/from-date
            (s/gen (s/inst-in #inst "2000" #inst "2016"))))

(s/def ::last-response-time (s/with-gen (s/nilable #(instance? DateTime %))
                              (constantly date-time-generator)))

(s/def ::dmr-unit (s/keys :req [::fleet-address ::latitude ::longitude
                                ::speed ::address]
                          :opt [::last-response-time]))
