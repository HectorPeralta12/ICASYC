(ns ICA)

(import [java.time LocalDateTime]
        [java.time.format DateTimeFormatter])

(def cities
  (atom {:warsaw  {:initial 450 :min 30 :max 500 :current 450}
         :krakow  {:initial 0 :min 100 :max 100 :current 0}
         :hamburg {:initial 80 :min 60 :max 100 :current 80}
         :munich  {:initial 10 :min 50 :max 150 :current 10}
         :brno    {:initial 0 :min 70 :max 80 :current 0}
         :prague  {:initial 50 :min 70 :max 80 :current 50}
         :berlin  {:initial 150 :min 50 :max 500 :current 150}}))

(def distances
  {:warsaw {:krakow 300 :hamburg 600 :munich 800 :brno 400 :prague 500 :berlin 350}
   :krakow {:warsaw 300 :hamburg 700 :munich 500 :brno 200 :prague 300 :berlin 600}
   :hamburg {:warsaw 600 :krakow 700 :munich 800 :brno 1000 :prague 900 :berlin 280}
   :munich {:warsaw 800 :krakow 500 :hamburg 800 :brno 600 :prague 700 :berlin 750}
   :brno {:warsaw 400 :krakow 200 :hamburg 1000 :munich 600 :prague 300 :berlin 500}
   :prague {:warsaw 500 :krakow 300 :hamburg 900 :munich 700 :brno 300 :berlin 350}
   :berlin {:warsaw 350 :krakow 600 :hamburg 280 :munich 750 :brno 500 :prague 350}})

(defn transfer-products [from to amount]
  ;; Function to transfer products between cities
  (let [available-from (get-in @cities [from :current])
        current-to (get-in @cities [to :current])
        capacity-to (get-in @cities [to :max])
        free-space (- capacity-to current-to)
        transfer-amount (min amount available-from free-space)]
    (if (pos? transfer-amount)
      (do
        (swap! cities update-in [from :current] - transfer-amount)
        (swap! cities update-in [to :current] + transfer-amount)
        (println (str "Ride: " (name from) " > " (name to) " (" transfer-amount " cans) - Distance: "
                      (get-in distances [from to]) " km"))
        (println (str "Situation after transfer - " (name from) ": " (get-in @cities [from :current])
                      ", " (name to) ": " (get-in @cities [to :current]))))
      nil))) ;; No message if transfer amount is zero or invalid

(defn meet-minimums []
  ;; Ensure each city meets its minimum stock requirement, prioritizing closer cities for transfers
  (doseq [[city data] @cities]
    (let [current (:current data)
          min-required (:min data)]
      (when (< current min-required)
        ;; Prioritize cities by shortest distance
        (doseq [[source _] (sort-by #(get-in distances [city (key %)]) @cities)]
          (let [source-stock (:current (get @cities source))
                source-min (:min (get @cities source))
                extra (- source-stock source-min)
                needed (- min-required current)]
            (when (and (> extra 0) (< current min-required))
              (transfer-products source city (min extra needed)))))))))

(defn print-final-status []
  ;; Print the final stock status of all cities
  (println "\nFinal status of all cities:")
  (doseq [[city data] @cities]
    (println (str (name city) ": " (:current data) " cans (Min: " (:min data) ", Max: " (:max data) ")"))))

(defn plan-routes []
  ;; Initial planned routes to start fulfilling minimums
  (meet-minimums)
  (print-final-status))

(plan-routes)
