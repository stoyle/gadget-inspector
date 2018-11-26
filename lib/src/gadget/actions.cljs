(ns gadget.actions
  (:require [gadget.std :refer [get-in* state-data]]))

(defn to-clipboard [text]
  (let [text-area (js/document.createElement "textarea")]
    (set! (.-textContent text-area) text)
    (js/document.body.appendChild text-area)
    (.select text-area)
    (js/document.execCommand "copy")
    (.blur text-area)
    (js/document.body.removeChild text-area)))

(defn atom-path [state label & path]
  (let [idx (.indexOf (map :label (:atoms state)) label)]
    (concat [:atoms idx] path)))

(defmulti exec-action (fn [store action args] action))

(defmethod exec-action :set-path [store _ [label path]]
  (swap! store assoc-in [:atoms label :path] path))

(defmethod exec-action :copy-to-clipboard [store _ [label path]]
  (to-clipboard (pr-str (get-in* (state-data @store label) path))))


(defmethod exec-action :default [store action args]
  (prn "Unsupported action" action))
