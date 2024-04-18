(defn create-operation [op]
  (fn [arg1 arg2]
    (cond
      (and (vector? arg1) (every? number? arg1))
      (mapv op arg1 arg2)
      (and (vector? arg1) (every? vector? arg1))
      (mapv (create-operation op) arg1 arg2))))


(def v+ (create-operation +))

(defn v- [v1 v2]
  ((create-operation -) v1 v2))

(defn v* [v1 v2]
  ((create-operation *) v1 v2))

(defn vd [v1 v2]
  ((create-operation (fn [x y] (/ (double x) (double y)))) v1 v2))

(defn scalar [v1 v2]
  (apply + (mapv * v1 v2)))

(defn vect [v1 v2]
  (vector
    (- (* (v1 1) (v2 2)) (* (v1 2) (v2 1)))
    (- (* (v1 2) (v2 0)) (* (v1 0) (v2 2)))
    (- (* (v1 0) (v2 1)) (* (v1 1) (v2 0)))))

(defn v*s [v s]
  (mapv (partial * s) v))


(defn m+ [m1, m2]
  ((create-operation +) m1 m2))

(defn m- [m1, m2]
  ((create-operation -) m1 m2))

(defn m* [m1, m2]
  ((create-operation *) m1 m2))

(defn md [m1 m2]
  ((create-operation (fn [x y] (/ (double x) (double y)))) m1 m2))

(defn m*s [m s]
  (mapv (fn [row] (mapv (fn [el] (* el s)) row)) m))

(defn m*v [m v]
  (mapv (fn [row] (reduce + (map * row v))) m))

(defn transpose [m]
  (apply mapv vector m))

(defn m*m [m1 m2]
  (let [cols_m2 (transpose m2)]
    (mapv (fn [row_m1]
            (mapv (fn [col_m2]
                    (reduce + (map * row_m1 col_m2)))
                  cols_m2))
          m1)))


(defn c4+ [c1 c2]
  ((create-operation +) c1 c2))

(defn c4- [c1 c2]
  ((create-operation -) c1 c2))

(defn c4* [c1 c2]
  ((create-operation *) c1 c2))

(defn c4d [c1 c2]
  ((create-operation (fn [x y] (/ (double x) (double y)))) c1 c2))

