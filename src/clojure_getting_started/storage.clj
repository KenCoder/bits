(ns clojure-getting-started.storage
  (:refer-clojure :exclude [distinct group-by update bigint boolean char double float time])
  (:require [clojure.java.jdbc :as jdbc]
            [yesql.core :refer [defqueries]]
            [environ.core :refer [env]]))

(def db-spec {:classname   "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname     "//localhost:5432/bits"
              :user        "ken"})

(defqueries "create.sql"
            {:connection db-spec})


(def patrol-box [["Camping Gear"
                  [:lantern "Propane lantern + case"]
                  [:gloves "Cowhide work gloves"]
                  [:water-jug "5 gallon, collapsable water container"]]
                 ["Cooking Gear"
                  [:dual-stove "Dual burner propane stove"]
                  [:single-stove "Single burner propane stove"]
                  [:griddle "Large griddle"]
                  :cutting-board
                  :cocoa-pot
                  ]
                 ["Cooking Utensil Box"
                  :spatula
                  :ladle
                  :slotted-spoon
                  :can-opener
                  :large-knife
                  :small-knife
                  [:plasticware "Plastic knife, fork, spoon"]
                  [:lighter "Butane stove lighter"]
                  ]
                 ["Patrol cook kit"
                  :large-pot
                  :medium-pot
                  [:large-pan "Large frying pan"]
                  [:small-pan "Small frying pan"]
                  [:plates "plates" 4]
                  ]
                 ["Cleaning gear"
                  [:wash-basins "Wash basins" 3]
                  [:hand-sanitizer "Hand sanitizer (Germex)"]
                  [:sponges "Sponges" 2]
                  [:rubber-gloves "Pair of rubber gloves"]
                  ]
                 ["Miscellaneous"
                  [:trash-bags "Box of trash bags"]
                  [:utility-line "50 yards of utility line"]
                  [:duct-tape "Roll of duct tape"]
                  [:small-propane "Small propane canisters" 3]
                  ]])

(def post-process-count {:result-set-fn first
                         :row-fn        :count
                         :identifiers   identity})

(defn log [msg x]
  (println msg x)
  x)

(defn load-gear [loc-type gear]
  (let [loc-type-map {:location_type_id loc-type}
        counter (atom 0)]
    (if (zero? (test-location-type loc-type-map post-process-count))
      (insert-location-type! loc-type-map))
    (clear-desired! loc-type-map)
    (doseq [[category & section] gear
            gear_desc section
            :let [_ (println gear_desc)
                  [id desc & count] (if (vector? gear_desc) gear_desc [gear_desc (name gear_desc) 1])
                  gear-map {:gear_id (name id) :description desc :category category}
                  gear_exists (= 1 (test-gear gear-map post-process-count))
                  ]]
      (println "Inserting " gear-map)
      ((if gear_exists update-gear! insert-gear!)
        gear-map)
      (insert-desired! (log "insert desired " {:location_type_id loc-type :gear_id (name id) :sort_order (swap! counter inc) :quantity (or (first count) 1)})))))




;
;(def db (sdb/postgresql))
;
;(defn exec [lst] (jdbc/execute! (env :database-url "postgres://localhost:5432/kebabs")
;                                lst))
;
;(println (sql (drop-table db :gear)))
;(exec (sql (drop-table db :gear)))
;(exec (sql (drop-table db :locations)))
;(exec (sql (drop-table db :gear)))
;
;
;(exec (sql (drop-table db :gear-locations)))
;(println (sql (create-table db :gear-locations
;                            (column :loc-id :varchar :length 20 :primary-key? true)
;                            (column :gear-id :varchar :length 20 :primary-key? true)
;                            (column :count :integer :not-null? true))))
;(exec (sql (create-table db :gear-locations
;                         (column :loc-id :varchar :length 20 :primary-key? true)
;                         (column :gear-id :varchar :length 20 :primary-key? true)
;                         (column :count :integer :not-null? true))))
;
;(exec (sql (create-table db :gear
;                         (column :gear-id :varchar :length 20 :primary-key? true)
;                         (column :description :varchar :length 100 :not-null? true)
;                         (column :created-at :timestamp-with-time-zone :not-null? true :default '(now))
;                         (column :updated-at :timestamp-with-time-zone :not-null? true :default '(now)))))
;
;(exec (sql (create-table db :locations
;                         (column :loc-id :varchar :length 20 :primary-key? true)
;                         (column :description :varchar :length 100 :not-null? true)
;                         (column :created-at :timestamp-with-time-zone :not-null? true :default '(now))
;                         (column :updated-at :timestamp-with-time-zone :not-null? true :default '(now)))))
;
;(exec (sql (create-table db :shakedowns
;                         (column :day :varchar :length 20 :primary-key? true)
;                         (column :loc-id :varchar :length 20 :primary-key? true)
;                         (column :gear-id :varchar :length 20 :primary-key? true)
;                         (column :scout-id :varchar :length 20)
;                         (column :count :integer :not-null? true))))
