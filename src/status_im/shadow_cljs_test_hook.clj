(ns status-im.shadow-cljs-test-hook)

(defn configure
  {:shadow.build/stage :configure}
  [{:shadow.build/keys [mode] :as build-state}]
  (def x build-state)
  ;; so that shadow-cljs will inject node repl deps
  ;; https://github.com/thheller/shadow-cljs/blob/c2d491f88419fe2a3b574585c1218ccba875e74e/src/main/shadow/build/targets/node_test.clj#L29
  (let [a (if (= mode :dev)
            (assoc-in build-state [:node-config :ui-driven] true)
            build-state)]
    (prn "--------------------------------------------------------")
    (prn (get-in a [:node-config :ui-driven]))
    a))
