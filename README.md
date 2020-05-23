# clowg

A Clojure library for generating Clojure wrappers around Java

## Installing

[![Clojars Project](https://img.shields.io/clojars/v/clowg.svg)](https://clojars.org/clowg)

## Usage

```
(require '[clowg.core :refer [get-code-str]])
```

## Example: Wrapping java.util.concurrent.LinkedBlockingDeque

- import Java class

```
(import java.util.concurrent.LinkedBlockingDeque)
```

- generate code and save output to a file

```
(get-code-str LinkedBlockingDeque)
=>
"(defn
  make-LinkedBlockingDeque
  ([] (java.util.concurrent.LinkedBlockingDeque.)))
  ...
```

- use generated code

```
; assuming that code is written to src/com/example/linked_blocking_deque.clj

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
