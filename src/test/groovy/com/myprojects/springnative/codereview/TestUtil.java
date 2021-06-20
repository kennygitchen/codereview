package com.myprojects.springnative.codereview;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;

public class TestUtil {
    public static <T> T testRunBlocking(Function2<CoroutineScope, Continuation, T> block) throws InterruptedException {
        return (T) BuildersKt.runBlocking(EmptyCoroutineContext.INSTANCE, block);
    }
}
