/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import java.lang.reflect.Field;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;

import sun.misc.Unsafe;

/**
 *
 * @author lahvac
 */
public class SilverBulletersTestCaseCheck {

    public void run() {
        ClassWriter w = new ClassWriter(0);
        w.visit(Opcodes.V1_8, Opcodes.ACC_ABSTRACT | Opcodes.ACC_PUBLIC, "com/sun/tools/javac/code/Scope$WriteableScope", null, "com/sun/tools/javac/code/Scope", null);
        byte[] classData = w.toByteArray();
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe unsafe = (Unsafe) theUnsafe.get(null);
            Class scopeClass = Class.forName("com.sun.tools.javac.code.Scope");
            // Noncompliant@+1
            unsafe.defineClass("com.sun.tools.javac.code.Scope$WriteableScope", classData, 0, classData.length, scopeClass.getClassLoader(), scopeClass.getProtectionDomain());
        } catch (Throwable t) {
            //ignore...

        }
    }
}