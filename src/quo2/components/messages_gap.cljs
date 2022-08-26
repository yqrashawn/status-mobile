(ns quo2.components.messages-gap
  (:require
   ["react-native-svg" :refer [Svg Line Circle Path]]
   [quo.react-native :as rn]
   [quo.theme :as theme]
   [quo2.components.text :as text]
   [quo2.foundations.colors :as colors]
   [re-frame.core :as re-frame]
   [reagent.core :as reagent]
   [status-im.i18n.i18n :as i18n]
   [status-im.ui.components.react :refer [pressable-class]]))

(def themes
  {:light {:icon colors/neutral-40
           :time colors/neutral-50}
   :dark  {:icon colors/neutral-60
           :time colors/neutral-40}})

(defn get-color [key]
  (get-in themes [(theme/get-theme) key]))

(def svg (reagent/adapt-react-class Svg))
(def svg-line (reagent/adapt-react-class Line))
(def svg-circle (reagent/adapt-react-class Circle))
(def svg-path (reagent/adapt-react-class Path))

(defn on-press
  [chat-id gap-ids]
  (fn []
    (re-frame/dispatch [:chat.ui/fill-gaps chat-id gap-ids])))

(defn circle []
  [rn/view
   {:width         7
    :height        7
    :border-width  1
    :margin        4
    :flex          0
    :border-color  (get-color :icon)
    :border-radius 50}])

(defn timestamp [str]
  [text/text {:size  :label
              :style {:text-transform :none
                      :color          (get-color :time)}} str])

(defn info-button [on-press]
  [pressable-class
   {:on-press on-press}
   [svg {:width 16 :height 16 :view-box "0 0 16 16" :fill :none}
    [svg-circle {:cx 8 :cy 8 :r 6 :stroke colors/primary-50 :stroke-opacity 0.4}]
    [svg-path {:d "M8 7V11" :stroke colors/primary-50 :stroke-width 1.3}]
    [svg-path {:d "M8 5V6" :stroke colors/primary-50 :stroke-width 1.3}]]])

(defn left []
  [rn/view {:flex            0
            :align-items     :center
            :justify-content :space-between}
   [circle]
   [rn/view
    {:flex 1}
    [svg {:width 1 :height 58 :view-box "0 0 1 54"}
     [svg-line {:x1 0.5
                :y1 0.5
                :x2 0.5
                :y2 54
                :stroke (get-color :icon)
                :stroke-linecap :round
                :stroke-dasharray "0.1 4"}]]]
   [circle]])

(defn right [timestamp-far timestamp-near chat-id gap-ids on-info-button-pressed]
  [rn/view {:margin-left 20.5
            :flex        1}
   [rn/view
    {:flex-direction  :row
     :justify-content :space-between}
    [timestamp timestamp-far]
    [info-button on-info-button-pressed]]

   [pressable-class
    {:style    {:flex 2 :margin-top 16}
     :on-press (when (and chat-id gap-ids)
                 (on-press chat-id gap-ids))}
    [text/text
     (i18n/label :messages-gap-warning)]]

   [timestamp timestamp-near]])

(defn messages-gap
  [{:keys [timestamp-far
           timestamp-near
           gap-ids
           chat-id
           on-info-button-pressed
           style]}]
  [rn/view (merge {:width           "100%"
                   :flex-direction  :row
                   :padding-top     20
                   :padding-bottom  20
                   :justify-content :space-between}
                  style)

   [left]
   [right timestamp-far timestamp-near chat-id gap-ids on-info-button-pressed]])
