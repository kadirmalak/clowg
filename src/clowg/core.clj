(ns clowg.core
  (:require [clojure.string :as str]
            [clojure.pprint :as pp])
  (:import (java.lang.reflect Modifier Method)))

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

(defn is-public? [^Method method]
  (Modifier/isPublic (.getModifiers method)))

(defn is-static? [^Method method]
  (Modifier/isStatic (.getModifiers method)))

(defn get-parameter-types [^Method method]
  (->> method
       (.getParameterTypes)
       (map #(.getName %))
       (apply list)
       ))

(defn collect-info [^Method method]
  (let [parameter-types (get-parameter-types method)]
    {:name (.getName method)
     :parameter-types parameter-types
     :arity (count parameter-types)}))

(defn needs-multi? [versions]
  (->> versions
       (group-by :arity)
       (map (fn [[arity versions]] (count versions)))
       (apply max)
       (< 1 ,,,)))

(defn get-instance-methods [class]
  (->>
    class
    (.getDeclaredMethods)
    (filter is-public?)
    (filter #(not (is-static? %)))
    (map collect-info)
    (group-by :name)
    (map (fn [[name versions]]
           {:name name
            :needs-multi? (needs-multi? versions)
            :versions (sort-by :arity versions)}))))

(defn make-symbols [params]
  (let [alphabet "abcdefghijklmnopqrstuvwxyz"
        len (count alphabet)
        arity (count params)
        letters (if (> arity len)
                  (map #(str (get alphabet (int (/ % len)))
                             (get alphabet (mod % len)))
                       (range arity))
                  (map #(str (get alphabet %)) (range arity)))]
    (map symbol letters)))

(defn make-params [parameter-types]
  (let [params (make-symbols parameter-types)
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

(defn make-body-params [parameter-types]
  (let [params (make-symbols parameter-types)]
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

(defn make-function-body [class inst version]
  (let [parameter-types (:parameter-types version)
        params (make-params parameter-types)
        params (if inst
                 ; [a b c]
                 params
                 ; [^some.Class this a b c]
                 (vec (concat [(symbol (str "^" (.getName class))) 'this] params)))
        body-params (make-body-params parameter-types)
        body-params (if inst
                      (cons inst body-params)
                      (cons 'this body-params))
        body (list
               (symbol (str "." (:name version))))]
    (list params (concat body body-params))))

(defn make-function [class inst method]
  (let [name (symbol (:name method))
        body (map (partial make-function-body class inst)
                  (:versions method))]
    (concat (list 'defn name) body)))

(defn make-functions [class inst]
  (->> (get-instance-methods class)
       (filter #(not (:needs-multi? %))) ; no support for multi-methods yet...
       (map (partial make-function class inst))
       ))

(defn get-code
  ([class] (get-code class false))
  ([class inst]
   (let [fns (make-functions class inst)
         code (with-out-str
                (doall
                  (map pp/pprint fns)))]
     code)))
