(ns starter.browser
  (:require [reagent.core :as r]
            [reagent.dom :as rd]))

(defonce state (r/atom {:items ["Hello" "World!"]}))

(defn new-item []
  [:input
   {:type "text"
    :placeholder "Enter a new item"
    :on-key-down (fn [e]
                   (when (= "Enter" (.-key e))
                     (swap! state update :items conj (.. e -target -value))))}])

(defn hello-world []
  [:div
   [new-item]
   [:ul (map (fn [item]
               [:li {:key item} item])
             (:items @state))]])


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

