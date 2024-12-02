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
  {:warsaw {:krakow 100 :hamburg 700 :munich 900 :brno 600 :prague 500 :berlin 300}
   :krakow {:warsaw 100 :hamburg 800 :munich 600 :brno 700 :prague 600 :berlin 400}
   :hamburg {:warsaw 700 :krakow 800 :munich 200 :brno 400 :prague 300 :berlin 100}
   :munich {:warsaw 900 :krakow 600 :hamburg 200 :brno 400 :prague 300 :berlin 100}
   :brno {:warsaw 600 :krakow 700 :hamburg 400 :munich 400 :prague 100 :berlin 300}
   :prague {:warsaw 500 :krakow 600 :hamburg 300 :munich 300 :brno 100 :berlin 200}
   :berlin {:warsaw 300 :krakow 400 :hamburg 100 :munich 100 :brno 300 :prague 200}})


(def current-time
  (atom (LocalDateTime/now)))

(def clock-running
  (atom true)) ;; Flag to control when the clock stopss

(defn format-time []
  ;; Format the current time for display
  (let [formatter (DateTimeFormatter/ofPattern "HH:mm:ss")]
    (.format @current-time formatter)))

(defn update-time []
  ;; Increment the time by 1 minute
  (swap! current-time #(.plusSeconds % 1)))

(defn display-clock []
  ;; Function to display the clock
  (loop []
    (when @clock-running
      (print "\r" (format-time)) ;; Print the current time
      (flush)
      (Thread/sleep 1000)
      (update-time)
      (recur)))) ;; Keep updating the time until stopped

(defn transfer-products [from to amount delay-time]
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
        (println (str "\nRide: " (name from) " > " (name to) " (" transfer-amount " cans) - Distance: "
                      (get-in distances [from to]) " km"))
        (println (str "Situation after transfer - " (name from) ": " (get-in @cities [from :current])
                      ", " (name to) ": " (get-in @cities [to :current])))
              (Thread/sleep delay-time))
      nil))) ;; No message if transfer amount is zero or invalid

(defn meet-minimums [delay-time]
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
              (transfer-products source city (min extra needed) delay-time))))))))

(defn print-final-status []
  ;; Print the final stock status of all cities
  (println "\nFinal status of all cities:")
  (doseq [[city data] @cities]
    (println (str (name city) ": " (:current data) " cans (Min: " (:min data) ", Max: " (:max data) ")"))))

(defn plan-routes [delay-time]
  ;; Run the main program
  (meet-minimums delay-time)
  (print-final-status)
  ;; Stop the clock after finishing
  (reset! clock-running false))

(defn main []
  ;; Main function to ask the user and execute the logic
  (println "Do you want to use the function of the clock mode? (1: Yes, 2: No)")
  (let [choice (read-line)]
    (cond
      (= choice "1") (do
                       ;; Start the clock in a separate thread
                       (future (display-clock))
                       ;; Run the main program with normal delay
                       (plan-routes 1000)) ;; 2 seconds delay for each transfer
      (= choice "2") (do
                       ;; Disablee the clock immediately
                       (reset! clock-running false)
                       ;; Run the main program with no delay
                       (plan-routes 0)) ;; No delay for each transfer
      :else (println "Invalid choice, please use a valid number."))))

;; main functionx
(main)
