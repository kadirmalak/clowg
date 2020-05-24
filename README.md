# clowg

A Clojure library for generating Clojure wrappers around Java

## Installing

[![Clojars Project](https://img.shields.io/clojars/v/clowg.svg)](https://clojars.org/clowg)

## Usage

### Example 1: Wrapping java.util.concurrent.LinkedBlockingDeque (CLI usage)

- cd to the src directory

- run cmd to generate source file (change X.X.X part with the latest version number)

```
> clojure -Sdeps "{:deps {clowg {:mvn/version \"X.X.X\"}}}" -m clowg.core java.util.concurrent.LinkedBlockingDeque com.example.linked-blocking-deque
./com/example/linked_blocking_deque.clj written...
```

- use generated file

```
(require '[com.example.linked-blocking-deque :as dq])

(def q (dq/make-LinkedBlockingDeque-with-int 5)) ; **int** parameter is capacity

(dq/addFirst q "f1")
(dq/addFirst q "f2")
(dq/addFirst q "f3")
(dq/addLast q "l1")
(dq/addLast q "l2")

(dq/poll q)
=> "f3"
(dq/poll q)
=> "f2"
(dq/poll q)
=> "f1"
(dq/poll q)
=> "l1"
(dq/poll q)
=> "l2"
(dq/poll q)
=> nil
```

### Example 2: Wrapping java.util.Random (LIB usage)

- generate the code string and manually copy into a file

```
(require '[clowg.core :refer [get-code-str]])
(get-code-str java.util.Random)
```

- use generated code

```
; assuming that code is copied into src/com/example/random.clj

(require '[com.example.random :as rnd])

(def r (rnd/make-Random 42))
(repeatedly 10 #(rnd/nextInt r 10))
=> (0 3 8 4 0 5 5 8 9 3)

```

### Example 3: Wrapping 3rd party classes

- you may add the external dependency to the cli command as in the following example:

```
clojure -Sdeps "{:deps {clowg {:mvn/version \"X.X.X\"} org.processing/core {:mvn/version \"3.3.7\"}}}" -m clowg.core processing.core.PApplet com.example.p-applet
./com/example/p_applet.clj written...
```

- or you can always add clowg as a dependency to your project and prefer manual usage as in Example 2

## Conversion Details

- If a Java method has at least two overloads with same number of parameters, an arity problem occurs. In order to resolve this issue, new functions are generated with different names based on their parameter types.

```
(defn make-String ...
(defn make-String-with-StringBuilder ...
(defn make-String-with-StringBuffer ...
(defn make-String-with-byte-array ...
(defn make-String-with-byte-array-and-Charset ... 
```

- Constructors start with make-SimpleClassName

- Instance methods and static methods start with their original names.

- Fields are **function**s starting with "-"

- Static final fields are **def**s starting with "-"

## TODO

...

## License

MIT License

Copyright (c) 2020 Kadir Malak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
