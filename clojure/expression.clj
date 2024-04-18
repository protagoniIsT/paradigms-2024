(defn create-operation [op]
  (fn [& operands]
    (fn [vars_map]
      (apply op (mapv (fn [f] (f vars_map)) operands)))))

(defn constant [c]
  (fn [_] c))

(defn variable [v]
  (fn [vars_map] (get vars_map v)))

(def add (create-operation +))

(def subtract (create-operation -))

(def multiply (create-operation *))

(defn divide [operand1 operand2]
  (fn [vars_map]
    (let [op1 (operand1 vars_map)
          op2 (operand2 vars_map)]
      (cond
        (and (= op2 0.0) (>= op1 0)) Double/POSITIVE_INFINITY
        (and (= op2 0.0) (< op1 0)) Double/NEGATIVE_INFINITY
        :else (double (/ op1 op2)))
    )))

(def negate (create-operation -))

(def sinh (create-operation (fn [operand] (Math/sinh operand))))

(def cosh (create-operation (fn [operand] (Math/cosh operand))))

(def operations {'+ add
                 '- subtract
                 '* multiply
                 '/ divide
                 'negate negate
                 'sinh sinh
                 'cosh cosh
                 })

(defn parseFunction [expression]
  (letfn [(parse [expr]
            (cond
              (list? expr)
              (let [op (get operations (first expr))
                    args (mapv parse (rest expr))]
                (fn [vars_map]
                  ((apply op args) vars_map)))
              (number? expr)
              (constant expr)
              :else (variable (str expr))
            ))]
    (parse (read-string expression))))


