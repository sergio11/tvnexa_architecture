package com.dreamsoftware.core

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

/**
 * This method will declare your implementation inside module lambda
 */
inline fun <reified I, reified O> Module.mapper(
    noinline definition: Definition<IMapper<I, O>>
): KoinDefinition<IMapper<I, O>> {
    return factory(qualifier = named(identifier<I, O>()), definition = definition)
}

/**
 * This will recover and inject your interface like the method get() in koin.
 */
inline fun <reified I, reified O> Scope.getMapper(): IMapper<I, O> {
    return get(named(identifier<I, O>()))
}

/**
 * This will recover and inject your interface in implementations of KoinComponent
 */
inline fun <reified I, reified O> KoinComponent.injectMapper(): Lazy<IMapper<I, O>> {
    return inject(named(identifier<I, O>()))
}

/**
 * This method indentifies the input and output returning a string.
 */
inline fun <reified I, reified O> identifier(): String {
    val inputIdentifier = I::class.java.`package`.name + I::class.java.name
    val outputIdentifier = O::class.java.`package`.name + O::class.java.name
    return inputIdentifier + outputIdentifier
}