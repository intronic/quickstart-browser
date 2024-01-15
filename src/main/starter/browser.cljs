(ns starter.browser
  (:require [reagent.core :as r]
            [reagent.dom :as rd]
            [missionary.core :as m]))

(defonce state (r/atom {:items ["Hello" "World!"]}))
(defonce browser-online (r/atom js/window.navigator.onLine))

(defn compare-and-set [old new]
  (prn "compare-and-set" old new "->" (if (= old new) old new))
  (if (= old new) old new))

(.addEventListener js/window "offline" #(r/rswap! browser-online compare-and-set false))
(.addEventListener js/window "online" #(r/rswap! browser-online compare-and-set true))

(.addEventListener js/window "offline" #(js/console.log "> event offline"))
(.addEventListener js/window "online" #(js/console.log "> event online"))

;; hero icons online/syncing/offline:
;; online (normal operations) -> no symbol
;; syncing (intermittent action) -> <arrows-up-down>  | arrow-path
;; offline (restricted operations) -> <arrows-up-down> <no-symbol> | arrow-path no-symbol
;; uploading   -> no need for separate icon? or: <arrow-up-on-square-stack> | arrow-up-circle | cloud-arrow-up
;; downloading -> no need for separate icon? or: <arrow-down-on-square-stack>

(defn online-badge []
  (println "online-badge" @browser-online)
  [:div {} (str @browser-online)])

(defn new-item []
  [:input
   {:type "text"
    :placeholder "Enter a new item"
    :on-key-down (fn [e]
                   (when (= "Enter" (.-key e))
                     (swap! state update :items conj (.. e -target -value))))}])

(defn hello-world []
  [:div {}
   "OK hello"
   [online-badge]
   [:div
    [new-item]
    [:ul (map (fn [item]
                [:li {:key item} item])
              (:items @state))]]])


;; start is called by init and after code reloading finishes

(defn ^:dev/after-load start []
  (rd/render [hello-world] (js/document.getElementById "app")))

#_(defn ^:dev/after-load start []
    (js/console.log "start")
    (js/console.log js/window.navigator.onLine))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))

;; currently called by shadow-deps.edn   :init-fn starter.browser/init
(defn init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (start))
