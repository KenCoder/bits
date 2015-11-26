(ns qbits.core
  (:use [domina :only [by-id log]])
  (:require
    [domina.events :as ev]
    [clojure.browser.repl :as repl]
    ))
(log "initializing")
;(set! (.-onload js/window) init)
