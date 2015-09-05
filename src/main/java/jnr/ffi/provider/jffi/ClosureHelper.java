/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Sergey Tselovalnikov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package jnr.ffi.provider.jffi;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.util.Collection;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.mapper.CompositeTypeMapper;
import jnr.ffi.mapper.DefaultSignatureType;
import jnr.ffi.mapper.FromNativeContext;
import jnr.ffi.mapper.FromNativeConverter;
import jnr.ffi.provider.ClosureManager;
import java.util.Collections;

public class ClosureHelper {
    public static ClosureHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ClosureHelper INSTANCE = new ClosureHelper();
    }

    private final SimpleNativeContext ctx;
    private final ClassValue<FromNativeConverter<?, Pointer>> cache;

    private ClosureHelper() {
        try {
            final ClosureManager closureManager = Runtime.getSystemRuntime().getClosureManager();

            final AsmClassLoader cl = (AsmClassLoader) accessible(NativeClosureManager.class.getDeclaredField("classLoader")).get(closureManager);
            final CompositeTypeMapper ctm = (CompositeTypeMapper) accessible(NativeClosureManager.class.getDeclaredField("typeMapper")).get(closureManager);
            this.ctx = new SimpleNativeContext(Runtime.getSystemRuntime(), (Collection<Annotation>) Collections.EMPTY_LIST);
            this.cache = new ClassValue<FromNativeConverter<?, Pointer>>() {
                @Override
                protected FromNativeConverter<?, Pointer> computeValue(Class<?> closureClass) {
                    return ClosureFromNativeConverter.
                            getInstance(Runtime.getSystemRuntime(), DefaultSignatureType.create(closureClass, (FromNativeContext) ctx), cl, ctm);
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("Unable to create helper", e);
        }
    }

    public <T> T fromNative(Pointer nativeValue, Class<T> closureClass) {
        return (T) cache.get(closureClass).fromNative(nativeValue, ctx);
    }

    public static <T> Pointer toNative(Class<? extends T> closureClass, T instance) {
        return Runtime.getSystemRuntime().getClosureManager().getClosurePointer(closureClass, instance);
    }

    private static <T extends AccessibleObject> T accessible(T obj) {
        obj.setAccessible(true);
        return obj;
    }
}
