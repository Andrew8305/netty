/*
 * Copyright 2017 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.buffer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractByteBufAllocatorTest extends ByteBufAllocatorTest {

    @Override
    protected abstract AbstractByteBufAllocator newAllocator(boolean preferDirect);

    @Override
    protected final int defaultMaxCapacity() {
        return AbstractByteBufAllocator.DEFAULT_MAX_CAPACITY;
    }

    @Override
    protected final int defaultMaxComponents() {
        return AbstractByteBufAllocator.DEFAULT_MAX_COMPONENTS;
    }

    @Test
    public void testCalculateNewCapacity() {
        testCalculateNewCapacity(true);
        testCalculateNewCapacity(false);
    }

    private void testCalculateNewCapacity(boolean preferDirect) {
        ByteBufAllocator allocator = newAllocator(preferDirect);
        assertEquals(8, allocator.calculateNewCapacity(1, 8));
        assertEquals(7, allocator.calculateNewCapacity(1, 7));
        assertEquals(64, allocator.calculateNewCapacity(1, 129));
        assertEquals(AbstractByteBufAllocator.CALCULATE_THRESHOLD,
                allocator.calculateNewCapacity(AbstractByteBufAllocator.CALCULATE_THRESHOLD,
                        AbstractByteBufAllocator.CALCULATE_THRESHOLD + 1));
        assertEquals(AbstractByteBufAllocator.CALCULATE_THRESHOLD * 2,
                allocator.calculateNewCapacity(AbstractByteBufAllocator.CALCULATE_THRESHOLD + 1,
                        AbstractByteBufAllocator.CALCULATE_THRESHOLD * 4));
        try {
            allocator.calculateNewCapacity(8, 7);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            allocator.calculateNewCapacity(-1, 8);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
