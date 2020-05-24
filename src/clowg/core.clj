(ns ^{:author "Kadir Malak"}
  clowg.core
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.java.io :as io])
  (:import (java.lang.reflect Modifier)))

(def primitives {"float" 'float
                 "[F" 'float-array

                 "int" 'int
                 "[I" 'int-array

                 "double" 'double
                 "[D" 'double-array

                 "long" 'long
                 "[J" 'long-array

                 "short" 'short
                 "[S" 'short-array

                 "byte" 'byte
                 "[B" 'byte-array

                 "char" 'char
                 "[C" 'char-array

                 "boolean" 'boolean
                 "[Z" 'boolean-array
                 })

(defn is-public? [x]
  (Modifier/isPublic (.getModifiers x)))

(defn is-static? [x]
  (Modifier/isStatic (.getModifiers x)))

(defn is-final? [x]
  (Modifier/isFinal (.getModifiers x)))

(defn get-parameter-types [x]
  (->> x
       (.getParameterTypes)
       (map #(.getName %))
       (apply list)
       ))

(defn get-method-info [x]
  (let [parameter-types (get-parameter-types x)
        arity (count parameter-types)
        static (is-static? x)]
    {:name (.getName x)
     :is-static? static
     :parameter-types parameter-types
     :arity (if static arity (inc arity))}))

(defn get-field-info [x]
  {:name (.getName x)
   :is-static? (is-static? x)
   :is-final? (is-final? x)
   })

(defn has-overloads-with-same-arity? [overloads]
  (->> overloads
       (group-by :arity)
       (map (fn [[arity overloads]] (count overloads)))
       (apply max)
       (< 1 ,,,)))

(defn handle-field [class inst field]
  (let [class-name (.getName class)
        field-name (:name field)
        name (symbol (str "-" field-name))
        class-type (symbol (str "^" class-name))
        static-field (symbol (str class-name "/" field-name))
        field-accessor (symbol (str ".-" field-name))
        ]
    (if (:is-static? field)
      (if (:is-final? field)
        (list 'def name static-field)
        (list 'defn name [] static-field))
      (if inst
        (list 'defn name [] (list field-accessor inst))
        (list 'defn name [class-type 'this] (list field-accessor 'this))))))

(defn handle-fields [class inst]
  (->> class
       (.getDeclaredFields)
       (filter is-public?)
       (map get-field-info)
       (map #(handle-field class inst %))
       ))

(defn get-constructors [class]
  (->>
    class
    (.getConstructors)
    (filter is-public?)
    (map get-method-info)
    (group-by :name)
    (map (fn [[name overloads]]
           {:name (.getSimpleName class)
            :arity-clash? (has-overloads-with-same-arity? overloads)
            :overloads (sort-by :arity overloads)}))))

(defn array-type-to-suffix [type]
  (let [matches (re-find #"^(\[+)([A-Z])([^;]*)(;*)$" type)
        [_ level letter long-name _] matches
        n (count level)]
    (condp = letter
      "L" (let [short-name (last (str/split long-name #"\."))
                arr (condp = n
                      1 "array"
                      (str "array" (count level) "D"))]
            (str short-name "-" arr))
      (let [primitive-arr (str (get primitives (str "[" letter)))]
        (condp = n
          1 primitive-arr
          (str primitive-arr (count level) "D"))))))

(defn type-to-suffix [type]
  (cond
    (contains? primitives type) (str (get primitives type))
    (str/starts-with? type "[") (array-type-to-suffix type)
    :else (last (str/split type #"\."))
    ))

(defn make-overload-suffix [parameter-types beginning]
  (if (zero? (count parameter-types))
    ""
    (str beginning
         (->>
           parameter-types
           (map type-to-suffix)
           (str/join "-and-")
           ))))

(defn make-constructor-name [class parameter-types]
  (str
    (.getSimpleName class)
    (make-overload-suffix parameter-types "-with-")))

(defn make-method-name [overload]
  (str
    (:name overload)
    (make-overload-suffix (:parameter-types overload) "-")))

(defn get-key [key map]
  (get map key))

(defn get-suffixed-constructors [class]
  (->>
    (get-constructors class)
    (first)
    (:overloads)
    (map #(dissoc % :is-static?))
    (map #(assoc % :name (make-constructor-name class (:parameter-types %))))
    (map (fn [m] {:name (:name m)
                  :overloads (list m)}))))

(defn get-methods [class]
  (->>
    class
    (.getDeclaredMethods)
    (filter is-public?)
    (map get-method-info)
    (group-by :name)
    (map (fn [[name overloads]]
           {:name name
            :arity-clash? (has-overloads-with-same-arity? overloads)
            :overloads (sort-by :arity overloads)}))))

(defn get-original-methods [class]
  (->>
    class
    (get-methods)
    (filter #(not (:arity-clash? %)))))

(defn get-suffixed-methods [class]
  (->>
    (get-methods class)
    (filter :arity-clash?)
    (map :overloads)
    (apply concat)
    (map #(assoc % :new-name (make-method-name %)))
    (map (fn [m] {:name (:name m)
                  :new-name (:new-name m)
                  :is-static? (:is-static? m)
                  :overloads (list m)}))
    ))

(defn make-params [parameter-types]
  (let [alphabet "abcdefghijklmnopqrstuvwxyz"
        len (count alphabet)
        arity (count parameter-types)
        letters (if (> arity len)
                  (map #(str (get alphabet (int (/ % len)))
                             (get alphabet (mod % len)))
                       (range arity))
                  (map #(str (get alphabet %)) (range arity)))]
    (map symbol letters)))

(defn make-function-params-vector [parameter-types]
  (let [params (make-params parameter-types)
        parts (map (fn [type param]
                     (cond
                       ; a
                       (contains? primitives type) (list param)
                       ; a
                       (str/starts-with? type "[") (list param)
                       ; ^some.Class a
                       :else (list (symbol (str "^" type)) param)
                       ))
                   parameter-types
                   params)]
    (vec (apply concat parts))))

(defn make-function-body-params [parameter-types]
  (let [params (make-params parameter-types)]
    (map (fn [type param]
           (cond
             ; (float a)
             (contains? primitives type) (list (get primitives type) param)
             ; (into-array a)
             (str/starts-with? type "[") (list 'into-array param)
             ; a
             :else param))
         parameter-types
         params)))

(defn handle-constructor-overload [class overload]
  (let [parameter-types (:parameter-types overload)
        params (make-function-params-vector parameter-types)
        body-params (make-function-body-params parameter-types)
        body (list
               (symbol (str (.getName class) ".")))]
    (list params (concat body body-params))))

(defn handle-constructor [class constructor]
  (let [name (symbol (str "make-" (:name constructor)))
        body (map (partial handle-constructor-overload class)
                  (:overloads constructor))]
    (concat (list 'defn name) body)))

(defn handle-constructors [class]
  (let [constructors (->> (get-constructors class)
                          (filter #(not (:arity-clash? %))))
        constructors (if (zero? (count constructors))
                       (get-suffixed-constructors class)
                       constructors)]
    (->> constructors
         (map (partial handle-constructor class)))))

(defn make-function-body [class inst overload]
  (let [parameter-types (:parameter-types overload)
        params (make-function-params-vector parameter-types)
        params (if inst
                 ; [a b c]
                 params
                 ; [^some.Class this a b c]
                 (vec (concat [(symbol (str "^" (.getName class))) 'this] params)))
        body-params (make-function-body-params parameter-types)
        body-params (if inst
                      (cons inst body-params)
                      (cons 'this body-params))
        body (list
               (symbol (str "." (:name overload))))]
    (list params (concat body body-params))))

(defn make-static-function-body [class overload]
  (let [parameter-types (:parameter-types overload)
        params (make-function-params-vector parameter-types)
        body-params (make-function-body-params parameter-types)
        body (list
               (symbol (str (.getName class) "/" (:name overload))))]
    (list params (concat body body-params))))

(defn handle-overload [class inst overload]
  (if (:is-static? overload)
    (make-static-function-body class overload)
    (make-function-body class inst overload)))

(defn handle-method [class inst method]
  (let [name (symbol (:new-name method (:name method)))
        body (map (partial handle-overload class inst)
                  (:overloads method))]
    (concat (list 'defn name) body)))

(defn handle-methods
  ([class] (handle-methods class false))
  ([class inst]
   (let [original (get-original-methods class)
         suffixed (get-suffixed-methods class)
         methods (concat original suffixed)]
     (->> methods
          (map (partial handle-method class inst))
          ))))

(defn get-code
  ([class] (get-code class nil))
  ([class inst]
   (let [constructors (handle-constructors class)
         methods (handle-methods class inst)
         fields (handle-fields class inst)]
     (concat constructors
             methods
             fields))))

(defn get-code-str
  ([class] (get-code-str class nil))
  ([class inst]
   (with-out-str
     (doall
       (map pp/pprint
            (get-code class inst))))))

(defn -main [& args]
  (if (< (count args) 2)
    (println "usage: ... <fully-qualified-class-name> <target-ns>")
    (let [clazz (first args)
          ns (second args)
          class (Class/forName clazz)
          code (str "(ns " ns ")\n\n" (get-code-str class))
          dest (-> ns
                   (str/replace "." "/")
                   (str/replace "-" "_")
                   (str ".clj"))]
      (io/make-parents dest)
      (spit dest code)
      (println (str "./" dest) "written..."))))

(comment
  (-main "java.util.concurrent.LinkedBlockingDeque"
         "com.example.linked-blocking-queue"))