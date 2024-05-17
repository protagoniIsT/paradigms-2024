(def ops-func-map (atom {}))

(defn add-to-map [map op fobj]
  (swap! map assoc op fobj))

(defn create-operation [op op-str]
  (let [operation (fn [& operands]
                    (fn [vars_map]
                      (apply op (mapv (fn [f] (f vars_map)) operands))))]
    (add-to-map ops-func-map op-str operation)
    operation))

(defn constant [c]
  (constantly c))

(defn variable [v]
  (fn [vars_map] (get vars_map v)))

(def add (create-operation + '+))

(def subtract (create-operation - '-))

(def multiply (create-operation * '*))

(def divide (create-operation (fn [a b] (/ (double a) (double b))) '/))

(def negate (create-operation - 'negate))

(def sinh (create-operation #(Math/sinh %) 'sinh))

(def cosh (create-operation #(Math/cosh %) 'cosh))

(defn createParser [expression map const-impl variable-impl]
  (letfn [(parse [expr]
            (cond
              (list? expr)
              (let [op-symbol (first expr) op (get @map op-symbol) args (mapv parse (rest expr))]
                (apply op args))
              (number? expr)
              (const-impl expr)
              :else (variable-impl (str expr))))]
    (parse (read-string expression))))

(defn parseFunction [expression] (createParser expression ops-func-map constant variable))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(load-file "proto.clj")

(def ops-obj-map (atom {}))

(defn expr-interface [eval to-str]
  {:evaluate eval
   :toString to-str})

(def evaluate (method :evaluate))
(def toString (method :toString))

(defn createOp [op op-eval]
  (let [operation (constructor
    (fn [this & operands]
      (assoc this :operands operands))
    (expr-interface
      (fn [this vars]
        (apply op-eval (map #(evaluate % vars) (:operands this))))
      (fn [this]
        (let [ops-str (mapv toString (:operands this))]
          (str "(" op " " (clojure.string/join " " ops-str) ")")))))]
    (add-to-map ops-obj-map op operation) operation))

(def Constant
  (constructor
    (fn [this val]
      (assoc this :value val))
    (expr-interface
      (fn [this _] (:value this))
      (fn [this] (str (:value this))))))

(def Variable
  (constructor
    (fn [this name]
      (assoc this :name name))
    (expr-interface
      (fn [this vars]
        (get vars (:name this)))
      (fn [this] (:name this)))))

(def Add (createOp '+ +))

(def Subtract (createOp '- -))

(def Multiply (createOp '* *))

(def Divide (createOp '/ (fn [a b] (/ (double a) (double b)))))

(def Negate (createOp 'negate -))

(def Pow (createOp 'pow (fn [base exp] (Math/pow base exp))))

(def Log (createOp 'log (fn [base arg] (/ (Math/log (abs arg)) (Math/log (abs base))))))

(defn parseObject [expression] (createParser expression ops-obj-map Constant Variable))

