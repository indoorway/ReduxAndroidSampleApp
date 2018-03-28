package com.indoorway.reduxandroidsampleapp.common.utils

import io.reactivex.Observable
import io.reactivex.functions.BiFunction

inline fun <P : Any?, N : Any> Observable<P>.mapNotNull(crossinline value: (P) -> N?): Observable<N> = filter { value(it) != null }.map { value(it)!! }

fun <T, R> Observable<T>.onLatestFrom(other: Observable<R>, action: R.(T) -> R): Observable<R> {
    return withLatestFrom(other, BiFunction { t1, t2 ->
        t2.action(t1)
    })
}