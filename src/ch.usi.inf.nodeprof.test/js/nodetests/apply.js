/*******************************************************************************
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Copyright 2020 Dynamic Analysis Group, Università della Svizzera Italiana (USI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
function foo(a) {
    return a;
}
function bar() {
    return foo.apply(undefined, arguments);
}
function baz(a) {
    return a;
}
baz(bar(1));