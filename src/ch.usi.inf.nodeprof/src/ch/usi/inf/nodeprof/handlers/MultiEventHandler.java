/*******************************************************************************
 * Copyright 2018 Dynamic Analysis Group, Università della Svizzera Italiana (USI)
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
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
package ch.usi.inf.nodeprof.handlers;

import java.util.Arrays;
import java.util.Comparator;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import ch.usi.inf.nodeprof.ProfiledTagEnum;

public class MultiEventHandler<T extends BaseEventHandlerNode> extends BaseSingleTagEventHandler {

    @Children final T[] handlers;

    /**
     *
     * @param handlers should be of the same kind T
     */
    protected MultiEventHandler(ProfiledTagEnum tag, T[] handlers) {
        super(handlers[0].context, tag);
        this.handlers = handlers.clone();

        // sort handlers by their priority
        Arrays.sort(this.handlers, Comparator.comparingInt(BaseEventHandlerNode::getPriority));
    }

    public static <T extends BaseEventHandlerNode> MultiEventHandler<T> create(ProfiledTagEnum tag, T[] handlers) {
        assert (handlers != null && handlers.length > 0);
        return new MultiEventHandler<>(tag, handlers);
    }

    @Override
    @ExplodeLoop
    public void executePre(VirtualFrame frame, Object[] inputs) throws Exception {
        for (T handler : handlers) {
            handler.executePre(frame, inputs);
        }
    }

    @Override
    @ExplodeLoop
    public void executePost(VirtualFrame frame, Object result, Object[] inputs) throws Exception {
        for (T handler : handlers) {
            handler.executePost(frame, result, inputs);
        }
    }

    @ExplodeLoop
    @Override
    public void executeExceptional(VirtualFrame frame, Throwable exception) throws Exception {
        for (T handler : handlers) {
            handler.executeExceptional(frame, exception);
        }
    }

    @Override
    @ExplodeLoop
    public void executeExceptionalCtrlFlow(VirtualFrame frame, Throwable exception, Object[] inputs) throws Exception {
        for (T handler : handlers) {
            handler.executeExceptionalCtrlFlow(frame, exception, inputs);
        }
    }

    @ExplodeLoop
    @Override
    public Object onUnwind(VirtualFrame frame, Object info) {
        Object res = null;
        for (T handler : handlers) {
            res = handler.onUnwind(frame, info);
            if (res != null) {
                return res;
            }
        }
        return null;
    }
}
