(ns ^{:author "Kadir Malak"}
  clowg.core
  (:require [clojure.string :as str]
            [clojure.pprint :as pp])
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
  (let [parameter-types (get-parameter-types x)]
    {:name (.getName x)
     :is-static? (is-static? x)
     :parameter-types parameter-types
     :arity (count parameter-types)}))

(defn get-field-info [^Field field]
  {:name (.getName field)
   :is-static? (is-static? field)
   :is-final? (is-final? field)
   })

(defn needs-multi-method? [overloads]
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
           {:name name
            :needs-multi? (needs-multi-method? overloads)
            :overloads (sort-by :arity overloads)}))))

(defn get-methods [class]
  (->>
    class
    (.getDeclaredMethods)
    (filter is-public?)
    (map get-method-info)
    (group-by :name)
    (map (fn [[name overloads]]
           {:name name
            :needs-multi? (needs-multi-method? overloads)
            :overloads (sort-by :arity overloads)}))))

(defn gen-params [parameter-types]
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
  (let [params (gen-params parameter-types)
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
  (let [params (gen-params parameter-types)]
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
        _ (println parameter-types)
        params (make-function-params-vector parameter-types)
        body-params (make-function-body-params parameter-types)
        body (list
               (symbol (str (.getName class) ".")))]
    (list params (concat body body-params))))

(defn handle-constructor [class constructor]
  (let [name (symbol (str "make-" (.getSimpleName class)))
        body (map (partial handle-constructor-overload class)
                  (:overloads constructor))]
    (concat (list 'defn name) body)))

(defn handle-constructors [class]
  (->> (get-constructors class)
       (filter #(not (:needs-multi? %))) ; no support for multi-methods yet...
       (map (partial handle-constructor class))
       ))

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
  (let [name (symbol (:name method))
        body (map (partial handle-overload class inst)
                  (:overloads method))]
    (concat (list 'defn name) body)))

(defn handle-methods
  ([class] (handle-methods class false))
  ([class inst]
   (->> (get-methods class)
        (filter #(not (:needs-multi? %))) ; no support for multi-methods yet...
        (map (partial handle-method class inst))
        )))

(defn get-code
  ([class] (get-code class false))
  ([class inst]
   (let [constructors (handle-constructors class)
         methods (handle-methods class inst)
         fields (handle-fields class inst)
         code (concat constructors methods fields)
         code-str (with-out-str (doall (map pp/pprint code)))]
     code-str)))
