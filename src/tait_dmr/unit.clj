;; ## DMR Unit Schema
;;
;; This namespace just contains the Prismatic schema used to describe
;; the data for representing a vehicle unit.

(ns tait-dmr.unit
  (:require [schema.core :as s])
  (:import org.joda.time.DateTime))

(s/defschema ErrorFields
  "A schema for defining the optional errors that might happen."
  {(s/optional-key :errors) {:last-response-time [s/Str]}})

(s/defschema FleetAddress
  (s/constrained String #(re-find #"[0-9]{3}-[0-9]{4}-[0-9]{3}" %)))

(s/defschema Latitude
  (s/constrained Double #(and (<= % 90) (>= % -90))))

(s/defschema Longitude
  (s/constrained Double #(and (<= % 180) (>= % -180))))

(s/defschema Speed
  (s/constrained s/Int #(and (<= % 410) (>= % 0))))

(s/defschema Address
   (s/constrained s/Int pos?))

(s/defschema BaseDmrUnit
  "A schema for the expected final result of processing the raw units
  from the DMR node, the format as follows:"
  ;; Top speed of 410, we don't expect to be tracking anything faster
  ;; than a Veyron.
  {:fleet-address FleetAddress
   :latitude Latitude
   :longitude Longitude
   :speed Speed
   :address Address
   :last-response-time (s/maybe DateTime)})

(def DmrUnit
  (s/if #(or (nil? (:last-response-time %))
             (string? (:last-response-time %)))
    (merge BaseDmrUnit ErrorFields)
    BaseDmrUnit))
