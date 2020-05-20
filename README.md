[![Clojars Project](https://img.shields.io/clojars/v/clowg.svg)](https://clojars.org/clowg)

# clowg

A Clojure library for generating Clojure wrappers around Java

## Installing

- Leiningen/Boot

```
[clowg "0.1.3"]
```

## Usage

```
(require '[clowg.core :refer [get-code-str]])
```

Wrap Integer class

```
(get-code-str Integer)
=>
"(defn make-Integer-from-int ([a] (java.lang.Integer. (int a))))
 (defn
  make-Integer-from-String
  ([^java.lang.String a] (java.lang.Integer. a)))
 (defn compare ([a b] (java.lang.Integer/compare (int a) (int b))))
 (defn
  remainderUnsigned
  ([a b] (java.lang.Integer/remainderUnsigned (int a) (int b))))
 (defn shortValue ([^java.lang.Integer this] (.shortValue this)))
 (defn min ([a b] (java.lang.Integer/min (int a) (int b))))
 (defn toHexString ([a] (java.lang.Integer/toHexString (int a))))
 (defn
  divideUnsigned
  ([a b] (java.lang.Integer/divideUnsigned (int a) (int b))))
 (defn doubleValue ([^java.lang.Integer this] (.doubleValue this)))
 (defn highestOneBit ([a] (java.lang.Integer/highestOneBit (int a))))
 (defn longValue ([^java.lang.Integer this] (.longValue this)))
 (defn sum ([a b] (java.lang.Integer/sum (int a) (int b))))
 (defn reverse ([a] (java.lang.Integer/reverse (int a))))
 (defn decode ([^java.lang.String a] (java.lang.Integer/decode a)))
 (defn byteValue ([^java.lang.Integer this] (.byteValue this)))
 (defn max ([a b] (java.lang.Integer/max (int a) (int b))))
 (defn
  toString
  ([^java.lang.Integer this] (.toString this))
  ([a] (java.lang.Integer/toString (int a)))
  ([a b] (java.lang.Integer/toString (int a) (int b))))
 (defn
  rotateRight
  ([a b] (java.lang.Integer/rotateRight (int a) (int b))))
 (defn
  compareUnsigned
  ([a b] (java.lang.Integer/compareUnsigned (int a) (int b))))
 (defn floatValue ([^java.lang.Integer this] (.floatValue this)))
 (defn toOctalString ([a] (java.lang.Integer/toOctalString (int a))))
 (defn reverseBytes ([a] (java.lang.Integer/reverseBytes (int a))))
 (defn bitCount ([a] (java.lang.Integer/bitCount (int a))))
 (defn rotateLeft ([a b] (java.lang.Integer/rotateLeft (int a) (int b))))
 (defn intValue ([^java.lang.Integer this] (.intValue this)))
 (defn
  hashCode
  ([^java.lang.Integer this] (.hashCode this))
  ([a] (java.lang.Integer/hashCode (int a))))
 (defn toBinaryString ([a] (java.lang.Integer/toBinaryString (int a))))
 (defn
  numberOfLeadingZeros
  ([a] (java.lang.Integer/numberOfLeadingZeros (int a))))
 (defn toUnsignedLong ([a] (java.lang.Integer/toUnsignedLong (int a))))
 (defn lowestOneBit ([a] (java.lang.Integer/lowestOneBit (int a))))
 (defn
  equals
  ([^java.lang.Integer this ^java.lang.Object a] (.equals this a)))
 (defn
  numberOfTrailingZeros
  ([a] (java.lang.Integer/numberOfTrailingZeros (int a))))
 (defn
  parseUnsignedInt
  ([^java.lang.String a] (java.lang.Integer/parseUnsignedInt a))
  ([^java.lang.String a b]
   (java.lang.Integer/parseUnsignedInt a (int b))))
 (defn signum ([a] (java.lang.Integer/signum (int a))))
 (defn
  toUnsignedString
  ([a] (java.lang.Integer/toUnsignedString (int a)))
  ([a b] (java.lang.Integer/toUnsignedString (int a) (int b))))
 (defn
  parseInt
  ([^java.lang.String a] (java.lang.Integer/parseInt a))
  ([^java.lang.String a b] (java.lang.Integer/parseInt a (int b))))
 (def -MIN_VALUE java.lang.Integer/MIN_VALUE)
 (def -MAX_VALUE java.lang.Integer/MAX_VALUE)
 (def -TYPE java.lang.Integer/TYPE)
 (def -SIZE java.lang.Integer/SIZE)
 (def -BYTES java.lang.Integer/BYTES)
 "
```

Or if you want to refer to a specific instance all the time (omits *this*)

```
(get-code-str Integer 'the-int)
=>
"(defn make-Integer-from-int ([a] (java.lang.Integer. (int a))))
 (defn
  make-Integer-from-String
  ([^java.lang.String a] (java.lang.Integer. a)))
 (defn compare ([a b] (java.lang.Integer/compare (int a) (int b))))
 (defn
  remainderUnsigned
  ([a b] (java.lang.Integer/remainderUnsigned (int a) (int b))))
 (defn shortValue ([] (.shortValue the-int)))
 (defn min ([a b] (java.lang.Integer/min (int a) (int b))))
 (defn toHexString ([a] (java.lang.Integer/toHexString (int a))))
 (defn
  divideUnsigned
  ([a b] (java.lang.Integer/divideUnsigned (int a) (int b))))
 (defn doubleValue ([] (.doubleValue the-int)))
 (defn highestOneBit ([a] (java.lang.Integer/highestOneBit (int a))))
 (defn longValue ([] (.longValue the-int)))
 (defn sum ([a b] (java.lang.Integer/sum (int a) (int b))))
 (defn reverse ([a] (java.lang.Integer/reverse (int a))))
 (defn decode ([^java.lang.String a] (java.lang.Integer/decode a)))
 (defn byteValue ([] (.byteValue the-int)))
 (defn max ([a b] (java.lang.Integer/max (int a) (int b))))
 (defn
  toString
  ([] (.toString the-int))
  ([a] (java.lang.Integer/toString (int a)))
  ([a b] (java.lang.Integer/toString (int a) (int b))))
 (defn
  rotateRight
  ([a b] (java.lang.Integer/rotateRight (int a) (int b))))
 (defn
  compareUnsigned
  ([a b] (java.lang.Integer/compareUnsigned (int a) (int b))))
 (defn floatValue ([] (.floatValue the-int)))
 (defn toOctalString ([a] (java.lang.Integer/toOctalString (int a))))
 (defn reverseBytes ([a] (java.lang.Integer/reverseBytes (int a))))
 (defn bitCount ([a] (java.lang.Integer/bitCount (int a))))
 (defn rotateLeft ([a b] (java.lang.Integer/rotateLeft (int a) (int b))))
 (defn intValue ([] (.intValue the-int)))
 (defn
  hashCode
  ([] (.hashCode the-int))
  ([a] (java.lang.Integer/hashCode (int a))))
 (defn toBinaryString ([a] (java.lang.Integer/toBinaryString (int a))))
 (defn
  numberOfLeadingZeros
  ([a] (java.lang.Integer/numberOfLeadingZeros (int a))))
 (defn toUnsignedLong ([a] (java.lang.Integer/toUnsignedLong (int a))))
 (defn lowestOneBit ([a] (java.lang.Integer/lowestOneBit (int a))))
 (defn equals ([^java.lang.Object a] (.equals the-int a)))
 (defn
  numberOfTrailingZeros
  ([a] (java.lang.Integer/numberOfTrailingZeros (int a))))
 (defn
  parseUnsignedInt
  ([^java.lang.String a] (java.lang.Integer/parseUnsignedInt a))
  ([^java.lang.String a b]
   (java.lang.Integer/parseUnsignedInt a (int b))))
 (defn signum ([a] (java.lang.Integer/signum (int a))))
 (defn
  toUnsignedString
  ([a] (java.lang.Integer/toUnsignedString (int a)))
  ([a b] (java.lang.Integer/toUnsignedString (int a) (int b))))
 (defn
  parseInt
  ([^java.lang.String a] (java.lang.Integer/parseInt a))
  ([^java.lang.String a b] (java.lang.Integer/parseInt a (int b))))
 (def -MIN_VALUE java.lang.Integer/MIN_VALUE)
 (def -MAX_VALUE java.lang.Integer/MAX_VALUE)
 (def -TYPE java.lang.Integer/TYPE)
 (def -SIZE java.lang.Integer/SIZE)
 (def -BYTES java.lang.Integer/BYTES)
 "
```

If you want the actual code generated, use *get-code* instead of *get-code-str*

```
(require '[clowg.core :refer [get-code]])

(get-code Integer)
=>
((defn make-Integer-from-int ([a] (java.lang.Integer. (int a))))
 (defn make-Integer-from-String ([^java.lang.String a] (java.lang.Integer. a)))
 (defn compare ([a b] (java.lang.Integer/compare (int a) (int b))))
 (defn remainderUnsigned ([a b] (java.lang.Integer/remainderUnsigned (int a) (int b))))
 (defn shortValue ([^java.lang.Integer this] (.shortValue this)))
 (defn min ([a b] (java.lang.Integer/min (int a) (int b))))
 (defn toHexString ([a] (java.lang.Integer/toHexString (int a))))
 (defn divideUnsigned ([a b] (java.lang.Integer/divideUnsigned (int a) (int b))))
 (defn doubleValue ([^java.lang.Integer this] (.doubleValue this)))
 (defn highestOneBit ([a] (java.lang.Integer/highestOneBit (int a))))
 (defn longValue ([^java.lang.Integer this] (.longValue this)))
 (defn sum ([a b] (java.lang.Integer/sum (int a) (int b))))
 (defn reverse ([a] (java.lang.Integer/reverse (int a))))
 (defn decode ([^java.lang.String a] (java.lang.Integer/decode a)))
 (defn byteValue ([^java.lang.Integer this] (.byteValue this)))
 (defn max ([a b] (java.lang.Integer/max (int a) (int b))))
 (defn
  toString
  ([^java.lang.Integer this] (.toString this))
  ([a] (java.lang.Integer/toString (int a)))
  ([a b] (java.lang.Integer/toString (int a) (int b))))
 (defn rotateRight ([a b] (java.lang.Integer/rotateRight (int a) (int b))))
 (defn compareUnsigned ([a b] (java.lang.Integer/compareUnsigned (int a) (int b))))
 (defn floatValue ([^java.lang.Integer this] (.floatValue this)))
 (defn toOctalString ([a] (java.lang.Integer/toOctalString (int a))))
 (defn reverseBytes ([a] (java.lang.Integer/reverseBytes (int a))))
 (defn bitCount ([a] (java.lang.Integer/bitCount (int a))))
 (defn rotateLeft ([a b] (java.lang.Integer/rotateLeft (int a) (int b))))
 (defn intValue ([^java.lang.Integer this] (.intValue this)))
 (defn hashCode ([^java.lang.Integer this] (.hashCode this)) ([a] (java.lang.Integer/hashCode (int a))))
 (defn toBinaryString ([a] (java.lang.Integer/toBinaryString (int a))))
 (defn numberOfLeadingZeros ([a] (java.lang.Integer/numberOfLeadingZeros (int a))))
 (defn toUnsignedLong ([a] (java.lang.Integer/toUnsignedLong (int a))))
 (defn lowestOneBit ([a] (java.lang.Integer/lowestOneBit (int a))))
 (defn equals ([^java.lang.Integer this ^java.lang.Object a] (.equals this a)))
 (defn numberOfTrailingZeros ([a] (java.lang.Integer/numberOfTrailingZeros (int a))))
 (defn
  parseUnsignedInt
  ([^java.lang.String a] (java.lang.Integer/parseUnsignedInt a))
  ([^java.lang.String a b] (java.lang.Integer/parseUnsignedInt a (int b))))
 (defn signum ([a] (java.lang.Integer/signum (int a))))
 (defn
  toUnsignedString
  ([a] (java.lang.Integer/toUnsignedString (int a)))
  ([a b] (java.lang.Integer/toUnsignedString (int a) (int b))))
 (defn
  parseInt
  ([^java.lang.String a] (java.lang.Integer/parseInt a))
  ([^java.lang.String a b] (java.lang.Integer/parseInt a (int b))))
 (def -MIN_VALUE java.lang.Integer/MIN_VALUE)
 (def -MAX_VALUE java.lang.Integer/MAX_VALUE)
 (def -TYPE java.lang.Integer/TYPE)
 (def -SIZE java.lang.Integer/SIZE)
 (def -BYTES java.lang.Integer/BYTES))

```

## TODO

- Handle Java method overloads with same arity (they're skipped for now)

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
