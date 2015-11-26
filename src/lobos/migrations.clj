(ns lobos.migration
  [:require
    [lobos [connectivity :refer :all] [lobos.migrations :refer :all] [schema :refer :all]]
    [clojure-getting-started.storage :refer :all]])

(defmigration add-users-table
              (up [] (create
                       (tbl :users
                            (varchar :name 100 :unique)
                            (check :name (> (length :name) 1)))))
              (down [] (drop (table :users))))